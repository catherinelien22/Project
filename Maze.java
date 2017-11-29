import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
public class Maze{
    public int[][] grid;
    private int width,height;
    public Maze(int r, int c){
        if (r%2 == 0 || c % 2 == 0)
            return;
        else{
            grid = new int[r][c];
            width = c; height = r;
                //set everything to wall
            for (int i = 0;  i< r; i++)
                for (int j = 0; j < c; j++)
                    grid[i][j] = 1;
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
                case 1: // Up
                //?Whether 2 cells up is out or not
                if (r - 2 <= 0)
                    continue;
                if (grid[r - 2][c] != 0) {
                    grid[r-2][c] = 0;
                    grid[r-1][c] = 0;
                    recursiveDFSGenerate(r - 2, c);
                }
                break;
                case 2: // Right
                // Whether 2 cells to the right is out or not
                if (c + 2 >= width - 1)
                    continue;
                if (grid[r][c + 2] != 0) {
                    grid[r][c + 2] = 0;
                    grid[r][c + 1] = 0;
                    recursiveDFSGenerate(r, c + 2);
                }
                break;
                case 3: // Down
                // Whether 2 cells down is out or not
                if (r + 2 >= height - 1)
                    continue;
                if (grid[r + 2][c] != 0) {
                    grid[r+2][c] = 0;
                    grid[r+1][c] = 0;
                    recursiveDFSGenerate(r + 2, c);
                }
                break;
                case 4: // Left
                // Whether 2 cells to the left is out or not
                if (c - 2 <= 0)
                    continue;
                if (grid[r][c - 2] != 0) {
                    grid[r][c - 2] = 0;
                    grid[r][c - 1] = 0;
                    recursiveDFSGenerate(r, c - 2);
                }
                break;
            }
        }
    }    
}

