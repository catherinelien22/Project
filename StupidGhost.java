public class StupidGhost extends Ghost
{
    public StupidGhost(int a, int b, User user, Maze grid){
        super(a,b,user,grid);
    }

    @Override
    public Node sense(){
        Node end = world.grid[target.r][target.c];
        Node start = world.grid[r][c];
        double distance = end.getDist(start);
        if (distance < 4)
            end = world.grid[1][1];
        return SearchMethod.greedySearch(start, end, world);
    }
}
