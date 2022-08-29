/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class ItemSpawner {
    ArrayList<Item> items = new ArrayList<>();
    int itemMax = 3;
    int[] coordsTopLeft;
    int[] coordsBotRight;

    Class<?> itemType;
    int time = 0;
    int delay;
    ItemSpawner(Class<?> itemType, int x1, int y1, int x2, int y2, int max, int delay) {
        coordsTopLeft = new int[] {x1,y1};
        coordsBotRight = new int[] {x2,y2};
        this.itemType = itemType;
        itemMax = max;
        this.delay = delay;
    }

    public Item spawnItem(ArrayList<Item> existingItems) {
        time++;
        if (time >= delay) {
            time = 0;
            if (items.size() < itemMax) {
                Item itemNew = null;
                try {
                    itemNew = (Item) itemType.getDeclaredConstructors()[0].newInstance(this.toString());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
                ArrayList<ArrayList<Integer>> freeSpaces = new ArrayList<>();
                boolean free = true;
                for (int i=coordsTopLeft[0]; i < coordsBotRight[0]; i++) {
                    for (int j=coordsTopLeft[1]; j < coordsBotRight[1]; j++) {
                        free = true;
                        for (int k=0; k < existingItems.size(); k++) {
                            for (int l=0; l < existingItems.get(k).x.size(); l++) {
                                if (i == existingItems.get(k).x.get(l) && j == existingItems.get(k).y.get(l)) {
                                    free = false;
                                }
                            }
                        }
                        if (free) {
                            ArrayList<Integer> space = new ArrayList<>();
                            space.add(i);
                            space.add(j);
                            freeSpaces.add(space);
                        }
                    }
                }
                if (freeSpaces.size() > 0) {
                    int newSpace = new Random().nextInt(freeSpaces.size());
                    itemNew.setPosition(freeSpaces.get(newSpace).get(0),freeSpaces.get(newSpace).get(1));
                    items.add(itemNew);
                    return itemNew;
                }
            }
        }
        return null;
    }

    public void remove(Item i) {
        items.remove(i);
    }
}
