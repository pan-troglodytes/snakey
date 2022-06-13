import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Snake extends Item implements  ActionListener {
    int bodyParts = 1;
    int xStart;
    int yStart;
    int sizeStart;
    public char direction = 'r';
    KeyHandler keyHandler = new KeyHandler();

    Snake(int startingSize, int delay, int x, int y, ItemSpawner spawner, Blueprint map) {
        super(spawner);
        this.xStart = x;
        this.yStart = y;
        for (int i=0; i <= startingSize; i++) {
            this.x.add(x);
            this.y.add(y);
        }
        color = new Color(0,255,0);
        this.sizeStart = startingSize;
        this.bodyParts = startingSize;
        Timer timer = new Timer(delay,this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
        for (int i = bodyParts; i > 0; i--) {
            y.set(i,y.get(i-1));
            x.set(i,x.get(i-1));
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


    private void observe() {
        ArrayList<Item> collisions = map.getCoords(x.get(0), y.get(0));
        if (collisions == null ){
            die();
        } else {
            for (int i=0; i < collisions.size(); i++) {
                if (collisions.get(i).getClass() == Snake.class) {
                    die();
                }
                if (collisions.size() > 0 && collisions.get(i).getClass() == Apple.class) {
                    grow();
                    collisions.get(i).die();
                    map.removeCoords(x.get(0), y.get(0), collisions.get(i));
                }
            }
        }
    }

    public void die() {
        bodyParts = sizeStart;
        for (int i=0; i < x.size(); i++) {
            //map.removeCoord(x.get(i), y.get(i), this);
        }
        for (int j = x.size()-1; j > bodyParts; j--) {
            map.removeCoords(x.get(j), y.get(j), this);
            x.remove(j);
            y.remove(j);
        }
        map.removeCoords(x.get(x.size()-1), y.get(y.size()-1), this);
        x.set(0,xStart);
        y.set(0, yStart);;
    }

    public void grow() {
        x.add(x.get(x.size()-1)-1);
        y.add(y.get(y.size()-1)-1);
        bodyParts++;
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        for ( int i = 0; i < bodyParts; i++ ) {
            if (i == 0) {
                g.setColor(new Color(162,65,65));
            } else if (i == bodyParts-1) {
                g.setColor(new Color(18,214,120));
            }else if ( i % 2 == 0) {
                g.setColor(new Color(24,150,76));
            } else {
                g.setColor(new Color(45,232,55));
            }
            g.fillRect(x.get(i) * tileSize, y.get(i) * tileSize, tileSize, tileSize);
        }
    }

    public KeyHandler getSnakeControls() {
        return keyHandler;
    }
}
