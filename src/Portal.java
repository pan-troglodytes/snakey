/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.imageio.ImageIO;
import java.io.IOException;

public class Portal extends Item {
    protected Portal pair;

    public Portal(ItemSpawner spawner) {
        super(spawner);
        x.add(0);
        y.add(0);
        try {
            sprite = ImageIO.read(getClass().getResource("sprites/portal-blue.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            snake.observe();
        }
    }
}