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
import java.awt.FontMetrics;

public class Displayer extends JPanel implements KeyListener
{
    boolean menu, pause, game, started, gameover,victory,resumed,updatedUser;
    static final int mazeWidth = 25, mazeHeight = 19;
    int score = 0;
    Timer updater, specialModeTimer;
    Maze world;
    User user;
    Ghost blinky, inky, pinky, clyde;
    Ghost[] ghosts;
    BufferedImage[] ghostImages, pacmanImages; //pacmanImages is for the different orientations of pacman
    BufferedImage wall, pointer;
    Button[] displayMenuButtons,pauseMenuButtons,gameOverMenuButtons;
    Font currFont;
    FontMetrics metrics;
    int updated;

    public Displayer(){
        super();
        menu = true;
        pause = false;
        game = false;
        started = false;
        gameover = false;
        resumed = false;
        updatedUser = false;
        ghosts = new Ghost[4];
        ghostImages = new BufferedImage[4];
        pacmanImages = new BufferedImage[4];
        pauseMenuButtons = new Button[3];
        displayMenuButtons = new Button[2];
        gameOverMenuButtons = new Button[2];
        displayMenuButtons[0] = new Button("Start", true);
        displayMenuButtons[1] = new Button("Quit", false);
        pauseMenuButtons[0] = new Button("Continue", true);
        pauseMenuButtons[1] = new Button("New Game", false);
        pauseMenuButtons[2] = new Button("To Menu", false);
        gameOverMenuButtons[0] = new Button("New Game", true);
        gameOverMenuButtons[1] = new Button("Menu", false);
        int updated = 0;
        try{
            currFont = Font.createFont(Font.TRUETYPE_FONT, new File("ARCADECLASSIC.TTF"));
            currFont = currFont.deriveFont(72f);
            this.setFont(currFont);
            metrics = this.getFontMetrics(currFont);
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
            pointer = ImageIO.read(new File("pointer.png"));
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
        //super.paintComponent(g);
        if (menu){
            super.paintComponent(g);
            displayMenu(g);
        }else if (game){
            if (pause){
                super.paintComponent(g);
                displayPauseMenu(g);
            }else{
                if (!started){
                    super.paintComponent(g);
                    world = new Maze(mazeHeight,mazeWidth);
                    user = new User(1,1,0);
                    ghosts[0] = new ChasingGhost(mazeWidth/2-1,mazeHeight/2,user, world);
                    ghosts[1] = new AmbushGhost(mazeWidth/2,mazeHeight/2,user,world);
                    ghosts[2] = new UnpredictableGhost(mazeWidth/2+1, mazeHeight/2,user,world);
                    ghosts[3] = new StupidGhost(mazeWidth/2, mazeHeight/2+1,user,world);
                    displayGameBackground(g);
                    updateTimerTask("GAME");
                    started = true;
                }else if (gameover){
                    super.paintComponent(g);
                    displayGameOver(g);
                }else if (resumed){
                    super.paintComponent(g);
                    displayGameBackground(g);
                    updateTimerTask("GAME");
                    resumed = false;
                }else if (started && !gameover){
                    displayGame(g);
                }
            }
        }
    }

    public void displayMenu(Graphics g){
        //background picture
        final Dimension frameSize = GameRunner.frame.getContentPane().getSize();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int) frameSize.getWidth(), (int) frameSize.getHeight());
        final int width = (int)(frameSize.getWidth() / 2);
        final int height = (int)(frameSize.getHeight() / 4);
        int buttonX = (int)(frameSize.getWidth() / 2 - width / 2);
        int buttonY = (int)(frameSize.getHeight() / 4 - height / 2);
        try {
            BufferedImage logo = ImageIO.read(new File("pacman logo.png"));
            g.drawImage(logo, buttonX, buttonY, width, height, null);
        } catch (IOException e) { System.out.println("logo ERROR"); };
        buttonY += frameSize.getHeight() / 2;
        for (int i = 0; i < displayMenuButtons.length; i++) {
            g.setColor(Color.WHITE);
            drawCenteredString(g,displayMenuButtons[i].name,(int)(frameSize.getWidth()/2),buttonY);
            if(displayMenuButtons[i].selected){
                int pointX = determinePointerLocation(displayMenuButtons[i].name,(int)(frameSize.getWidth()/2));
                /*int pointHeight = determinePointerHeight(displayMenuButtons[i].name);*/
                int stringHeight = metrics.getHeight();
                g.drawImage(pointer,pointX,buttonY-stringHeight,stringHeight/2,stringHeight,null, null);
            }
            buttonY += frameSize.getHeight() / 6;
        }
    }

    public int determinePointerLocation(String str, int starting){
        return (starting-(int)(metrics.stringWidth(str)/2)-(int)(1.2*metrics.getHeight()/2));
    }

    public void displayPauseMenu(Graphics g){
        final Dimension frameSize = GameRunner.frame.getContentPane().getSize();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int) frameSize.getWidth(), (int) frameSize.getHeight());
        g.setColor(Color.YELLOW);
        drawCenteredString(g,"GAME PAUSED",(int)frameSize.getWidth()/2,(int)frameSize.getHeight()/5);
        int buttonY = (int)frameSize.getHeight()/5*2;
        for (int i = 0; i < pauseMenuButtons.length; i++) {
            g.setColor(Color.WHITE);
            drawCenteredString(g,pauseMenuButtons[i].name,(int)(frameSize.getWidth()/2),buttonY);
            if(pauseMenuButtons[i].selected){
                int pointX = determinePointerLocation(pauseMenuButtons[i].name,(int)(frameSize.getWidth()/2));
                /*int pointHeight = determinePointerHeight(displayMenuButtons[i].name);*/
                int stringHeight = metrics.getHeight();
                g.drawImage(pointer,pointX,buttonY-stringHeight,stringHeight/2,stringHeight,null, null);
            }
            buttonY += (int)frameSize.getHeight()/5;
        }
    }

    public void drawCenteredString(Graphics g, String text, int x, int y){
        x-=(int)(metrics.stringWidth(text)/2);
        g.drawString(text, x,y);
    }

    public void displayGameBackground(Graphics g){
        final Dimension fs = GameRunner.frame.getContentPane().getSize();
        final int gridSize =  (int) (fs.getHeight() / mazeHeight); 
        final int pointSize = 10;
        final int bigPointSize = 15;
        for( int i = 0; i < world.grid.length; i++){
            for (int j = 0; j < world.grid[i].length; j++){
                //add background for all cases
                /*g.setColor(Color.BLACK);
                g.fillRect(j*gridSize, i*gridSize, gridSize, gridSize);*/
                if (world.grid[i][j].obstacle){
                    try {
                        String fileName = "wall"+generateCode(i,j)+".png";
                        BufferedImage wallImage = ImageIO.read(new File(fileName));
                        g.drawImage(wallImage, j*gridSize, i*gridSize, gridSize, gridSize, null, null);
                    } 
                    catch(IOException e) {System.out.println("ERROR");};
                }
            }
        }
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
                if (!world.grid[i][j].obstacle){
                    g.setColor(Color.BLACK);
                    g.fillRect(j*gridSize, i*gridSize, gridSize, gridSize);
                    if (world.grid[i][j].point){
                        g.setColor(Color.WHITE);
                        g.fillOval(j*gridSize, i, pointSize, pointSize);
                    }else if (world.grid[i][j].bigPoint){
                        g.setColor(Color.YELLOW);
                        g.fillOval(j*gridSize, i, bigPointSize, bigPointSize);
                    }
                }
            }
        }
        //draw the ghosts
        for (int k = 0; k < ghosts.length; k++) {
            g.drawImage(ghostImages[k], ghosts[k].c*gridSize, ghosts[k].r*gridSize, gridSize, gridSize, null, null); 
        }
        g.drawImage(pacmanImages[user.orientation], user.c*gridSize, user.r*gridSize, gridSize, gridSize, null, null);

        g.drawString("SCORE", (int)(fs.getWidth() * 0.75), (int)(fs.getHeight() * 0.5));
        g.drawString(Integer.toString(score), (int)(fs.getWidth() * 0.75), (int)(fs.getHeight() * 0.75));
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
        final Dimension frameSize = GameRunner.frame.getContentPane().getSize();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int) frameSize.getWidth(), (int) frameSize.getHeight());
        g.setColor(Color.YELLOW);
        drawCenteredString(g,"GAME OVER",(int)frameSize.getWidth()/2,(int)frameSize.getHeight()/5);
        int buttonY = (int)frameSize.getHeight()/4;
        for (int i = 0; i < gameOverMenuButtons.length; i++) {
            g.setColor(Color.WHITE);
            drawCenteredString(g,gameOverMenuButtons[i].name,(int)(frameSize.getWidth()/2),buttonY);
            if(gameOverMenuButtons[i].selected){
                int pointX = determinePointerLocation(gameOverMenuButtons[i].name,(int)(frameSize.getWidth()/2));
                /*int pointHeight = determinePointerHeight(displayMenuButtons[i].name);*/
                int stringHeight = metrics.getHeight();
                g.drawImage(pointer,pointX,buttonY-stringHeight,stringHeight/2,stringHeight,null, null);
            }
            buttonY += (int)frameSize.getHeight()/4;
        }
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
            if (e.getKeyCode() == KeyEvent.VK_DOWN||e.getKeyCode() == KeyEvent.VK_UP){
                for (Button b : displayMenuButtons) {
                    b.selected = !b.selected;
                }
                updateTimerTask("");
            }else if (e.getKeyCode() == KeyEvent.VK_ENTER){
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
                    for (int i = 0;  i< pauseMenuButtons.length; i++){
                        if (pauseMenuButtons[i].selected){
                            pauseMenuButtons[i].selected = false;
                            pauseMenuButtons[(i+1)%pauseMenuButtons.length].selected = true;
                            break;
                        }
                    }
                }else if (e.getKeyCode() == KeyEvent.VK_UP){
                    for (int i = pauseMenuButtons.length-1;  i>=0; i--){
                        if (pauseMenuButtons[i].selected){
                            pauseMenuButtons[i].selected = false;
                            pauseMenuButtons[(i-1+pauseMenuButtons.length)%pauseMenuButtons.length].selected = true;
                            break;
                        }
                    }
                }else if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    if (pauseMenuButtons[0].selected){
                        pause = false;
                        resumed = true;
                    }else if (pauseMenuButtons[1].selected){
                        pause = false;
                        started = false;
                    }else if (pauseMenuButtons[2].selected){
                        game = false;
                        started = false;
                        pause = false;
                        menu = true;
                    }
                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pause = false;
                    updateTimerTask("GAME");
                }
            }else{
                if (started && !gameover){
                    if(updated %4 == 0){
                        if (e.getKeyCode() == KeyEvent.VK_DOWN){
                            user.orientation = 2;
                            if (!world.outOfBound(user.r+1,true) && !world.grid[user.r+1][user.c].obstacle){
                                user.r++;
                            }
                        }else if (e.getKeyCode() == KeyEvent.VK_UP){
                            user.orientation = 0;
                            if (!world.outOfBound(user.r-1,true) && !world.grid[user.r-1][user.c].obstacle){
                                user.r--;
                            }
                        }else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                            user.orientation = 1;
                            if (!world.outOfBound(user.c+1,false) && !world.grid[user.r][user.c+1].obstacle){
                                user.c++;
                            }
                        }else if (e.getKeyCode() == KeyEvent.VK_LEFT){
                            user.orientation = 3;
                            if (!world.outOfBound(user.c-1,false) && !world.grid[user.r][user.c-1].obstacle){
                                user.c--;
                            }
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        pause = true;
                        updateTimerTask("");
                    }
                }else if (gameover){
                    if (e.getKeyCode() == KeyEvent.VK_DOWN||e.getKeyCode() == KeyEvent.VK_UP){
                        for (Button b : displayMenuButtons) {
                            b.selected = !b.selected;
                        }
                        updateTimerTask("");
                    }else if (e.getKeyCode() == KeyEvent.VK_ENTER){
                        for (int i = 0; i < gameOverMenuButtons.length; i++) {
                            if (gameOverMenuButtons[i].selected && i == 0) {
                                started = false;
                                gameover = false;
                            }
                            else if (gameOverMenuButtons[i].selected && i == 1) {
                                //back to menu
                                menu = true;
                                pause = false;
                                game = false;
                                started = false;
                                gameover = false;
                                resumed = false;
                                updatedUser = false;
                            }
                        }
                    }
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
            specialModeTimer = new Timer(100, e -> {
                user.specialMode = true;
                specialModeTimer.stop();
            });
            specialModeTimer.start();
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
                        checkGhostsStatus();
                        eatPoint();
                        if (updated % 20 == 0)
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
