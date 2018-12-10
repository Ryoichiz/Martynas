/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package circularbuffer;

import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;
import laborai.gui.fx.Lab3WindowFX;

/**
 *
 * @author Martynas
 */
public class VykdymoModulisFX extends Application {

    public static void main(String [] args) {
        VykdymoModulisFX.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus 
        Lab3WindowFX.createAndShowFXGUI(primaryStage);
    }
}
