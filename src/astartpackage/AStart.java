package astartpackage;
import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*; 
import drawerpackage.*;

public class AStart extends JFrame implements ActionListener{

    // frame 
    static JFrame frame; 
    private Drawer drawer ;
    public AStart(int widthin, int heightin)
    {
        // create a new frame 
        frame = new JFrame("AstarAlgorythm"); 
        // create a panel 
        drawer = new Drawer(widthin, heightin); 
        drawer.setBackground(Color.BLACK);

        frame.add(drawer);

        // set the size of frame 
        frame.setSize(widthin, heightin); 
  
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) 
    { 
    }
}
