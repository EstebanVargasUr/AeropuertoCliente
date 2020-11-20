package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
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
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.HorarioDTO;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.WebService.HorarioWebService;
import org.una.aeropuertocliente.WebService.UsuarioWebService;
import org.una.aeropuertocliente.controllersView.UsuarioController.UsuarioC;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

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
    @FXML private JFXButton btn_nuevo;
    @FXML private JFXButton btn_modificar;
    @FXML private VBox vb_barraInferior;
    @FXML private JFXComboBox<String> cb_diaEntrada;
    @FXML private JFXComboBox<String> cb_diaSalida;
    @FXML private JFXTextField txt_horaEntrada;
    @FXML private JFXTextField txt_horaSalida;
    @FXML private JFXComboBox<String> cb_BInf_estado;
    @FXML private JFXComboBox<String> cb_Usuario;
    @FXML private JFXTextField txt_Usuario;
    
    boolean BotonGuardar;
    
    private String EstadoUsuario, JefeUsuario, RolUsuario, AreaTrabajo;
    ObservableList<HorarioC> DatosHorarios = FXCollections.observableArrayList();
    AuthenticationResponse authenticationResponse = FlowController.getInstance().authenticationResponse;
    Mensaje msg = new Mensaje();
    public static UsuarioC usuarioActual;
    HorarioDTO HorarioSeleccionado = new HorarioDTO();
      
    @Override
    public void initialize() {
        ModoDesarrollador();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModificarFormaCargando();
        initTabla();
        initComboBox();
        CargaLogicaBuscarTodos();
        LimitaAccionesRol();
    }    
    
    @Override
    public Node getRoot() {
        return root;
    }
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V03-H-EMP");
        else
            FlowController.getInstance().titulo("Horario de Empleado");
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
        cb_BInf_estado.getItems().add("Activo");
        cb_BInf_estado.getItems().add("Inactivo");
        
        cb_diaEntrada.getItems().add("Lunes");
        cb_diaEntrada.getItems().add("Martes");
        cb_diaEntrada.getItems().add("Miércoles");
        cb_diaEntrada.getItems().add("Jueves");
        cb_diaEntrada.getItems().add("Viernes");
        cb_diaEntrada.getItems().add("Sábado");
        cb_diaEntrada.getItems().add("Domingo");
        
        cb_diaSalida.getItems().add("Lunes");
        cb_diaSalida.getItems().add("Martes");
        cb_diaSalida.getItems().add("Miércoles");
        cb_diaSalida.getItems().add("Jueves");
        cb_diaSalida.getItems().add("Viernes");
        cb_diaSalida.getItems().add("Sábado");
        cb_diaSalida.getItems().add("Domingo");
        
        cb_Usuario.getItems().add("Id");
        cb_Usuario.getItems().add("Cedula");

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
            else {
                if (cb_estado.getValue() == null) 
                {
                    CargaGraficaMsg("Por favor complete los campos respectivos");
                }
                else {
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

                        HorarioC horario1 = new HorarioC(horarios.get(i).getId(),DeterminaDia(horarios.get(i).getDiaEntrada()),
                        DeterminaDia(horarios.get(i).getDiaEntrada()),horarios.get(i).getHoraEntrada().toString(),
                        horarios.get(i).getHoraSalida().toString(),EstadoHorario);

                        DatosHorarios.add(horario1);
                }
              }
            }
            tablaHorario.setItems(DatosHorarios);
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


            HorarioC horario1 = new HorarioC(horarios.get(i).getId(),DeterminaDia(horarios.get(i).getDiaEntrada()),DeterminaDia(horarios.get(i).getDiaEntrada()),horarios.get(i).getHoraEntrada().toString(),
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
    private Short DeterminaNumDia(String dia) {
        short Dia=1;
        
        if (dia.equals("Lunes")) 
            Dia = 1;
        if (dia.equals("Martes")) 
            Dia = 2;
        if (dia.equals("Miércoles")) 
            Dia = 3;
        if (dia.equals("Jueves")) 
            Dia = 4;
        if (dia.equals("Viernes")) 
            Dia = 5;
        if (dia.equals("Sábado")) 
            Dia = 6;
        if (dia.equals("Domingo")) 
            Dia = 7;
        
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
    
    private void CargaGraficaMsg(String cuerpo){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            msg.alerta(root, "Alerta", cuerpo);
        }
        });
    }

    @FXML
    private void nuevo(MouseEvent event) {
        LimpiaBarraInferior();
        BotonGuardar = false;
        configurarBarraInferior(true);
    }

    @FXML
    private void modificar(MouseEvent event) {
        
        if (HorarioSeleccionado.getId() == null) {
            CargaGraficaMsg("Por favor seleccione un horario para modificar");
        }
        else
        {   LimpiaBarraInferior();
            BotonGuardar = true;
            configurarBarraInferior(true);

            cb_Usuario.setValue("Id");

            if (HorarioSeleccionado.getEstado() == true) {
                cb_BInf_estado.setValue("Activo");
            }
            else {
                cb_BInf_estado.setValue("Inactivo");
            }

            txt_Usuario.setText(HorarioSeleccionado.getId().toString());
            txt_horaEntrada.setText(HorarioSeleccionado.getHoraEntrada().toString());
            txt_horaSalida.setText(HorarioSeleccionado.getHoraSalida().toString());
            
            cb_diaEntrada.setValue(DeterminaDia(HorarioSeleccionado.getDiaEntrada()));
            cb_diaSalida.setValue(DeterminaDia(HorarioSeleccionado.getDiaSalida()));
            
        }  
    }

    @FXML
    private void cancelarESC(KeyEvent event) {
    }

    @FXML
    private void cancelar(MouseEvent event) {
        LimpiaBarraInferior();
        configurarBarraInferior(false);
    }

    @FXML
    private void guardarEnter(KeyEvent event) {
    }

    @FXML
    private void guardar(MouseEvent event) {
         if (txt_horaEntrada.getText().equals("")||txt_horaSalida.getText().equals("")||txt_Usuario.getText().equals("")||
        cb_BInf_estado.getValue() == null||cb_Usuario.getValue() == null||cb_diaEntrada.getValue() == null||cb_diaSalida.getValue() == null) 
        {
           CargaGraficaMsg("Por favor complete los campos necesarios para crear el vuelo");
        }
        else{
           CargaLogicaGuardar();
        }
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
    
    private void LimitaAccionesRol()
    {
        if (authenticationResponse.getRoles().getNombre().equals("Gestor")){
               List<UsuarioAreaTrabajoDTO> usuarioAreaTrabajo = FlowController.getInstance().areaTrabajo;
               for (UsuarioAreaTrabajoDTO usuarioTrabajo : usuarioAreaTrabajo) 
               {AreaTrabajo = usuarioTrabajo.getAreaTrabajo().getNombreArea();
                   if (AreaTrabajo.equals("Recursos Humanos")) {
                       btn_nuevo.setDisable(false);
                       btn_modificar.setDisable(false);
                       break;
                   }
                   else {
                       btn_nuevo.setDisable(true);
                       btn_modificar.setDisable(true);
                   }
               }
               
              
          }
          else {btn_nuevo.setDisable(true);
               btn_modificar.setDisable(true);}
    }
    
    private void RealizarGuardar() {
       
        try{
            if (BotonGuardar == false) 
            {HorarioSeleccionado = new HorarioDTO();}
            
            HorarioDTO horarioAccion = new HorarioDTO();
       
            HorarioSeleccionado.setDiaEntrada(DeterminaNumDia(cb_diaEntrada.getValue()));
            HorarioSeleccionado.setDiaSalida(DeterminaNumDia(cb_diaSalida.getValue()));
            
            HorarioSeleccionado.setHoraEntrada(Time.valueOf(txt_horaEntrada.getText()));
            HorarioSeleccionado.setHoraSalida(Time.valueOf(txt_horaSalida.getText()));
            if (cb_BInf_estado.getValue().equals("Activo")) 
                    HorarioSeleccionado.setEstado(true);
                else
                    HorarioSeleccionado.setEstado(false);
            
            
            if (cb_Usuario.getValue().equals("Id")) {
                long id = Long.parseLong(txt_Usuario.getText());
                HorarioSeleccionado.setUsuario(UsuarioWebService.getUsuarioById(id, authenticationResponse.getJwt()));           
            }
            else if (cb_Usuario.getValue().equals("Cedula")) {
                HorarioSeleccionado.setUsuario(UsuarioWebService.getUsuarioByCedulaAproximate(txt_Usuario.getText(), authenticationResponse.getJwt()).get(0));     
            }
            else  {CargaGraficaMsg("Por favor ingrese la informacion requerida");}
       
            if(!BotonGuardar) {
               
                HorarioWebService.createHorario(HorarioSeleccionado, authenticationResponse.getJwt());
            }
            else{
                HorarioWebService.updateHorario(HorarioSeleccionado, HorarioSeleccionado.getId() ,authenticationResponse.getJwt());
            }

        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void LimpiaBarraInferior(){
        txt_horaEntrada.clear();
        txt_horaSalida.clear();
        txt_Usuario.clear();
        cb_diaEntrada.setValue("");   
        cb_diaSalida.setValue("");   
        cb_BInf_estado.setValue("");   
        cb_Usuario.setValue("");   
    }
    
    private void CargaLogicaSeleccion(){
        Thread t = new Thread(new Runnable(){
        public void run(){
            cargando.setVisible(true);
            root.setDisable(true);
            
            Long Id = tablaHorario.getSelectionModel().selectedItemProperty().get().Id;
            
            if (Id==null) {
                CargaGraficaMsg("Seleccione un horario válido");
            }
            else{
                
                try {

                    HorarioSeleccionado = HorarioWebService.getHorarioById(Id, authenticationResponse.getJwt());

                } catch (InterruptedException | ExecutionException | IOException ex) {
                    Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
                }  
            }

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
            
            configurarBarraInferior(false);
        }
        });
    }

    @FXML
    private void tablaHorarioClicked(MouseEvent event) throws InterruptedException, ExecutionException, IOException {
       // long Id = tablaHorario.getSelectionModel().selectedItemProperty().get().Id;
       // HorarioSeleccionado = HorarioWebService.getHorarioById(Id, authenticationResponse.getJwt());
        CargaLogicaSeleccion();
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor 
    @ToString
    public class HorarioC {
    
        private Long Id;
        private String diaEntrada;   
        private String diaSalida;
        private String horaEntrada;
        private String horaSalida;
        private String estado;

        public void HorarioC (long Id, String diaEntrada, String diaSalida, String horaEntrada, String horaSalida,String estado) {
        this.Id = Id;
        this.diaEntrada = diaEntrada;
        this.diaSalida = diaSalida;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
    }
  }
}
