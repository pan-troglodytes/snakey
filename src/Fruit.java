import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Fruit extends Item implements ActionListener {
	boolean rotten = false;
	Color colorRotten;
	public Fruit(ItemSpawner spawner) {
		super(spawner);
		x.add(0);
		y.add(0);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (!rotten) {
			value = -value;
			color= colorRotten;
			rotten = true;
		} else {
			die();
		}
	}

	@Override
	public void interact(Item interactee) {
		interactee.addValue(value);
		die();
	}
}
