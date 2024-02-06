import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;

public class Drawing extends Item{
    public Drawing(String idSpawner) {
        super(idSpawner);
    }
    private boolean present;
    public Drawing(ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Character> d, ArrayList<Integer> io ,ArrayList<String> in, Color c, String id, int value) {
        super(null);
        this.x = x;
        this.y = y;
        this.d = d;
        setId(id);
        setColor(c);
        this.value = value;

        this.imageOrder = io;

        stringToImage(in);
    }

    public void updateValues(ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Character> d, ArrayList<Integer> io, int value, String color) {
        this.x = x;
        this.y = y;
        this.d = d;
        this.value = value;
        this.imageOrder = io;
        String[] colors = color.split("-");
        this.color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
    }

    public void stringToImage(ArrayList<String> in) {
        if (in != null) {
            for (int i=0; i < in.size(); i++) {
                BufferedImage bi;
                ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(in.get(i)));
                try {
                    bi = ImageIO.read(bais);
                    images.add(bi);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics g, int tileSize) {
        //System.out.println(imageOrder);
        if (images.size() > 0 && imageOrder != null) {
        for (int i=0; i < x.size(); i++) {
                g.setColor(color);
                g.drawImage(images.get(imageOrder.get(i)), x.get(i) * tileSize, y.get(i) * tileSize, tileSize, tileSize, null);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
                g.drawString(id, x.get(0)*tileSize, (y.get(0)-1)*tileSize);
        }
        }  else {
            super.draw(g, tileSize);
        }
    } 

    public boolean getPresent() {
        return this.present;
    }
    public void setPresent(boolean b) {
        this.present = b;
    }
    @Override
    public String toString() {
        return id;
    }
}
