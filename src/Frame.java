import javax.swing.*;

public class Frame extends JFrame{

    Frame(){
        this.add(new Panel());
        this.setTitle("Saper");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
