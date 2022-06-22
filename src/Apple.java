import javax.swing.*;
import java.awt.*;

public class Apple extends Fruit {

    public Apple(ItemSpawner spawner) {
        super(spawner);
        this.value = 1;
        new Timer(4000, this).start();
        color = new Color(255, 0, 0);
        colorRotten = new Color(85, 151, 109);
    }
}
