import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500,500);
        window.setTitle("snake");
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);

        GamePanel gamePanel = new GamePanel(15, 15, 16, 2);
        window.add(gamePanel);
        window.pack();
    }
}
