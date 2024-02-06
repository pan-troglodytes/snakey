/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Snake extends Item {

    protected int xStart;
    protected int yStart;
    protected int sizeStart;
    protected char direction = 'r';
    protected KeyHandler keyHandler;
    protected int headL, headR, headU, headD, bodyLR, bodyUD, tailL, tailR, tailU, tailD, cornerUR, cornerRD, cornerDL, cornerLU, beheadL, beheadR, beheadU, beheadD;
    protected static Client c;
    protected int delay;
    protected int r, g, b;

    public Snake(String id, KeyHandler keyHandler, int startingSize, int delay, int x, int y, String idSpawner, int r, int g, int b, ArrayList<String> imagePaths) throws IOException {
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
        this.color = new Color(0,255,0);

        for (int i=0; i < imagePaths.size(); i++) {
            System.out.println(imagePaths.get(i));
            images.add(ImageIO.read( new File(imagePaths.get(i))));
        }

        /*
        images.add(ImageIO.read(new File("sprites2/snake-head-l.png")));
        images.add(ImageIO.read(new File("sprites2/snake-head-r.png")));
        images.add(ImageIO.read(new File("sprites2/snake-head-u.png")));
        images.add(ImageIO.read(new File("sprites2/snake-head-d.png")));
        images.add(ImageIO.read(new File("sprites2/snake-body-lr.png")));
        images.add(ImageIO.read(new File("sprites2/snake-body-ud.png")));
        images.add(ImageIO.read(new File("sprites2/snake-body-corner-ur.png")));
        images.add(ImageIO.read(new File("sprites2/snake-body-corner-rd.png")));
        images.add(ImageIO.read(new File("sprites2/snake-body-corner-dl.png")));
        images.add(ImageIO.read(new File("sprites2/snake-body-corner-lu.png")));
        images.add(ImageIO.read(new File("sprites2/snake-tail-u.png")));
        images.add(ImageIO.read(new File("sprites2/snake-tail-d.png")));
        images.add(ImageIO.read(new File("sprites2/snake-tail-l.png")));
        images.add(ImageIO.read(new File("sprites2/snake-tail-r.png")));
        images.add(ImageIO.read(new File("sprites2/snake-behead-d.png")));
        images.add(ImageIO.read(new File("sprites2/snake-behead-l.png")));
        images.add(ImageIO.read(new File("sprites2/snake-behead-r.png")));
        images.add(ImageIO.read(new File("sprites2/snake-behead-u.png")));
        */

        beheadD = 0;
        beheadL = 1;
        beheadR = 2;
        beheadU = 3;
        cornerDL = 4;
        cornerLU = 5;
        cornerRD = 6;
        cornerUR = 7;
        bodyLR = 8;
        bodyUD = 9;
        headD = 10;
        headL = 11;
        headR = 12;
        headU = 13;
        tailD = 14;
        tailL = 15;
        tailR = 16;
        tailU = 17;
        
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
            case 'l':
                x.set(0, x.get(0) - 1);
                break;
            case 'd':
                y.set(0, y.get(0) + 1);
                break;
            case 'u':
                y.set(0, y.get(0) - 1);
                break;
            case 'r':
                x.set(0, x.get(0) + 1);
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
                            drawWith = images.get(beheadL);
                        } else if (d.get(0) == 'd') {
                            drawWith = images.get(beheadD);
                        } else if (d.get(0) == 'u') {
                            drawWith = images.get(beheadU);
                        } else if (d.get(0) == 'r') {
                            drawWith = images.get(beheadR);
                        }
                    } else {
                        if (d.get(0) == 'l') {
                            drawWith = images.get(headL);
                        } else if (d.get(0) == 'd') {
                            drawWith = images.get(headD);
                        } else if (d.get(0) == 'u') {
                            drawWith = images.get(headU);
                        } else if (d.get(0) == 'r') {
                            drawWith = images.get(headR);
                        }
                    }
                } else if (i == x.size()-1  ) {
                    for (int j=i; j >= 0; j--) {
                        if (x.get(j) != x.get(i) || y.get(j) != y.get(i)) {
                            if (y.get(j) < y.get(i) && x.get(j) == x.get(i)) {
                                drawWith = images.get(tailU);
                            }
                            if (y.get(j) > y.get(i) && x.get(j) == x.get(i)) {
                                drawWith = images.get(tailD);
                            }
                            if (x.get(j) < x.get(i) && y.get(j) == y.get(i)) {
                                drawWith = images.get(tailL);
                            }
                            if (x.get(j) > x.get(i) && y.get(j) == y.get(i)) {
                                drawWith = images.get(tailR);
                            }
                            break;
                        }
                    }
                } else {
                    if (x.get(i) == x.get(i - 1) && y.get(i) != y.get(i - 1) && y.get(i) != y.get(i + 1) || x.get(i) == x.get(i + 1) && y.get(i) != y.get(i + 1) && y.get(i) != y.get(i - 1)) {
                          drawWith = images.get(bodyUD);
                        }

                        if (y.get(i) == y.get(i - 1) && x.get(i) != x.get(i - 1) && x.get(i) != x.get(i + 1) || y.get(i) == y.get(i + 1) && x.get(i) != x.get(i + 1) && x.get(i) != x.get(i - 1)) {
                            drawWith = images.get(bodyLR);
                        }

                        // corners are determined by segment direction, so that they work properly when disjointed by portals
                        if (d.get(i + 1) == 'r' && d.get(i) == 'd' || d.get(i + 1) == 'u' && d.get(i) == 'l') {
                            // if the previous segment is facing the right and the current segment is facing down
                            // OR
                            // the previous segment is facing up and the current segment is facing the left
                            drawWith = images.get(cornerDL);
                        }
                        if (d.get(i + 1) == 'l' && d.get(i) == 'u' || d.get(i + 1) == 'd' && d.get(i) == 'r') {
                            drawWith = images.get(cornerUR);
                        }
                        if (d.get(i + 1) == 'l' && d.get(i) == 'd' || d.get(i + 1) == 'u' && d.get(i) == 'r') {
                            drawWith = images.get(cornerRD);
                        }
                        if (d.get(i + 1) == 'r' && d.get(i) == 'u' || d.get(i + 1) == 'd' && d.get(i) == 'l') {
                            drawWith = images.get(cornerLU);
                        }
                }
                g.setColor(new Color(r,this.g,b));
            g.drawString(id, x.get(0)*tileSize, (y.get(0)-1)*tileSize);
            g.drawImage(drawWith, x.get(i) * tileSize, y.get(i) * tileSize, tileSize, tileSize, null);
        }

            //g.drawImage(images.get(0), 0, 0, tileSize, tileSize, null);
    }
    public ArrayList<Integer> getRenderOrder() {
        ArrayList<Integer> imageOrder = new ArrayList<>();
        for (int i = 0; i < x.size() ; i++ ) {
            int imageNew = 0;
                if (i == 0) {
                    if (x.size() == 1) {
                        if (d.get(0) == 'l') {
                            imageNew = beheadL;
                        } else if (d.get(0) == 'd') {
                            imageNew = beheadD;
                        } else if (d.get(0) == 'u') {
                            imageNew = beheadU;
                        } else if (d.get(0) == 'r') {
                            imageNew = beheadR;
                        }
                    } else {
                        if (d.get(0) == 'l') {
                            imageNew = headL;
                        } else if (d.get(0) == 'd') {
                            imageNew = headD;
                        } else if (d.get(0) == 'u') {
                            imageNew = headU;
                        } else if (d.get(0) == 'r') {
                            imageNew = headR;
                        }
                    }
                } else if (i == x.size()-1  ) {
                    for (int j=i; j >= 0; j--) {
                        if (x.get(j) != x.get(i) || y.get(j) != y.get(i)) {
                            if (y.get(j) < y.get(i) && x.get(j) == x.get(i)) {
                                imageNew = tailU;
                            }
                            if (y.get(j) > y.get(i) && x.get(j) == x.get(i)) {
                                imageNew = tailD;
                            }
                            if (x.get(j) < x.get(i) && y.get(j) == y.get(i)) {
                                imageNew = tailL;
                            }
                            if (x.get(j) > x.get(i) && y.get(j) == y.get(i)) {
                                imageNew = tailR;
                            }
                            break;
                        }
                    }
                } else {
                    if (x.get(i) == x.get(i - 1) && y.get(i) != y.get(i - 1) && y.get(i) != y.get(i + 1) || x.get(i) == x.get(i + 1) && y.get(i) != y.get(i + 1) && y.get(i) != y.get(i - 1)) {
                          imageNew = bodyUD;
                        }

                        if (y.get(i) == y.get(i - 1) && x.get(i) != x.get(i - 1) && x.get(i) != x.get(i + 1) || y.get(i) == y.get(i + 1) && x.get(i) != x.get(i + 1) && x.get(i) != x.get(i - 1)) {
                            imageNew = bodyLR;
                        }

                        // corners are determined by segment direction, so that they work properly when disjointed by portals
                        if (d.get(i + 1) == 'r' && d.get(i) == 'd' || d.get(i + 1) == 'u' && d.get(i) == 'l') {
                            // if the previous segment is facing the right and the current segment is facing down
                            // OR
                            // the previous segment is facing up and the current segment is facing the left
                            imageNew = cornerDL;
                        }
                        if (d.get(i + 1) == 'l' && d.get(i) == 'u' || d.get(i + 1) == 'd' && d.get(i) == 'r') {
                            imageNew = cornerUR;
                        }
                        if (d.get(i + 1) == 'l' && d.get(i) == 'd' || d.get(i + 1) == 'u' && d.get(i) == 'r') {
                            imageNew = cornerRD;
                        }
                        if (d.get(i + 1) == 'r' && d.get(i) == 'u' || d.get(i + 1) == 'd' && d.get(i) == 'l') {
                            imageNew = cornerLU;
                        }
                }
                imageOrder.add(imageNew);
        }
        return imageOrder;
    }
    public KeyHandler getSnakeControls() {
        return keyHandler;
    }


    public char getDirection() {
        return d.get(0);
    }

    public String toString() {
        return id;
    }
    public void update(ArrayList<Drawing> items) {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).equals(this)) {
                    x = items.get(i).x;
                    y = items.get(i).y;
                    d = items.get(i).d;
                    value = items.get(i).value;
                }
            }
        }
    }
    @Override
    public JSONObject jsonify() {
        JSONObject json = super.jsonify();
        json.put("d", d);
        return json;
    }
}

