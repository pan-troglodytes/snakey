import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler extends KeyAdapter {

    public char directionNew = 'd';

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();

        if (keycode == KeyEvent.VK_H) {
            directionNew = 'l';
        }
        if (keycode == KeyEvent.VK_J) {
            directionNew = 'd';
        }
        if (keycode == KeyEvent.VK_I) {
            directionNew = 'u';
        }
        if (keycode == KeyEvent.VK_K) {
            directionNew = 'r';
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
