import javax.swing.JFrame;
import java.awt.Dimension;

public class Window extends JFrame
{
   private final int WIDTH = 800, HEIGHT = 600; //800, 600
   private final String TITLE = "Pacman";
   
   public Window(){
       setTitle(TITLE);
       setResizable(false);
       setVisible(true);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       add(new Maze(17, 17)); 
       setSize(new Dimension(WIDTH,HEIGHT));
       
   }
}
