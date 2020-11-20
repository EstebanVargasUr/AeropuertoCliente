package org.una.aeropuertocliente.utility;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class IndicadorCarga {
 
    public static Stage stage;
    public void ejecutarCarga() throws IOException{
 
        Parent root = FXMLLoader.load(getClass().getResource("/org/una/aeropuertocliente/view/Cargando.fxml"));
        stage = new Stage();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.toFront();
      //  stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    public void detenerCarga(){
        stage.close();
    }
}