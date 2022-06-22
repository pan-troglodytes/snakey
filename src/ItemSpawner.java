import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class ItemSpawner implements ActionListener {
    static Blueprint map;
    ArrayList<Item> items = new ArrayList<>();
    int itemMax = 3;
    int[] coordsTopLeft;
    int[] coordsBotRight;

    Class<?> itemType;
    private Timer timer;
    public static void setBlueprint(Blueprint b) {
        map = b;
    }
    ItemSpawner(Class<?> itemType, int x1, int y1, int x2, int y2, int max, int delay) {
        this.timer = new Timer(delay, this);
        timer.start();
        coordsTopLeft = new int[] {x1,y1};
        coordsBotRight = new int[] {x2,y2};
        this.itemType = itemType;
        itemMax = max;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (items.size() < itemMax) {
            Item itemNew = null;
            try {
                itemNew = (Item) itemType.getDeclaredConstructors()[0].newInstance(this);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
            ArrayList<Integer> newPos;
            ArrayList<ArrayList<Integer>> freeSpace = new ArrayList<>();
            for (int i=coordsTopLeft[0]; i <= coordsBotRight[0]; i++) {
                for (int j=coordsTopLeft[1]; j <= coordsBotRight[1]; j++) {
                    if (map.getCoords(i,j).size() == 0) {
                        freeSpace.add(new ArrayList<>());
                        freeSpace.get(freeSpace.size()-1).add(i);
                        freeSpace.get(freeSpace.size()-1).add(j);
                    }
                }
            }
            if (freeSpace.size() > 0) {
                newPos = freeSpace.get(new Random().nextInt(freeSpace.size()));
                itemNew.setPosition(newPos.get(0), newPos.get(1));
                items.add(itemNew);
            }
        }
    }

    public void drawItems(Graphics g, int tileSize) {
        for (Item item : items) {
            if (item != null) {
                item.draw(g, tileSize);
            }
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void remove(Item i) {
        items.remove(i);
    }
}
