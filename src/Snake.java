/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Snake extends Item {

    int xStart;
    int yStart;
    int sizeStart;
    char direction = 'r';
    KeyHandler keyHandler;
    ArrayList<Character> d = new ArrayList<>();
    ImageIcon headL, headR, headU, headD, bodyLR, bodyUD, tailL, tailR, tailU, tailD, cornerUR, cornerRD, cornerDL, cornerLU, beheadL, beheadR, beheadU, beheadD;
    static Client c;
    int delay;
    int r, g, b;

    public Snake(String id, KeyHandler keyHandler, int startingSize, int delay, int x, int y, String idSpawner, int r, int g, int b) throws IOException {
        super(idSpawner);
        this.id = id;
        this.keyHandler = keyHandler;
        this.xStart = x;
        this.yStart = y;
        this.x.add(x);
        this.y.add(y);
        this.delay = delay;
        d.add(direction);
        this.r = r;
        this.g = g;
        this.b = b;


        headL = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-head-l.png")));
        headR = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-head-r.png")));
        headU = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-head-u.png")));
        headD = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-head-d.png")));
        bodyLR = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-body-lr.png")));
        bodyUD = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-body-ud.png")));
        cornerUR = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-body-corner-ur.png")));
        cornerRD = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-body-corner-rd.png")));
        cornerDL = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-body-corner-dl.png")));
        cornerLU = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-body-corner-lu.png")));
        tailU = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-tail-u.png")));
        tailD = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-tail-d.png")));
        tailL = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-tail-l.png")));
        tailR = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-tail-r.png")));
        beheadD = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-behead-d.png")));
        beheadL = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-behead-l.png")));
        beheadR = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-behead-r.png")));
        beheadU = new ImageIcon(ImageIO.read(getClass().getResource("sprites/snake-behead-u.png")));

        this.sizeStart = startingSize;
        this.value = startingSize;
    }

    public void run() {
        while (true) {
			turn();
			move();
            c.sendItem(this);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void turn() {
		if (direction == 'r' && keyHandler.directionNew != 'l' ||
				direction == 'l' && keyHandler.directionNew != 'r' ||
				direction == 'd' && keyHandler.directionNew != 'u' ||
				direction == 'u' && keyHandler.directionNew != 'd') {
			direction = keyHandler.directionNew;
			d.set(0,direction);
		}
    }

    public void move() {
        if (x.size() > 1) {
            for (int j = x.size() - 1; j > 0; j--) {
                y.set(j, y.get(j - 1));
                x.set(j, x.get(j - 1));
                d.set(j, d.get(j - 1));
            }
        }
        if (x.size() < value) {
            x.add(x.get(x.size() - 1));
            y.add(y.get(y.size() - 1));
            d.add(d.get(d.size() - 1));
        } else if (x.size() > value) {
            x.remove(x.size()-1);
            y.remove(y.size()-1);
            d.remove(d.size()-1);
        }

        if (x.size() <= 0) {
            die();
        }
        switch (direction) {
            case 'l' -> x.set(0, x.get(0) - 1);
            case 'd' -> y.set(0, y.get(0) + 1);
            case 'u' -> y.set(0, y.get(0) - 1);
            case 'r' -> x.set(0, x.get(0) + 1);
        }
    }

    public void observe(Item collision) {
        if (collision == null ){
        } else {
            if (collision.getClass() == Snake.class) {
                die();
            }
            collision.interact(this);
        }
    }

    @Override
    public void die() {
        value = sizeStart;
        x = new ArrayList<>();
        y = new ArrayList<>();
        d = new ArrayList<>();
        x.add(xStart);
        y.add(yStart);
        d.add(direction);
    }


    @Override
    public void draw(Graphics g, int tileSize) {
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        for (int i = 0; i < x.size() ; i++ ) {
            Image drawWith = null;
                if (i == 0) {
                    if (x.size() == 1) {
                        if (d.get(0) == 'l') {
                            drawWith = beheadL.getImage();
                        } else if (d.get(0) == 'd') {
                            drawWith = beheadD.getImage();
                        } else if (d.get(0) == 'u') {
                            drawWith = beheadU.getImage();
                        } else if (d.get(0) == 'r') {
                            drawWith = beheadR.getImage();
                        }
                    } else {
                        if (d.get(0) == 'l') {
                            drawWith = headL.getImage();
                        } else if (d.get(0) == 'd') {
                            drawWith = headD.getImage();
                        } else if (d.get(0) == 'u') {
                            drawWith = headU.getImage();
                        } else if (d.get(0) == 'r') {
                            drawWith = headR.getImage();
                        }
                    }
                } else if (i == x.size()-1  ) {
                    for (int j=i; j >= 0; j--) {
                        if (x.get(j) != x.get(i) || y.get(j) != y.get(i)) {
                            if (y.get(j) < y.get(i) && x.get(j) == x.get(i)) {
                                drawWith = tailU.getImage();
                            }
                            if (y.get(j) > y.get(i) && x.get(j) == x.get(i)) {
                                drawWith = tailD.getImage();
                            }
                            if (x.get(j) < x.get(i) && y.get(j) == y.get(i)) {
                                drawWith = tailL.getImage();
                            }
                            if (x.get(j) > x.get(i) && y.get(j) == y.get(i)) {
                                drawWith = tailR.getImage();
                            }
                            break;
                        }
                    }
                } else {
                    if (x.get(i) == x.get(i - 1) && y.get(i) != y.get(i - 1) && y.get(i) != y.get(i + 1) || x.get(i) == x.get(i + 1) && y.get(i) != y.get(i + 1) && y.get(i) != y.get(i - 1)) {
                          drawWith = bodyUD.getImage();
                        }

                        if (y.get(i) == y.get(i - 1) && x.get(i) != x.get(i - 1) && x.get(i) != x.get(i + 1) || y.get(i) == y.get(i + 1) && x.get(i) != x.get(i + 1) && x.get(i) != x.get(i - 1)) {
                            drawWith = bodyLR.getImage();
                        }

                        // corners are determined by segment direction, so that they work properly when disjointed by portals
                        if (d.get(i + 1) == 'r' && d.get(i) == 'd' || d.get(i + 1) == 'u' && d.get(i) == 'l') {
                            // if the previous segment is facing the right and the current segment is facing down
                            // OR
                            // the previous segment is facing up and the current segment is facing the left
                            drawWith = cornerDL.getImage();
                        }
                        if (d.get(i + 1) == 'l' && d.get(i) == 'u' || d.get(i + 1) == 'd' && d.get(i) == 'r') {
                            drawWith = cornerUR.getImage();
                        }
                        if (d.get(i + 1) == 'l' && d.get(i) == 'd' || d.get(i + 1) == 'u' && d.get(i) == 'r') {
                            drawWith = cornerRD.getImage();
                        }
                        if (d.get(i + 1) == 'r' && d.get(i) == 'u' || d.get(i + 1) == 'd' && d.get(i) == 'l') {
                            drawWith = cornerLU.getImage();
                        }
                }
                g.setColor(new Color(r,this.g,b));
            g.drawString(id, x.get(0)*tileSize, (y.get(0)-1)*tileSize);
            g.drawImage(drawWith, x.get(i) * tileSize, y.get(i) * tileSize, tileSize, tileSize, null);
        }
    }
    public KeyHandler getSnakeControls() {
        return keyHandler;
    }


    public char getDirection() {
        return direction;
    }

    public String toString() {
        return id;
    }
    public void update(ArrayList<Item> items) {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).equals(this)) {
                    x = items.get(i).x;
                    y = items.get(i).y;
                    d = ((Snake) items.get(i)).d;
                    value = items.get(i).value;
                }
            }
        }
    }
}


