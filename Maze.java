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
                for (int j = 0; j < c; j++)
                    grid[i][j].obstacle = true;
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
    //get rid of the dead ends
    public void breakWalls() {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                
                if (grid[r][c].obstacle == false) {// if current isn't a wall
                    //check if has dead ends
                    //up, left, right blocked
                    if (grid[r-1][c].obstacle && grid[r][c-1].obstacle && grid[r][c+1].obstacle) {
                        if (r-1 != 0) {// break top one
                            grid[r-1][c].obstacle = false;
                        }
                        //break right one
                        else if (c!= grid[0].length-1) {
                            grid[r][c+1].obstacle = false;
                        }
                        else { // break left one
                            grid[r][c-1].obstacle = false;
                        }
                    }
                    //up, left, down blocked
                    if (grid[r-1][c].obstacle && grid[r][c-1].obstacle && grid[r+1][c].obstacle) {
                        if (c-1!= 0) { //break left
                            grid[r][c-1].obstacle = false;
                        }
                        //break up
                        else if (r-1 != 0) {
                            grid[r-1][c].obstacle = false;
                        }
                        else { // break down
                            grid[r+1][c].obstacle = false;
                        }
                    }
                    //up, right, down blocked
                    if (grid[r-1][c].obstacle && grid[r][c+1].obstacle && grid[r+1][c].obstacle) {
                        if (c+1 != grid[0].length-1) {//break right
                            grid[r][c+1].obstacle = false;
                        }
                        
                        else if (r+1 != grid.length-1) {//break down
                            grid[r+1][c].obstacle = false;
                        }
                        else { // break up
                            grid[r-1][c].obstacle = false;
                        }
                    }
                    //left, right, down blocked
                    if (grid[r][c-1].obstacle && grid[r][c+1].obstacle && grid[r+1][c].obstacle) {
                        if (c-1 != 0) {//break left
                            grid[r][c-1].obstacle = false;
                        }
                        
                        else if (c+1 != grid[0].length-1) {//break right
                            grid[r][c+1].obstacle = false;
                        }
                        else {//break down
                            grid[r+1][c].obstacle = false;
                        }
                    }
                }
            }
        }
    }
}

