import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

public class Displayer extends JPanel implements KeyListener
{
    boolean menu, pause, game, started, gameover;
    final int mazeWidth = 25, mazeHeight = 19;
    Timer updater;
    Maze world;
    User user;
    Ghost blinky, inky, pinky, clyde;
    Ghost[] ghosts;
    BufferedImage[] ghostImages, pacmanImages; //pacmanImages is for the different orientations of pacman
    BufferedImage wall;
    final Dimension frameSize = GameRunner.frame.getContentPane().getSize();

    public Displayer(){
        super();
        menu = true;
        pause = false;
        game = false;
        started = false;
        gameover = false;
        ghosts = new Ghost[4];
        ghostImages = new BufferedImage[4];
        pacmanImages = new BufferedImage[4];
        try {
            ghostImages[0] = ImageIO.read(new File("blinky file.png"));
            ghostImages[1] = ImageIO.read(new File("pinky file.png"));
            ghostImages[2] = ImageIO.read(new File("inky file.png"));
            ghostImages[3] = ImageIO.read(new File("clyde file.png"));
            pacmanImages[0] = ImageIO.read(new File("pacman 0.png"));
            pacmanImages[1] = ImageIO.read(new File("pacman 1.png"));
            pacmanImages[2] = ImageIO.read(new File("pacman 2.png"));
            pacmanImages[3] = ImageIO.read(new File("pacman 3.png"));
        } 
        catch(IOException e) {System.out.println("ERROR");};
        updater = new Timer(40, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    repaint();
                }
            });
        updater.start();
        addKeyListener(this);
        requestFocusInWindow();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (menu){
            //displayMenu(g); //<- this is the code that is suppose to be here
        }else if (game){
            if (pause){
                displayPauseMenu(g);
            }else{
                if (!started){
                    world = new Maze(mazeHeight,mazeWidth);
                    user = new User(1,1,0);
                    ghosts[0] = new ChasingGhost(mazeWidth/2-1,mazeHeight/2,user, world); //blinky
                    ghosts[1] = new AmbushGhost(mazeWidth/2,mazeHeight/2,user,world); //debug
                    ghosts[2] = new UnpredictableGhost(mazeWidth/2+1, mazeHeight/2,user,world); //debug
                    ghosts[3] = new StupidGhost(mazeWidth/2, mazeHeight/2+1,user,world); //debug
                    updateTimerTask("GAME");
                    started = true;
                }else if (started && !gameover){
                    displayGame(g);
                }else if (gameover){
                    displayGameOver(g);
                }
            }
        }
    }

    public void displayMenu(Graphics g){
        //background picture
        //"selected" buttons are bigger
        //work in progress
        //         BufferedImage buttonImage;
        //         final int buttonWidth = ;
        //         final int buttonHeight = ;
        //         int buttonX = ;
        //         int buttonY = ;
        //         Button startButton = new Button("Start", true);
        //         Button quitButton = new Button("Quit", false);
        //         for (int i = 0; i < displayMenuButtons.length; i++) {
        //             if (displayMenuButtons[i].selected) {
        //                 buttonImage = new ImageIO(new File("selected button.png")); 
        //             }
        //             else {
        //                 buttonImage = new ImageIO(new File("button.png"));
        //             }
        //             g.drawImage(buttonImage, buttonX, buttonY, buttonWidth, buttonHeight, null, null);
        //             //g.setFont();
        //             //g.setColor();
        //             g.drawString(displayMenuButtons[i].text, buttonX, buttonY);
        //             buttonX += ;
        //             buttonY += ;    
        //         }

    }

    public void displayPauseMenu(Graphics g){
        //background picture
        //selected buttons are bigger
    }

    public void displayGame(Graphics g){
        //top menu for points and lives and time
        final Dimension fs = GameRunner.frame.getContentPane().getSize();
        final int gridSize =  (int) (fs.getHeight() / mazeHeight); 
        final int pointSize = 10;
        final int bigPointSize = 15;
        for( int i = 0; i < world.grid.length; i++){
            for (int j = 0; j < world.grid[i].length; j++){
                //add background for all cases
                g.setColor(Color.BLACK);
                g.fillRect(j*gridSize, i*gridSize, gridSize, gridSize);
                if (world.grid[i][j].obstacle){
                    try {
                        String fileName = "wall"+generateCode(i,j)+".png";
                        BufferedImage wallImage = ImageIO.read(new File(fileName));
                        g.drawImage(wallImage, j*gridSize, i*gridSize, gridSize, gridSize, null, null);
                    } 
                    catch(IOException e) {System.out.println("ERROR");};
                }else if (world.grid[i][j].point){
                    g.setColor(Color.WHITE);
                    g.fillOval(j*gridSize, i*gridSize, pointSize, pointSize);
                }else if (world.grid[i][j].bigPoint){
                    g.setColor(Color.WHITE);
                    g.fillOval(j*gridSize, i*gridSize, bigPointSize, bigPointSize);
                }
            }
        }
        //draw the ghosts
        for (int k = 0; k < ghosts.length; k++) {
            g.drawImage(ghostImages[k], ghosts[k].c*gridSize, ghosts[k].r*gridSize, gridSize, gridSize, null, null); 
        }
        g.drawImage(pacmanImages[user.orientation], user.c*gridSize, user.r*gridSize, gridSize, gridSize, null, null);
    }

    public String generateCode(int r, int c){
        String ans = "";
        boolean[] status = new boolean[4];
        if (outOfBound(r+1,true))
            status[0] = false;
        else status[0] = world.grid[r+1][c].obstacle;
        if (outOfBound(c+1,false))
            status[1] = false;
        else status[1] = world.grid[r][c+1].obstacle;
        if (outOfBound(r-1,true))
            status[2] = false;
        else status[2] = world.grid[r-1][c].obstacle;
        if (outOfBound(c-1,false))
            status[3] = false;
        else status[3] = world.grid[r][c-1].obstacle;
        int num = world.countWalls(status);
        ans+=num;
        if (num != 0 || num != 4){
            ans+=findIndex(num, status);
        }
        return ans;
    }

    public String findIndex(int num, boolean[] status){
        if (num == 1){
            return Integer.toString(findFirstTrue(status));
        }else if (num == 2){
            if (world.isTurn(status)){
                return findRightTurnStart(status);
            }else{
                return Integer.toString(findFirstTrue(status));
            }
        }else if (num == 3){
            return Integer.toString(findOnlyFalse(status));
        }
        return "";
    }

    public String findRightTurnStart(boolean[] stuff){
        for (int i = 0; i < stuff.length; i++)
            if (!stuff[(i+stuff.length)%stuff.length] && !stuff[(i+1+stuff.length)%stuff.length])
                return Integer.toString(i)+"t";
        return "";
    }

    public int findOnlyFalse(boolean[] status){
        for (int i = 0; i < status.length; i++)
            if (status[i] == false)
                return i;
        return -1;
    }

    public int findFirstTrue(boolean[] status){
        for (int i = 0; i < status.length; i++){
            if (status[i] == true)
                return i;
        }
        return -1;
    }

    public boolean outOfBound(int i, boolean r){
        if (r){
            if (i <=world.height-1 && i >=0)
                return false;
        }else{
            if (i <= world.width-1 && i >= 0)
                return false;
        }
        return true;
    }

    public void displayGameOver(Graphics g){

    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyReleased(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e){
        if (menu){
            if (e.getKeyCode() == KeyEvent.VK_DOWN){
                //updateTimerTask();
            }else if (e.getKeyCode() == KeyEvent.VK_UP){
                //updateTimeTask();
            }else if (e.getKeyCode() == KeyEvent.VK_ENTER){
                //game = true;
                //menu = false;
            }
        }else if (game){
            if (pause){
                if (e.getKeyCode() == KeyEvent.VK_DOWN){

                }else if (e.getKeyCode() == KeyEvent.VK_UP){

                }else if (e.getKeyCode() == KeyEvent.VK_ENTER){

                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pause = false;
                    //update Timer task
                }
            }else{
                if (started && !gameover){
                    if (e.getKeyCode() == KeyEvent.VK_DOWN){
                        user.orientation = 2;
                        if (!world.outOfBound(user.r+1,true) && !world.grid[user.r+1][user.c].obstacle)
                            user.r++;
                    }else if (e.getKeyCode() == KeyEvent.VK_UP){
                        user.orientation = 0;
                        if (!world.outOfBound(user.r-1,true) && !world.grid[user.r-1][user.c].obstacle)
                            user.r--;
                    }else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                        user.orientation = 1;
                        if (!world.outOfBound(user.c+1,false) && !world.grid[user.r][user.c+1].obstacle)
                            user.c++;
                    }else if (e.getKeyCode() == KeyEvent.VK_LEFT){
                        user.orientation = 3;
                        if (!world.outOfBound(user.c-1,false) && !world.grid[user.r][user.c-1].obstacle)
                            user.c--;
                    }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        pause = true;
                        //update Timer Task
                    }
                }else if (gameover){
                }
            }
        }
    }

    public void checkGhostsStatus(){
        if ((user.r == pinky.r && user.c == pinky.c)||(user.r == inky.r && user.c == inky.c)||
        (user.r == blinky.r && user.c == blinky.c)||(user.r == blinky.r && user.c == blinky.c)){
            //if ghosts are in special mode, ghosts return to mid and get reset to normal mode
            //else user dies and then if user's life >= 0, reset the ghosts, else gameover
        }
    }

    public void checkVictory(){
        //something happens
    }

    public void eatPoint(){
        if (world.grid[user.r][user.c].point){
            //add to score
            world.grid[user.r][user.c].point = false;
        }else if (world.grid[user.r][user.c].bigPoint){
            world.grid[user.r][user.c].bigPoint = false;
        }
    }

    public void ghostUpdate(){
        for (int i = 0; i < ghosts.length; i++) 
            ghosts[i].performSimpleAgentTask(); 
    }

    public void updateTimerTask(String type){
        if (type.equals("GAME")){
            updater.stop();
            updater = new Timer(200, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        //checkGhostsStatus();
                        //eatPoint();
                        //ghostUpdate();
                        //checkVictory();
                        repaint();
                    }
                });
            updater.start();
        }else if (type.equals("")){
            updater.stop();
            updater = new Timer(40, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        repaint();
                    }
                });
            updater.start();
        }
    }
}
