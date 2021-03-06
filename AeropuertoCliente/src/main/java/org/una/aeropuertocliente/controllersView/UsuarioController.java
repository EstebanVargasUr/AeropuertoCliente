package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.*;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.una.aeropuertocliente.DTOs.AreaTrabajoDTO;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.WebService.AreaTrabajoWebService;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.RolWebService;
import org.una.aeropuertocliente.WebService.UsuarioAreaTrabajoWebService;
import org.una.aeropuertocliente.WebService.UsuarioWebService;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 * FXML Controller class
 *
 * @author Esteban Vargas
 */
public class UsuarioController extends Controller implements Initializable {

    @FXML private StackPane root;
    @FXML private JFXTextField txt_buscar;
    @FXML private JFXComboBox<String> cb_filtro;
    @FXML private TableColumn<UsuarioC, Long> tbc_id;
    @FXML private TableColumn<UsuarioC, String> tbc_cedula;
    @FXML private TableColumn<UsuarioC, String> tbc_nombre;
    @FXML private TableColumn<UsuarioC, String> tbc_telefono;
    @FXML private TableColumn<UsuarioC, String> tbc_areaTrabajo;
    @FXML private TableColumn<UsuarioC, String> tbc_jefe;
    @FXML private TableColumn<UsuarioC, String> tbc_rol;
    @FXML private TableColumn<UsuarioC, String> tbc_fechaRegistro;
    @FXML private TableColumn<UsuarioC, String> tbc_fechaModificacion;
    @FXML private TableColumn<UsuarioC, String> tbc_estado;
    @FXML private TableColumn<UsuarioC, JFXButton> tbc_horario;
    @FXML private TableColumn<UsuarioC, JFXButton> tbc_modificar;
    @FXML private TableColumn<UsuarioC, JFXButton> tbc_horaMarcaje;
    @FXML private JFXButton btn_nuevo;
    @FXML private TableView<UsuarioC> tablaUsuarios;
    @FXML private VBox vb_barraInferior;
    @FXML private JFXTextField txt_cedula;
    @FXML private JFXTextField txt_nombre;
    @FXML private JFXTextField txt_telefono;
    @FXML private JFXTextField txt_cedulaJefe;
    @FXML private JFXComboBox<String> cb_rol;
    @FXML private JFXPasswordField txt_password;
    @FXML private DatePicker dP_FechaInicial;
    @FXML private DatePicker dp_FechaFinal;
    @FXML private ImageView cargando;
    @FXML private JFXComboBox<String> cb_BarEstado;
    @FXML private JFXComboBox<String> cb_areaTrabajo;
    
