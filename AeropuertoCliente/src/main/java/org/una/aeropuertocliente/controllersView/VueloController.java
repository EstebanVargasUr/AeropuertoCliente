package org.una.aeropuertocliente.controllersView;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.*;
import org.una.aeropuertocliente.DTOs.VueloDTO;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.VueloWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class VueloController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private DatePicker datePFechaFinal;
    @FXML
    private DatePicker datePFechaInicial;
    @FXML
    private JFXTextField txt_buscar;
    @FXML
    private JFXComboBox<String> cb_filtro;
    @FXML
    private JFXComboBox<String> cb_filtroEstado;
    @FXML
    private TableView<VueloC> tablaVuelos;
    @FXML
    private TableColumn<VueloC, Long> tbc_id;
    @FXML
    private TableColumn<VueloC, String> tbc_matricula;
    @FXML
    private TableColumn<VueloC, String> tbc_destino;
    @FXML
    private TableColumn<VueloC, Long> tbc_distancia;
    @FXML
    private TableColumn<VueloC, Float> tbc_duracion;
    @FXML
    private TableColumn<VueloC, String> tbc_fechaSalida;
    @FXML
    private TableColumn<VueloC, String> tbc_fechaLlegada;
    @FXML
    private TableColumn<VueloC, String> tbc_estado;

    String token;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            token = AutenticationWebService.login("admin", "Una2020");
        } catch (InterruptedException | ExecutionException | JsonProcessingException ex) 
        {Logger.getLogger(ServicioController.class.getName()).log(Level.SEVERE, null, ex);}
        
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Aeropuerto de destino");
        cb_filtro.getItems().add("Estado");
        cb_filtro.getItems().add("Fecha de llegada");
        cb_filtro.getItems().add("Fecha de salida");
        cb_filtro.getItems().add("Id del avion"); 
        cb_filtroEstado.getItems().add("True"); 
        cb_filtroEstado.getItems().add("False");    
    }    

    @Override
    public void initialize() {
        root.styleProperty().set("-fx-background-color: #4AB19D");
    }

    @Override
    public Node getRoot() {
        return root;
    }     

    @FXML
    private void buscarK(KeyEvent event) {
    }

    @FXML
    private void buscar(MouseEvent event) throws InterruptedException, ExecutionException, IOException 
    {
        ObservableList<VueloC> DatosServicios = FXCollections.observableArrayList();
        String EstadoVuelo = "-";
        
         tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
         tbc_destino.setCellValueFactory(new PropertyValueFactory<>("Destino"));
         tbc_distancia.setCellValueFactory(new PropertyValueFactory<>("Distancia"));
         tbc_duracion.setCellValueFactory(new PropertyValueFactory<>("Duracion"));
         tbc_estado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
         tbc_fechaLlegada.setCellValueFactory(new PropertyValueFactory<>("FechaLlegada"));
         tbc_fechaSalida.setCellValueFactory(new PropertyValueFactory<>("FechaSalida"));
         tbc_matricula.setCellValueFactory(new PropertyValueFactory<>("MatriculaAvion"));
         
        if(cb_filtro.getValue().equals("Id")) {        
            
            long Id = Long.parseLong(txt_buscar.getText());
            VueloDTO vuelo = VueloWebService.getVueloById(Id, token);

            if (vuelo.getEstado().toString().equals("true")) 
            EstadoVuelo = "Activo";
            else EstadoVuelo = "Inactivo";
            
            VueloC vuelo1 = new VueloC(vuelo.getId(),vuelo.getDuracion(),vuelo.getAeropuerto(),
            vuelo.getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            vuelo.getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),vuelo.getDistancia(),
            EstadoVuelo,vuelo.getAvion().getMatricula());

            DatosServicios.add(vuelo1);
        }
        
        else
        {
            List<VueloDTO> vuelo = null;
            
            if (cb_filtro.getValue().equals("Estado")) {
        
                boolean Estado = Boolean.parseBoolean(cb_filtroEstado.getValue().toLowerCase());    
                vuelo = VueloWebService.getVueloByEstado(Estado, token);
            }
            if (cb_filtro.getValue().equals("Aeropuerto de destino")) {
          
                vuelo = VueloWebService.getVueloByAeropuerto(txt_buscar.getText(), token);
            }
            if (cb_filtro.getValue().equals("Id del avion")) {

               long Id = Long.parseLong(txt_buscar.getText());    
               vuelo = VueloWebService.getVueloByAvionId(Id, token);
            }
            if (cb_filtro.getValue().equals("Fecha de llegada") || cb_filtro.getValue().equals("Fecha de salida")) {

               LocalDate localDate = datePFechaInicial.getValue();
               Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
               Date inicio = Date.from(instant);

               LocalDate localDate2 = datePFechaFinal.getValue();
               Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
               Date fin = Date.from(instant2);
               
                if (cb_filtro.getValue().equals("Fecha de llegada")) {
                    vuelo = VueloWebService.getVueloByFechaLlegadaBetween(inicio,fin, token);
                }
                if (cb_filtro.getValue().equals("Fecha de salida")) {
                    vuelo = VueloWebService.getVueloByFechaSalidaBetween(inicio,fin, token);
                }
            }
            
            for (int i = 0; i < vuelo.toArray().length; i++) 
            {
                if (vuelo.get(i).getEstado().toString().equals("true")) 
                EstadoVuelo = "Activo";
                else EstadoVuelo = "Inactivo";
            
                VueloC vuelo1 = new VueloC(vuelo.get(i).getId(),vuelo.get(i).getDuracion(),vuelo.get(i).getAeropuerto(),
                vuelo.get(i).getFechaLlegada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                vuelo.get(i).getFechaSalida().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),vuelo.get(i).getDistancia(),
                EstadoVuelo,vuelo.get(i).getAvion().getMatricula());
                
                DatosServicios.add(vuelo1);
            }
        }
        
        tablaVuelos.setItems(DatosServicios);
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
    @FXML
    private void volver(MouseEvent event) {
        FlowController.getInstance().goView("MenuGestion");
    }

    @FXML
    private void nuevo(MouseEvent event) {
        FlowController.getInstance().goView("RegistroVuelo");
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
