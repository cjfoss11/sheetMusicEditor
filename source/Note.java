import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Note {
    //variable declaration
    public int duration;
    public String type;
    public int x;
    public int y;
    public String pitch;
    public Image note;
    public boolean isSelected = false;
    public Accidental accidental;
    public List<BufferedImage> rests;
    public List<BufferedImage> notes;
    public List<BufferedImage> accidentals;
    public Staff staff;
    public URL url1 = getClass().getResource("/images/sixteenthNote.png");
    public URL url2 = getClass().getResource("/images/eighthNote.png");
    public URL url3 = getClass().getResource("/images/quarterNote.png");
    public URL url4 = getClass().getResource("/images/halfNote.png");
    public URL url5 = getClass().getResource("/images/wholeNote.png");
    public URL url1r = getClass().getResource("/images/sixteenthRest.png");
    public URL url2r = getClass().getResource("/images/eighthRest.png");
    public URL url3r = getClass().getResource("/images/quarterRest.png");
    public URL url4r = getClass().getResource("/images/halfRest.png");
    public URL url5r = getClass().getResource("/images/wholeRest.png");
   
    //constructor
    public Note(int duration, String note, int x, int y) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        try {
            this.notes = Collections.unmodifiableList(
                new ArrayList<BufferedImage>() {{
                    add(ImageIO.read(url1));
                    add(ImageIO.read(url2));
                    add(ImageIO.read(url3));
                    add(ImageIO.read(url4));
                    add(ImageIO.read(url5));
                }}
            );
        
        this.rests = Collections.unmodifiableList(
            new ArrayList<BufferedImage>() {{
                add(ImageIO.read(url1r));
                add(ImageIO.read(url2r));
                add(ImageIO.read(url3r));
                add(ImageIO.read(url4r));
                add(ImageIO.read(url5r));
                }}
            );
        } catch(IOException e) {

        }
        

        if (note == "note") {
            this.type = "note";
            this.note = notes.get(duration);
        } else {
            this.type = "rest";
            this.note = rests.get(duration);
        }

    }

    public void paint(Graphics g) {
        g.drawImage(this.note, this.x, this.y, null);

        //selected bounding box
        if (this.isSelected) {
            g.setColor(Color.RED);
            int width = this.note.getWidth(null);
            int height = this.note.getHeight(null);
            g.drawLine(this.x, this.y, this.x + width, this.y);
            g.drawLine(this.x, this.y, this.x, this.y + height);
            g.drawLine(this.x, this.y + height, this.x + width, this.y + height);
            g.drawLine(this.x + width, this.y, this.x + width, this.y + height);
        }

        if (this.accidental != null) {
            this.accidental.paint(g);
        }

        //paint ledger lines
        g.setColor(Color.BLACK);
        if (this.pitch != null && this.staff != null && this.type == "note") {
            int width = this.note.getWidth(null);
            int offset = this.duration == 4 ? 0 : 5;
            if (this.pitch == "A5" || this.pitch == "B5") {
                g.drawLine(this.x - offset, this.staff.getY() - 12, this.x + width - offset, this.staff.getY() - 12);
            } else if (this.pitch == "C6" || this.pitch == "D6") {
                g.drawLine(this.x - offset, this.staff.getY() - 12, this.x + width - offset, this.staff.getY() - 12);
                g.drawLine(this.x - offset, this.staff.getY() - 24, this.x + width - offset, this.staff.getY() - 24);
            } else if (this.pitch == "G3" || this.pitch == "A3") {
                g.drawLine(this.x - offset, this.staff.getY() + 60, this.x + width - offset, this.staff.getY() + 60);
                g.drawLine(this.x - offset, this.staff.getY() + 72, this.x + width - offset, this.staff.getY() + 72);
            } else if (this.pitch == "B3" || this.pitch == "C4") {
                g.drawLine(this.x - offset, this.staff.getY() + 60, this.x + width - offset, this.staff.getY() + 60);
            }
        }
    }
    
    //getters and setters
    public void setPitch(int yCoord) {
        int yForStaff = yCoord % 150;
        if (yForStaff >= 18 && yForStaff <= 23) {
            this.pitch =  "D6";
        } else if (yForStaff >=24 && yForStaff <= 29) {
            this.pitch =  "C6";
        } else if (yForStaff >=30 && yForStaff <= 35) {
            this.pitch =  "B5";
        } else if (yForStaff >= 36 && yForStaff <= 41) {
            this.pitch = "A5";
        } else if (yForStaff >= 42 && yForStaff <= 47) {
            this.pitch =  "G5";
        } else if (yForStaff >= 48 && yForStaff <= 53) {
            this.pitch =  "F5";
        } else if (yForStaff >=54 && yForStaff <= 59) {
            this.pitch =  "E5";
        } else if (yForStaff >=60 && yForStaff <= 65) {
            this.pitch =  "D5";
        } else if (yForStaff >= 66 && yForStaff <= 71) {
            this.pitch = "C5";
        } else if (yForStaff >= 72 && yForStaff <= 77) {
            this.pitch =  "B4";
        } else if (yForStaff >= 78 && yForStaff <= 83) {
            this.pitch =  "A4";
        } else if (yForStaff >= 84 && yForStaff <= 89) {
            this.pitch =  "G4";
        } else if (yForStaff >= 90 && yForStaff <= 95) {
            this.pitch =  "F4";
        } else if (yForStaff >= 96 && yForStaff <= 101) {
            this.pitch =  "E4";
        } else if (yForStaff >= 102 && yForStaff <= 107){
            this.pitch = "D4";
        } else if (yForStaff >= 108 && yForStaff <= 113) {
            this.pitch = "C4";
        } else if (yForStaff >= 114 && yForStaff <= 119) {
            this.pitch = "B3";
        } else if (yForStaff >= 120 && yForStaff <= 125) {
            this.pitch = "A3";
        } else if (yForStaff >= 126 && yForStaff <= 131) {
            this.pitch = "G3";
        } else {
            this.pitch =  "Outside Any Staff";
            this.staff = null;
        }
    }
    //set accidental and change status bar 
    public void setAccidental(Accidental sAccidental) {
        if (sAccidental != null) {
            if (this.accidental == null) {
                Homework5.label.setText(String.format("Note changed from %1$s to %2$s %3$s", this.pitch, this.pitch, sAccidental.flatOrSharp));  
            } else {
                Homework5.label.setText(String.format("Note changed from %1$s %2$s to %3$s %4$s", this.pitch, this.accidental.flatOrSharp, this.pitch, sAccidental.flatOrSharp));  
            }
            this.accidental = sAccidental;
        } else {
            if (this.accidental != null) {
                Homework5.label.setText(String.format("Note changed from %1$s %2$s to %3$s", this.pitch, this.accidental.flatOrSharp, this.pitch));
            }
            this.accidental = null;
        }
       
    }

    public void setStaff(Staff currStaff) {
        this.staff = currStaff;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
