/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import java.util.ArrayList;

public class Blueprint {
    ArrayList<ArrayList<ArrayList<Item>>> xy = new ArrayList<>();

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
