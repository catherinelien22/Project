public class AmbushGhost extends Ghost
{
    public AmbushGhost(int a, int b, User user, Maze grid){
        super(a,b,user,grid);
        super.startC= mazeWidth / 2;
        super.startR = mazeHeight / 2;
    }

    @Override
    public Node sense(){
            Node end = findAmbushPoint();
            Node start = world.grid[r][c];
            return SearchMethod.greedySearch(start, end, world);
    }
}
