public class AmbushGhost extends Ghost
{
    public AmbushGhost(int a, int b, User user, Maze grid){
        super(a,b,user,grid);
        super.startC= mazeWidth / 2;
        super.startR = mazeHeight / 2;
    }

    @Override
    public void sense(){
        if (super.route.size()==0){
            Node end = findAmbushPoint();
            Node start = world.grid[r][c];
            route = SearchMethod.greedySearch(start, end, world);
        }
    }
}
