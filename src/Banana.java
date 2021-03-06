/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Banana extends Fruit {

	public Banana(ItemSpawner spawner) {
		super(spawner);
		this.value = 3;
		new Timer((map.getCol() + map.getRow()) * 75, this).start();
		try {
			sprite = ImageIO.read(getClass().getResource("sprites/banana.png"));
			rot = ImageIO.read(getClass().getResource("sprites/banana-rotten.png"));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
