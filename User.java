
public class User
{
    public int points, life, r, c, orientation;
    //subject to change
    public User(int a, int b, int o) {
        r= a;
        c = b;
        orientation = o;
        life = 2;
    }
    
    public void die(){
        life--;
        if ( life >=0)
            resurrect();
    }
    
    public void resurrect(){
        //not implemented yet
    }
}
