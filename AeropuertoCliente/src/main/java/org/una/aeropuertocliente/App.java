package org.una.aeropuertocliente;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.una.aeropuertocliente.utility.FlowController;


/**
 * JavaFX App
 */
public class App extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        stage.getIcons().add(new Image("org/una/aeropuertocliente/resources/IconoAvion.png"));
        stage.setTitle("Aeropuerto");
        FlowController.getInstance().goMain();
        FlowController.getInstance().goView("login");
    } 

    public static void main(String[] args) {
        launch();
    }
}