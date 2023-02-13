package gui;
import java.awt.*;
import javax.swing.*;

public class TextArea extends JScrollPane {
    JScrollPane scrollPane;

    public TextArea(MusicView musicView) {
        musicView.setPreferredSize(new Dimension(600, 600));
		musicView.setSize(new Dimension(600, 600));
		scrollPane = new JScrollPane(musicView);
       
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollBar vertical = scrollPane.createVerticalScrollBar();
		JScrollBar horizontal = scrollPane.createHorizontalScrollBar();
		scrollPane.setHorizontalScrollBar(horizontal);
		scrollPane.setVerticalScrollBar(vertical);
    }

    public JScrollPane getTextArea() {
        return scrollPane;
    }
}
