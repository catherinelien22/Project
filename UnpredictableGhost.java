public class UnpredictableGhost extends Ghost
{
    /*if mode = 0 chase
     * if 1 ambush
     * if 2 stupid
     */
    private int mode;

    public UnpredictableGhost(int a, int b, User user, Maze grid){
        super(a,b,user,grid);
        mode = 0;   
    }

    @Override
    public Node sense(){
        Node end;
        final Node start = world.grid[r][c];
        if (mode%3 == 0){
            end = world.grid[target.r][target.c];
        }else if (mode % 3 == 1){
            end = findAmbushPoint();
        }else{
            end = findAmbushPoint();
            double distance = end.getDist(start);
            if (distance < 4)
                end = world.grid[1][1];
        }
        return SearchMethod.greedySearch(start, end, world);
    }
}
