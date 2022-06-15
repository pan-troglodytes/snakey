import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Snake extends Item implements  ActionListener {

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
        this.value = startingSize;
        Timer timer = new Timer(delay,this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(this.value);
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
        for (int j = x.size()-1; j > value; j--) {
            map.removeCoords(x.get(j), y.get(j), this);
            x.remove(j);
            y.remove(j);
        }
        map.removeCoords(x.get(x.size()-1), y.get(y.size()-1), this);
        x.set(0,xStart);
        y.set(0, yStart);
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        for (int i = 0; i < value; i++ ) {
            if (i == 0) {
                g.setColor(new Color(162,65,65));
            } else if (i == value -1) {
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

    @Override
    public void addValue(int value){
        super.addValue(value);
        // grow new segment for each new value point
        for (int j=0; j < value; j++) {
            x.add(x.get(x.size()-1));
            y.add(y.get(y.size()-1));
        }
    }

    public char getDirection() {
        return direction;
    }
}
