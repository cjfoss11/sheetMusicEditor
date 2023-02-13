package gui;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import music.*;
import recognizer.*;

public class MusicView extends JComponent implements MouseListener, MouseMotionListener, KeyListener{
    //variable initialization
    public List<Staff> staves = new ArrayList<Staff>(4);
    public List<Note> notesAndRests = new ArrayList<Note>();
    public Note selectedNote;
    public Accidental currAccidental;
    public Accidental selectedAccidental;
    public ArrayList<Point2D> stroke = new ArrayList<Point2D>(0);
    public DollarRecognizer recognizer = new DollarRecognizer();
    public static Timer timer;
    public List<Note> orderedList;
    public int ballX;
    public int ballY;
    public int travel;
    public int index;
    public int steps;
    public Note item;
    //constructor
    public MusicView() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);

        for (int i = 0; i < 4; i++) {
            Staff staff = new Staff(false, 20, 150 *i + 50);
            if (i == 3) {
                staff.setLastStaff(true);
            }
            staves.add(i, staff);
        }
        ballY = staves.get(0).getY() - 80;
        ballX = staves.get(0).getX();
        timer = new Timer(10, null);
        travel = 0;
        index = 0;
        steps = 0;
        
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (int i = 0; i < staves.size(); i++) {
            staves.get(i).paintComponent(g); 
        }
        for (int i = 0; i < notesAndRests.size(); i++) {
            notesAndRests.get(i).paint(g);
        }
        if (currAccidental != null) {
            currAccidental.paint(g);
        }

        g.setColor(Color.RED);
        for (int i = 0; i < stroke.size() - 1; i++) {
            if (stroke.size() >= 2) {
                g.drawLine((int)stroke.get(i).getX(), (int)stroke.get(i).getY(), (int)stroke.get(i + 1).getX(), (int)stroke.get(i + 1).getY());
            }
        }

        if (timer.isRunning()) {
            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, 20, 20);
        }
    }

    //called when a new staff is needed
    public void newStaff() {
        staves.get(this.staves.size() - 1).setLastStaff(false);
        Staff staff = new Staff(true, 20, 150 *this.staves.size() + 50);
        staves.add(this.staves.size(), staff);
        if (staff.getY() + 50 > this.getHeight()) {
            this.setSize(600, this.getHeight() + 60);
            this.setPreferredSize(new Dimension(600, this.getHeight() + 50));
        }
        repaint();
    }

    //called when a staff deletion is needed
    public void deleteStaff() {
        if (this.staves.size() > 1) {
            this.staves.remove(this.staves.get(this.staves.size() - 1));
            this.staves.get(this.staves.size() - 1).setLastStaff(true);
            this.setSize(this.getWidth(), this.getHeight() - 50);
            this.setPreferredSize(new Dimension(600, this.getHeight() - 50));
        }
        repaint();
    }

    public void nearestStaff(Note note) {
        for (int i = 0; i < staves.size(); i++){
            if (note.getY() + note.note.getHeight(null) >= staves.get(i).getY() - 24 && note.getY() <= staves.get(i).getY() + 72) {
                note.setStaff(staves.get(i));
            }
        }
    }

    public ArrayList<Note> orderList(List<Staff> staves, List<Note> notesAndRests) {
        Map<Staff, ArrayList<Note>> map = new HashMap<Staff, ArrayList<Note>>();
        ArrayList<Note> orderedList = new ArrayList<Note>();
        for (int i = 0; i < notesAndRests.size(); i++) {
            if (map.containsKey(notesAndRests.get(i).staff)) {
                map.get(notesAndRests.get(i).staff).add(notesAndRests.get(i));
            } else {
                map.put(notesAndRests.get(i).staff, new ArrayList<Note>());
                map.get(notesAndRests.get(i).staff).add(notesAndRests.get(i));
            }
        }

        for (int i = 0; i < staves.size(); i++) {
            if (map.get(staves.get(i)) != null) {
                Collections.sort(map.get(staves.get(i)), new Comparator<Note>(){
                    public int compare(Note a, Note b){
                        if(a.x == b.x)
                            return 0;
                        return a.x < b.x ? -1 : 1;
                    }
                });
            }
        }
        for (int i = 0; i < staves.size(); i++) {
            ArrayList<Note> currNotes = map.get(staves.get(i));
            if (currNotes != null) {
                for (int j = 0; j < currNotes.size(); j++) {
                    orderedList.add(currNotes.get(j));
                }
            }
        }
        return orderedList;
    }
    public void timerStart() {
        orderedList = orderList(staves, notesAndRests);
        item = orderedList.get(0);
        ballX = orderedList.get(0).getX();
        ballY = orderedList.get(0).getY() - 80;
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (travel == 160) {
                    index += 1;
                    travel = 0;
                    try {
                        item = orderedList.get(index);
                        ballY = item.y - 100;
                        ballX = item.x;
                    } catch (IndexOutOfBoundsException f) {
                        EditBtns.playBtn.setSelected(false);
                        timer.stop();
                        timerEnd();
                    }
                }
                if (item.duration == 0) {
                    steps = 10;
                } else if (item.duration == 1) {
                    steps = 20;
                } else if (item.duration == 2) {
                    steps = 40;
                } else if (item.duration == 3) {
                    steps = 80;
                } else {
                    steps = 160;
                }

                if (travel <= 80) {
                    ballY += 160 / steps;
                } else {
                    ballY -= 160 / steps;
                }
                travel += 160 / steps;
                repaint();
            }
        });
        timer.start();
        repaint();
    }

    public void timerEnd() {
        timer = new Timer(10, null);
        travel = 0;
        index = 0;
        steps = 0;
        item = orderedList.get(0);
        ballX = orderedList.get(0).getX();
        ballY = orderedList.get(0).getY() - 80;
    }

    public void mouseDragged(MouseEvent e) {
        //in add mode, adding accidentals
        if (currAccidental != null) {
            this.currAccidental.setX(e.getX());
            this.currAccidental.setY(e.getY());
        
        } else {
            //in add mode, adding notes/rests
            if (!EditBtns.addBtn.isEnabled() && this.notesAndRests.size() > 0) {
                this.notesAndRests.get(0).setX(e.getX());
                this.notesAndRests.get(0).setY(e.getY());

                //set pitch based on type of note to get ledger lines
                Note currNote = this.notesAndRests.get(0);
                if (currNote.duration == 0) {
                    currNote.setPitch(e.getY() + 35);
                } else if (currNote.duration == 1) {
                    currNote.setPitch(e.getY() + 36);
                } else if (currNote.duration == 2) {
                    currNote.setPitch(e.getY() + 35);
                } else if (currNote.duration == 3) {
                    currNote.setPitch(e.getY() + 34);
                } else {
                    currNote.setPitch(e.getY() + 6);
                }

                //reassign associated staff if necessary
                nearestStaff(this.notesAndRests.get(0));

                //chords
                for (int i = 1; i < this.notesAndRests.size(); i++) {
                    //if compared things are both notes and have same staff value(where the value is not null)
                    if (this.notesAndRests.get(i).type == "note" && this.notesAndRests.get(0).type == "note" && this.notesAndRests.get(i).staff == this.notesAndRests.get(0).staff && this.notesAndRests.get(i).staff != null) {
                        int xCoord = this.notesAndRests.get(i).getX();
                        int width = this.notesAndRests.get(i).note.getWidth(null);
                        if (e.getX() >= xCoord - width && e.getX() <= xCoord + width) {
                            this.notesAndRests.get(0).setX(xCoord);

                        }
                    }
                }
            //in select mode
            } else if (!EditBtns.selectBtn.isEnabled()) {
                if (this.selectedNote != null && this.selectedNote.isSelected) {
                    this.selectedNote.setX(e.getX());
                    this.selectedNote.setY(e.getY());
                    
                    //set pitch based on type of note to get ledger lines
                    if (this.selectedNote.duration == 0) {
                        this.selectedNote.setPitch(e.getY() + 35);
                    } else if (this.selectedNote.duration == 1) {
                        this.selectedNote.setPitch(e.getY() + 36);
                    } else if (this.selectedNote.duration == 2) {
                        this.selectedNote.setPitch(e.getY() + 35);
                    } else if (this.selectedNote.duration == 3) {
                        this.selectedNote.setPitch(e.getY() + 34);
                    } else {
                        this.selectedNote.setPitch(e.getY() + 6);
                    }
                    //reassign associated staff if necessary
                    nearestStaff(this.selectedNote);

                    //move associated accidental with note as necessary
                    if (this.selectedNote.accidental != null) {
                        this.selectedNote.accidental.setX(e.getX()-10);
                        this.selectedNote.accidental.setY(e.getY() + 20);
                    }

                    //chords
                    for (int i = 0; i < this.notesAndRests.size(); i++) {
                        if (this.notesAndRests.get(i) != this.selectedNote){
                            if (this.selectedNote.type == "note" && this.notesAndRests.get(0).type == "note" && this.notesAndRests.get(i).staff == this.selectedNote.staff && this.notesAndRests.get(i).staff != null) {
                                int xCoord = this.notesAndRests.get(i).getX();
                                int width = this.notesAndRests.get(i).note.getWidth(null);
                                if (e.getX() >= xCoord - width && e.getX() <= xCoord + width) {
                                    this.selectedNote.setX(xCoord);
                                    if (this.selectedNote.accidental != null) {
                                        this.selectedNote.accidental.setX(xCoord - 10);
                                    }
                                }
                            }
                        }
                    }
                }
            //in pen mode
            } else {
                stroke.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        }
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
            //in add mode
            if (!EditBtns.addBtn.isEnabled()) {
                //adding notes and rests
                if (RadioBtns.noteType != null) {
                    Note newNote = new Note(EditBtns.slider.getValue(), RadioBtns.noteType, e.getX(), e.getY());
                    this.notesAndRests.add(0, newNote);
                    for (int i = 0; i < staves.size(); i++){
                        if (newNote.getY() + newNote.note.getHeight(null) >= staves.get(i).getY() - 24 && newNote.getY() <= staves.get(i).getY() + 72) {
                            newNote.setStaff(staves.get(i));
                        }
                    }
                //adding accidentals
                } else if (RadioBtns.accidentalType != null) {
                    if (this.notesAndRests.size() >= 1) {
                        Accidental accidental = new Accidental(RadioBtns.accidentalType, e.getX(), e.getY());
                        this.currAccidental = accidental;
                        //set all notes to selected for dragging accidentals 
                        for (int i = 0; i < this.notesAndRests.size(); i++) {
                            if (this.notesAndRests.get(i).type == "note") {
                                this.notesAndRests.get(i).isSelected = true;
                            }
                        }
                    }
                }
            //in select mode
            } else if (!EditBtns.selectBtn.isEnabled()) {
                //determining what is selected
                for (int i = 0; i < this.notesAndRests.size(); i++) {
                    int height = this.notesAndRests.get(i).note.getHeight(null);
                    int width = this.notesAndRests.get(i).note.getWidth(null);
                    int xCoord = this.notesAndRests.get(i).x;
                    int yCoord = this.notesAndRests.get(i).y;
                    //is a note/rest selected?
                    if ((e.getX() >= xCoord && e.getX() <= xCoord + width) && (e.getY() >= yCoord && e.getY() <= yCoord + height)) {
                        this.notesAndRests.get(i).isSelected = true; 
                        this.selectedNote = this.notesAndRests.get(i);
                        this.selectedAccidental = null;
                    //is an accidental selected?
                    } else {
                        this.notesAndRests.get(i).isSelected = false;
                        if (this.notesAndRests.get(i).accidental != null) {
                            Accidental acc = this.notesAndRests.get(i).accidental;
                            int accH = acc.type.getHeight(null);
                            int accW = acc.type.getWidth(null);
                            int aXCoord = acc.x;
                            int aYCoord = acc.y;

                            //assign accidental to a note
                            if ((e.getX() >= aXCoord && e.getX() <= aXCoord + accW) && (e.getY() >= aYCoord && e.getY() <= aYCoord + accH)) {
                                this.notesAndRests.get(i).accidental.isSelected = true;
                                this.selectedAccidental = acc;
                                this.selectedNote = null;
                            } else {
                                this.notesAndRests.get(i).accidental.isSelected = false;
                            }
                        }
                    }

                }
                //reassign associated staff if necessary
                if (this.selectedNote != null) {
                    nearestStaff(this.selectedNote);  
                }
            //in pen mode
            } else {
                stroke.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        //add mode 
        if (!EditBtns.addBtn.isEnabled()) {
            //adding notes
            if (this.notesAndRests.size() > 0 && this.notesAndRests.get(0).type == "note" && RadioBtns.noteType != null){
                //set pitch based on type of note to get ledger lines
                Note currNote = this.notesAndRests.get(0);
                if (currNote.duration == 0) {
                    currNote.setPitch(e.getY() + 35);
                } else if (currNote.duration == 1) {
                    currNote.setPitch(e.getY() + 36);
                } else if (currNote.duration == 2) {
                    currNote.setPitch(e.getY() + 35);
                } else if (currNote.duration == 3) {
                    currNote.setPitch(e.getY() + 34);
                } else {
                    currNote.setPitch(e.getY() + 6);
                }
                //reassign associated staff if necessary
                nearestStaff(currNote);
            //adding accidentals
            } else if (this.currAccidental != null && RadioBtns.accidentalType != null) {
                for (int i = 0; i < this.notesAndRests.size(); i++) {
                    if (this.notesAndRests.get(i).type == "note") {
                        int height = this.notesAndRests.get(i).note.getHeight(null);
                        int width = this.notesAndRests.get(i).note.getWidth(null);
                        int xCoord = this.notesAndRests.get(i).x;
                        int yCoord = this.notesAndRests.get(i).y;
                        
                        //assign accidental to a note
                        if ((e.getX() >= xCoord && e.getX() <= xCoord + width) && (e.getY() >= yCoord && e.getY() <= yCoord + height)) {
                            this.notesAndRests.get(i).setAccidental(this.currAccidental);
                            this.notesAndRests.get(i).accidental.setX(xCoord - 10);
                            this.notesAndRests.get(i).accidental.setY(yCoord + 20);
                        }
                    }
                }

                for (int i = 0; i < this.notesAndRests.size(); i++) {
                    this.notesAndRests.get(i).isSelected = false;
                }
            }
            
        } else if (!EditBtns.selectBtn.isEnabled()) {
            //reassign associated staff if necessary
            if (this.selectedNote != null) {
                nearestStaff(this.selectedNote);
            }
        } else {
            Result result = recognizer.recognize(stroke);
            if (result.getName() != "No match") {
                if (result.getName() == "circle") {
                    notesAndRests.add(new Note(4, "note", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "half note") {
                    notesAndRests.add(new Note(3, "note", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "quarter note") {
                    notesAndRests.add(new Note(2, "note", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "eighth note") {
                    notesAndRests.add(new Note(1, "note", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "sixteenth note") {
                    notesAndRests.add(new Note(0, "note", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "rectangle") {
                    notesAndRests.add(new Note(4, "rest", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "half rest") {
                    notesAndRests.add(new Note(3, "rest", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "right curly brace") {
                    notesAndRests.add(new Note(2, "rest", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "eighth rest") {
                    notesAndRests.add(new Note(1, "rest", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "sixteenth rest") {
                    notesAndRests.add(new Note(0, "rest", (int) stroke.get(0).getX(), (int) stroke.get(0).getY()));
                    notesAndRests.get(notesAndRests.size() - 1).setPitch((int)stroke.get(0).getY());
                } else if (result.getName() == "flat") {
                    for (int i = 0; i < this.notesAndRests.size(); i++) {
                        if (this.notesAndRests.get(i).type == "note") {
                            int height = this.notesAndRests.get(i).note.getHeight(null);
                            int width = this.notesAndRests.get(i).note.getWidth(null);
                            int xCoord = this.notesAndRests.get(i).x;
                            int yCoord = this.notesAndRests.get(i).y;
                            
                            //assign accidental to a note
                            if (((int)stroke.get(0).getX() >= xCoord && (int)stroke.get(0).getX() <= xCoord + width) && ((int) stroke.get(0).getY() >= yCoord && (int) stroke.get(0).getY() <= yCoord + height)) {
                                this.notesAndRests.get(i).setAccidental(new Accidental("flat", (int)stroke.get(0).getX(), (int) stroke.get(0).getY()));
                                this.notesAndRests.get(i).accidental.setX(xCoord - 10);
                                this.notesAndRests.get(i).accidental.setY(yCoord + 20);
                                break;
                            }
                        }
                    }
                } else if (result.getName() == "star") {
                    for (int i = 0; i < this.notesAndRests.size(); i++) {
                        if (this.notesAndRests.get(i).type == "note") {
                            int height = this.notesAndRests.get(i).note.getHeight(null);
                            int width = this.notesAndRests.get(i).note.getWidth(null);
                            int xCoord = this.notesAndRests.get(i).x;
                            int yCoord = this.notesAndRests.get(i).y;
                            
                            //assign accidental to a note
                            if (((int)stroke.get(0).getX() >= xCoord && (int)stroke.get(0).getX() <= xCoord + width) && ((int) stroke.get(0).getY() >= yCoord && (int) stroke.get(0).getY() <= yCoord + height)) {
                                this.notesAndRests.get(i).setAccidental(new Accidental("sharp", (int)stroke.get(0).getX(), (int) stroke.get(0).getY()));
                                this.notesAndRests.get(i).accidental.setX(xCoord - 10);
                                this.notesAndRests.get(i).accidental.setY(yCoord + 20);
                                break;
                            }
                        }
                    }
                }
            }
            stroke = new ArrayList<Point2D>();
        }
        this.currAccidental = null;
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (this.notesAndRests.size() > 0) {
                //remove a note
                if (selectedNote != null) {
                    this.notesAndRests.remove(selectedNote);
                    if (this.notesAndRests.size() == 0) {
                        EditBtns.selectBtn.setEnabled(true);
                        EditBtns.penBtn.setEnabled(true);
                        EditBtns.addBtn.setEnabled(false);
                    }
                //remove an accidental
                } else if (selectedAccidental != null) {
                    for (int i = 0; i < this.notesAndRests.size(); i++) {
                        if (this.notesAndRests.get(i).accidental == selectedAccidental) {
                            this.notesAndRests.get(i).setAccidental(null);
                        }
                    }
                    this.selectedAccidental = null;
                }
                
            }
            repaint();
        }
    }
 
    public void mouseEntered(MouseEvent e) {}
 
    public void mouseExited(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}
 
    public void mouseClicked(MouseEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}
    
}
