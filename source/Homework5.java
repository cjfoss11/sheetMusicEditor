import java.awt.*;
import javax.swing.*;
import gui.*;
import gui.Menu;
import gui.TextArea;


public class Homework5 {
	static MusicView musicView = new MusicView();
	
	//creating control panel and setting its attributes
	private static JPanel createControlPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		EditBtns editBtnContainer = new EditBtns(musicView);
		
		JPanel noteTypeContainer = new JPanel();
		noteTypeContainer.setLayout(new BoxLayout(noteTypeContainer, BoxLayout.X_AXIS));
		
		RadioBtns radioBtns = new RadioBtns();
		noteTypeContainer.add(radioBtns.getPanel());

		JSlider slider = gui.EditBtns.createSlider();
		noteTypeContainer.add(slider);
		
		panel.add(editBtnContainer.getPanel());
		panel.add(noteTypeContainer);
		return panel;
	}

	//build out frame with gui components
	private static void createAndShowGUI() {
		int width = 750;
		int height = 750;
		JFrame frame = new JFrame("My Music Editor");
		frame.setMinimumSize(new Dimension(600, 375));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		TextArea contentArea = new TextArea(musicView);
		frame.getContentPane().add(contentArea.getTextArea(), BorderLayout.CENTER);

		Menu menuBar = new Menu(musicView);
		frame.setJMenuBar(menuBar.getMenu());

		JPanel panel = createControlPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);

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