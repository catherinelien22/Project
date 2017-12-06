import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;

public class Displayer extends JPanel implements KeyListener
{
    boolean menu, pause, game, started, gameover;
    static final int mazeWidth = 25, mazeHeight = 19;
    int score = 0;
    Timer updater;
    Maze world;
    User user;
    Ghost blinky, inky, pinky, clyde;
    Ghost[] ghosts;
    BufferedImage[] ghostImages, pacmanImages; //pacmanImages is for the different orientations of pacman
    BufferedImage wall;
    Button[] displayMenuButtons;
    Font currFont;

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
        displayMenuButtons = new Button[2];
        displayMenuButtons[0] = new Button("Start", true);
        displayMenuButtons[1] = new Button("Quit", false);
        try{
            currFont = Font.createFont(Font.TRUETYPE_FONT, new File("ARCADECLASSIC.TTF"));
            currFont = currFont.deriveFont(72f);
            this.setFont(currFont);
        }
        catch(IOException|FontFormatException e){System.out.println("set font failed");}
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
            displayMenu(g);
        }else if (game){
            if (pause){
                displayPauseMenu(g);
            }else{
                if (!started){
                    world = new Maze(mazeHeight,mazeWidth);
                    user = new User(1,1,0);
                    ghosts[0] = new ChasingGhost(mazeWidth/2-1,mazeHeight/2,user, world);
                    ghosts[1] = new AmbushGhost(mazeWidth/2,mazeHeight/2,user,world);
                    ghosts[2] = new UnpredictableGhost(mazeWidth/2+1, mazeHeight/2,user,world);
                    ghosts[3] = new StupidGhost(mazeWidth/2, mazeHeight/2+1,user,world);
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
        final Dimension frameSize = GameRunner.frame.getContentPane().getSize();
        g.setColor(Color.BLACK);
        System.out.println("debug");
        System.out.println(frameSize.getWidth() + " " +  frameSize.getHeight());
        g.fillRect(0, 0, (int) frameSize.getWidth(), (int) frameSize.getHeight());
        final int width = (int)(frameSize.getWidth() / 2);
        final int height = (int)(frameSize.getHeight() / 4);

        int buttonX = (int)(frameSize.getWidth() / 2 - width / 2);
        int buttonY = (int)(frameSize.getHeight() / 4 - height / 2);
        try {
            BufferedImage logo = ImageIO.read(new File("pacman logo.png"));
            g.drawImage(logo, buttonX, buttonY, width, height, null);
        } catch (IOException e) { System.out.println("logo ERROR"); };
        buttonY += frameSize.getHeight() / 4;

        for (int i = 0; i < displayMenuButtons.length; i++) {
            BufferedImage buttonImage = null;
            try {
                if (displayMenuButtons[i].selected) {
                    buttonImage = ImageIO.read(new File("clicked button.png")); 
                }
                else {
                    buttonImage = ImageIO.read(new File("button.png"));
                }
            } catch(IOException e) { System.out.println("ERROR"); };
            g.drawImage(buttonImage, buttonX, buttonY, width, height, null);
            //g.setFont(new Font(g.getFont().toString(), Font.PLAIN, 32));
            //g.setColor(); font is ugly
            //g.drawString(displayMenuButtons[i].name, buttonX + width / 2, buttonY + height / 2);
            buttonY += frameSize.getHeight() / 6;    
        }

    }

    public void displayPauseMenu(Graphics g){
        //background picture
        //selected buttons are bigger
        final Dimension frameSize = GameRunner.frame.getContentPane().getSize();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int) frameSize.getWidth(), (int) frameSize.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("PAUSED",(int)frameSize.getWidth()/2,(int)frameSize.getHeight()/3);
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
        g.drawString("GAMEOVER", 200,200);
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
                for (Button b : displayMenuButtons) {
                    b.selected = !b.selected;
                }
                updateTimerTask("");
            }else if (e.getKeyCode() == KeyEvent.VK_UP){
                for (Button b : displayMenuButtons) {
                    b.selected = !b.selected;
                }
                updateTimerTask("");
            }else if (e.getKeyCode() == KeyEvent.VK_ENTER){
                System.out.println("!");
                for (int i = 0; i < displayMenuButtons.length; i++) {
                    if (displayMenuButtons[i].selected && i == 0) {
                        game = true;
                        menu = false;
                    }
                    else if (displayMenuButtons[i].selected && i == 1) {
                        //quit game
                        GameRunner.frame.dispatchEvent(new WindowEvent(GameRunner.frame, WindowEvent.WINDOW_CLOSING));
                    }
                }
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
                        updateTimerTask("GAME");
                    }else if (e.getKeyCode() == KeyEvent.VK_UP){
                        user.orientation = 0;
                        if (!world.outOfBound(user.r-1,true) && !world.grid[user.r-1][user.c].obstacle)
                            user.r--;
                        updateTimerTask("GAME");
                    }else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                        user.orientation = 1;
                        if (!world.outOfBound(user.c+1,false) && !world.grid[user.r][user.c+1].obstacle)
                            user.c++;
                        updateTimerTask("GAME");
                    }else if (e.getKeyCode() == KeyEvent.VK_LEFT){
                        user.orientation = 3;
                        if (!world.outOfBound(user.c-1,false) && !world.grid[user.r][user.c-1].obstacle)
                            user.c--;
                        updateTimerTask("GAME");
                    }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        pause = true;
                        updateTimerTask("GAME");
                        //update Timer Task
                    }
                }else if (gameover){
                }
            }
        }
    }

    public void checkGhostsStatus(){
        //if ghosts are in special mode, ghosts return to mid and get reset to normal mode
        //else user dies and then if user's life >= 0, reset the ghosts, else gameover
        for (int i = 0; i < ghosts.length; i++) {
            if (user.specialMode) {
                try {
                    ghostImages[i] = ImageIO.read(new File("dead ghost.png"));
                } catch (IOException e) {};
                if (ghosts[i].r == user.r && ghosts[i].c == user.c) {
                    ghosts[i].dead = true;
                }
            } 
            else if (ghosts[i].r == user.r && ghosts[i].c == user.c && !user.specialMode) {
                user.die();
            }
        }
    }

    public void checkVictory(){
        //something happens
        boolean win = true;
        for (int r = 0; r < world.grid.length; r++) {
            for (int c = 0; c < world.grid[0].length; c++) {
                if (world.grid[r][c].point)
                    win = false;
            }
        }
        if (win) {
            game = false;
            started = false;
            gameover = true;
        }
    }

    public void eatPoint(){
        if (world.grid[user.r][user.c].point){
            //add to score
            score += 10;
            world.grid[user.r][user.c].point = false;
        }else if (world.grid[user.r][user.c].bigPoint){
            score += 50;
            world.grid[user.r][user.c].bigPoint = false;
            user.specialMode = true;
        }
    }

    public void ghostUpdate(){
        for (int i = 0; i < ghosts.length; i++) 
            ghosts[i].performSimpleAgentTask(); 
    }

    public void updateTimerTask(String type){
        if (type.equals("GAME")){
            updater.stop();
            updater = new Timer(100, new ActionListener() {
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
