package org.una.aeropuertocliente.controllersView;

import java.io.IOException;
import java.net.URL;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.HoraMarcajeDTO;
import org.una.aeropuertocliente.WebService.HoraMarcajeWebService;
import org.una.aeropuertocliente.controllersView.UsuarioController.UsuarioC;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author adria
 */
public class HoraMarcajeController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private TableView<HoraMarcajeC> tablaHoraMarcaje;
    @FXML private TableColumn<HoraMarcajeC, String> tbc_horaEntrada;
    @FXML private TableColumn<HoraMarcajeC, String> tbc_horaSalida;
    @FXML private TableColumn<HoraMarcajeC, String> tbc_fechaRegistro;
    @FXML private TableColumn<HoraMarcajeC, String> tbc_fechaModificacion;
    @FXML private ImageView cargando;
    @FXML private DatePicker dP_FechaInicial;
    @FXML private DatePicker dP_FechaFinal;
    
    boolean BotonGuardar;
    ObservableList<HoraMarcajeC> DatosHorasMarcaje = FXCollections.observableArrayList();
    AuthenticationResponse authenticationResponse = FlowController.getInstance().authenticationResponse;;
    Mensaje msg = new Mensaje();
    public static UsuarioC usuarioActual;
    private List<HoraMarcajeDTO> ListaHoraMarcaje;

    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        initTabla();
    }
    
    @Override
    public void initialize() {
        root.styleProperty().set("-fx-background-color: #4AB19D");
        LimpiaDatos();
        dP_FechaFinal.setValue(null);
        dP_FechaInicial.setValue(null);
    }
    
    @Override
    public Node getRoot() {
        return root;
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);
    }    

    private void initTabla(){
        
        tbc_fechaModificacion.setCellValueFactory(new PropertyValueFactory("fechaModificacion"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory("fechaRegistro"));
        tbc_horaEntrada.setCellValueFactory(new PropertyValueFactory("horaEntrada"));
        tbc_horaSalida.setCellValueFactory(new PropertyValueFactory("horaSalida"));
        
        tablaHoraMarcaje.setEditable(true);
    }
    
    
    @FXML
    private void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }
    
    private void RealizarBusqueda() {
            LimpiaDatos();

            if (dP_FechaInicial.getValue() == null || dP_FechaFinal.getValue() == null) 
            {CargaGraficaMsg("Por favor complete los campos respectivos");}
            else
            {   
                try {
                    DatosHorasMarcaje = FXCollections.observableArrayList();
                    
                    ListaHoraMarcaje = null;
                    
                    ObtieneHorasMarcaje();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    
                    if (!ListaHoraMarcaje.isEmpty()) {
                        for (int i = 0; i < ListaHoraMarcaje.toArray().length ; i++) {
                            HoraMarcajeC horaMarcaje1 = new HoraMarcajeC(ListaHoraMarcaje.get(i).getId(),ListaHoraMarcaje.get(i).getHoraEntrada().toInstant().atZone(ZoneId.systemDefault()).format(formatter),ListaHoraMarcaje.get(i).getHoraSalida().toInstant().atZone(ZoneId.systemDefault()).format(formatter),
                                    ListaHoraMarcaje.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),ListaHoraMarcaje.get(i).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                            
                            DatosHorasMarcaje.add(horaMarcaje1);
                        }
                    }
                    else{
                        CargaGraficaMsg("No se encontraron horas de marcaje");
                    }
                    
 
                    tablaHoraMarcaje.setItems(DatosHorasMarcaje);
                } catch (InterruptedException | ExecutionException | IOException ex) {
                    Logger.getLogger(HoraMarcajeController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }   
  }

    private void ObtieneHorasMarcaje() throws InterruptedException, ExecutionException, IOException{
        LocalDate localDate = dP_FechaInicial.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date inicio = Date.from(instant);

        LocalDate localDate2 = dP_FechaFinal.getValue();
        Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
        Date fin = Date.from(instant2);

        ListaHoraMarcaje = HoraMarcajeWebService.getHoraMarcajeByFechaRegistroBetweenAndUsuarioId(inicio,fin,usuarioActual.getId() ,authenticationResponse.getJwt());  
    }
    private void LimpiaDatos(){
        ObservableList items = FXCollections.observableArrayList(); 
        tablaHoraMarcaje.setItems(items);    
    }
    
    private void CargaGraficaMsg(String cuerpo){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            msg.alerta(root, "Información", cuerpo);
        }
        });
    }
    
    private void CargaLogicaBusqueda(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            RealizarBusqueda(); 
            CargaGrafica();
        }
        });
        t.start();
    }
    
    private void CargaGrafica(){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            cargando.setVisible(false);
            root.setDisable(false);
        }
        });
    }

    @FXML
    private void tablaHorarioClicked(MouseEvent event) {
    }

    @FXML
    private void volver(MouseEvent event) {
        FlowController.getInstance().goView("Usuario");
    }

    public class HoraMarcajeC {
            
        Long id;
        String horaEntrada;
        String horaSalida;
        String fechaRegistro;
        String fechaModificacion;

        public HoraMarcajeC(long id,String horaEntrada, String horaSalida, String fechaRegistro, String fechaModificacion) {
            this.id = id;
            this.horaEntrada = horaEntrada;
            this.horaSalida = horaSalida;
            this.fechaRegistro = fechaRegistro;
            this.fechaModificacion = fechaModificacion;
        }

        public HoraMarcajeC() {
        }
        
        
        
        public Long getId() {
            return id;
        }
        
        public String getHoraEntrada() {
            return horaEntrada;
        }

        public String getHoraSalida() {
            return horaSalida;
        }

        public String getFechaRegistro() {
            return fechaRegistro;
        }

        public String getFechaModificacion() {
            return fechaModificacion;
        }   
    }     
}
