import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Accidental {
    public URL url1a = getClass().getResource("/images/flat.png");
    public URL url2a = getClass().getResource("/images/sharp.png");
    public Image type;
    public List<BufferedImage> accidentals;
    public int x;
    public int y;
    public String flatOrSharp;
    public boolean isSelected;

    //constructor
    public Accidental(String type, int x, int y) {
        this.x = x;
        this.y = y;
        try{
            this.accidentals = Collections.unmodifiableList(
                new ArrayList<BufferedImage>() {{
                    add(ImageIO.read(url1a));
                    add(ImageIO.read(url2a));  
                }}
            );
        } catch (IOException e) {

        }
        if (type == "sharp") {
            this.type = this.accidentals.get(1);
        } else if (type == "flat") {
            this.type = this.accidentals.get(0);
        }
        this.flatOrSharp = type;
        
    }

    public void paint(Graphics g) {
        g.drawImage(this.type, this.x, this.y, null);
        if (this.isSelected) {
            g.setColor(Color.RED);
            int width = this.type.getWidth(null);
            int height = this.type.getHeight(null);
            g.drawLine(this.x, this.y, this.x + width, this.y);
            g.drawLine(this.x, this.y, this.x, this.y + height);
            g.drawLine(this.x, this.y + height, this.x + width, this.y + height);
            g.drawLine(this.x + width, this.y, this.x + width, this.y + height);
        }
        
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
}