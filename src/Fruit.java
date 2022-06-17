import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Fruit extends Item implements ActionListener {
	protected boolean rotten = false;
	protected Color colorRotten;
	public Fruit(ItemSpawner spawner) {
		super(spawner);
		x.add(0);
		y.add(0);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (!rotten) {
			rotten = true;
			value = -value;
			color= colorRotten;
		} else {
			die();
		}
	}
}
