public class Node implements Comparable<Node> {
	public int c, r;
	//for searching and fringe 
	public double f, h;
	//for backtrack
	public Node parent;
	//for drawing and searching
	public boolean obstacle,point,bigPoint;

	public Node(int a, int b) {
		r = a;
		c = b;
		parent = null;
		obstacle = false;
		point = false;
		bigPoint = false;
		f = 0;
		h = 0;
	}

	public double getDist(Node stuff) {
		return (Math.sqrt(Math.pow(c - stuff.c, 2) + Math.pow(r - stuff.r, 2)));
	}

	public int compareTo(Node stuff) {
		return (int) ((this.f - stuff.f) * 100);
	}

	public void reset() {
		parent = null;
		f = 0;
		h = 0;
	}
}