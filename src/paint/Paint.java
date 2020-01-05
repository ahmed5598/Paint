/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import paint.View.*;
import paint.controller.*;
import paint.model.*;

/**
 *
 * @author Ahmed
 */
public class Paint {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        View view = new View();
        controller.setView(view);
        view.setVisible(true);
    }
    
}
