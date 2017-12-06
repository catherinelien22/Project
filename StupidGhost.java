public class StupidGhost extends Ghost
{
    private boolean corner;

    public StupidGhost(int a, int b, User user, Maze grid){
        super(a,b,user,grid);
        corner = false;
    }

    @Override
    public Node sense(){
        Node end = world.grid[target.r][target.c];
        Node start = world.grid[r][c];
        double distance = end.getDist(start);
        if (!corner && distance < 4)
            if (Math.random() <= 0.4)
                corner = true;
        if (corner && distance > 15 )
            if (Math.random() <= 0.2)
                corner = false;
        if (corner)
            end = world.grid[1][1];
        if (end.getDist(start) == 0)
            end = world.grid[target.r][target.c];
        return SearchMethod.greedySearch(start, end, world);
    }
}
