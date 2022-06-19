import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Snake extends Item implements  ActionListener {

    int xStart;
    int yStart;
    int sizeStart;
    public char direction = 'r';
    KeyHandler keyHandler;


    private Image headL, headR, headU, headD, bodyLR, bodyUD, tailL, tailR, tailU, tailD, cornerUR, cornerRD, cornerDL, cornerLU;



    Snake(KeyHandler keyHandler, int startingSize, int delay, int x, int y, ItemSpawner spawner, Blueprint map) throws IOException {
        super(spawner);
        this.keyHandler = keyHandler;
        this.xStart = x;
        this.yStart = y;
        for (int i=0; i <= startingSize; i++) {
            this.x.add(x);
            this.y.add(y);
        }

        headL = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-head-l.png"));
        headR = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-head-r.png"));
        headU = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-head-u.png"));
        headD = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-head-d.png"));
        bodyLR = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-body-lr.png"));
        bodyUD = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-body-ud.png"));
        cornerUR = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-body-corner-ur.png"));
        cornerRD = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-body-corner-rd.png"));
        cornerDL = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-body-corner-dl.png"));
        cornerLU = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-body-corner-lu.png"));
        tailU = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-tail-u.png"));
        tailD = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-tail-d.png"));
        tailL = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-tail-l.png"));
        tailR = Toolkit.getDefaultToolkit().getImage(Snake.class.getResource("sprites/snake-tail-r.png"));






        color = new Color(0,255,0);
        this.sizeStart = startingSize;
        this.value = startingSize;
        Timer timer = new Timer(delay,this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        observe();
        turn();
        move();
        observe();
    }

    public void turn() {
        if (direction == 'r' && keyHandler.directionNew != 'l' ||
                direction == 'l' && keyHandler.directionNew != 'r' ||
                direction == 'd' && keyHandler.directionNew != 'u' ||
                direction == 'u' && keyHandler.directionNew != 'd') {
            direction = keyHandler.directionNew;
        }
    }
    public void move() {

        for (int j = value; j > 0; j--) {
            y.set(j,y.get(j-1));
            x.set(j,x.get(j-1));
        }
        map.setCoords(x.get(0), y.get(0), this);
        map.removeCoords(x.get(x.size()-1), y.get(y.size()-1), this);
        switch (direction) {
            case 'l' -> x.set(0, x.get(0) - 1);
            case 'd' -> y.set(0, y.get(0) + 1);
            case 'u' -> y.set(0, y.get(0) - 1);
            case 'r' -> x.set(0, x.get(0) + 1);
        }
    }


    public void observe() {
        ArrayList<Item> collisions = map.getCoords(x.get(0), y.get(0));
        if (collisions == null ){
            die();
        } else {
            for (int i=0; i < collisions.size(); i++) {
                if (collisions.get(i).getClass() == Snake.class) {
                    die();
                }
                if (collisions.size() > 0 && Item.class.isAssignableFrom(collisions.get(i).getClass())) {
                    collisions.get(i).interact(this);
                }
            }
        }
    }

    @Override
    public void die() {
        value = sizeStart;
        if (x.size() > value) {
            for (int j = x.size()-1; j > value; j--) {
                map.removeCoords(x.get(j), y.get(j), this);
                x.remove(j);
                y.remove(j);
            }
        } else {
            for (int i = x.size(); i <= value; i++) {
                x.add(xStart);
                y.add(yStart);
            }
        }

        map.removeCoords(x.get(x.size()-1), y.get(y.size()-1), this);
        x.set(0,xStart);
        y.set(0, yStart);
    }

    @Override
    public void draw(Graphics g, int tileSize) {

        for (int i = 0; i < value; i++ ) {
            Image drawWith = null;

                if (i == 0) {
                    if (direction == 'l') {
                        drawWith = headL;
                    } else if (direction == 'd') {
                        drawWith = headD;
                    } else if (direction == 'u') {
                        drawWith = headU;
                    } else if (direction == 'r') {
                        drawWith = headR;
                    }
                } else if (i == value -1 ) {
                    for (int j=i; j > 0; j--) {
                        if (x.get(j) != x.get(i) || y.get(j) != y.get(i)) {

                            if (y.get(j) < y.get(i) && x.get(j) == x.get(i)) {
                                drawWith = tailU;
                            }
                            if (y.get(j) > y.get(i) && x.get(j) == x.get(i)) {
                                drawWith = tailD;
                            }
                            if (x.get(j) < x.get(i) && y.get(j) == y.get(i)) {
                                drawWith = tailL;
                            }
                            if (x.get(j) > x.get(i) && y.get(j) == y.get(i)) {
                                drawWith = tailR;
                            }
                            break;
                        }
                    }


                } else {


                    if (x.get(i) == x.get(i - 1) && y.get(i) != y.get(i-1)  && y.get(i) != y.get(i+1) || x.get(i) == x.get(i + 1) && y.get(i) != y.get(i+1)  && y.get(i) != y.get(i-1) ) {
                        drawWith = bodyUD;
                    }

                    if (y.get(i) == y.get(i - 1) && x.get(i) != x.get(i-1) && x.get(i) != x.get(i+1) || y.get(i) == y.get(i + 1) && x.get(i) != x.get(i+1) && x.get(i) != x.get(i-1)) {
                        drawWith = bodyLR;
                    }




                    if (x.get(i - 1) < x.get(i) && y.get(i - 1) == y.get(i) && x.get(i + 1) == x.get(i) && y.get(i + 1) > y.get(i) ||
                            x.get(i + 1) < x.get(i) && y.get(i + 1) == y.get(i) && x.get(i - 1) == x.get(i) && y.get(i - 1) > y.get(i)) {
                        drawWith = cornerDL;
                    }

                    if (x.get(i - 1) > x.get(i) && y.get(i - 1) == y.get(i) && x.get(i + 1) == x.get(i) && y.get(i + 1) < y.get(i) ||
                            x.get(i + 1) > x.get(i) && y.get(i + 1) == y.get(i) && x.get(i - 1) == x.get(i) && y.get(i - 1) < y.get(i)) {
                        drawWith = cornerUR;
                    }

                    if (x.get(i - 1) > x.get(i) && y.get(i - 1) == y.get(i) && x.get(i + 1) == x.get(i) && y.get(i + 1) > y.get(i) ||
                            x.get(i + 1) > x.get(i) && y.get(i + 1) == y.get(i) && x.get(i - 1) == x.get(i) && y.get(i - 1) > y.get(i)) {
                        drawWith = cornerRD;
                    }

                    if (x.get(i - 1) < x.get(i) && y.get(i - 1) == y.get(i) && x.get(i + 1) == x.get(i) && y.get(i + 1) < y.get(i) ||
                            x.get(i + 1) < x.get(i) && y.get(i + 1) == y.get(i) && x.get(i - 1) == x.get(i) && y.get(i - 1) < y.get(i)) {
                        drawWith = cornerLU;
                    }




            }

            g.drawImage(drawWith, x.get(i) * tileSize, y.get(i) * tileSize, tileSize, tileSize, null);

        }
    }

    public KeyHandler getSnakeControls() {
        return keyHandler;
    }

    @Override
    public void addValue(int value){
        super.addValue(value);
        if (this.value < 1) {
            die();
            return;
        }

        // grow new segment for each new value point
        if (value > 0) {
            for (int j=0; j < value; j++) {
                x.add(x.get(x.size()-1));
                y.add(y.get(y.size()-1));
            }
        } else {
            for (int i=value; i < 0; i++) {
                map.removeCoords(x.get(x.size()-2), y.get(y.size()-2), this);
                x.remove(x.size()-1);
                y.remove(y.size()-1);
            }
        }

    }

    public char getDirection() {
        return direction;
    }
}
