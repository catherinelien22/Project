public abstract class Ghost
{
    public int r,c;
    public Maze world;
    public User target;

    public Ghost(int a, int b, User user, Maze grid)
    {
        r = a;
        c = b;
        world = grid;
        target = user;
    }

    public abstract Node sense();

    private String decide(Node stuff){
        /*
         * preconditoin: the search function does not return diagonal results
         */
        Node goal = sense();
        if (goal.r -r == 1) // if goal is one below
            return "DOWN";
        else if (goal.c-c == 1) // if goal one to the right
            return "RIGHT";
        else if (goal.r-r == -1) // if goal one above
            return "UP";
        else if (goal.c-c == 1) // if goal one to the left
            return "LEFT";
        else
            return null;
    }

    private void act(String command){
        /*
         * precondition: the search function is correct and the direction is not an obstacle
         */
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
        act(decide(sense()));
    }

    public Node findAmbushPoint(){
        int currRow = target.r;
        int currCol = target.c;
        if (target.orientation == 0){ // faces UP
            while(!world.grid[currRow++][currCol].obstacle){}
            return world.grid[currRow-1][currCol];
        }else if (target.orientation == 1){ // faces RIGHT
            while(!world.grid[currRow][currCol++].obstacle){}
            return world.grid[currRow][currCol-1];
        }else if (target.orientation == 2){ // faces DOWN
            while(!world.grid[currRow--][currCol].obstacle){}
            return world.grid[currRow+1][currCol];
        }else if (target.orientation == 3){ // faces LEFT
            while(!world.grid[currRow][currCol--].obstacle){}
            return world.grid[currRow][currCol+1];
        }else 
            return null;
    }
}