package gui;
import java.awt.event.*;
import javax.swing.*;


public class RadioBtns extends JPanel{
    private JPanel radiobtns; 
    public static String noteType = "note";
	public static String accidentalType = null;

    public RadioBtns() {
		this.radiobtns = new JPanel();
		this.radiobtns.setLayout(new BoxLayout(radiobtns, BoxLayout.Y_AXIS));

		JRadioButton note = new JRadioButton("Note", true);
		JRadioButton rest = new JRadioButton("Rest");
		JRadioButton flat = new JRadioButton("Flat");
		JRadioButton sharp = new JRadioButton("Sharp");
		note.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				note.setSelected(true);
				rest.setSelected(false);
				flat.setSelected(false);
				sharp.setSelected(false);
				noteType = "note";
				accidentalType = null;
		}});
		rest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rest.setSelected(true);
				note.setSelected(false);
				flat.setSelected(false);
				sharp.setSelected(false);
				noteType = "rest";
				accidentalType = null;
		}});
		flat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flat.setSelected(true);
				note.setSelected(false);
				rest.setSelected(false);
				sharp.setSelected(false);
				accidentalType = "flat";
				noteType = null;
		}});
		sharp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sharp.setSelected(true);
				note.setSelected(false);
				flat.setSelected(false);
				rest.setSelected(false);
				accidentalType = "sharp";
				noteType = null;
		}});

		//add buttons to panel
		this.radiobtns.add(note);
		this.radiobtns.add(rest);
		this.radiobtns.add(flat);
		this.radiobtns.add(sharp);
    }

    public JPanel getPanel () {
        return this.radiobtns;
    }
}
