public class StupidGhost extends Ghost
{
    private boolean corner;

    public StupidGhost(int a, int b, User user, Maze grid){
        super(a,b,user,grid);
        corner = false;
        super.startC = mazeWidth / 2 + 1;
        super.startR = mazeHeight / 2;
    }

    @Override
    public void sense(){
        if (route.size() == 0){
            Node end = world.grid[target.r][target.c];
            Node start = world.grid[r][c];
            double distance = end.getDist(start);
            if (distance < 4)
                corner = true;
            if (corner)
                end = world.grid[1][1];
            /*if (end.getDist(start) == 0)
                end = world.grid[target.r][target.c];*/
            route= SearchMethod.greedySearch(start, end, world);
        }
    }
}
