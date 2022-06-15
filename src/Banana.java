import java.awt.*;

public class Banana extends Item {
	public Banana(ItemSpawner spawner) {
		super(spawner);
		x.add(0);
		y.add(0);
		color = new Color(255, 255, 0);
		this.value = 3;
	}
	@Override
	public void interact(Item interactee) {
		interactee.addValue(value);
		die();
	}
}
