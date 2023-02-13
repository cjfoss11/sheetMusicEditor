package gui;
import java.awt.event.*;
import javax.swing.*;

public class Menu extends JMenuBar{
    JMenuBar menuBar;
   
    public Menu(MusicView musicView) {
        menuBar = new JMenuBar();

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
				musicView.newStaff();
				if (musicView.notesAndRests.size() > 1) {
					EditBtns.deleteBtn.setEnabled(true);
				}
		}});
		JMenuItem deleteStaff = new JMenuItem("Delete Staff");
		deleteStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicView.deleteStaff();
				if (musicView.notesAndRests.size() == 1) {
					EditBtns.deleteBtn.setEnabled(false);
				} else {
					EditBtns.deleteBtn.setEnabled(true);
				}
		}});
		edit.add(newStaff);
		edit.add(deleteStaff);

		//put menu together
		menuBar.add(file);
		menuBar.add(edit);
    }

	public JMenuBar getMenu() {
		return this.menuBar;
	}
	
}
