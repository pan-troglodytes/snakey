/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import org.json.JSONObject;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Item implements Serializable, Runnable{
    protected int value = 0;
    protected String id = this.getClass().getName() + "@" + Integer.toHexString(this.hashCode());
    protected ArrayList<Integer> x = new ArrayList<>();
    protected ArrayList<Integer> y = new ArrayList<>();
    protected ArrayList<Character> d = new ArrayList<>();
    protected Color color;
    protected String idSpawner;


    public Item(String idSpawner) {
        this.idSpawner = idSpawner;
    };

    public void draw(Graphics g, int tileSize) {
        for (Integer i:x) {
            for (Integer j:y) {
                g.setColor(color);
                g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
            }
        }
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void interact(Item interactee) {
    }

    public void addValue(int value) {
        this.value += value;
    }

    public void setPosition(int x, int y) {
        this.x.set(0,x);
        this.y.set(0,y);
    }

    public void die() {
        x.clear();
        y.clear();
    }

    public int[] getPosition() {
        return new int[] {x.get(0), y.get(0)};
    }

    public void setColor(Color c) {
        this.color = c;
    }



    public boolean equals(Object o) {
        return o instanceof Item && getId().equals(((Item) o).getId());
    }

    @Override
    public void run() {
    }
    public JSONObject jsonify() {
        JSONObject json = new JSONObject();
        json.put("id", toString());
        json.put("x", x);
        json.put("y", y);
        json.put("value", value);
        json.put("color", color.getRed()+"-"+color.getGreen()+"-"+color.getBlue());
        return json;
    }
}