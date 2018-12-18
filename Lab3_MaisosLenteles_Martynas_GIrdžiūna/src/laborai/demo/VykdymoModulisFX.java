package laborai.demo;

import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;
import laborai.gui.fx.Lab3WindowFX;


/*
 * Darbo atlikimo tvarka - čia yra pradinė klasė.
 */
public class VykdymoModulisFX extends Application {

    public static void main(String[] args) {
        VykdymoModulisFX.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus 
        AutoTestai.atvaizdzioTestas();
        //setUserAgentStylesheet(STYLESHEET_MODENA);    //Nauja FX išvaizda
        setUserAgentStylesheet(STYLESHEET_CASPIAN); //Sena FX išvaizda
        Lab3WindowFX.createAndShowFXGUI(primaryStage);
    }
}
