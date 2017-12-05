import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
public class Maze extends JPanel {
    public static Node[][] grid;
    public static int width,height;
    public Maze(int r, int c){
        if (((r-1)/2)%2==1 && ((c-3)/2)%2==1){
            grid = new Node[r][c];
            width = c; height = r;
            //set everything to wall
            for (int i = 0;  i< r; i++)
                for (int j = 0; j < c; j++){
                    grid[i][j] = new Node(i,j);
                    grid[i][j].obstacle = true;
                } 
            //set middle part to not wall to prevent expansion there
            grid[r/2][c/2-1].obstacle = false;
            grid[r/2][c/2].obstacle = false;
            grid[r/2+1][c/2].obstacle = false;
            grid[r/2+2][c/2].obstacle = false;
            grid[r/2][c/2+1].obstacle = false;
            grid[r/2][c/2-1].obstacle = false;
            grid[r/2][c/2-2].unbreakable = true;
            grid[r/2][c/2+2].unbreakable=true;
            grid[r/2-1][c/2-1].unbreakable = true;
            grid[r/2-1][c/2].unbreakable= true;
            grid[r/2-1][c/2+1].unbreakable = true;
            grid[r/2+1][c/2-1].unbreakable = true;
            grid[r/2+1][c/2+1].unbreakable = true;
            recursiveDFSGenerate(1,1);
            //removeDeadEnds();
            addPoints();
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
                if (grid[r - 2][c].obstacle) {
                    grid[r-2][c].obstacle = false;
                    grid[r-1][c].obstacle = false;
                    recursiveDFSGenerate(r - 2, c);
                }
                break;
                case 2:
                if (c + 2 >= width - 1)
                    continue;
                if (grid[r][c + 2].obstacle) {
                    grid[r][c + 2].obstacle = false;
                    grid[r][c + 1].obstacle = false;
                    recursiveDFSGenerate(r, c + 2);
                }
                break;
                case 3:
                if (r + 2 >= height - 1)
                    continue;
                if (grid[r + 2][c].obstacle) {
                    grid[r+2][c].obstacle = false;
                    grid[r+1][c].obstacle = false;
                    recursiveDFSGenerate(r + 2, c);
                }
                break;
                case 4:
                if (c - 2 <= 0)
                    continue;
                if (grid[r][c - 2].obstacle) {
                    grid[r][c - 2].obstacle = false;
                    grid[r][c - 1].obstacle = false;
                    recursiveDFSGenerate(r, c - 2);
                }
                break;
            }
        }
    }    
    //get rid of the dead ends
    public static void removeDeadEnds() {
        for (int r = 1; r < grid.length-1; r++) {
            for (int c = 1; c < grid[0].length-1; c++) {
                if (!grid[r][c].obstacle) {// if current isn't a wall
                    //check if has dead ends
                    boolean[] status = {grid[r+1][c].obstacle,grid[r][c+1].obstacle,grid[r-1][c].obstacle,grid[r][c-1].obstacle};
                    int blockedSides = countWalls(status);
                    if (r == height/2 && c == width/2+1) continue;
                    if (r== height/2 && c == width/2-1) continue;
                    if (blockedSides != 3)
                        continue;
                    else{
                        boolean removedWall = false;
                        while(!removedWall){
                            switch((int)(Math.random()*4)+1){
                                case 1:
                                if (outOfBound(r+1,true) || (grid[r+1][c].obstacle && grid[r+1][c].unbreakable) || 
                                    !grid[r+1][c].obstacle)
                                    continue;
                                else{
                                    grid[r+1][c].obstacle = false;
                                    removedWall = true;
                                    continue;
                                }
                                case 2:
                                if (outOfBound(c+1,false) || (grid[r][c+1].unbreakable&& grid[r][c+1].obstacle )|| 
                                    !grid[r][c+1].obstacle)
                                    continue;
                                else{
                                    grid[r][c+1].obstacle = false;
                                    removedWall = true;
                                    continue;
                                }
                                case 3:
                                if (outOfBound(r-1,true) || (grid[r-1][c].unbreakable && grid[r-1][c].obstacle) || 
                                    !grid[r-1][c].obstacle)
                                    continue;
                                else{
                                    grid[r-1][c].obstacle = false;
                                    removedWall = true;
                                    continue;
                                }
                                case 4:
                                if (outOfBound(c-1,false) || (grid[r][c-1].unbreakable&& grid[r][c-1].obstacle )|| 
                                    !grid[r][c-1].obstacle)
                                    continue;
                                else{
                                    grid[r][c-1].obstacle = false;
                                    removedWall = true;
                                    continue;
                                }
                            }
                        }
                    }   
                }
            }
        }
    }

    public static int countWalls(boolean[] stuff){
        int ans = 0;
        for (int i = 0; i < stuff.length; i++)
            if (stuff[i])
                ans++;
        return ans;
    }

    public static boolean outOfBound(int i, boolean r){
        if (r){
            if (i <height-1 && i >0)
                return false;
        }else{
            if (i < width-1 && i > 0)
                return false;
        }
        return true;
    }

    //just for debugging don't use it lmao
    public static void main (String[] args){
        Maze stuff = new Maze(19,25);
        StdDraw.setCanvasSize(700,700);
        StdDraw.setYscale(0,700);
        StdDraw.setXscale(0,700);
        drawMap(stuff);
        removeDeadEnds();
        drawMap(stuff);
    }
    
    public static void drawMap(Maze stuff){
        for (int i = 0; i < stuff.height; i++){
            for (int j = 0; j < stuff.width; j++){
                if (stuff.grid[i][j].unbreakable){
                    StdDraw.setPenColor(StdDraw.GREEN);
                }else if (stuff.grid[i][j].obstacle){
                    StdDraw.setPenColor(StdDraw.BLACK);
                }else{
                    StdDraw.setPenColor(StdDraw.WHITE);
                }
                StdDraw.filledRectangle(j*20+10,i*20+10,10,10);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.rectangle(j*20+10,i*20+10,10,10);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Maze stuff = new Maze(width,height);
        for (int i = 0; i < stuff.height; i++){
            for (int j = 0; j < stuff.width; j++){
                if (stuff.grid[i][j].obstacle){
                    g.setColor(Color.BLACK);
                }else{
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j*20+10,i*20+10,20,20);
                g.setColor(Color.BLACK);
                g.drawRect(j*20+10,i*20+10,20,20);
            }
        }

        stuff.removeDeadEnds();

        for (int i = 0; i < stuff.height; i++){
            for (int j = 0; j < stuff.width; j++){
                if (stuff.grid[i][j].obstacle){
                    g.setColor(Color.BLACK);
                }else{
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j*20+350,i*20+350,20,20);
                g.setColor(Color.BLACK);
                g.drawRect(j*20+350,i*20+350,20,20);
            }
        }
    }

    public void addPoints(){
        //loop through the entire Maze
        //place specific amount of "Big Points"
        //the rest are points   
    }
}

