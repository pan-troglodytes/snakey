/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Item {
    int value = 0;
    ArrayList<Integer> x = new ArrayList<>();
    ArrayList<Integer> y = new ArrayList<>();
    BufferedImage sprite;
    static Blueprint map;
     ItemSpawner spawner;
    static void setBlueprint(Blueprint b) {
        map = b;
    }

    public Item(ItemSpawner spawner) {
        this.spawner = spawner;
    };

    public void draw(Graphics g, int tileSize) {
        g.drawImage(sprite, x.get(0) * tileSize, y.get(0) * tileSize, tileSize, tileSize, null);
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

    public void setSprite(String path) {
        try {
            sprite = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}