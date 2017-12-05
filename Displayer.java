import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Displayer extends JPanel implements KeyListener
{
    boolean menu, pause, game, started, gameover;
    Timer updater;
    Maze world;
    User user;
    Ghost blinky, inky, pinky, clyde;

    public Displayer(){
        super();
        menu = true;
        pause = false;
        game = false;
        started = false;
        gameover = false;
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
            displayMenu(g);
        }else if (game){
            if (pause){
                displayPauseMenu(g);
            }else{
                if (!started){
                    world = new Maze(17,17);
                    user = new User(1,1,0);
                    blinky = new ChasingGhost(1,1,user, world); 
                    pinky = new AmbushGhost(1,1,user,world);
                    inky = new UnpredictableGhost(1,1,user,world);
                    clyde = new StupidGhost(1,1,user,world);
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
        for( int i = 0; i < world.grid.length; i++){
            for (int j = 0; j < world.grid[i].length; j++){
                //add background for all cases
                if (world.grid[i][j].obstacle){

                }else if (blinky.r == i && blinky.c == j){

                }else if (pinky.r == i && pinky.c == j){

                }else if (inky.r == i && inky.c == j){

                }else if (clyde.r == i && clyde.c == j){

                }else if (user.r == i && user.c == j){

                }else if (world.grid[i][j].point){

                }else if (world.grid[i][j].bigPoint){

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
                        checkGhostsStatus();
                        eatPoint();
                        ghostUpdate();
                        checkVictory();
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
