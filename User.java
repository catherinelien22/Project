
public class User
{
    public int points, life, r, c, orientation;
    boolean specialMode;
    //subject to change
    public User(int a, int b, int o) {
        r= a;
        c = b;
        orientation = o;
        life = 0;
        specialMode = false;
    }
    
    public void die(){
        life--;
        if ( life >=0)
            resurrect();
        if (life < 0) { 
            Displayer.game = false;
            Displayer.started = false;
            Displayer.gameover = true; 
            System.out.println("IM dead");
        }
    }
    
    public void resurrect(){
        //not implemented yet
    }
}
