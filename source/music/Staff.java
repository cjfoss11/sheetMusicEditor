package music;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Staff extends JComponent{
    //variable initialization
    boolean lastStaff;
    int x;
    int y;
    int increment = 12;
    int staffWidth = 500; 
    URL url1 = getClass().getResource("/images/trebleClef.png");
    URL url2 = getClass().getResource("/images/commonTime.png");

    //constructor
    public Staff(boolean lastStaff, int x, int y) {
        this.lastStaff = lastStaff;
        this.x = x;
        this.y = y;
    }

    public void paintComponent(Graphics g) {
        requestFocusInWindow();
        g.setColor(Color.BLACK);
        g.drawLine(x, y, staffWidth, y);
        g.drawLine(x, y + increment, staffWidth, y + increment);
        g.drawLine(x, y + (increment * 2), staffWidth, y + (increment * 2));
        g.drawLine(x, y + (increment * 3), staffWidth, y + (increment * 3));
        g.drawLine(x, y + (increment * 4), staffWidth, y + (increment * 4));
        g.drawLine(x, y, x, y + (increment * 4));

        if (!this.lastStaff) {
            g.drawLine(staffWidth, y, staffWidth, y + (increment * 4));
        } else {
            g.fillRect(staffWidth - 10, y, 11, increment * 4);
        }
        try {
            BufferedImage trebleClefImage = ImageIO.read(url1);
            BufferedImage commonTimeImage = ImageIO.read(url2);
            g.drawImage(trebleClefImage, x, y - 20, null);
            g.drawImage(commonTimeImage, x + 50, y - 5, null);
        } catch (IOException e) {
            
        }
       
    }

    public static void nearestStaff(Note note, List<Staff> staves) {
        for (int i = 0; i < staves.size(); i++){
            if (note.getY() + note.note.getHeight(null) >= staves.get(i).getY() - 24 && note.getY() <= staves.get(i).getY() + 72) {
                note.setStaff(staves.get(i));
            }
        }
    }

    //getters and setters
    public void setLastStaff(boolean value) {
            this.lastStaff = value;
    }

    public int getX() {
            return this.x;
    }

    public int getY() {
            return this.y;
    }

}
