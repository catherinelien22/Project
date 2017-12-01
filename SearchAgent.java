import java.util.PriorityQueue;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class SearchAgent{
    public SearchAgent() {
    }
    //does the searching and the animation
    public void greedySearch(Node start, Node end, Maze world) {
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        ArrayList<Node> visited = new ArrayList<Node>();
        start.h = start.getDist(end);
        start.f = start.h;
        queue.add(start);
        while (queue.size() > 0) {
            Node q = queue.poll();
            if (q.equals(end)) {
                ArrayList<Node> list = new ArrayList<>();
                list = backTrack(q, list);
                list.add(0, q);
                Collections.reverse(list);
                /*for (int i = 0; i < list.size(); i++) {
                    list.get(i).solution = true;
                }*/
                return;
            }
            visited.add(q);
            // all the neighbors
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    int currR = i + q.r;
                    int currC = j + q.c;
                    if (i*j != 0) //no diagonal
                        continue;
                    if (currR >= world.height || currR < 0)
                        continue;
                    if (currC >= world.width || currC < 0)
                        continue;
                    Node child = world.grid[currR][currC];
                    if (child.obstacle)
                        continue;
                    double currF = child.getDist(end);
                    if (visited.contains(child)) // expanded this place
                        continue;
                    else if (queue.contains(child) && currF >= child.f) {
                        continue;
                    } else {
                        if (!child.obstacle) { // if the child isn't an obstacle
                            child.h = child.getDist(end);
                            child.f = child.h;
                            queue.add(child);
                            child.parent = q;
                        }
                    }
                }
            }
            return;
        }
    }

    //does various type of reset. references to the reset defined in the class "Node"
    public void reset(Maze world) {
        for (int i = 0; i < world.grid.length; i++)
            for (int j = 0; j < world.grid[0].length; j++)
                world.grid[i][j].reset();
    }

    //backtrack for the solution
    public ArrayList backTrack(Node child, ArrayList list) {
        if (child.parent == null) {
            return list;
        } else {
            ArrayList ans = list;
            ans.add(child.parent);
            return backTrack(child.parent, ans);
        }
    }

}