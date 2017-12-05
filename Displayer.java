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
    boolean menu, pause, game, started, gameover, specialMode;
    int mazeWidth, mazeHeight = 0;
        Timer updater;
        Maze world;
        User user;
        Ghost blinky, inky, pinky, clyde;
        static Ghost[] ghosts;
        BufferedImage[] ghostImages, pacmanImages; //pacmanImages is for the different orientations of pacman
        
        public Displayer(){
            super();
            menu = true;
            pause = false;
            game = false;
            started = false;
            gameover = false;
            specialMode = false;
            mazeWidth = 19; //19, 25
            mazeHeight = 25;
            ghosts = new Ghost[4];
            ghostImages = pacmanImages = new BufferedImage[4];
            world = new Maze(mazeWidth, mazeHeight);
            try {
            ghostImages[0] = ImageIO.read(new File("blinky file.png"));
            ghostImages[1] = ImageIO.read(new File("pinky file.png"));
            ghostImages[2] = ImageIO.read(new File("inky file.png"));
            ghostImages[3] = ImageIO.read(new File("clyde file.png"));
            //pacmanImages[0] = ImageIO.read(new File("pacman 0.png")); //up
            //pacmanImages[1] = ImageIO.read(new File("pacman 1.png")); //right
            //pacmanImages[2] = ImageIO.read(new File("pacman 2.png")); //down
            //pacmanImages[3] = ImageIO.read(new File("pacman 3.png")); //left
        } 
        catch(IOException e) {System.out.println("ERROR");};
        updater = new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        });
        updater.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (menu){
            //displayMenu(g); //<- this is the code that is suppose to be here
            user = new User(1,1,0); //debug            
            ghosts[0] = new ChasingGhost(mazeWidth / 2 + 1, mazeHeight / 2,user, world); //debug
            ghosts[1] = new AmbushGhost(mazeWidth / 2, mazeHeight / 2,user,world); //debug
            ghosts[2] = new UnpredictableGhost(mazeWidth / 2, mazeHeight / 2 - 1, user,world); //debug
            ghosts[3] = new StupidGhost(mazeWidth / 2, mazeHeight / 2 + 1,user,world); //debug          
            displayGame(g); //debug -> test maze graphics
            updateTimerTask("GAME");
        }else if (game){
            if (pause){
                displayPauseMenu(g);
            }else{
                if (!started){
                    //world = new Maze(17,17);
                    //user = new User(1,1,0);
                    //blinky = new ChasingGhost(1,1,user, world); 
                    //pinky = new AmbushGhost(1,1,user,world);
                    //inky = new UnpredictableGhost(1,1,user,world);
                    //clyde = new StupidGhost(1,1,user,world);

                    ghosts[0] = new ChasingGhost(1,1,user, world); //blinky
                    ghosts[1] = new AmbushGhost(1,1,user,world); //pinky
                    ghosts[2] = new UnpredictableGhost(1,1,user,world); //inky
                    ghosts[3] = new StupidGhost(1,1,user,world); //clide
                    //update timer task
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
    }

    public void displayPauseMenu(Graphics g){
        //background picture
        //selected buttons are bigger
    }

    public void displayGame(Graphics g){
        //top menu for points and lives and time
        final Dimension frameSize = GameRunner.frame.getContentPane().getSize();
        final int gridSize =  (int) (frameSize.getHeight() / mazeHeight); 
        final int pointSize = 10;
        final int bigPointSize = 15;
        for( int i = 0; i < world.grid.length; i++){
            for (int j = 0; j < world.grid[i].length; j++){
                //add background for all cases
                g.setColor(Color.BLACK);
                g.fillRect(i*gridSize, j*gridSize, gridSize, gridSize);
                for (int k = 0; k < ghosts.length; k++) {
                    if (ghosts[k].r == i && ghosts[k].c == j) {
                        g.drawImage(ghostImages[k], i*gridSize, j*gridSize, gridSize, gridSize, null, null); 
                    }
                }
                if (world.grid[i][j].unbreakable){
                    g.setColor(Color.YELLOW);
                    g.fillRect(i*gridSize, j*gridSize, gridSize, gridSize);
                }else if (world.grid[i][j].obstacle){
                    g.setColor(Color.BLUE); //wall color
                    g.fillRect(i*gridSize, j*gridSize, gridSize, gridSize);
                }
                else if (user.r == i && user.c == j){
                    for (int l = 0; l < pacmanImages.length; l++) {
                        if (user.orientation == l) {
                            //g.drawImage(pacmanImages[1], i*gridSize, j*gridSize, gridSize, gridSize, null, null);
                        }
                    }
                }else if (world.grid[i][j].point){
                    g.setColor(Color.WHITE);
                    g.fillOval(i*gridSize+pointSize, j*gridSize + pointSize, pointSize, pointSize);
                }else if (world.grid[i][j].bigPoint){
                    g.setColor(Color.WHITE);
                    g.fillOval(i*gridSize+pointSize, j*gridSize+pointSize, bigPointSize, bigPointSize);
                }                
            }
        }
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

            }else if (e.getKeyCode() == KeyEvent.VK_UP){

            }else if (e.getKeyCode() == KeyEvent.VK_ENTER){

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
                        if (!world.outOfBound(user.c-1,true) && !world.grid[user.r][user.c-1].obstacle)
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
            //if (specialMode) { //return to center box }
                
            
        }
    }

    public void checkVictory(){
        //something happens
        //if no more points you win
    }

    public void eatPoint(){
        if (world.grid[user.r][user.c].point){
            //add to score
            //score++;
            world.grid[user.r][user.c].point = false;
        }else if (world.grid[user.r][user.c].bigPoint){
            //specialMode = true;
            world.grid[user.r][user.c].bigPoint = false;
        }
    }

    public void ghostUpdate(){
        for (int i = 0; i < ghosts.length; i++) 
            ghosts[i].performSimpleAgentTask(); //ghost.performSimpleAgentTask() does not work
    }

    public void updateTimerTask(String type){
        if (type.equals("GAME")){
            updater.stop();
            updater = new Timer(40, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        //checkGhostsStatus(); //commented out bc it is empty
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
