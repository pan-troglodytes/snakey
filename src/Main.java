/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int col = 15;
        int row = 15;
        for (int i=0; i < args.length; i++) {
            if (args[i].equals("--col")) {
                col = Integer.parseInt(args[i+1]);
            } else if (args[i].equals("--row")) {
                row = Integer.parseInt(args[i+1]);
            }
        }
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("snake");
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
        GamePanel gamePanel = new GamePanel(col, row, 16, 2);
        window.add(gamePanel);
        window.pack();
    }
}
