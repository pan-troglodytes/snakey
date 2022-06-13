import java.awt.*;
import java.util.ArrayList;

public abstract class Item {
    protected ArrayList<Integer> x = new ArrayList<>();
    protected ArrayList<Integer> y = new ArrayList<>();
    protected Color color;
    protected static Blueprint map;
    protected ItemSpawner spawner;

    public static void setBlueprint(Blueprint b) {
        map = b;
    }


    public Item(ItemSpawner spawner) {
        this.spawner = spawner;
    };

    public void draw(Graphics g, int tileSize) {
        for (Integer i:x) {
            for (Integer j:y) {
                g.setColor(color);
                g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
            }
        }
    }

    public void setPosition(int x, int y) {
        this.x.set(0,x);
        this.y.set(0,y);
        map.setCoords(x, y, this);
    }

    public void die() {
        spawner.remove(this);
    }


}
