package gui;
import java.awt.*;
import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;

public class EditBtns extends JPanel{
    private JPanel container;
    protected static JButton deleteBtn = new JButton("Delete Staff");
	protected static JButton selectBtn = new JButton("Select");
	protected static JButton addBtn = new JButton("Add");
	protected static JButton penBtn = new JButton("Pen");
	protected static JButton playBtn = new JButton("Play");
    public static JSlider slider;

    public EditBtns(MusicView musicView) {
        container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		
        JPanel editContainer = createBasicButtons(musicView);
		JPanel staffContainer = createStaffButtons(musicView);
		JPanel playContainer = createPlayBackButtons(musicView);

		//adding to overall container
		container.add(editContainer);
		container.add(staffContainer);
		container.add(playContainer);

        createSlider();
    }

    public JPanel createBasicButtons(MusicView musicView) {
        JPanel editContainer = new JPanel();
		editContainer.setLayout(new BoxLayout(editContainer, BoxLayout.X_AXIS));
		
		addBtn.setEnabled(false);
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBtn.setEnabled(false);
				selectBtn.setEnabled(true);
				penBtn.setEnabled(true);
		}});

		selectBtn.setEnabled(true);
		selectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBtn.setEnabled(true);
				selectBtn.setEnabled(false);
				penBtn.setEnabled(true);
		}});
			
		penBtn.setEnabled(true);
		penBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (musicView.notesAndRests.size() > 0) {
					selectBtn.setEnabled(true);
				}
				addBtn.setEnabled(true);
				penBtn.setEnabled(false);
		}});
		editContainer.add(selectBtn);
		editContainer.add(addBtn);
		editContainer.add(penBtn);

        return editContainer;
    }
    
    public static JSlider createSlider() {
        slider = new JSlider(JSlider.VERTICAL, 0, 4, 0);
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

    public JPanel createStaffButtons(MusicView musicView) {
        JPanel staffContainer = new JPanel();
        staffContainer.setLayout(new BoxLayout(staffContainer, BoxLayout.X_AXIS));
		JButton newBtn = new JButton("New Staff");
		newBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicView.newStaff();
				if (musicView.staves.size() > 1) {
					deleteBtn.setEnabled(true);
				}
		}});
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicView.deleteStaff();
				if (musicView.staves.size() == 1) {
					deleteBtn.setEnabled(false);
				} else {
					deleteBtn.setEnabled(true);
				}
		}});
		staffContainer.add(newBtn);
		staffContainer.add(deleteBtn);

        return staffContainer;
    }

    public JPanel createPlayBackButtons(MusicView musicView) {
        JPanel playContainer = new JPanel();
		playContainer.setLayout(new BoxLayout(playContainer, BoxLayout.X_AXIS));
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playBtn.setSelected(true);
				musicView.timerStart();
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
			}}
		);
		playContainer.add(playBtn);
		playContainer.add(stopBtn);

        return playContainer;
    }

    public JPanel getPanel() {
        return this.container;
    }
}
