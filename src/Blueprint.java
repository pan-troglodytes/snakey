import java.util.ArrayList;

public class Blueprint {
    final ArrayList<ArrayList<ArrayList<Item>>> xy = new ArrayList<>();

    Blueprint(int maxScreenCol, int maxScreenRow) {
       for (int i=0; i < maxScreenCol; i++) {
           xy.add(new ArrayList<>());
           for (int j=0; j < maxScreenRow; j++) {
               xy.get(i).add(new ArrayList<>());
           }
       }
    }
    public void setCoords(int x, int y, Item item) {
        if (!xy.get(x).get(y).contains(item)) {
            xy.get(x).get(y).add(item);
        }
    }

    public void removeCoords(int x, int y, Item item) {
        xy.get(x).get(y).remove(item);
    }

    public ArrayList<Item> getCoords(int x, int y) {
        if (x > xy.size() -1 || x < 0 || y > xy.size() -1 || y < 0) {
            return null;
        }
        return xy.get(x).get(y);
    }

    public void print() {
        for (int i=0; i < xy.size(); i++) {
            System.out.println(xy.get(i));
        }
    }


}
