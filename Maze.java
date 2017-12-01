import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
public class Maze{
    public Node[][] grid;
    private int width,height;
    public Maze(int r, int c){
        if (r%2 == 0 || c % 2 == 0)
            return;
        else{
            grid = new Node[r][c];
            width = c; height = r;
                //set everything to wall
            for (int i = 0;  i< r; i++)
                for (int j = 0; j < c; j++){
                    grid[i][j] = new Node(i,j);
                    grid[i][j].obstacle = true;
                }
            recursiveDFSGenerate(1,1);
        } 
    }

    public void recursiveDFSGenerate(int r, int c){
        ArrayList<Integer> randomDirections = new ArrayList<Integer>();
        for (int i = 1; i <= 4; i++)
            randomDirections.add(i);
        Collections.shuffle(randomDirections);
        
        // Examine each direction
        for (int i = 0; i < randomDirections.size(); i++) {
            switch(randomDirections.get(i)){
                case 1:
                if (r - 2 <= 0)
                    continue;
                if (!grid[r - 2][c].obstacle) {
                    grid[r-2][c].obstacle = false;
                    grid[r-1][c].obstacle = false;
                    recursiveDFSGenerate(r - 2, c);
                }
                break;
                case 2:
                if (c + 2 >= width - 1)
                    continue;
                if (!grid[r][c + 2].obstacle) {
                    grid[r][c + 2].obstacle = false;
                    grid[r][c + 1].obstacle = false;
                    recursiveDFSGenerate(r, c + 2);
                }
                break;
                case 3:
                if (r + 2 >= height - 1)
                    continue;
                if (!grid[r + 2][c].obstacle) {
                    grid[r+2][c].obstacle = false;
                    grid[r+1][c].obstacle = false;
                    recursiveDFSGenerate(r + 2, c);
                }
                break;
                case 4:
                if (c - 2 <= 0)
                    continue;
                if (!grid[r][c - 2].obstacle) {
                    grid[r][c - 2].obstacle = false;
                    grid[r][c - 1].obstacle = false;
                    recursiveDFSGenerate(r, c - 2);
                }
                break;
            }
        }
    }    
}

