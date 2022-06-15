import java.awt.*;
import java.util.ArrayList;

public abstract class Item {
    protected int value = 0;
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

    public void interact(Item interactee) {
    }

    public void addValue(int value) {
        this.value += value;
    }

    public void setPosition(int x, int y) {
        this.x.set(0,x);
        this.y.set(0,y);
        map.setCoords(x, y, this);
    }

    public void die() {
        if (spawner == null) {
            x.clear();
            y.clear();
        } else {
            map.removeCoords(x.get(0), y.get(0), this);
            spawner.remove(this);
        }

    }

    public int[] getPosition() {
        return new int[] {x.get(0), y.get(0)};
    }

    public void setColor(Color c) {
        this.color = c;
    }
}

