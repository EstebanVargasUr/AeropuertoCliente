package org.una.aeropuertocliente.utility;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.TransaccionWebService;

/**
 *
 * @author Esteban Vargas
 */
public class Mensaje {
    
    public void alerta(StackPane root,String titulo,String cuerpo){
        JFXDialogLayout contenido = new JFXDialogLayout();
        contenido.setHeading(new Text(titulo));
        contenido.setBody(new Text(cuerpo));
        JFXDialog dialogo = new JFXDialog(root, contenido, JFXDialog.DialogTransition.RIGHT);
        JFXButton botonAceptar = new JFXButton("Aceptar");
        
        botonAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                dialogo.close();
            }
        });
        contenido.setActions(botonAceptar);
        dialogo.show();
    }
    
     public void reporteAveria(StackPane root, ImageView cargando){
        JFXDialogLayout contenido = new JFXDialogLayout();
        contenido.setHeading(new Text("Reporte de Averias del Sistema"));
        JFXTextArea areaTexto = new JFXTextArea();
        areaTexto.setPrefSize(150, 80);
        areaTexto.setPromptText("Digite una descripción detallada sobre el problema encontrado en la aplicación.");
        contenido.setBody(areaTexto);
        JFXDialog dialogo = new JFXDialog(root, contenido, JFXDialog.DialogTransition.RIGHT);
        JFXButton botonAceptar = new JFXButton("Aceptar");
        JFXButton botonCancelar = new JFXButton("Cancelar");
        botonCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
               dialogo.close();
            }
        });
        botonAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.out.println(areaTexto.getText());
                 Thread thread = new Thread(new Runnable(){
                    public void run(){
                        cargando.setVisible(true);
                        root.setDisable(true);
                        try{
                            TransaccionWebService.createTransaccion(areaTexto.getText(),"Soporte",
                            FlowController.getInstance().authenticationResponse.getUsuario(), FlowController.getInstance().authenticationResponse.getJwt());
                        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(Mensaje.class.getName()).log(Level.SEVERE, null, ex);}
                        cargando.setVisible(false);
                        root.setDisable(false);
                        dialogo.close();
                    }
                    });
                    thread.start();
            }
        });
        contenido.setActions(botonCancelar,botonAceptar);
        
        dialogo.show();
    }
}



