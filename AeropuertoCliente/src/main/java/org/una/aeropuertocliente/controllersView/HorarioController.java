package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.HorarioDTO;
import org.una.aeropuertocliente.WebService.HorarioWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class HorarioController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private TableView<HorarioC> tablaHorario;
    @FXML
    private TableColumn<HorarioC, String> tbc_diaEntrada;
    @FXML
    private TableColumn<HorarioC, String> tbc_diaSalida;
    @FXML
    private TableColumn<HorarioC, String> tbc_horaEntrada;
    @FXML
    private TableColumn<HorarioC, String> tbc_horaSalida;
    @FXML
    private TableColumn<HorarioC, String> tbc_estado;
    @FXML
    private JFXComboBox<String> cb_filtro;
    @FXML
    private JFXComboBox<String> cb_estado;

    
    ObservableList<HorarioC> DatosHorarios = FXCollections.observableArrayList();
    AuthenticationResponse authenticationResponse = FlowController.getInstance().authenticationResponse;
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        cb_filtro.getItems().add("Todos los horarios"); 
        cb_filtro.getItems().add("Por estado"); 
        cb_filtro.setValue("Todos los horarios");
        cb_estado.getItems().add("Activo");
        cb_estado.getItems().add("Inactivo");
        
        tbc_diaEntrada.setCellValueFactory(new PropertyValueFactory<>("diaEntrada"));
        tbc_diaSalida.setCellValueFactory(new PropertyValueFactory("diaSalida"));
        tbc_horaEntrada.setCellValueFactory(new PropertyValueFactory("horaEntrada"));
        tbc_horaSalida.setCellValueFactory(new PropertyValueFactory("horaSalida"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory("estado"));
        
        BuscaTodos();
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }
    @FXML
    void buscar(MouseEvent event) throws InterruptedException, ExecutionException, IOException 
    {
        ObservableList items = FXCollections.observableArrayList(); 
        tablaHorario.setItems(items);    
        
        if (cb_filtro.getValue().equals("Todos los horarios")) 
        {
            BuscaTodos();
        }
        else
        {
            DatosHorarios = FXCollections.observableArrayList();
            List<HorarioDTO> horarios = null;
            String EstadoHorario = "-";
       
        Boolean Estado = false;
        if (cb_estado.getValue().equals("Activo")) 
        {Estado = true;}
        else
        {Estado = false;}

        horarios = HorarioWebService.getHorarioByEstadoAndUsuario(Estado,FlowController.getInstance().authenticationResponse.getUsuario().getId(),FlowController.getInstance().token);

         for (int i = 0; i < horarios.toArray().length; i++) 
         {
             if (horarios.get(i).getEstado().toString().equals("true")) 
             EstadoHorario = "Activo";
             else EstadoHorario = "Inactivo";

             HorarioC horario1 = new HorarioC(DeterminaDia(horarios.get(i).getDiaEntrada()),DeterminaDia(horarios.get(i).getDiaEntrada()),horarios.get(i).getHoraEntrada().toString(),
             horarios.get(i).getHoraSalida().toString(),EstadoHorario);

             DatosHorarios.add(horario1);
         }
        tablaHorario.setItems(DatosHorarios);
      }
    }    
    @FXML
    void cb_filtroAction(ActionEvent event) 
    {
        if (cb_filtro.getValue().equals("Todos los horarios")) {
            cb_estado.setPrefWidth(0);
            cb_estado.setVisible(false);
        }
        else
        {
            cb_estado.setPrefWidth(200);
            cb_estado.setVisible(true);
        }
    }
    
    private void BuscaTodos()
    {
        DatosHorarios = FXCollections.observableArrayList();
        List<HorarioDTO> horarios = null;
        String EstadoHorario = "-";
        
        try {
            horarios = HorarioWebService.getHorarioByUsuarioId(authenticationResponse.getUsuario().getId() ,authenticationResponse.getJwt());
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);}


        for (int i = 0; i < horarios.toArray().length; i++) 
        {
            if (horarios.get(i).getEstado().toString().equals("true")) 
            EstadoHorario = "Activo";
            else EstadoHorario = "Inactivo";


            HorarioC horario1 = new HorarioC(DeterminaDia(horarios.get(i).getDiaEntrada()),DeterminaDia(horarios.get(i).getDiaEntrada()),horarios.get(i).getHoraEntrada().toString(),
            horarios.get(i).getHoraSalida().toString(),EstadoHorario);

            DatosHorarios.add(horario1);
        }
        tablaHorario.setItems(DatosHorarios);
    }
    
    
    private String DeterminaDia(Short dia)
    {
        String Dia = "-";
        
        if (dia == 1) {
            Dia = "Lunes";
        }
        if (dia == 2) {
            Dia = "Martes";
        }
        if (dia == 3) {
            Dia = "Miércoles";
        }
        if (dia == 4) {
            Dia = "Jueves";
        }
        if (dia == 5) {
            Dia = "Viernes";
        }
        if (dia == 6) {
            Dia = "Sábado";
        }
        if (dia == 7) {
            Dia = "Domingo";
        }
        return Dia;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor 
    @ToString
    public class HorarioC {
    
    private String diaEntrada;   
    private String diaSalida;
    private String horaEntrada;
    private String horaSalida;
    private String estado;

        public void HorarioC (String diaEntrada, String diaSalida, String horaEntrada, String horaSalida,String estado) {
        this.diaEntrada = diaEntrada;
        this.diaSalida = diaSalida;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
    }
  }
}
