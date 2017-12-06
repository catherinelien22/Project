import java.util.ArrayList;
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
        super.startC = mazeWidth / 2;
        super.startR = mazeHeight / 2 + 1;
    }

    @Override
    public void sense(){
        if (route.size() == 0){
            Node end;
            final Node start = world.grid[r][c];
            mode = (int)(Math.random()*3);
            if (mode%3 == 0){
                if (super.route.size()==0){
                    end = world.grid[target.r][target.c];
                    route = SearchMethod.greedySearch(start, end, world);
                    return;
                }
            }else if (mode % 3 == 1){
                end = findAmbushPoint();
                route = SearchMethod.greedySearch(start,end,world);
                return;
            }else{ //stupid ghost
                end = world.grid[target.r][target.c];
                double distance = end.getDist(start);
                if (distance < 4)
                    end = world.grid[1][1];
                /*if (end.getDist(start) == 0)
                end = world.grid[target.r][target.c];*/
                route= SearchMethod.greedySearch(start, end, world);
                return;
            }
        }
    }
}
