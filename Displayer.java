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
import java.io.File;
import java.io.IOException;

public class Displayer extends JPanel implements KeyListener
{
    boolean menu, pause, game, started, gameover;
    Timer updater;
    Maze world;
    User user;
    Ghost blinky, inky, pinky, clyde;
    Ghost[] ghosts;
    BufferedImage[] ghostImages, pacmanImages; //pacmanImages is for the different orientations of pacman
    public Displayer(){
        super();
        menu = true;
        pause = false;
        game = false;
        started = false;
        gameover = false;
        ghosts = new Ghost[4];
        ghostImages = pacmanImages = new BufferedImage[4];
//         try {
//             ghostImages[0] = ImageIO.read(new File("blinky file"));
//             ghostImages[1] = ImageIO.read(new File("pinky file"));
//             ghostImages[2] = ImageIO.read(new File("inky file"));
//             ghostImages[3] = ImageIO.read(new File("clide file"));
//         } 
//         catch(IOException e) {};
//         updater = new Timer(40, new ActionListener() {
//                 public void actionPerformed(ActionEvent evt) {
//                     repaint();
//                 }
//             });
//         updater.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (menu){
            //displayMenu(g); <- this is the code that is suppose to be here
            world = new Maze(17, 17); //debug
            user = new User(1, 1, 0); //debug
            ghosts[0] = new ChasingGhost(1,1,user, world); //debug
            ghosts[1] = new AmbushGhost(1,1,user,world); //debug
            ghosts[2] = new UnpredictableGhost(1,1,user,world); //debug
            ghosts[3] = new StupidGhost(1,1,user,world); //debug
            displayGame(g); //debug -> test maze graphics
        }else if (game){
            if (pause){
                displayPauseMenu(g);
            }else{
                if (!started){
                    world = new Maze(17,17);
                    user = new User(1,1,0);
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
        final int gridSize = 20; //these are random numbers
        final int pointSize = 10;
        final int bigPointSize = 15;
        for( int i = 0; i < world.grid.length; i++){
            for (int j = 0; j < world.grid[i].length; j++){
                //add background for all cases
                g.setColor(Color.BLACK);
                g.fillRect(i*gridSize, j*gridSize, gridSize, gridSize);
                for (int k = 0; k < ghosts.length; k++) {
                    if (ghosts[k].r == i && ghosts[k].c == j) {
                        //g.drawImage(ghostImages[k], i*gridSize, j*gridSize, gridSize, gridSize, null, null);  
                    }
                }
                if (world.grid[i][j].obstacle){
                    g.setColor(Color.BLUE); //wall color
                    g.drawRect(i*gridSize, j*gridSize, gridSize, gridSize);
                }//else if (blinky.r == i && blinky.c == j){}    
                //else if (pinky.r == i && pinky.c == j){}
                //else if (inky.r == i && inky.c == j){}
                //else if (clyde.r == i && clyde.c == j){}
                else if (user.r == i && user.c == j){
                    for (int l = 0; l < pacmanImages.length; l++) {
                        if (user.orientation == l) {
                            //g.drawImage(pacmanImages[l], i*gridSize, j*gridSize, gridSize, gridSize, null, null);
                        }
                    }
                }else if (world.grid[i][j].point){
                    g.setColor(Color.WHITE);
                    g.fillOval(i*gridSize, j*gridSize, pointSize, pointSize);
                }else if (world.grid[i][j].bigPoint){
                    g.setColor(Color.WHITE);
                    g.fillOval(i*gridSize, j*gridSize, bigPointSize, bigPointSize);
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

    public void checkGameOver(){
        if (user.r == pinky.r && user.c == pinky.c){
            gameover = true;
        }else if (user.r == inky.r && user.c == inky.c){
            gameover = true;
        }else if (user.r == blinky.r && user.c == blinky.c){
            gameover = true;
        }else if (user.r == blinky.r && user.c == blinky.c){
            gameover = true;
        }
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
        pinky.performSimpleAgentTask();
        inky.performSimpleAgentTask();
        blinky.performSimpleAgentTask();
        clyde.performSimpleAgentTask();
    }

    public void updateTimerTask(String type){
        if (type.equals("GAME")){
            updater.stop();
            updater = new Timer(40, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        checkGameOver();
                        eatPoint();
                        repaint();
                        ghostUpdate();
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
