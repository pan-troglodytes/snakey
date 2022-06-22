import javax.swing.*;
import java.awt.*;

public class Banana extends Fruit {

	public Banana(ItemSpawner spawner) {
		super(spawner);
		this.value = 3;
		new Timer(2000, this).start();
		color = new Color(255, 215, 0);
		colorRotten = new Color(111, 75, 4);
	}
}
