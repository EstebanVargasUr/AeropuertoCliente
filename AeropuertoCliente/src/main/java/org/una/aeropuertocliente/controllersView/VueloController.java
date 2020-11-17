 package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import lombok.*;
import org.una.aeropuertocliente.DTOs.VueloDTO;
import org.una.aeropuertocliente.WebService.VueloWebService;
import org.una.aeropuertocliente.utility.FlowController;

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
    @FXML private JFXComboBox<?> cb_buscarAvion;
    @FXML private JFXTextField txt_avion;
    @FXML private DatePicker dpk_fechaSalida;
    @FXML private DatePicker dpk_fechaLlegada;
    @FXML private JFXTextField txt_distancia;
    @FXML private JFXTextField txt_duracion;
    @FXML private ImageView cargando;
    
    private String token, EstadoVuelo;
    private ObservableList<VueloC> DatosServicios;
    private List<VueloDTO> vuelo;
    
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

    }
    
    @FXML
    private void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }

    private void RealizarBusqueda() {
        try{
            DatosServicios = FXCollections.observableArrayList();
            EstadoVuelo = "-";

            if(cb_filtro.getValue().equals("Id"))      
                busquedaIndividual();
            else
                busquedaLista();    

            tablaVuelos.setItems(DatosServicios);
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(VueloController.class.getName()).log(Level.SEVERE, null, ex);}
    }
     
    private void busquedaIndividual() throws InterruptedException, IOException, ExecutionException {
        long Id = Long.parseLong(txt_buscar.getText());
        VueloDTO vuelo = VueloWebService.getVueloById(Id, token);

        if (vuelo.getEstado().toString().equals("true")) 
            EstadoVuelo = "Activo";
        else 
            EstadoVuelo = "Inactivo";

        VueloC vuelo1 = new VueloC(vuelo.getId(),vuelo.getDuracion(),vuelo.getAeropuerto(),
        vuelo.getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
        vuelo.getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),vuelo.getDistancia(),
        EstadoVuelo,vuelo.getAvion().getMatricula());

        DatosServicios.add(vuelo1);
    }
     
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
        vuelo = null;

        filtoBusqueda();

        for (int i = 0; i < vuelo.toArray().length; i++) 
        {
            if (vuelo.get(i).getEstado().toString().equals("true")) 
                EstadoVuelo = "Activo";
            else 
                EstadoVuelo = "Inactivo";

            VueloC vuelo1 = new VueloC(vuelo.get(i).getId(),vuelo.get(i).getDuracion(),vuelo.get(i).getAeropuerto(),
            vuelo.get(i).getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            vuelo.get(i).getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),vuelo.get(i).getDistancia(),
            EstadoVuelo,vuelo.get(i).getAvion().getMatricula());

            DatosServicios.add(vuelo1);
        }
    }
     
    private void filtoBusqueda() throws InterruptedException, IOException, ExecutionException{
         switch (cb_filtro.getValue()) {
            case "Estado":
                Boolean Estado = false;
                if(cb_filtroEstado.getValue().equals("Activo"))
                    Estado = true;
                else
                    Estado = false;
                vuelo = VueloWebService.getVueloByEstado(Estado, token);
                break;
            case "Aeropuerto de destino":
                vuelo = VueloWebService.getVueloByAeropuerto(txt_buscar.getText(), token);
                break;
            case "Id del avion":
                long Id = Long.parseLong(txt_buscar.getText());
                vuelo = VueloWebService.getVueloByAvionId(Id, token);
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
                    vuelo = VueloWebService.getVueloByFechaLlegadaBetween(inicio,fin, token);
                if (cb_filtro.getValue().equals("Fecha de salida")) 
                    vuelo = VueloWebService.getVueloByFechaSalidaBetween(inicio,fin, token);
                break;
            default:
                break;
        }
    }
    
    @FXML
    private void cb_filtroAction(ActionEvent event) 
    {
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
    }
    private void volver(MouseEvent event) {
        FlowController.getInstance().goView("MenuGestion");
    }

    @FXML
    private void nuevo(MouseEvent event) {
        
    }

    @FXML
    private void cancelar(MouseEvent event) {
    }

    @FXML
    private void guardar(MouseEvent event) {
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
    
    /*private void CargaLogicaGuardar(){
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
    }*/
    
    private void CargaGrafica(){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
        }
        });
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor 
    @ToString
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
  }
}
