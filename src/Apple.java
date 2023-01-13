/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.swing.Timer;
import java.awt.Color;

public class Apple extends Fruit {

    public Apple(String idSpawner) {
        super(idSpawner);
        this.value = 1;
        new Timer(10000, this).start();
        color = new Color(255, 0, 0);
        colorRotten = new Color(85, 151, 109);
    }
}
