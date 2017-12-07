import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;

public class SearchMethod{
    public static Node greedySearch(Node start, Node end, Maze world) {
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
                reset(world);
                if (list.size() > 1)
                    return list.get(1);
                else
                    return list.get(0);
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
        }
        return null;
    }

    //does various type of reset. references to the reset defined in the class "Node"
    public static void reset(Maze world) {
        for (int i = 0; i < world.grid.length; i++)
            for (int j = 0; j < world.grid[0].length; j++)
                world.grid[i][j].reset();
    }

    //backtrack for the solution
    public static ArrayList backTrack(Node child, ArrayList list) {
        if (child.parent == null) {
            return list;
        } else {
            ArrayList ans = list;
            ans.add(child.parent);
            return backTrack(child.parent, ans);
        }
    }
}