/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Fruit extends Item implements ActionListener {
	boolean rotten = false;
	Color colorRotten;
	public Fruit(String idSpawner) {
		super(idSpawner);
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
