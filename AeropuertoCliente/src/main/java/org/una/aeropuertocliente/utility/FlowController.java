package org.una.aeropuertocliente.utility;

import org.una.aeropuertocliente.App;
import org.una.aeropuertocliente.controllersView.Controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;

public class FlowController {
    
    private static FlowController INSTANCE = null;
    private static Stage mainStage;
    private static ResourceBundle idioma;
    private static HashMap<String, FXMLLoader> loaders = new HashMap<>();
    public List<UsuarioAreaTrabajoDTO> areaTrabajo;
    public AuthenticationResponse authenticationResponse;
    public boolean modoDesarrollo = false;
    //FMLLoader: interfaz y controlador de la interfaz grafica
    private FlowController() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FlowController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlowController();
                }
            }
        }
    }

    public static FlowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void InitializeFlow(Stage stage, ResourceBundle idioma) {
        getInstance();
        this.mainStage = stage;
        this.idioma = idioma;
    }
 
    private FXMLLoader getLoader(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader == null) {
            synchronized (FlowController.class) {
                if (loader == null) {
                    try {
                        loader = new FXMLLoader(App.class.getResource("/org/una/aeropuertocliente/view/" + name + ".fxml"), this.idioma);
                        loader.load();
                        loaders.put(name, loader);
                    } catch (Exception ex) {
                        loader = null;
                        java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Creando loader [" + name + "].", ex);
                    }
                }
            }
        }
        return loader;
    }

    public void goMain() {
        try {
            this.mainStage.setScene(new Scene(FXMLLoader.load(App.class.getResource("/org/una/aeropuertocliente/view/Principal.fxml"), this.idioma)));
            this.mainStage.show();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Error inicializando la vista base.", ex);
        }
    }

    public void goView(String viewName) {
        goView(viewName, "Center", null);
       
    }
    
    public void goViewLeft(String viewName) {
        goView(viewName, "Left", null);
    }
    
    public void goViewRight(String viewName) {
        goView(viewName, "Right", null);
    }
    
    public void goViewTop(String viewName) {
        goView(viewName, "Top", null);
    }
    
    public void goViewBottom(String viewName) {
        goView(viewName, "Bottom", null);
    }
    
    public void goView(String viewName, String accion) {
        goView(viewName, "Center", accion);
    }

    public void goView(String viewName, String location, String accion) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setAccion(accion);
        controller.initialize();
        Stage stage = controller.getStage();
        if (stage == null) {
            stage = this.mainStage;
            controller.setStage(stage);
        }
        switch (location) {
            case "Center":
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().clear();   
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().add(loader.getRoot()); 
                break;
            case "Top":
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getTop()).getChildren().clear();
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getTop()).getChildren().add(loader.getRoot());
                break;
            case "Bottom":
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getBottom()).getChildren().clear();
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getBottom()).getChildren().add(loader.getRoot());
                break;
            case "Right":
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getRight()).getChildren().clear();
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getRight()).getChildren().add(loader.getRoot());
                break;
            case "Left":
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getLeft()).getChildren().clear();
                ((StackPane) ((BorderPane) stage.getScene().getRoot()).getLeft()).getChildren().add(loader.getRoot());
                break;
                
            default:
                break;
        }
    }

    public void goViewInStage(String viewName, Stage stage) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.getScene().setRoot(loader.getRoot());
    }

    public void goViewInWindow(String viewName) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
       // stage.getIcons().add(new Image("/cr/ac/una/tareacooperativa/resources/logo.png"));
        stage.setTitle("Aeropuerto");
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
      //  stage.getIcons().add(new Image("/cr/ac/una/tareacooperativa/resources/logo.png"));
        stage.setTitle("Aeropuerto");
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();

    }

    public Controller getController(String viewName) {
        return getLoader(viewName).getController();
    }

    public static void setIdioma(ResourceBundle idioma) {
        FlowController.idioma = idioma;
    }
    
    public void initialize() {
        this.loaders.clear();
    }

    public void salir() {
        this.mainStage.close();
    }
    
    public void titulo(String titulo) {
        this.mainStage.setTitle(titulo);
    }
}
