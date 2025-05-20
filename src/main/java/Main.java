import java.util.ArrayList;
import java.util.List;
import model.Personne;
import view.Login;
import view.FRAME;



public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Login frame = new Login();
            frame.setVisible(true); // Rend la fenêtre visible            
        });
    }
 

}