    private AuthenticationResponse authenticationResponse;
    private UsuarioC UsuarioSeleccionado;
    public static ObservableList<UsuarioC> DatosUsuarios;
    boolean BotonGuardar;
    private String EstadoUsuario, JefeUsuario, RolUsuario, AreaTrabajo;
    private List<UsuarioDTO> ListaUsuario;
    private JFXDialogLayout contenido = new JFXDialogLayout();
    private JFXDialog dialogo;
    private JFXTextField cedula;
    private JFXPasswordField password;
    Mensaje msg = new Mensaje();
    
    
    
    
    
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
        LimitaAccionesRol();
        LimitaAccionesRolGestor();
    }

    @Override
    public Node getRoot() {
        return root;
    }   
    
    private void ModoDesarrollador(){
        if(FlowController.getInstance().modoDesarrollo)
           FlowController.getInstance().titulo("V12-G-USU");
        else
            FlowController.getInstance().titulo("Gestión de Usuarios");
    }
    
    private void ModificarFormaCargando(){
        Rectangle clip = new Rectangle(cargando.getFitWidth(), cargando.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        cargando.setClip(clip);      
    }

    private void initTabla(){
        initColumnas();
        tablaUsuarios.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_cedula.setCellValueFactory(new PropertyValueFactory<>("Cedula"));
        tbc_nombre.setCellValueFactory(new PropertyValueFactory<>("NombreCompleto"));
        tbc_telefono.setCellValueFactory(new PropertyValueFactory<>("Telefono"));
        tbc_areaTrabajo.setCellValueFactory(new PropertyValueFactory<>("AreaTrabajo"));
        tbc_jefe.setCellValueFactory(new PropertyValueFactory<>("JefeDirecto"));
        tbc_rol.setCellValueFactory(new PropertyValueFactory<>("Rol"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory<>("FechaRegistro"));
        tbc_fechaModificacion.setCellValueFactory(new PropertyValueFactory<>("FechaModificacion"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tbc_horario.setCellValueFactory(new PropertyValueFactory<>("Horario"));
        tbc_modificar.setCellValueFactory(new PropertyValueFactory<>("Modificar"));
        tbc_horaMarcaje.setCellValueFactory(new PropertyValueFactory<>("HoraMarcaje"));
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Cedula");
        cb_filtro.getItems().add("Nombre");
        cb_filtro.getItems().add("Fecha de Registro");
        cb_filtro.getItems().add("Id del Jefe");
        
        cb_rol.getItems().add("Empleado");
        cb_rol.getItems().add("Gestor");
        cb_rol.getItems().add("Gerente");
        cb_rol.getItems().add("Administrador");
        cb_rol.getItems().add("Auditor");
        
        cb_areaTrabajo.getItems().add("Servicios");
        cb_areaTrabajo.getItems().add("Torre de Control");
        cb_areaTrabajo.getItems().add("Recursos Humanos");
        cb_areaTrabajo.getItems().add("Funcionamiento General");
        
        cb_BarEstado.getItems().add("Activo");
        cb_BarEstado.getItems().add("Inactivo");
        
    }
    
    @FXML
    private void buscar(MouseEvent event) {
        CargaLogicaBusqueda();
    }

    private void RealizarBusqueda() {
        try{
            LimpiaDatos();
            DatosUsuarios = FXCollections.observableArrayList();
            EstadoUsuario = "Inactivo"; JefeUsuario = "Sin Jefe"; RolUsuario = "Empleado"; AreaTrabajo = "Funcionamiento General";
            
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
                else
                {
                     if (cb_filtro.getValue().equals("Fecha de Registro")) {
                        
                        if (dP_FechaInicial.getValue() == null || dp_FechaFinal.getValue() == null) 
                        CargaGraficaMsg("Por favor complete los campos respectivos");
                        else
                        busquedaLista();  
                    }
                     else if (cb_filtro.getValue().equals("Cedula") ||cb_filtro.getValue().equals("Nombre")||cb_filtro.getValue().equals("Id del Jefe")) {
                        if (txt_buscar.getText().equals(""))
                        CargaGraficaMsg("Por favor complete los campos respectivos");
                        else
                            busquedaLista();
                    }
                }
          tablaUsuarios.setItems(DatosUsuarios);
          }
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    private void busquedaIndividual() throws InterruptedException, IOException, ExecutionException {
        long Id = Long.parseLong(txt_buscar.getText());
        UsuarioDTO usuario = UsuarioWebService.getUsuarioById(Id, authenticationResponse.getJwt());
        List<UsuarioAreaTrabajoDTO> usuarioTrabajo = UsuarioAreaTrabajoWebService.getUsuarioAreaTrabajoByUsuarioId(Id, authenticationResponse.getJwt());
        
        if (usuario.getId() != null)
        {
           if (usuario.getEstado().toString().equals("true")) 
           {EstadoUsuario = "Activo";}

            if(usuario.getUsuarioJefe() != null)
            {JefeUsuario = usuario.getUsuarioJefe().getCedula();}

            if(usuario.getRol() != null)
            {RolUsuario = usuario.getRol().getNombre();}

            for(UsuarioAreaTrabajoDTO usuario2 : usuarioTrabajo)
                if(usuario2.getAreaTrabajo() != null)
                    AreaTrabajo = usuario2.getAreaTrabajo().getNombreArea()+"  ";   

            UsuarioC usuario1 = new UsuarioC(usuario.getId(),usuario.getCedula(),usuario.getNombreCompleto(),usuario.getTelefono(),AreaTrabajo,
            JefeUsuario,RolUsuario,usuario.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            usuario.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoUsuario,new JFXButton("Horario"),new JFXButton("Modificar"),new JFXButton("Marcaje"));

            DatosUsuarios.add(usuario1); 
        }
        else
        {CargaGraficaMsg("No se encontraron usuarios");}    
        
    }
     
    private void busquedaLista() throws InterruptedException, IOException, ExecutionException {
        ListaUsuario = null;
        obtenerUsuarios();
        
        if (ListaUsuario.toArray().length != 0) {
            for(UsuarioDTO usuario : ListaUsuario) {
            AreaTrabajo = "Funcionamiento General";
            if (usuario.getEstado().toString().equals("true")) 
                EstadoUsuario = "Activo";

            if(usuario.getUsuarioJefe() != null)
                JefeUsuario = usuario.getUsuarioJefe().getCedula();
                else
                    JefeUsuario = "Sin Jefe";

            if(usuario.getRol() != null)
                RolUsuario = usuario.getRol().getNombre();

            List<UsuarioAreaTrabajoDTO> usuarioTrabajo = UsuarioAreaTrabajoWebService.getUsuarioAreaTrabajoByUsuarioId(usuario.getId(), authenticationResponse.getJwt());

            for(UsuarioAreaTrabajoDTO usuario2 : usuarioTrabajo){
                if(usuario2.getAreaTrabajo() != null)
                    AreaTrabajo = usuario2.getAreaTrabajo().getNombreArea()+"  ";   
            }

            UsuarioC usuario1 = new UsuarioC(usuario.getId(),usuario.getCedula(),usuario.getNombreCompleto(),usuario.getTelefono(),AreaTrabajo,
            JefeUsuario,RolUsuario,usuario.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            usuario.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoUsuario,new JFXButton("Horario"),new JFXButton("Modificar"),new JFXButton("Marcaje"));

            DatosUsuarios.add(usuario1);
        }
      }
        else
        {CargaGraficaMsg("No se encontraron usuarios");}  
    }
      
    private void obtenerUsuarios() throws InterruptedException, IOException, ExecutionException{
        switch(cb_filtro.getValue()){
            case "Cedula":
                ListaUsuario = UsuarioWebService.getUsuarioByCedulaAproximate(txt_buscar.getText(), authenticationResponse.getJwt());
            break;

            case "Nombre":
                ListaUsuario = UsuarioWebService.getUsuarioByNombreCompletoAproximateIgnoreCase(txt_buscar.getText(), authenticationResponse.getJwt());
            break;

            case "Id del Jefe":
                long IdJefe = Long.parseLong(txt_buscar.getText());    
                ListaUsuario = UsuarioWebService.getUsuarioByUsuarioJefeId(IdJefe, authenticationResponse.getJwt());
            break;

            case "Fecha de Registro":
                LocalDate localDate = dP_FechaInicial.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                Date inicio = Date.from(instant);

                LocalDate localDate2 = dp_FechaFinal.getValue();
                Instant instant2 = Instant.from(localDate2.atStartOfDay(ZoneId.systemDefault()));
                Date fin = Date.from(instant2);

                ListaUsuario = UsuarioWebService.getUsuarioByFechaRegistroBetween(inicio,fin, authenticationResponse.getJwt());
            break;

            default:
                break;
        }
    }
    
    @FXML
    private void nuevo(MouseEvent event) {
        LimpiaBarraInferior();
        BotonGuardar = true;
        configurarBarraInferior(true);
    }

    @FXML
    private void cancelar(MouseEvent event) {
        LimpiaBarraInferior();
        configurarBarraInferior(false);
    }

    @FXML
    private void cancelarESC(KeyEvent event) {
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
        if (txt_cedula.getText().equals("")||txt_nombre.getText().equals("")||txt_telefono.getText().equals("")||
        txt_cedulaJefe.getText().equals("")) 
        {
           CargaGraficaMsg("Por favor complete los campos necesarios para crear el usuario");
        }
        else{
            loginEncargado();
        }
    }
    
    private void RealizarGuardar() {
        configurarBarraInferior(false);
        try{
            UsuarioDTO usuarioAccion = new UsuarioDTO();
            AreaTrabajoDTO areaTrabajo = AreaTrabajoWebService.getAreaTrabajoByNombreArea(cb_areaTrabajo.getValue(), authenticationResponse.getJwt()).get(0);
            usuarioAccion.setCedula(txt_cedula.getText());
            usuarioAccion.setNombreCompleto(txt_nombre.getText());
            usuarioAccion.setPasswordEncriptado(txt_password.getText());
            usuarioAccion.setTelefono(txt_telefono.getText());
       
            if (cb_BarEstado.getValue().equals("Activo")) {
                usuarioAccion.setEstado(true);
            }else {usuarioAccion.setEstado(false);}
            
            
            if(!txt_cedulaJefe.getText().equals("Sin Jefe"))
                usuarioAccion.setUsuarioJefe(UsuarioWebService.getUsuarioByCedulaAproximate(txt_cedulaJefe.getText(), authenticationResponse.getJwt()).get(0));
            if(!cb_rol.getValue().equals("Empleado"))
                usuarioAccion.setRol(RolWebService.getRolByNombre(cb_rol.getValue(), authenticationResponse.getJwt()).get(0));
            if(!BotonGuardar) {
                usuarioAccion.setId(UsuarioSeleccionado.getId());
                System.out.println(UsuarioSeleccionado.getId());
                UsuarioWebService.updateUsuario(usuarioAccion, UsuarioSeleccionado.getId(), authenticationResponse.getJwt());
                UsuarioAreaTrabajoWebService.updateUsuarioAreaTrabajo(areaTrabajo, usuarioAccion, UsuarioAreaTrabajoWebService.getUsuarioAreaTrabajoByUsuarioId
                (UsuarioSeleccionado.getId(), authenticationResponse.getJwt()).get(0).getId(), authenticationResponse.getJwt());
            }
            else{
                UsuarioSeleccionado = new UsuarioC();
                UsuarioWebService.createUsuario(usuarioAccion, authenticationResponse.getJwt());
                usuarioAccion = UsuarioWebService.getUsuarioByCedulaAproximate(usuarioAccion.getCedula(), authenticationResponse.getJwt()).get(0);
                UsuarioAreaTrabajoWebService.createUsuarioAreaTrabajo(areaTrabajo, usuarioAccion, authenticationResponse.getJwt());
            }

        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    
    @FXML
    private void guardarEnter(KeyEvent event) {
    }
    
    @FXML
    private void tablaUsuariosClicked(MouseEvent event) {
        UsuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void cb_filtroAction(ActionEvent event) {

        dP_FechaInicial.setPrefWidth(0);  dP_FechaInicial.setVisible(false);
        dp_FechaFinal.setPrefWidth(0);    dp_FechaFinal.setVisible(false);
        txt_buscar.setDisable(false);    txt_buscar.setPromptText("Digite lo que desea buscar");   
        txt_buscar.setText("");
          
        if(cb_filtro.getValue().equals("Fecha de Registro"))
        {
            dP_FechaInicial.setPrefWidth(130);
            dP_FechaInicial.setVisible(true);
            dp_FechaFinal.setPrefWidth(130);
            dp_FechaFinal.setVisible(true);
            txt_buscar.setDisable(true); 
        }
    }
    
    private void LimitaAccionesRol()
    {
        if (authenticationResponse.getRoles().getNombre().equals("Administrador")){
               List<UsuarioAreaTrabajoDTO> usuarioAreaTrabajo = FlowController.getInstance().areaTrabajo;
               for (UsuarioAreaTrabajoDTO usuarioTrabajo : usuarioAreaTrabajo) 
               {AreaTrabajo = usuarioTrabajo.getAreaTrabajo().getNombreArea();
                    if (AreaTrabajo.equals("Recursos Humanos")) {
                       btn_nuevo.setDisable(true);
                       txt_cedula.setDisable(true);
                       txt_nombre.setDisable(true);
                       txt_telefono.setDisable(true);
                       txt_cedulaJefe.setDisable(true);
                       cb_areaTrabajo.setDisable(true);
                       txt_password.setDisable(true);
                       cb_BarEstado.setDisable(true);
                       break;
                   }
                   else {
                       cb_rol.setDisable(true);
                   }
               }
          }
          else {cb_rol.setDisable(true);}
    }
    
    private void LimitaAccionesRolGestor()
    {
        if (authenticationResponse.getRoles().getNombre().equals("Gestor")){
               List<UsuarioAreaTrabajoDTO> usuarioAreaTrabajo = FlowController.getInstance().areaTrabajo;
               for (UsuarioAreaTrabajoDTO usuarioTrabajo : usuarioAreaTrabajo) 
               {AreaTrabajo = usuarioTrabajo.getAreaTrabajo().getNombreArea();
               
                    if (AreaTrabajo.equals("Recursos Humanos")) {
                      tbc_horaMarcaje.setVisible(true);
                      break;
                    }
                    else {
                         tbc_horaMarcaje.setVisible(false);
                    }
               }
          }
          else { tbc_horaMarcaje.setVisible(false);}
    }
    private void LimpiaBarraInferior(){
        txt_cedula.clear();
        txt_nombre.clear();
        txt_telefono.clear();
        txt_cedulaJefe.setText("Sin Jefe");
        txt_password.clear();
        cb_areaTrabajo.setValue("");
        cb_rol.setValue("Empleado");   
    }
    
    private void LimpiaDatos(){
        ObservableList items = FXCollections.observableArrayList(); 
        tablaUsuarios.setItems(items);    
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
    
    public class UsuarioC {
    
        Long Id;  
        String Cedula;
        String NombreCompleto;
        String Telefono;
        String AreaTrabajo;
        String JefeDirecto;
        String Rol;
        String FechaRegistro;
        String FechaModificacion;
        String Estado;
        JFXButton horario;
        JFXButton modificar;
        JFXButton horaMarcaje;

        public UsuarioC(long Id, String Cedula, String NombreCompleto, String Telefono, String AreaTrabajo,
                String JefeDirecto, String Rol, String FechaRegistro, String FechaModificacion, String Estado, JFXButton horario, JFXButton modificar,JFXButton horaMarcaje) {
            this.Id = Id;
            this.Cedula = Cedula;
            this.NombreCompleto = NombreCompleto;
            this.Telefono = Telefono;
            this.AreaTrabajo = AreaTrabajo;
            this.JefeDirecto = JefeDirecto;
            this.Rol = Rol;
            this.FechaRegistro = FechaRegistro;
            this.FechaModificacion = FechaModificacion;
            this.Estado = Estado;
            this.horario = horario;
            this.modificar = modificar;
            this.horaMarcaje = horaMarcaje;

            horario.getStyleClass().add("jfx-buttonStandard1");
            modificar.getStyleClass().add("jfx-buttonStandard1");
            horaMarcaje.getStyleClass().add("jfx-buttonStandard1");
            
            horario.setOnAction(e -> {
                for(UsuarioC usuario : DatosUsuarios){
                    if(usuario.getHorario() == horario){
                        HorarioController.usuarioActual = usuario;
                        FlowController.getInstance().goView("Horario");
                    }
                }

            });
            
            modificar.setOnAction(e -> {
                for(UsuarioC usuario : DatosUsuarios){
                    if(usuario.getModificar() == modificar){
                        LimpiaBarraInferior();
                        BotonGuardar = false;
                        configurarBarraInferior(true);
                        
                        UsuarioSeleccionado = usuario;
                        txt_cedula.setText(usuario.getCedula());
                        txt_nombre.setText(usuario.getNombreCompleto());
                        txt_telefono.setText(usuario.getTelefono());
                        txt_cedulaJefe.setText(usuario.getJefeDirecto());
                        cb_areaTrabajo.setValue(usuario.getAreaTrabajo());
                        cb_rol.setValue(usuario.getRol());
                    }
                }

            });
            
            horaMarcaje.setOnAction(e -> {
                for(UsuarioC usuario : DatosUsuarios){
                    if(usuario.getHoraMarcaje() == horaMarcaje){  
                        HoraMarcajeController.usuarioActual = usuario;
                        FlowController.getInstance().goView("HoraMarcaje");
                    }
                }

            });
        }

        private UsuarioC() {
           
        }

        public Long getId() {
            return Id;
        }

        public String getCedula() {
            return Cedula;
        }

        public String getNombreCompleto() {
            return NombreCompleto;
        }

        public String getTelefono() {
            return Telefono;
        }

        public String getAreaTrabajo() {
            return AreaTrabajo;
        }

        public String getJefeDirecto() {
            return JefeDirecto;
        }

        public String getRol() {
            return Rol;
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

        public JFXButton getHorario() {
            return horario;
        }
    
        public JFXButton getModificar() {
            return modificar;
        }
        
        public JFXButton getHoraMarcaje() {
            return horaMarcaje;
        }
  }
    
    public void cuerpoLoginEncargado(){
        contenido.setHeading(new Text("Aprobación del Gerente"));
        
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
