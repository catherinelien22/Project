import javax.swing.JFrame;
import java.awt.Dimension;

public class Window extends JFrame
{
   private final int WIDTH = 800, HEIGHT = 600;
   private final String TITLE = "Pacman";
   
   public Window(){
       setTitle(TITLE);
       setResizable(false);
       setVisible(true);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(new Dimension(WIDTH,HEIGHT));
       
   }
}
