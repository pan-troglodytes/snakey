import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends JPanel implements ActionListener {
    final int resolution;
    final int scale;
    final int tileSize;
    final int col;
    final int row;
    final int width;
    final int height;
    Blueprint map;
    ArrayList<ItemSpawner> itemSpawners = new ArrayList<>();
    ArrayList<Snake> snakes = new ArrayList<>();

    public GamePanel(int col, int row, int resolution, int scale) {
        this.resolution = resolution;
        this.scale = scale;
        this.tileSize = resolution * scale;
        this.col = col;
        this.row = row;
        this.width = col * tileSize;
        this.height = row * tileSize;
        this.map = new Blueprint(col, row);
        ItemSpawner.setBlueprint(map);
        Item.setBlueprint(map);
        this.snakes.add(new Snake(4, 150,3,3, null, map));
        this.itemSpawners.add(new ItemSpawner(0, 0, col-1, row-1));
        //this.itemSpawners.add(new ItemSpawner(1, 1, 2, 1));
        //this.itemSpawners.add(new ItemSpawner((int) (col * .40), (int) (col * .60), (int) (row *.40), (int) (row * .60)));

        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        for (Snake snake : snakes) {
            this.addKeyListener(snake.getSnakeControls());
        }
        this.setFocusable(true);
        new Timer(100,this).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Snake snake : snakes) {
            snake.draw(g, tileSize);
        }
        for (ItemSpawner itemSpawner : itemSpawners) {
            itemSpawner.drawItems(g, tileSize);
        }
    }
}

