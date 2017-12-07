import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
public abstract class Ghost
{
    public int r,c,startR,startC;
    public Maze world;
    public User target;
    boolean dead;
    int mazeWidth = Displayer.mazeWidth;
    int mazeHeight = Displayer.mazeHeight;
    static int timeUntilRevive,updateRate;
    public boolean scaredMode;

    public Ghost(int a, int b, User user, Maze grid)
    {
        r = b;
        c = a;
        world = grid;
        target = user;
        dead = false;
        updateRate = 2;
        scaredMode = false;
    }

    public abstract Node sense();

    private String decide(Node goal){
        /*
         * preconditoin: the search function does not return diagonal results
         */
        if (goal == null)
            return null;
        if(!scaredMode){
            if (goal.r -r == 1) // if goal is one below
                return "DOWN";
            else if (goal.c-c == 1) // if goal one to the right
                return "RIGHT";
            else if (goal.r-r == -1) // if goal one above
                return "UP";
            else if (goal.c-c == -1){ // if goal one to the left
                return "LEFT";
            }
        }else{
            String[] choices = {"DOWN","RIGHT","UP","LEFT"};
            String code = "";
            if (goal.r -r == 1){ // if goal is one below
                code = "DOWN";
            }else if (goal.c-c == 1){ // if goal one to the right
                code = "RIGHT";
            }else if (goal.r-r == -1){ // if goal one above
                code = "UP";
            }else if (goal.c-c == -1){ // if goal one to the left
                code = "LEFT";
            }
            String ans = code;
            boolean[] status = {world.grid[r+1][c].obstacle,world.grid[r][c+1].obstacle,world.grid[r-1][c].obstacle,world.grid[r][c-1].obstacle};
            int blockedSides = Maze.countWalls(status);
            while((ans.equals(code) || getNode(ans,r,c).obstacle) && blockedSides!= 3){
                ans = choices[(int)(Math.random()*choices.length)];
            }
            return ans;
        }
        return null;
    }
    
    public Node getNode(String dir, int r, int c){
        if (dir.equals("UP"))
            return world.grid[r-1][c];
        else if (dir.equals("DOWN"))
            return world.grid[r+1][c];
        else if (dir.equals("RIGHT"))
            return world.grid[ r][ c+1];
        else if (dir.equals("LEFT"))
            return world.grid[ r][ c-1];
        return null;
    }

    public void reset() {
        r = startR; c = startC;
        scaredMode = false;
    }

    private void act(String command){
        /*
         * precondition: the search function is correct and the direction is not an obstacle
         */
        if (command == null)
            return;
        if (command.equals("UP"))
            r--;
        else if (command.equals("DOWN"))
            r++;
        else if (command.equals("RIGHT"))
            c++;
        else if (command.equals("LEFT"))
            c--;
    }

    public void performSimpleAgentTask(){
        if (scaredMode){
            updateRate = 3;
        }else
            updateRate = 2;
        if (!dead){
            act(decide(sense()));
        }else if (dead) {
            reset();
            if (timeUntilRevive-- == 0){
                dead = false;
                scaredMode = false;
            }
        }
    }

    public Node findAmbushPoint(){
        int currRow = target.r;
        int currCol = target.c;
        if (target.orientation == 0){ // faces UP
            while(!world.grid[currRow--][currCol].obstacle){}
            return world.grid[currRow+2][currCol];
        }else if (target.orientation == 1){ // faces RIGHT
            while(!world.grid[currRow][currCol++].obstacle){}
            return world.grid[currRow][currCol-2];
        }else if (target.orientation == 2){ // faces DOWN
            while(!world.grid[currRow++][currCol].obstacle){}
            return world.grid[currRow-2][currCol];
        }else if (target.orientation == 3){ // faces LEFT
            while(!world.grid[currRow][currCol--].obstacle){}
            return world.grid[currRow][currCol+2];
        }else 
            return null;
    }
}
