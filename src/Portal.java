/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import java.awt.*;

public class Portal extends Item {
    Portal pair;

    public Portal(String idSpawner) {
        super(idSpawner);
        x.add(0);
        y.add(0);
        color = new Color(0, 255, 226);
    }

    public void linkPair(Portal p) {
        setPair(p);
        p.setPair(this);
    }
    public void setPair(Portal p) {
        pair = p;
    }

    public Portal getPair() {
        return pair;
    }

    public void draw(Graphics g, int tileSize) {
        for (Integer i:x) {
            for (Integer j:y) {
                g.setColor(color);
                g.fillOval(i * tileSize, j * tileSize, (int) (tileSize * .75), tileSize);
            }
        }
    }

    public void interact(Item interactee) {
        if (interactee.getClass() == Snake.class) {
            Snake snake = (Snake) interactee;
            int[] pos = this.getPair().getPosition();
            snake.x.set(0, pos[0]);
            snake.y.set(0, pos[1]);
            switch (snake.getDirection()) {
                case 'l' -> snake.x.set(0, snake.x.get(0) - 1);
                case 'd' -> snake.y.set(0, snake.y.get(0) + 1);
                case 'u' -> snake.y.set(0, snake.y.get(0) - 1);
                case 'r' -> snake.x.set(0, snake.x.get(0) + 1);
            }
        }
    }

}