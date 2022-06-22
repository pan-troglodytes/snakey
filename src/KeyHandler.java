import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

    int up, down, left, right;

    KeyHandler(int up, int down, int left, int right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
    public char directionNew = 'd';

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();

        if (keycode == left) {
            directionNew = 'l';
        }
        if (keycode == down) {
            directionNew = 'd';
        }
        if (keycode == up) {
            directionNew = 'u';
        }
        if (keycode == right) {
            directionNew = 'r';
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
