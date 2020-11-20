 package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.ServicioDTO;
import org.una.aeropuertocliente.DTOs.VueloDTO;
import org.una.aeropuertocliente.WebService.AlertaWebService;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.AvionWebService;
import org.una.aeropuertocliente.WebService.ServicioWebService;
import org.una.aeropuertocliente.WebService.VueloWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class VueloController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private DatePicker datePFechaFinal;
    @FXML private DatePicker datePFechaInicial;
    @FXML private JFXTextField txt_buscar;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private JFXComboBox<String> cb_filtroEstado;
    @FXML private TableView<VueloC> tablaVuelos;
    @FXML private TableColumn<VueloC, Long> tbc_id;
    @FXML private TableColumn<VueloC, String> tbc_matricula;
    @FXML private TableColumn<VueloC, String> tbc_destino;
    @FXML private TableColumn<VueloC, Long> tbc_distancia;
    @FXML private TableColumn<VueloC, Float> tbc_duracion;
    @FXML private TableColumn<VueloC, String> tbc_fechaSalida;
    @FXML private TableColumn<VueloC, String> tbc_fechaLlegada;
    @FXML private TableColumn<VueloC, String> tbc_estado;
    @FXML private VBox vb_barraInferior;
    @FXML private JFXTextField txt_destino;
    @FXML private JFXComboBox<String> cb_buscarAvion;
    @FXML private JFXTextField txt_avion;
    @FXML private DatePicker dpk_fechaSalida;
    @FXML private DatePicker dpk_fechaLlegada;
    @FXML private JFXTextField txt_distancia;
    @FXML private JFXTextField txt_duracion;
    @FXML private ImageView cargando;
    @FXML private JFXTextField txt_horaSalida;
    @FXML private JFXTextField txt_horaLlegada;
    @FXML private Label lblModfEstado;
    @FXML private JFXComboBox<String> cb_ModfEstado;
    
    private String token, EstadoVuelo;
    private ObservableList<VueloC> DatosServicios;
    private List<VueloDTO> vuelos;
    
    VueloDTO VueloSeleccionado = new VueloDTO();
    boolean BotonGuardar = false;
    private JFXDialogLayout contenido = new JFXDialogLayout();
    private JFXDialog dialogo;
    private JFXTextField cedula;
    private JFXPasswordField password;
    Mensaje msg = new Mensaje();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        token = FlowController.getInstance().authenticationResponse.getJwt();
        initTabla();
        initComboBox();  
    }    

    @Override
    public void initialize() {
        root.styleProperty().set("-fx-background-color: #4AB19D");
        ModoDesarrollador();
    }

    @Override
    public Node getRoot() {
        return root;
    }     
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V13-G-VUE");
        else
            FlowController.getInstance().titulo("Gestión de Vuelos");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }
    
    private void initTabla(){
        initColumnas();
        tablaVuelos.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_destino.setCellValueFactory(new PropertyValueFactory<>("Destino"));
        tbc_distancia.setCellValueFactory(new PropertyValueFactory<>("Distancia"));
        tbc_duracion.setCellValueFactory(new PropertyValueFactory<>("Duracion"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tbc_fechaLlegada.setCellValueFactory(new PropertyValueFactory<>("FechaLlegada"));
        tbc_fechaSalida.setCellValueFactory(new PropertyValueFactory<>("FechaSalida"));
        tbc_matricula.setCellValueFactory(new PropertyValueFactory<>("MatriculaAvion"));
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Aeropuerto de destino");
        cb_filtro.getItems().add("Estado");
        cb_filtro.getItems().add("Fecha de llegada");
        cb_filtro.getItems().add("Fecha de salida");
        cb_filtro.getItems().add("Id del avion"); 
        
        cb_filtroEstado.getItems().add("Activo"); 
        cb_filtroEstado.getItems().add("Inactivo");
        
        cb_ModfEstado.getItems().add("Activo"); 
        cb_ModfEstado.getItems().add("Inactivo");
        
        cb_buscarAvion.getItems().add("Por id");  
        cb_buscarAvion.getItems().add("Por matricula");
    }
    
    @FXML
    private void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }

    private void RealizarBusqueda() {
        try{
            DatosServicios = FXCollections.observableArrayList();
            EstadoVuelo = "-";
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
                    if (cb_filtro.getValue().equals("Id del avion") || cb_filtro.getValue().equals("Aeropuerto de destino")) {
                        
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
                    else if (cb_filtro.getValue().equals("Fecha de llegada") || cb_filtro.getValue().equals("Fecha de salida")) {
                        
                        if (datePFechaFinal.getValue() == null || datePFechaInicial.getValue() == null) 
                        CargaGraficaMsg("Por favor complete los campos respectivos");
                        else
                           busquedaLista();  
                    }
                }    
            tablaVuelos.setItems(DatosServicios);
            }     
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(VueloController.class.getName()).log(Level.SEVERE, null, ex);}
    }
     
    private void busquedaIndividual() throws InterruptedException, IOException, ExecutionException {
        long Id = Long.parseLong(txt_buscar.getText());
        VueloDTO vuelo_ = VueloWebService.getVueloById(Id, token);
        
        if (vuelo_.getId() != null) 
        {
            if (vuelo_.getEstado().toString().equals("true")) 
            {EstadoVuelo = "Activo";}
            else 
            {EstadoVuelo = "Inactivo";}
        
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");

            VueloC vuelo1 = new VueloC(vuelo_.getId(),vuelo_.getDuracion(),vuelo_.getAeropuerto(),
            vuelo_.getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).format(formatter),
            vuelo_.getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).format(formatter),vuelo_.getDistancia(),
            EstadoVuelo,vuelo_.getAvion().getMatricula());

            DatosServicios.add(vuelo1);
        }
        else
        {CargaGraficaMsg("No se encontraron vuelos");}
    }
     
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
        vuelos = null;

        filtroBusqueda();
        
        if (vuelos.toArray().length != 0) {
         
        for (VueloDTO vuelo : vuelos) 
        {
            if (vuelo.getEstado().toString().equals("true")) 
                EstadoVuelo = "Activo";
            else 
                EstadoVuelo = "Inactivo";
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");
            
            VueloC vuelo1 = new VueloC(vuelo.getId(),vuelo.getDuracion(),vuelo.getAeropuerto(),
            vuelo.getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).format(formatter),
            vuelo.getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).format(formatter),vuelo.getDistancia(),
            EstadoVuelo,vuelo.getAvion().getMatricula());

            DatosServicios.add(vuelo1);
        }
      }  
        else 
        {CargaGraficaMsg("No se encontraron vuelos");}  
    }
     
    private void filtroBusqueda() throws InterruptedException, IOException, ExecutionException{
         switch (cb_filtro.getValue()) {
            case "Estado":
                Boolean Estado = false;
                if(cb_filtroEstado.getValue().equals("Activo"))
                    Estado = true;
                else
                    Estado = false;
                vuelos = VueloWebService.getVueloByEstado(Estado, token);
                break;
            case "Aeropuerto de destino":
                vuelos = VueloWebService.getVueloByAeropuerto(txt_buscar.getText(), token);
                break;
            case "Id del avion":
                long Id = Long.parseLong(txt_buscar.getText());
                vuelos = VueloWebService.getVueloByAvionId(Id, token);
                break;
            case "Fecha de llegada":
            case "Fecha de salida":
                LocalDate localDate = datePFechaInicial.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date inicio = Date.from(instant);
                LocalDate localDate2 = datePFechaFinal.getValue();
                Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                Date fin = Date.from(instant2);
                if (cb_filtro.getValue().equals("Fecha de llegada")) 
                    vuelos = VueloWebService.getVueloByFechaLlegadaBetween(inicio,fin, token);
                if (cb_filtro.getValue().equals("Fecha de salida")) 
                    vuelos = VueloWebService.getVueloByFechaSalidaBetween(inicio,fin, token);
                break;
            default:
                break;
        }
    }
    
    private void LimpiaDatos() {
        ObservableList items = FXCollections.observableArrayList(); 
        tablaVuelos.setItems(items);    
    }
     
    @FXML
    private void cb_filtroAction(ActionEvent event) {
        txt_buscar.clear();
        datePFechaInicial.setPrefWidth(0);  datePFechaInicial.setVisible(false);
        datePFechaFinal.setPrefWidth(0);    datePFechaFinal.setVisible(false);
        cb_filtroEstado.setPrefWidth(0);    cb_filtroEstado.setVisible(false);
        txt_buscar.setDisable(false);       

        if (cb_filtro.getValue().equals("Estado")) 
        {
            cb_filtroEstado.setPrefWidth(100);
            cb_filtroEstado.setVisible(true);
            txt_buscar.setDisable(true); 
        }  
        if(cb_filtro.getValue().equals("Fecha de salida") || cb_filtro.getValue().equals("Fecha de llegada"))
        {
            datePFechaInicial.setPrefWidth(130);
            datePFechaInicial.setVisible(true);
            datePFechaFinal.setPrefWidth(130);
            datePFechaFinal.setVisible(true);
            txt_buscar.setDisable(true); 
        }
    }
    
    @FXML
    private void tablaVuelosClicked(MouseEvent event) throws InterruptedException, ExecutionException, IOException 
    {
        long Id = tablaVuelos.getSelectionModel().selectedItemProperty().get().Id;
        VueloSeleccionado = VueloWebService.getVueloById(Id, token);
    }
    
    private void volver(MouseEvent event) {
        FlowController.getInstance().goView("MenuGestion");
    }
    
    @FXML
    private void modificar(MouseEvent event) {        
        
        if (VueloSeleccionado.getId() == null) {
            CargaGraficaMsg("Por favor seleccione un vuelo para modificar");
        }
        else
        {   LimpiaBarraInferior();
            BotonGuardar = true;
            configurarBarraInferior(true);

            lblModfEstado.setVisible(true);
            cb_ModfEstado.setVisible(true);

            cb_buscarAvion.setValue("Por matricula");

            if (VueloSeleccionado.getEstado() == true) {
                cb_ModfEstado.setValue("Activo");
            }
            else {
                cb_ModfEstado.setValue("Inactivo");
            }

            txt_distancia.setText(VueloSeleccionado.getDistancia().toString());
            txt_duracion.setText(VueloSeleccionado.getDuracion().toString());
            txt_destino.setText(VueloSeleccionado.getAeropuerto());
            txt_avion.setText(VueloSeleccionado.getAvion().getMatricula());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            txt_horaSalida.setText(VueloSeleccionado.getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).format(formatter));
            txt_horaLlegada.setText(VueloSeleccionado.getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).format(formatter));

            dpk_fechaLlegada.setValue(VueloSeleccionado.getFechaLlegada().toInstant()
           .atZone(ZoneId.systemDefault()).toLocalDate());
            dpk_fechaSalida.setValue(VueloSeleccionado.getFechaSalida().toInstant()
           .atZone(ZoneId.systemDefault()).toLocalDate());
        }  
    }


    @FXML
    private void nuevo(MouseEvent event) {
        LimpiaBarraInferior();
        BotonGuardar = false;
        configurarBarraInferior(true);
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
        if (txt_horaSalida.getText().equals("")||dpk_fechaSalida.getValue() == null||txt_horaLlegada.getText().equals("")||dpk_fechaLlegada.getValue() == null||
                txt_distancia.getText().equals("")||txt_duracion.getText().equals("")||txt_destino.getText().equals("")||cb_buscarAvion.getValue() == null||
                txt_avion.getText().equals("")||cb_ModfEstado.getValue() == null) 
        {
           CargaGraficaMsg("Por favor complete los campos necesarios para crear el vuelo");
        }
        else
            loginEncargado(); 
    }
    
    private void RealizarGuardar() {
        try{
             if (BotonGuardar == false) 
            {VueloSeleccionado = new VueloDTO();}

            String horaSalida = txt_horaSalida.getText();
            String horaLlegada = txt_horaLlegada.getText();
            
            
            if (horaSalida.toCharArray().length !=8 || horaLlegada.toCharArray().length !=8) {
                CargaGraficaMsg("Por favor ingrese la hora con el formato correcto\n\nHH:mm:ss\n\nEjemplo: 22:55:10");
            }
            else {
                
            String FechaHoraSalida = dpk_fechaSalida.getValue().toString() + " " + horaSalida;
            Date dateSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(FechaHoraSalida);  
            System.out.println("Fecha a insertar: " + dateSalida);
            String FechaHoraLlegada = dpk_fechaLlegada.getValue().toString() + " " + horaLlegada;
            Date dateLlegada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(FechaHoraLlegada);  
            System.out.println("Fecha a insertar: " + dateLlegada);
            
            VueloSeleccionado.setFechaSalida(dateSalida);
            VueloSeleccionado.setFechaLlegada(dateLlegada);

            long Distancia = Long.parseLong(txt_distancia.getText());
            float Duracion = Float.parseFloat(txt_duracion.getText());

            VueloSeleccionado.setAeropuerto(txt_destino.getText());
            VueloSeleccionado.setDistancia(Distancia);
            VueloSeleccionado.setDuracion(Duracion);

            if (cb_buscarAvion.getValue().equals("Por id")) 
            {
                long Id = Long.parseLong(txt_avion.getText());
                VueloSeleccionado.setAvion(AvionWebService.getAvionById(Id, token));
            }
            else
            {VueloSeleccionado.setAvion(AvionWebService.getAvionByMatricula(txt_avion.getText(), token).get(0));}

            
                if (BotonGuardar == true){  
                    boolean Estado = cb_ModfEstado.getValue().equals("Activo");
                    
                    if(Estado && !VueloSeleccionado.getEstado()){
                    if(VueloSeleccionado.getDistancia()<=VueloSeleccionado.getAvion().getRecorridoMaximo()){
                        ServicioDTO servicio = ServicioWebService.getUltimoServicioByUsuarioIdAndTipoServicioId(VueloSeleccionado.getAvion().getId(), 1, token);
                        VueloDTO vuelo = VueloWebService.getUltimoVueloByAvionId(VueloSeleccionado.getAvion().getId(), token);

                        if(servicio !=null && vuelo != null && servicio.getFechaRegistro().compareTo(vuelo.getFechaLlegada()) > 0){
                            VueloSeleccionado.setEstado(Estado);
                            VueloWebService.updateVuelo(VueloSeleccionado, VueloSeleccionado.getId(), token); 
                            configurarBarraInferior(false);
                            
                         }else{
                            CargaGraficaMsg("El avión seleccionado no ha realizado una recarga de combustible desde el último vuelo,\n"
                                    + "por lo que no esta capacitado para despegar. Comuniquese con el departamento de servicios para\n"
                                    + "que le den mantenimiento al avión y pueda realizar el despegue.");
                            AlertaWebService.createAlerta("Falta de carga de combustible para el despegue", VueloSeleccionado, token);
                            }
                    }else{
                        CargaGraficaMsg("El avión seleccionado no es capaz de realizar distancias tan largas de recorrido.");
                        AlertaWebService.createAlerta("Exceso de distancia máxima de vuelo", VueloSeleccionado, token);
                    }
                    }else{
                        VueloSeleccionado.setEstado(Estado);
                        VueloWebService.updateVuelo(VueloSeleccionado, VueloSeleccionado.getId(), token); 
                        configurarBarraInferior(false);
                    }
                }
                else{
                    VueloSeleccionado.setEstado(cb_ModfEstado.getValue().equals("Activo"));
                    VueloWebService.createVuelo(VueloSeleccionado, token);
                    configurarBarraInferior(false);
                    
                }
            
            }
           
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaController.class.getName()).log(Level.SEVERE, null, ex);} catch (ParseException ex) {
            Logger.getLogger(VueloController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void LimpiaBarraInferior()
    {
        txt_destino.setText("");
        txt_avion.setText("");
        txt_distancia.setText("");
        txt_duracion.setText("");
        txt_horaSalida.setText("");
        txt_horaLlegada.setText("");
        cb_buscarAvion.setValue(""); 
        cb_ModfEstado.setValue(""); 
        dpk_fechaLlegada.setValue(null);
        dpk_fechaSalida.setValue(null);
        lblModfEstado.setVisible(false);
        cb_ModfEstado.setVisible(false);
    }
    
    public class VueloC {
    
        private Long Id;  
        private Float Duracion;
        private String Destino;
        private String FechaLlegada;
        private String FechaSalida;
        private Long Distancia;
        private String Estado;
        private String MatriculaAvion;

        public VueloC(long Id, Float Duracion, String Destino, String FechaLlegada, String FechaSalida, long Distancia, String Estado, String MatriculaAvion) {
            this.Id = Id;
            this.Duracion = Duracion;
            this.Destino = Destino;
            this.FechaLlegada = FechaLlegada;
            this.FechaSalida = FechaSalida;
            this.Distancia = Distancia;
            this.Estado = Estado;
            this.MatriculaAvion = MatriculaAvion;
        }

        public VueloC() {
        }

        public Long getId() {
            return Id;
        }

        public Float getDuracion() {
            return Duracion;
        }

        public String getDestino() {
            return Destino;
        }

        public String getFechaLlegada() {
            return FechaLlegada;
        }

        public String getFechaSalida() {
            return FechaSalida;
        }

        public Long getDistancia() {
            return Distancia;
        }

        public String getEstado() {
            return Estado;
        }

        public String getMatriculaAvion() {
            return MatriculaAvion;
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
