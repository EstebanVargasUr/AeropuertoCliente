package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
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
import org.una.aeropuertocliente.DTOs.AreaTrabajoAvionDTO;
import org.una.aeropuertocliente.DTOs.AreaTrabajoDTO;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.AvionDTO;
import org.una.aeropuertocliente.WebService.AerolineaWebService;
import org.una.aeropuertocliente.WebService.AreaTrabajoAvionWebService;
import org.una.aeropuertocliente.WebService.AreaTrabajoWebService;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.AvionWebService;
import org.una.aeropuertocliente.controllersView.AerolineaController.AerolineaC;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class AvionController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private JFXTextField txt_buscar;
    @FXML private JFXTextField txt_matricula;
    @FXML private JFXTextField txt_recorridoMax;
    @FXML private JFXTextField txt_aerolinea;
    @FXML private DatePicker dP_FechaInicial;
    @FXML private DatePicker dp_FechaFinal;
    @FXML private JFXComboBox<String> cb_filtroEstado;
    @FXML private JFXComboBox<String> cb_filtroTipo;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private JFXComboBox<String> cb_estado;
    @FXML private JFXComboBox<String> cb_tipo;
    @FXML private JFXComboBox<String> cb_zonaActual;
    @FXML private JFXComboBox<String> cb_aerolinea;
    @FXML private TableView<AvionC> tablaAviones;
    @FXML private TableColumn<AvionC, Long> tbc_id;
    @FXML private TableColumn<AvionC, String> tbc_matricula;
    @FXML private TableColumn<AvionC, String> tbc_tipo;
    @FXML private TableColumn<AvionC, String> tbc_aerolinea;
    @FXML private TableColumn<AvionC, String> tbc_recorridoActual;
    @FXML private TableColumn<AvionC, String> tbc_recorridoMaximo;
    @FXML private TableColumn<AvionC, String> tbc_zonaActual;
    @FXML private TableColumn<AvionC, String> tbc_fechaRegistro;
    @FXML private TableColumn<AvionC, String> tbc_fechaModificacion;
    @FXML private TableColumn<AvionC, String> tbc_estado;
    @FXML private TableColumn<AvionC, String> tbc_modificar;
    @FXML private VBox vb_barraInferior;
    @FXML private JFXButton btn_nuevo;
    @FXML private ImageView cargando;
   
    public static AerolineaC aerolineaActual;
    public static ObservableList<AvionC> DatosAviones;
    private AuthenticationResponse authenticationResponse;
    private AvionC AvionSeleccionado;
    private JFXDialogLayout contenido = new JFXDialogLayout();
    private JFXDialog dialogo;
    private JFXTextField cedula;
    private JFXPasswordField password;
    Mensaje msg = new Mensaje();
    boolean BotonGuardar;
    String EstadoAvion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        initTabla();
        initComboBox();
    }    
    
    @Override
    public void initialize() {
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        cb_filtro.setValue("Id de la Aerolinea");
        txt_buscar.setText(aerolineaActual.getId()+"");
        CargaLogicaBusqueda();
        root.styleProperty().set("-fx-background-color: #4AB19D");    
        ModoDesarrollador();
    }

    @Override
    public Node getRoot() {
        return root;
    } 
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V02-G-AVI");
        else
            FlowController.getInstance().titulo("Gestión de Aviones");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    private void initTabla(){
        initColumnas();
        tablaAviones.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_matricula.setCellValueFactory(new PropertyValueFactory<>("Matricula"));
        tbc_tipo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
        tbc_aerolinea.setCellValueFactory(new PropertyValueFactory<>("Aerolinea"));
        tbc_recorridoActual.setCellValueFactory(new PropertyValueFactory<>("RecorridoActual"));
        tbc_recorridoMaximo.setCellValueFactory(new PropertyValueFactory<>("RecorridoMaximo"));
        tbc_zonaActual.setCellValueFactory(new PropertyValueFactory<>("ZonaActual"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory<>("FechaRegistro"));
        tbc_fechaModificacion.setCellValueFactory(new PropertyValueFactory<>("FechaModificacion"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tbc_modificar.setCellValueFactory(new PropertyValueFactory<>("Modificar"));
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Matricula");
        cb_filtro.getItems().add("Tipo");
        cb_filtro.getItems().add("Fecha de Registro");
        cb_filtro.getItems().add("Estado");
        cb_filtro.getItems().add("Id de la Aerolinea");
        
        cb_filtroEstado.getItems().add("Activo");
        cb_filtroEstado.getItems().add("Inactivo");
        
        cb_filtroTipo.getItems().add("Comercial");
        cb_filtroTipo.getItems().add("Carga");
        cb_filtroTipo.getItems().add("Privado");
        cb_filtroTipo.getItems().add("Militar"); 
        
        cb_tipo.getItems().add("Comercial");
        cb_tipo.getItems().add("Carga");
        cb_tipo.getItems().add("Privado");
        cb_tipo.getItems().add("Militar");   
        
        cb_zonaActual.getItems().add("Descarga");
        cb_zonaActual.getItems().add("Hangar");
        cb_zonaActual.getItems().add("Mantenimiento");
        cb_zonaActual.getItems().add("Carga de Combustible");  
        
        cb_aerolinea.getItems().add("Nombre");
        cb_aerolinea.getItems().add("Id");
        
        cb_estado.getItems().add("Activo");
        cb_estado.getItems().add("Inactivo");
        
    }
    
    @FXML
    private void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }

    private void RealizarBusqueda() {
        LimpiaDatos();
        DatosAviones = FXCollections.observableArrayList();
        EstadoAvion = "Inactivo";    
        try{
            if(cb_filtro.getValue() == null) 
            {
                CargaGraficaMsg("Por favor seleccione un filtro");
            }
            else
            {
                if(cb_filtro.getValue().equals("Id"))      
                {   
                    if (txt_buscar.getText().equals("")) 
                    {CargaGraficaMsg("Por favor complete los campos respectivos");}
                    else
                    {busquedaIndividual();}
                }
                else
                {
                    if (cb_filtro.getValue().equals("Matricula") || cb_filtro.getValue().equals("Tipo") || cb_filtro.getValue().equals("Id de la Aerolinea")) {
                        
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
                    else if (cb_filtro.getValue().equals("Fecha de Registro")) {
                        
                        if (dP_FechaInicial.getValue() == null || dp_FechaFinal.getValue() == null) 
                        CargaGraficaMsg("Por favor complete los campos respectivos");
                        else
                           busquedaLista();  
                    }
                }
            tablaAviones.setItems(DatosAviones);
            }       
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AvionController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void busquedaIndividual() throws InterruptedException, IOException, ExecutionException {
       
        long Id = Long.parseLong(txt_buscar.getText());
        AvionDTO avion = AvionWebService.getAvionById(Id, authenticationResponse.getJwt());
        AreaTrabajoAvionDTO avionTrabajo = AreaTrabajoAvionWebService.getAreaTrabajoAvionById(Id, authenticationResponse.getJwt());

        if (avion.getId() != null) {
            if (avion.getEstado().toString().equals("true")) 
            {EstadoAvion = "Activo";}
            else
            {EstadoAvion = "Inactivo";}

            AvionC avion1 = new AvionC(avion.getId(),avion.getMatricula(),avion.getTipoAvion(),avion.getAerolinea().getNombreAerolinea(),avion.getRecorrido()+"",
            avion.getRecorridoMaximo()+"",avionTrabajo.getAreaTrabajo().getNombreArea(),avion.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            avion.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAvion,new JFXButton("Modificar"));

            DatosAviones.add(avion1);
        }
        else 
        {CargaGraficaMsg("No se encontraron aviones");}    
    }
    
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
        List<AvionDTO> ListaAvion = null;

        switch(cb_filtro.getValue()){
            case "Matricula":
                ListaAvion = AvionWebService.getAvionByMatricula(txt_buscar.getText(), authenticationResponse.getJwt());
            break;

            case "Tipo":
                ListaAvion = AvionWebService.getAvionByTipoAvion(txt_buscar.getText(), authenticationResponse.getJwt());
            break;

            case "Estado":
                Boolean Estado;
                if(cb_filtroEstado.getValue().equals("Activo")) 
                    Estado = true;
                else
                    Estado = false;
                ListaAvion = AvionWebService.getAvionByEstado(Estado, authenticationResponse.getJwt());
            break;

            case "Fecha de Registro":
                LocalDate localDate = dP_FechaInicial.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date inicio = Date.from(instant);

                LocalDate localDate2 = dp_FechaFinal.getValue();
                Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                Date fin = Date.from(instant2);

                ListaAvion = AvionWebService.getAvionByFechaRegistroBetween(inicio,fin, authenticationResponse.getJwt());
            break;

            case "Id de la Aerolinea":
                ListaAvion = AvionWebService.getAvionByAerolineaId(txt_buscar.getText(), authenticationResponse.getJwt());
            break;
            default:
                break;

        }
        if (ListaAvion.toArray().length != 0) {
            for(AvionDTO avion : ListaAvion) {
            if(avion.getEstado().toString().equals("true")) 
            {EstadoAvion = "Activo";}
            else
            {EstadoAvion = "Inactivo";}

            AreaTrabajoAvionDTO avionTrabajo = AreaTrabajoAvionWebService.getAreaTrabajoAvionByAvionId(avion.getId(), authenticationResponse.getJwt());

            AvionC avion1 = new AvionC(avion.getId(),avion.getMatricula(),avion.getTipoAvion(),avion.getAerolinea().getNombreAerolinea(),avion.getRecorrido()+"",
            avion.getRecorridoMaximo()+"",avionTrabajo.getAreaTrabajo().getNombreArea(),avion.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            avion.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAvion,new JFXButton("Modificar"));

            DatosAviones.add(avion1);
            }
        }
        else
        {CargaGraficaMsg("No se encontraron los aviones");}  
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
        AvionSeleccionado = tablaAviones.getSelectionModel().getSelectedItem();
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
        if (txt_matricula.getText().equals("")||txt_recorridoMax.getText().equals("")||txt_aerolinea.getText().equals("")||
        cb_estado.getValue() == null||cb_aerolinea.getValue() == null|| cb_tipo.getValue() == null|| cb_zonaActual.getValue() == null) 
           CargaGraficaMsg("Por favor complete los campos necesarios para registrar el avión");
        else
            loginEncargado();
    }
    
    private void RealizarGuardar() {
        configurarBarraInferior(false);
        try{
            AvionDTO avionAccion = new AvionDTO();
            AerolineaDTO aerolinea = new AerolineaDTO();
            AreaTrabajoDTO areaTrabajo = AreaTrabajoWebService.getAreaTrabajoByNombreArea(cb_zonaActual.getValue(), authenticationResponse.getJwt()).get(0);
            Boolean Estado;

            if(cb_aerolinea.getValue().equals("Nombre"))
            {aerolinea = AerolineaWebService.getAerolineaByNombreAerolinea(txt_aerolinea.getText(), authenticationResponse.getJwt()).get(0);}
            else if(cb_aerolinea.getValue().equals("Id"))
            {aerolinea = AerolineaWebService.getAerolineaById(Long.parseLong(txt_aerolinea.getText()), authenticationResponse.getJwt());}
            
            if(cb_estado.getValue().equals("Activo")) 
            {Estado = true;}
            else
            {Estado = false;}
            
            avionAccion.setEstado(Estado); 
            avionAccion.setMatricula(txt_matricula.getText());
            avionAccion.setTipoAvion(cb_tipo.getValue());
            avionAccion.setRecorrido(0);
            avionAccion.setRecorridoMaximo(Integer.parseInt(txt_recorridoMax.getText()) );
            avionAccion.setAerolinea(aerolinea);

            if (!BotonGuardar) {
                avionAccion.setId(AvionSeleccionado.getId());
                AreaTrabajoAvionDTO areaAvion= AreaTrabajoAvionWebService.getAreaTrabajoAvionByAvionId(avionAccion.getId(), authenticationResponse.getJwt());
                if(areaAvion.getAreaTrabajo().getNombreArea().equals("Torre de Control")){
                    if(areaTrabajo.getNombreArea().equals("Descarga")){
                        AvionWebService.updateAvion(avionAccion, AvionSeleccionado.getId(), authenticationResponse.getJwt());
                        AreaTrabajoAvionWebService.updateAreaTrabajoAvion(areaTrabajo, avionAccion, AreaTrabajoAvionWebService.getAreaTrabajoAvionByAvionId
                        (AvionSeleccionado.getId(), authenticationResponse.getJwt()).getId(), authenticationResponse.getJwt());
                    }else{
                        CargaGraficaMsg("Los aviones que aterrizan deben pasar a zona de descarga, antes de ir al hangar, area de mantenimiento, o carga de combustible.\n"
                            + "Por favor seleccione la zona de descarga para completar satisfactoriamente el cambio.");
                    }
                }
                else{
                    AvionWebService.updateAvion(avionAccion, AvionSeleccionado.getId(), authenticationResponse.getJwt());
                    AreaTrabajoAvionWebService.updateAreaTrabajoAvion(areaTrabajo, avionAccion, AreaTrabajoAvionWebService.getAreaTrabajoAvionByAvionId
                    (AvionSeleccionado.getId(), authenticationResponse.getJwt()).getId(), authenticationResponse.getJwt());
                }
                
                
            }
            else{
                if(areaTrabajo.getNombreArea().equals("Descarga")){
                    AvionSeleccionado = new AvionC();
                    AvionWebService.createAvion(avionAccion, authenticationResponse.getJwt());
                    avionAccion = AvionWebService.getAvionByMatricula(avionAccion.getMatricula(), authenticationResponse.getJwt()).get(0);
                    AreaTrabajoAvionWebService.createAreaTrabajoAvion(areaTrabajo, avionAccion, authenticationResponse.getJwt());
                }else
                    CargaGraficaMsg("Los aviones que aterrizan deben pasar a zona de descarga, antes de ir al hangar, area de mantenimiento, o carga de combustible.\n"
                            + "Por favor seleccione la zona de descarga para completar satisfactoriamente el registro.");
                    
                
            }

        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void LimpiaBarraInferior() {
        txt_matricula.clear();
        txt_recorridoMax.clear();
        cb_tipo.setValue("");
        cb_zonaActual.setValue("");  
        cb_aerolinea.setValue("");
        cb_estado.setValue("");
    }
    
     private void LimpiaDatos()
    {
        ObservableList items = FXCollections.observableArrayList(); 
        tablaAviones.setItems(items);    
    }

    @FXML
    private void cb_filtroAction(ActionEvent event) {
        txt_buscar.clear();
        dP_FechaInicial.setPrefWidth(0);  dP_FechaInicial.setVisible(false);
        dp_FechaFinal.setPrefWidth(0);    dp_FechaFinal.setVisible(false);
        cb_filtroEstado.setPrefWidth(0);    cb_filtroEstado.setVisible(false);
        txt_buscar.setDisable(false);       txt_buscar.setPromptText("Digite lo que desea buscar");
        
        if (cb_filtro.getValue().equals("Estado")) 
        {
            cb_filtroEstado.setPrefWidth(100);
            cb_filtroEstado.setVisible(true);
            txt_buscar.setDisable(true);
        }  
        if(cb_filtro.getValue().equals("Fecha de Registro"))
        {
            dP_FechaInicial.setPrefWidth(130);
            dP_FechaInicial.setVisible(true);
            dp_FechaFinal.setPrefWidth(130);
            dp_FechaFinal.setVisible(true);
            txt_buscar.setDisable(true); 
        }
        if(cb_filtro.getValue().equals("Tipo"))
        {
            txt_buscar.setPromptText("Nombre del tipo de avión");
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
    
    public class AvionC {
    
        Long Id;  
        String Matricula;
        String Tipo;
        String Aerolinea;
        String RecorridoActual;
        String RecorridoMaximo;
        String ZonaActual;
        String FechaRegistro;
        String FechaModificacion;
        String Estado;
        JFXButton Modificar;

        public AvionC(long Id, String Matricula, String Tipo, String Aerolinea, String RecorridoActual,
                String RecorridoMaximo,String ZonaActual, String FechaRegistro, String FechaModificacion, String Estado, JFXButton Modificar) {
            this.Id = Id;
            this.Matricula = Matricula;
            this.Tipo = Tipo;
            this.Aerolinea = Aerolinea;
            this.RecorridoActual = RecorridoActual;
            this.RecorridoMaximo = RecorridoMaximo;
            this.ZonaActual = ZonaActual;
            this.FechaRegistro = FechaRegistro;
            this.FechaModificacion = FechaModificacion;
            this.Estado = Estado;
            this.Modificar = Modificar;

            Modificar.getStyleClass().add("jfx-buttonStandard1");

            Modificar.setOnAction(e -> {
                for(AvionC avion : DatosAviones){
                    if(avion.getModificar() == Modificar){
                        LimpiaBarraInferior();
                        BotonGuardar = false;
                        configurarBarraInferior(true);
                        
                        AvionSeleccionado = avion;
                        txt_matricula.setText(avion.getMatricula());
                        txt_recorridoMax.setText(avion.getRecorridoMaximo());
                        txt_aerolinea.setText(avion.getAerolinea());
                        cb_tipo.setValue(avion.getTipo());
                        cb_aerolinea.setValue("Nombre");
                        cb_zonaActual.setValue(avion.getZonaActual());
                        cb_estado.setValue(avion.getEstado());
                    }
                }

            });
        }

        private AvionC() {
           
        }

        public Long getId() {
            return Id;
        }

        public String getMatricula() {
            return Matricula;
        }

        public String getTipo() {
            return Tipo;
        }

        public String getAerolinea() {
            return Aerolinea;
        }

        public String getRecorridoActual() {
            return RecorridoActual;
        }

        public String getRecorridoMaximo() {
            return RecorridoMaximo;
        }

        public String getZonaActual() {
            return ZonaActual;
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
