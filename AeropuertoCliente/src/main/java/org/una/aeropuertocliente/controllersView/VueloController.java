 package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
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
import org.una.aeropuertocliente.WebService.AvionWebService;
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
    @FXML private JFXComboBox<String> cb_buscarAvion;
    @FXML private JFXTextField txt_avion;
    @FXML private DatePicker dpk_fechaSalida;
    @FXML private DatePicker dpk_fechaLlegada;
    @FXML private JFXTextField txt_distancia;
    @FXML private JFXTextField txt_duracion;
    @FXML private ImageView cargando;
    @FXML private JFXTextField txt_horaSalida;
    @FXML private JFXTextField txt_horaLlegada;
    
    private String token, EstadoVuelo;
    private ObservableList<VueloC> DatosServicios;
    private List<VueloDTO> vuelo;
    
    VueloDTO VueloSeleccionado = new VueloDTO();
    boolean BotonGuardar = false;
    
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
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");
        
        VueloC vuelo1 = new VueloC(vuelo.getId(),vuelo.getDuracion(),vuelo.getAeropuerto(),
        vuelo.getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).format(formatter),
        vuelo.getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).format(formatter),vuelo.getDistancia(),
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
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");
            
            VueloC vuelo1 = new VueloC(vuelo.get(i).getId(),vuelo.get(i).getDuracion(),vuelo.get(i).getAeropuerto(),
            vuelo.get(i).getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).format(formatter),
            vuelo.get(i).getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).format(formatter),vuelo.get(i).getDistancia(),
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
        VueloSeleccionado = VueloWebService.getVueloById(Id, token);
    }
    private void volver(MouseEvent event) {
        FlowController.getInstance().goView("MenuGestion");
    }
    
    @FXML
    private void modificar(MouseEvent event) {
        
        LimpiaBarraInferior();
        BotonGuardar = true;
        vb_barraInferior.setPrefHeight(200);
        vb_barraInferior.setVisible(true);
        
        cb_buscarAvion.setValue("Por matricula");
        
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


    @FXML
    private void nuevo(MouseEvent event) {
        LimpiaBarraInferior();
        BotonGuardar = false;
        vb_barraInferior.setPrefHeight(200);
        vb_barraInferior.setVisible(true);
    }

    @FXML
    private void cancelar(MouseEvent event) {
        LimpiaBarraInferior();
        vb_barraInferior.setPrefHeight(0);
        vb_barraInferior.setVisible(false); 
    }
    
    
    @FXML
    private void guardar(MouseEvent event) throws ParseException, InterruptedException, ExecutionException, IOException {
    if (BotonGuardar == false) 
        {VueloSeleccionado = new VueloDTO();}
        
        vb_barraInferior.setPrefHeight(0);
        vb_barraInferior.setVisible(false);

        String horaSalida = txt_horaSalida.getText();
        String FechaHoraSalida = dpk_fechaSalida.getValue().toString() + " " + horaSalida;
        Date dateSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(FechaHoraSalida);  
        System.out.println("Fecha a insertar: " + dateSalida);
        
        String horaLlegada = txt_horaLlegada.getText();
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

        VueloSeleccionado.setEstado(true);
        
        if (BotonGuardar == true) 
        {VueloWebService.updateVuelo(VueloSeleccionado, VueloSeleccionado.getId(), token);}
        if (BotonGuardar == false) 
        {
           VueloWebService.createVuelo(VueloSeleccionado, token);
        }
        
        LimpiaBarraInferior();    
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
    
    private void LimpiaBarraInferior()
    {
        txt_destino.setText("");
        txt_avion.setText("");
        txt_distancia.setText("");
        txt_duracion.setText("");
        txt_horaSalida.setText("");
        txt_horaLlegada.setText("");
        cb_buscarAvion.setValue(""); 
        dpk_fechaLlegada.setValue(null);
        dpk_fechaSalida.setValue(null);             
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
