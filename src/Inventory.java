import java.util.ArrayList;

public class Inventory implements Runnable {

    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Item> orphans;
    ArrayList<ItemSpawner> spawners;

    public Inventory(ArrayList<Item> orphans, ArrayList<ItemSpawner> spawners) {
        this.orphans = orphans;
        this.spawners = spawners;
    }
    public void addOrphans() {
        for (Item orphan : orphans) {
            addItem(orphan);
        }
    }
    public void removeDeadItems() {
        for (int i=0; i < items.size(); i++) {
            if (items.get(i).x.size() == 0) {
                for (int j=0; j < spawners.size(); j++) {
                    if (items.get(i).idSpawner.equals(spawners.get(j).toString())) {
                        spawners.get(j).remove(items.get(i));
                    }
                }
                items.remove(i);
            }
        }
    }
    public void addItem(Item item) {
        if (items.contains(item)) {
            for (int i=0; i < items.size(); i++) {
                if (items.get(i).equals(item)) {
                    items.set(i, item);
                }
            }
        } else {
            items.add(item);
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    @Override
    public void run() {
        while (true) {
            for (int i=0; i < spawners.size(); i++) {
                Item newItem = spawners.get(i).spawnItem(items);
                if (newItem != null) {
                    items.add(newItem);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}