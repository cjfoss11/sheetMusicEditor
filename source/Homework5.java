import java.awt.*;
import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;

public class Homework5 {
	//global variables
	static JLabel label = new JLabel("Status");
	static MusicView musicView = new MusicView();
	static JButton deleteBtn = new JButton("Delete Staff");
	public static JButton selectBtn = new JButton("Select");
	public static JButton addBtn = new JButton("Add");
	public static JButton penBtn = new JButton("Pen");
	public static JButton playBtn = new JButton("Play");
	public static JSlider slider = new JSlider(JSlider.VERTICAL, 0, 4, 0);
	public static String noteType = "note";
	public static String accidentalType = null;
	public static JScrollPane createTextArea() {
		//add content to content area
		//creating scroll bars
		musicView.setPreferredSize(new Dimension(600, 600));
		musicView.setSize(new Dimension(600, 600));
		JScrollPane scrollPane = new JScrollPane(musicView);
       
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollBar vertical = scrollPane.createVerticalScrollBar();
		JScrollBar horizontal = scrollPane.createHorizontalScrollBar();

		//add scroll bars to content area
		scrollPane.setHorizontalScrollBar(horizontal);
		scrollPane.setVerticalScrollBar(vertical);
		return scrollPane;
	}
	public static JPanel createRadioBtns() {
		//creating panel and setting attributes
		JPanel radioBtns = new JPanel();
		radioBtns.setLayout(new BoxLayout(radioBtns, BoxLayout.Y_AXIS));

		//creating the radio buttons and actions
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
				label.setText("Note Selected");
				noteType = "note";
				accidentalType = null;
		}});
		rest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rest.setSelected(true);
				note.setSelected(false);
				flat.setSelected(false);
				sharp.setSelected(false);
				label.setText("Rest Selected");
				noteType = "rest";
				accidentalType = null;
		}});
		flat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flat.setSelected(true);
				note.setSelected(false);
				rest.setSelected(false);
				sharp.setSelected(false);
				label.setText("Flat Selected");
				accidentalType = "flat";
				noteType = null;
		}});
		sharp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sharp.setSelected(true);
				note.setSelected(false);
				flat.setSelected(false);
				rest.setSelected(false);
				label.setText("Sharp Selected");
				accidentalType = "sharp";
				noteType = null;
		}});

		//add buttons to panel
		radioBtns.add(note);
		radioBtns.add(rest);
		radioBtns.add(flat);
		radioBtns.add(sharp);

		return radioBtns;
	}

	private static JPanel createEditButtons() {
		//creating container and setting its attributes
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		//creating grouping of "Select" and "Pen" buttons and their functions
		JPanel editContainer = new JPanel();
		editContainer.setLayout(new BoxLayout(editContainer, BoxLayout.X_AXIS));
		
		addBtn.setEnabled(false);
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("Add Notes and Rests");
				addBtn.setEnabled(false);
				selectBtn.setEnabled(true);
				penBtn.setEnabled(true);
		}});

		selectBtn.setEnabled(true);
		selectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("Select");
				addBtn.setEnabled(true);
				selectBtn.setEnabled(false);
				penBtn.setEnabled(true);
		}});
			
		penBtn.setEnabled(true);
		penBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("Pen");
				if (musicView.notesAndRests.size() > 0) {
					selectBtn.setEnabled(true);
				}
				addBtn.setEnabled(true);
				penBtn.setEnabled(false);
		}});
		editContainer.add(selectBtn);
		editContainer.add(addBtn);
		editContainer.add(penBtn);

		//creating grouping of "New Staff" and "Delete Staff" buttons and their functions
		JPanel staffContainer = new JPanel();
		staffContainer.setLayout(new BoxLayout(staffContainer, BoxLayout.X_AXIS));
		JButton newBtn = new JButton("New Staff");
		newBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("New Staff");
				musicView.newStaff();
				if (musicView.staves.size() > 1) {
					deleteBtn.setEnabled(true);
				}
		}});
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("Delete Staff");
				musicView.deleteStaff();
				if (musicView.staves.size() == 1) {
					deleteBtn.setEnabled(false);
				} else {
					deleteBtn.setEnabled(true);
				}
		}});
		staffContainer.add(newBtn);
		staffContainer.add(deleteBtn);

		//creating grouping of "Play" and "Stop" buttons and their functions
		JPanel playContainer = new JPanel();
		playContainer.setLayout(new BoxLayout(playContainer, BoxLayout.X_AXIS));
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playBtn.setSelected(true);
				musicView.timerStart();
				label.setText("Play");
			}}
		);
		JButton stopBtn = new JButton("Stop");
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playBtn.isSelected()) {
                    playBtn.setSelected(false);
                }
                MusicView.timer.stop();
				musicView.timerEnd();
				label.setText("Stop");
			}}
		);
		playContainer.add(playBtn);
		playContainer.add(stopBtn);

		//adding to overall container
		container.add(editContainer);
		container.add(staffContainer);
		container.add(playContainer);

		return container;
	}

	private static JSlider createSlider() {

		//creating labels for type of note and assigning labels a place on slider
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		labels.put( 0, new JLabel("Sixteenth") );
		labels.put( 1, new JLabel("Eighth") );
		labels.put( 2, new JLabel("Quarter") );
		labels.put( 3, new JLabel("Half") );
		labels.put( 4, new JLabel("Whole") );

		//setting slider attributes
		slider.setLabelTable( labels );
		slider.setPaintLabels(true);
		slider.setMaximumSize(new Dimension(100, 200));
		slider.setSnapToTicks(true);
		return slider;
	}
	private static JPanel createControlPanel() {
		//creating control panel and setting its attributes
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		//creating core edit button container
		JPanel editBtnContainer = createEditButtons();
		
		//creating container for radio buttons and slider
		JPanel noteTypeContainer = new JPanel();
		noteTypeContainer.setLayout(new BoxLayout(noteTypeContainer, BoxLayout.X_AXIS));
		
		//creating radio buttons
		JPanel radioBtns = createRadioBtns();
		noteTypeContainer.add(radioBtns);

		//creating slider
		JSlider slider = createSlider();
		noteTypeContainer.add(slider);
		
		//adding button containers to panel
		panel.add(editBtnContainer);
		panel.add(noteTypeContainer);

		return panel;
	}

	private static JMenuBar createMenuBar() {
		//creating menu bar
		JMenuBar menuBar = new JMenuBar();

		//creating "File" tab
		JMenu file = new JMenu("File");
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> System.exit(0));
		file.add(exit);

		//creating "Edit" tab
		JMenu edit = new JMenu("Edit");
		JMenuItem newStaff = new JMenuItem("New Staff");
		newStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("New Staff");
				musicView.newStaff();
				if (musicView.notesAndRests.size() > 1) {
					deleteBtn.setEnabled(true);
				}
		}});
		JMenuItem deleteStaff = new JMenuItem("Delete Staff");
		deleteStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				label.setText("Delete Staff");
				musicView.deleteStaff();
				if (musicView.notesAndRests.size() == 1) {
					deleteBtn.setEnabled(false);
				} else {
					deleteBtn.setEnabled(true);
				}
		}});
		edit.add(newStaff);
		edit.add(deleteStaff);

		//put menu together
		menuBar.add(file);
		menuBar.add(edit);
		
		return menuBar;
	}

	private static void createAndShowGUI() {
		int width = 750;
		int height = 750;

		//frame creation and setting attributes
		JFrame frame = new JFrame("My Music Editor");
		frame.setMinimumSize(new Dimension(600, 375));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//content area creation
		JScrollPane contentArea = createTextArea();
		frame.getContentPane().add(contentArea, BorderLayout.CENTER);

		//menu bar creation
		JMenuBar menuBar = createMenuBar();
		frame.setJMenuBar(menuBar);

		//status bar integration
		frame.getContentPane().add(label, BorderLayout.SOUTH);

		//control panel creation
		JPanel panel = createControlPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);

		//setting final frame attributes
		frame.pack();
		frame.setSize(width, height);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}