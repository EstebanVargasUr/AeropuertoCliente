package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.una.aeropuertocliente.DTOs.AerolineaDTO;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.WebService.AerolineaWebService;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

public class AerolineaController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private JFXTextField txt_buscar;
    @FXML private JFXTextField txt_nombre;
    @FXML private JFXTextField txt_encargado;
    @FXML private JFXComboBox<String> cb_filtroEstado;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private JFXComboBox<String> cb_estado;
    @FXML private TableView<AerolineaC> tablaAerolineas;
    @FXML private TableColumn<AerolineaC, String> tbc_id;
    @FXML private TableColumn<AerolineaC, String> tbc_nombre;
    @FXML private TableColumn<AerolineaC, String> tbc_encargado;
    @FXML private TableColumn<AerolineaC, String> tbc_fechaRegistro;
    @FXML private TableColumn<AerolineaC, String> tbc_fechaModificacion;
    @FXML private TableColumn<AerolineaC, String> tbc_estado;
    @FXML private TableColumn<AerolineaC, String> tbc_aviones;
    @FXML private TableColumn<AerolineaC, String> tbc_modificar;
    @FXML private VBox vb_barraInferior;
    @FXML private JFXButton btn_nuevo;
    @FXML private ImageView cargando;

    public static ObservableList<AerolineaC> DatosAerolineas;
    private AuthenticationResponse authenticationResponse;
    private AerolineaC AerolineaSeleccionada;
    private JFXDialogLayout contenido = new JFXDialogLayout();
    private JFXDialog dialogo;
    private JFXTextField cedula;
    private JFXPasswordField password;
    private Mensaje msg = new Mensaje();
    private boolean BotonGuardar;
    private String EstadoAerolinea;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        initTabla();
        initComboBox();  
    }
    
    @Override
    public void initialize() {
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        root.styleProperty().set("-fx-background-color: #4AB19D"); 
        ModoDesarrollador();
        
    }

    @Override
    public Node getRoot() {
        return root;
    }
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V01-G-AER");
        else
           FlowController.getInstance().titulo("Gestión de Aerolineas");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    private void initTabla(){
        initColumnas();
        tablaAerolineas.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tbc_encargado.setCellValueFactory(new PropertyValueFactory<>("Encargado"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory<>("FechaRegistro"));
        tbc_fechaModificacion.setCellValueFactory(new PropertyValueFactory<>("FechaModificacion"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tbc_aviones.setCellValueFactory(new PropertyValueFactory<>("Aviones"));
        tbc_modificar.setCellValueFactory(new PropertyValueFactory<>("Modificar"));
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Todas");
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Nombre");
        cb_filtro.getItems().add("Estado");
        
        cb_filtroEstado.getItems().add("Activo");
        cb_filtroEstado.getItems().add("Inactivo");
        
        cb_estado.getItems().add("Activo");
        cb_estado.getItems().add("Inactivo");

    }

    @FXML
    private void buscar(MouseEvent event){
        CargaLogicaBusqueda();
    }
    
    private void RealizarBusqueda() {
        LimpiaDatos();
        DatosAerolineas = FXCollections.observableArrayList();
        EstadoAerolinea = "Inactivo";
        try{
            if(cb_filtro.getValue() == null) 
            {
                CargaGraficaMsg("Por favor seleccione un filtro");
            }
            else
            {
                if(cb_filtro.getValue().equals("Id")) {
                    
                    if (txt_buscar.getText().equals("")) 
                    {CargaGraficaMsg("Por favor complete los campos respectivos");}
                    else
                    {busquedaIndividual();}
                } 
                else {
                     if (cb_filtro.getValue().equals("Nombre")) {
                        
                        if (txt_buscar.getText().equals(""))
                        CargaGraficaMsg("Por favor complete los campos respectivos");
                        else
                            busquedaLista();
                    }
                    else if (cb_filtro.getValue().equals("Estado")) {
                        
                        if (cb_filtroEstado.getValue() == null) 
                        CargaGraficaMsg("Por favor complete los campos respectivos");
                        else
                            busquedaLista(); 
                    }
                    else if (cb_filtro.getValue().equals("Todas")) {
                        busquedaLista(); 
                    }
                }
            tablaAerolineas.setItems(DatosAerolineas);
            }
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaController.class.getName()).log(Level.SEVERE, null, ex);}
    }

    private void busquedaIndividual() throws InterruptedException, IOException, ExecutionException {
        
        long Id = Long.parseLong(txt_buscar.getText());
        AerolineaDTO aerolinea = AerolineaWebService.getAerolineaById(Id, authenticationResponse.getJwt());

        if (aerolinea.getId() != null) {
            if (aerolinea.getEstado().toString().equals("true")) 
            EstadoAerolinea = "Activo";
            else
            {EstadoAerolinea = "Inactivo";}

            AerolineaC aerolinea1 = new AerolineaC(aerolinea.getId(),aerolinea.getNombreAerolinea(),aerolinea.getNombreResponsable(),
            aerolinea.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            aerolinea.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,new JFXButton("Aviones"),new JFXButton("Modificar"));

            DatosAerolineas.add(aerolinea1);
        }
        else
        {CargaGraficaMsg("No se encontraron aerolineas");}    
        
    }
    
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
        List<AerolineaDTO> ListaAerolineas = null;
        EstadoAerolinea = "Inactivo";
        switch(cb_filtro.getValue()){

            case "Todas":
                ListaAerolineas = AerolineaWebService.getAllAerolineas(authenticationResponse.getJwt());
            break;

            case "Nombre":
                ListaAerolineas = AerolineaWebService.getAerolineaByNombreAerolinea(txt_buscar.getText(), authenticationResponse.getJwt());
            break;

            case "Estado": 
                Boolean Estado;
                if (cb_filtroEstado.getValue().equals("Activo")) 
                    Estado = true;
                else
                    Estado = false;
                ListaAerolineas = AerolineaWebService.getAerolineaByEstado(Estado, authenticationResponse.getJwt());
            break;

            default:
                break;
        }
        
        if (ListaAerolineas.toArray().length != 0) {
            for(AerolineaDTO aerolinea : ListaAerolineas){
            if (aerolinea.getEstado().toString().equals("true")) 
            {EstadoAerolinea = "Activo";}
            else
            {EstadoAerolinea = "Inactivo";}
            
            AerolineaC aerolinea1 = new AerolineaC(aerolinea.getId(),aerolinea.getNombreAerolinea(),aerolinea.getNombreResponsable(),
            aerolinea.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            aerolinea.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,new JFXButton("Aviones"),new JFXButton("Modificar"));

            DatosAerolineas.add(aerolinea1);
            }
        }
        else
        {CargaGraficaMsg("No se encontraron aerolineas");}    
    }
    
    @FXML
    private void nuevo(MouseEvent event) {
        LimpiaBarraInferior();
        BotonGuardar = true;
        cb_estado.setValue("Activo");
        configurarBarraInferior(true);
    }

    @FXML
    private void tablaUsuariosClicked(MouseEvent event) {
        AerolineaSeleccionada = tablaAerolineas.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void cancelar(MouseEvent event) {
        LimpiaBarraInferior();
        configurarBarraInferior(false);
    }
    
    private void configurarBarraInferior(boolean modo){
        if(modo){
            vb_barraInferior.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            vb_barraInferior.setMinSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            vb_barraInferior.setVisible(true);
        }
        else{
            vb_barraInferior.setPrefSize(0, 0);
            vb_barraInferior.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
            vb_barraInferior.setVisible(false);
        }
            
    }

    @FXML
    private void guardar(MouseEvent event) {
        if(txt_nombre.getText().equals("")||txt_encargado.getText().equals("")||cb_estado.getValue() == null) 
            CargaGraficaMsg("Por favor complete los campos necesarios para crear la aerolinea");
        else
            loginEncargado();
    }
    
    private void RealizarGuardar() {  
        configurarBarraInferior(false);
        
        try{
            Boolean Estado;
            AerolineaDTO aerolineaAccion = new AerolineaDTO();
            aerolineaAccion.setNombreAerolinea(txt_nombre.getText());
            aerolineaAccion.setNombreResponsable(txt_encargado.getText());
            
            if (cb_estado.getValue().equals("Activo")) 
                Estado = true;
            else
                Estado = false;
            aerolineaAccion.setEstado(Estado);

            if (!BotonGuardar) {
                aerolineaAccion.setId(AerolineaSeleccionada.getId());
                AerolineaWebService.updateAerolinea(aerolineaAccion, AerolineaSeleccionada.getId(), authenticationResponse.getJwt());

            }
            else{
               AerolineaSeleccionada = new AerolineaC();
               AerolineaWebService.createAerolinea(aerolineaAccion, authenticationResponse.getJwt());
            }

        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void LimpiaDatos(){
        ObservableList items = FXCollections.observableArrayList(); 
        tablaAerolineas.setItems(items);    
    }
    
    private void LimpiaBarraInferior() {
        txt_nombre.clear();
        txt_encargado.clear();
        cb_estado.setValue("");
    }

    @FXML
    private void cb_filtroAction(ActionEvent event) {
        txt_buscar.clear();
        cb_filtroEstado.setPrefWidth(0);    
        cb_filtroEstado.setVisible(false);
        txt_buscar.setDisable(false);       
        txt_buscar.setPromptText("Digite lo que desea buscar");
        
        if(cb_filtro.getValue().equals("Todas")) 
           txt_buscar.setDisable(true);
        if (cb_filtro.getValue().equals("Estado")) 
        {
            cb_filtroEstado.setPrefWidth(100);
            cb_filtroEstado.setVisible(true);
            txt_buscar.setDisable(true);
        }  
    }
    
    private void CargaLogicaBusqueda(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            RealizarBusqueda(); 
            
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }
    
    private void CargaLogicaGuardar(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            RealizarGuardar(); 
            CargaGraficaGuardar();
            
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
        t.start();
    }
    
    private void CargaGraficaGuardar(){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            LimpiaBarraInferior();
            LimpiaDatos();
        }
        });
    }
    
    private void CargaGraficaMsg(String cuerpo){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            msg.alerta(root, "Alerta", cuerpo);
        }
        });
    }
    
    public class AerolineaC {
    
        Long Id;  
        String Nombre;
        String Encargado;
        String FechaRegistro;
        String FechaModificacion;
        String Estado;
        JFXButton Aviones;
        JFXButton Modificar;

        public AerolineaC(long Id, String Nombre, String Encargado, String FechaRegistro,
                String FechaModificacion, String Estado, JFXButton Aviones, JFXButton Modificar) {
            this.Id = Id;
            this.Nombre = Nombre;
            this.Encargado = Encargado;
            this.FechaRegistro = FechaRegistro;
            this.FechaModificacion = FechaModificacion;
            this.Estado = Estado;
            this.Aviones = Aviones;
            this.Modificar = Modificar;

            Aviones.getStyleClass().add("jfx-buttonStandard1");
            Modificar.getStyleClass().add("jfx-buttonStandard1");
            
            Aviones.setOnAction(e -> {
                for(AerolineaC aerolinea : DatosAerolineas){
                    if(aerolinea.getAviones() == Aviones){
                        AvionController.aerolineaActual = aerolinea;
                        FlowController.getInstance().goView("Avion");
                    }
                }

            });
            
            Modificar.setOnAction(e -> {           
                for(AerolineaC aerolinea : DatosAerolineas){
                    if(aerolinea.getModificar() == Modificar){
                        LimpiaBarraInferior();
                        BotonGuardar = false;
                        configurarBarraInferior(true);
                        
                        AerolineaSeleccionada = aerolinea;
                        txt_nombre.setText(aerolinea.getNombre());
                        txt_encargado.setText(aerolinea.getEncargado());
                        cb_estado.setValue(aerolinea.getEstado());
                    }
                }

            });
        }

        private AerolineaC() {
           
        }
        
        public Long getId() {
            return Id;
        }

        public String getNombre() {
            return Nombre;
        }

        public String getEncargado() {
            return Encargado;
        }

        public String getFechaRegistro() {
            return FechaRegistro;
        }

        public String getFechaModificacion() {
            return FechaModificacion;
        }

        public String getEstado() {
            return Estado;
        }

        public JFXButton getAviones() {
            return Aviones;
        }

        public JFXButton getModificar() {
            return Modificar;
        }
    }
    
    public void cuerpoLoginEncargado(){
        contenido.setHeading(new Text("Aprovación del Gerente"));
        
        cedula = new JFXTextField();
        password = new JFXPasswordField();
    
        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("Cedula: "));
        vbox.getChildren().add(cedula);
        vbox.getChildren().add(new Label("Contraseña: "));
        vbox.getChildren().add(password);
        vbox.setSpacing(20);
        
        contenido.setBody(vbox);
    }
    
    public void realizarLoginEncargado(){
        if(cedula.getText().equals("") || password.getText().equals(""))
            msg.alerta(root, "Alerta", "Por favor complete los campos necesarios");
        else{
            Thread thread = new Thread(new Runnable(){
            public void run(){
                cargando.setVisible(true);
                root.setDisable(true);
                try{
                    AuthenticationResponse authenticationResponse = AutenticationWebService.login(cedula.getText(), password.getText(), root);
                    if(authenticationResponse != null)
                        if(authenticationResponse.getUsuario().getId().equals(FlowController.getInstance().authenticationResponse.getUsuario().getUsuarioJefe().getId())){
                            CargaLogicaGuardar();
                            dialogo.close();
                        }
                        else{
                            CargaGraficaMsg("El usuario autenticado no corresponde a su jefe directo");
                        }
                            
                } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(Mensaje.class.getName()).log(Level.SEVERE, null, ex);}

                cargando.setVisible(false);
                root.setDisable(false);
                dialogo.close();
            }
            });
            thread.start();
        }
    }
    
     public void loginEncargado(){
        cuerpoLoginEncargado();
        
        dialogo = new JFXDialog(root, contenido, JFXDialog.DialogTransition.RIGHT);
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
                realizarLoginEncargado();
            }
        });
        contenido.setActions(botonCancelar,botonAceptar);
        dialogo.show();
    }
}
