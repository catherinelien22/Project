public class ChasingGhost extends Ghost
{    
    public ChasingGhost(int a, int b, User user, Maze grid){
        super(a,b,user, grid);
        super.startR = mazeWidth / 2 - 1;
        super.startC = mazeHeight / 2;
    }
    
    @Override
    public Node sense(){
        Node end = world.grid[target.r][target.c];
        Node start = world.grid[r][c];
        return SearchMethod.greedySearch(start, end, world);
    }
}
