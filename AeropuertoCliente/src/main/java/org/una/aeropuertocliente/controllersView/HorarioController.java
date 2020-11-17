package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.HorarioDTO;
import org.una.aeropuertocliente.WebService.HorarioWebService;
import org.una.aeropuertocliente.controllersView.UsuarioController.UsuarioC;
import org.una.aeropuertocliente.utility.FlowController;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class HorarioController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private TableView<HorarioC> tablaHorario;
    @FXML private TableColumn<HorarioC, String> tbc_diaEntrada;
    @FXML private TableColumn<HorarioC, String> tbc_diaSalida;
    @FXML private TableColumn<HorarioC, String> tbc_horaEntrada;
    @FXML private TableColumn<HorarioC, String> tbc_horaSalida;
    @FXML private TableColumn<HorarioC, String> tbc_estado;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private JFXComboBox<String> cb_estado;
    @FXML private ImageView cargando;
    
    ObservableList<HorarioC> DatosHorarios = FXCollections.observableArrayList();
    AuthenticationResponse authenticationResponse = FlowController.getInstance().authenticationResponse;
    public static UsuarioC usuarioActual;
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        initTabla();
        initComboBox();
        CargaLogicaBuscarTodos();
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
        tablaHorario.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_diaEntrada.setCellValueFactory(new PropertyValueFactory<>("diaEntrada"));
        tbc_diaSalida.setCellValueFactory(new PropertyValueFactory("diaSalida"));
        tbc_horaEntrada.setCellValueFactory(new PropertyValueFactory("horaEntrada"));
        tbc_horaSalida.setCellValueFactory(new PropertyValueFactory("horaSalida"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory("estado"));
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Todos los horarios"); 
        cb_filtro.getItems().add("Por estado"); 
        cb_filtro.setValue("Todos los horarios");
        cb_estado.getItems().add("Activo");
        cb_estado.getItems().add("Inactivo");

    }
    
    @FXML
    void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }  
    
    private void RealizarBusqueda() {
        try{
            ObservableList items = FXCollections.observableArrayList(); 
            tablaHorario.setItems(items);    

            if (cb_filtro.getValue().equals("Todos los horarios")) {
                BuscaTodos();
            }
            else{
                DatosHorarios = FXCollections.observableArrayList();
                List<HorarioDTO> horarios = null;
                String EstadoHorario = "-";

                Boolean Estado = false;
                if (cb_estado.getValue().equals("Activo")) 
                    Estado = true;
                else
                    Estado = false;

                horarios = HorarioWebService.getHorarioByEstadoAndUsuario(Estado,FlowController.getInstance().
                authenticationResponse.getUsuario().getId(),FlowController.getInstance().authenticationResponse.getJwt());

                for (int i = 0; i < horarios.toArray().length; i++) {
                    if (horarios.get(i).getEstado().toString().equals("true")) 
                    EstadoHorario = "Activo";
                    else EstadoHorario = "Inactivo";

                    HorarioC horario1 = new HorarioC(DeterminaDia(horarios.get(i).getDiaEntrada()),
                            DeterminaDia(horarios.get(i).getDiaEntrada()),horarios.get(i).getHoraEntrada().toString(),
                    horarios.get(i).getHoraSalida().toString(),EstadoHorario);

                    DatosHorarios.add(horario1);
                }
                tablaHorario.setItems(DatosHorarios);
            }
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    @FXML
    void cb_filtroAction(ActionEvent event) {
        if (cb_filtro.getValue().equals("Todos los horarios")) {
            cb_estado.setPrefWidth(0);
            cb_estado.setVisible(false);
        }
        else{
            cb_estado.setPrefWidth(200);
            cb_estado.setVisible(true);
        }
    }
    
    private void BuscaTodos() {
        DatosHorarios = FXCollections.observableArrayList();
        List<HorarioDTO> horarios = null;
        String EstadoHorario = "-";    
        try {
            if(usuarioActual == null)
                horarios = HorarioWebService.getHorarioByUsuarioId(authenticationResponse.getUsuario().getId() ,authenticationResponse.getJwt());
            else
               horarios = HorarioWebService.getHorarioByUsuarioId(usuarioActual.getId() ,authenticationResponse.getJwt()); 
            
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
    
    
    private String DeterminaDia(Short dia) {
        String Dia = "-";
        if (dia == 1) 
            Dia = "Lunes";
        if (dia == 2) 
            Dia = "Martes";
        if (dia == 3) 
            Dia = "Miércoles";
        if (dia == 4) 
            Dia = "Jueves";
        if (dia == 5) 
            Dia = "Viernes";
        if (dia == 6) 
            Dia = "Sábado";
        if (dia == 7) 
            Dia = "Domingo";
        
        return Dia;
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
    
    private void CargaLogicaBuscarTodos(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            BuscaTodos();
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
