import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ItemSpawner implements ActionListener {
    private static Blueprint map;
    private ArrayList<Item> items = new ArrayList<>();
    private int itemsMax = 3;
    private int col;
    private int row;

    private int[] coordsTopLeft;
    private int[] coordsBotRight;

    public static void setBlueprint(Blueprint b) {
        map = b;
    }
    ItemSpawner(int x1, int y1, int x2, int y2 ) {
        new Timer(1500, this).start();
        coordsTopLeft = new int[] {x1,y1};
        coordsBotRight = new int[] {x2,y2};
        col = map.xy.size();
        row = map.xy.get(0).size();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (items.size() < itemsMax) {
            Item itemNew = new Apple(this);
            int[] newPos = new int[2];
            do {
                newPos[0] = ThreadLocalRandom.current().nextInt(coordsTopLeft[0], coordsTopLeft[1]);
                newPos[1] = ThreadLocalRandom.current().nextInt(coordsBotRight[0], coordsBotRight[1]);
            } while (map.getCoords(newPos[0], newPos[1]).iterator().hasNext() && map.getCoords(newPos[0], newPos[1]).iterator().next().equals(Snake.class));
            itemNew.setPosition(newPos[0], newPos[1]);
            items.add(itemNew);
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
