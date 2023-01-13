import java.awt.Color;
import java.util.ArrayList;

public class Drawing extends Item{
    public Drawing(String idSpawner) {
        super(idSpawner);
    }
    public Drawing(ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Character> d, Color c, String id, int value) {
        super(null);
        this.x = x;
        this.y = y;
        this.d = d;
        setId(id);
        setColor(c);
        this.value = value;
    }

}
