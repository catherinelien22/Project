import javax.swing.JFrame;

public class GameRunner
{
    static JFrame frame;
    public static void main (String args[]){
        frame = new JFrame("Pac-Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Displayer game = new Displayer();
        frame.add(game);
        frame.setSize(1280,720);
        //frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
