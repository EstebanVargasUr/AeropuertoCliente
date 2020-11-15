package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
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
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.una.aeropuertocliente.DTOs.AerolineaDTO;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.WebService.AerolineaWebService;
import org.una.aeropuertocliente.utility.FlowController;

public class AerolineaController extends Controller implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField txt_buscar;
    @FXML
    private JFXTextField txt_nombre;
    @FXML
    private JFXTextField txt_encargado;
    @FXML
    private JFXComboBox<String> cb_filtroEstado;
    @FXML
    private JFXComboBox<String> cb_filtro;
    @FXML
    private JFXComboBox<String> cb_estado;
    @FXML
    private TableView<AerolineaC> tablaAerolineas;
    @FXML
    private TableColumn<AerolineaC, String> tbc_id;
    @FXML
    private TableColumn<AerolineaC, String> tbc_nombre;
    @FXML
    private TableColumn<AerolineaC, String> tbc_encargado;
    @FXML
    private TableColumn<AerolineaC, String> tbc_fechaRegistro;
    @FXML
    private TableColumn<AerolineaC, String> tbc_fechaModificacion;
    @FXML
    private TableColumn<AerolineaC, String> tbc_estado;
    @FXML
    private TableColumn<AerolineaC, String> tbc_aviones;
    @FXML
    private TableColumn<AerolineaC, String> tbc_modificar;
    @FXML
    private VBox vb_barraInferior;
    @FXML
    private JFXButton btn_nuevo;

    private AuthenticationResponse authenticationResponse;
    private AerolineaC AerolineaSeleccionada;
    public static ObservableList<AerolineaC> DatosAerolineas;
    boolean BotonGuardar;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabla();
        initComboBox();
    }
    
    @Override
    public void initialize() {
        authenticationResponse = FlowController.getInstance().authenticationResponse;
        root.styleProperty().set("-fx-background-color: #4AB19D"); 
        
    }

    @Override
    public Node getRoot() {
        return root;
    }
    
    private void initTabla(){
        initColumnas();
        tablaAerolineas.setEditable(true);
    }
    
    private void initColumnas(){
        
        tbc_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tbc_nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tbc_encargado.setCellValueFactory(new PropertyValueFactory<>("Encargado"));
        tbc_fechaRegistro.setCellValueFactory(new PropertyValueFactory<>("FechaRegistro"));
        tbc_fechaModificacion.setCellValueFactory(new PropertyValueFactory<>("FechaModificacion"));
        tbc_estado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        tbc_aviones.setCellValueFactory(new PropertyValueFactory<>("Aviones"));
        tbc_modificar.setCellValueFactory(new PropertyValueFactory<>("Modificar"));
        
    }
    
    private void initComboBox(){
        
        cb_filtro.getItems().add("Id");
        cb_filtro.getItems().add("Todas");
        cb_filtro.getItems().add("Nombre");
        cb_filtro.getItems().add("Estado");
        
        cb_filtroEstado.getItems().add("Activo");
        cb_filtroEstado.getItems().add("Inactivo");
        
        cb_estado.getItems().add("Activo");
        cb_estado.getItems().add("Inactivo");

    }

    @FXML
    private void buscar(MouseEvent event){
        RealizarBusqueda();
    }
    
    private void RealizarBusqueda() {
        LimpiaDatos();
        DatosAerolineas = FXCollections.observableArrayList();
        String EstadoAerolinea = "Inactivo";
        try{
        if(cb_filtro.getValue().equals("Id")) {        
            
            long Id = Long.parseLong(txt_buscar.getText());
            AerolineaDTO aerolinea = AerolineaWebService.getAerolineaById(Id, authenticationResponse.getJwt());

            if (aerolinea.getEstado().toString().equals("true")) 
                EstadoAerolinea = "Activo";
            
            
            AerolineaC aerolinea1 = new AerolineaC(aerolinea.getId(),aerolinea.getNombreAerolinea(),aerolinea.getNombreResponsable(),
            aerolinea.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            aerolinea.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,new JFXButton("Aviones"),new JFXButton("Modificar"));

            DatosAerolineas.add(aerolinea1);
        }
        
        else
        {
            List<AerolineaDTO> ListaAerolineas = null;
            
            switch(cb_filtro.getValue()){
                
                case "Todas":
                    ListaAerolineas = AerolineaWebService.getAllAerolineas(authenticationResponse.getJwt());
                break;
                
                case "Nombre":
                    ListaAerolineas = AerolineaWebService.getAerolineaByNombreAerolinea(txt_buscar.getText(), authenticationResponse.getJwt());
                break;
                
                case "Estado": 
                    Boolean Estado;
                    if (cb_filtroEstado.getValue().equals("Activo")) 
                        Estado = true;
                    else
                        Estado = false;
                    ListaAerolineas = AerolineaWebService.getAerolineaByEstado(Estado, authenticationResponse.getJwt());
                break;
                
                default:
                    break;
                
            }
            for(AerolineaDTO aerolinea : ListaAerolineas)
            {
                if (aerolinea.getEstado().toString().equals("true")) 
                    EstadoAerolinea = "Activo";

                AerolineaC aerolinea1 = new AerolineaC(aerolinea.getId(),aerolinea.getNombreAerolinea(),aerolinea.getNombreResponsable(),
                aerolinea.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                aerolinea.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,new JFXButton("Aviones"),new JFXButton("Modificar"));

                DatosAerolineas.add(aerolinea1);
            }
        }
        
        tablaAerolineas.setItems(DatosAerolineas);
        } catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaController.class.getName()).log(Level.SEVERE, null, ex);}
    }

    @FXML
    private void nuevo(MouseEvent event) {
        LimpiaBarraInferior();
        BotonGuardar = true;
        cb_estado.setValue("Activo");
        vb_barraInferior.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        vb_barraInferior.setMinSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        vb_barraInferior.setVisible(true);
    }

    @FXML
    private void tablaUsuariosClicked(MouseEvent event) {
        AerolineaSeleccionada = tablaAerolineas.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void cancelar(MouseEvent event) {
        LimpiaBarraInferior();
        vb_barraInferior.setPrefSize(0, 0);
        vb_barraInferior.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        vb_barraInferior.setVisible(false);
    }

    @FXML
    private void guardar(MouseEvent event) throws InterruptedException, ExecutionException, IOException{
        vb_barraInferior.setPrefSize(0, 0);
        vb_barraInferior.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        vb_barraInferior.setVisible(false);
       
        Boolean Estado;
        AerolineaDTO aerolineaAccion = new AerolineaDTO();
        aerolineaAccion.setNombreAerolinea(txt_nombre.getText());
        aerolineaAccion.setNombreResponsable(txt_encargado.getText());
        if (cb_estado.getValue().equals("Activo")) 
            Estado = true;
        else
            Estado = false;
        aerolineaAccion.setEstado(Estado);
        
        if (!BotonGuardar) {
            aerolineaAccion.setId(AerolineaSeleccionada.getId());
            AerolineaWebService.updateAerolinea(aerolineaAccion, AerolineaSeleccionada.getId(), authenticationResponse.getJwt());
            
        }
        else{
           AerolineaSeleccionada = new AerolineaC();
           AerolineaWebService.createAerolinea(aerolineaAccion, authenticationResponse.getJwt());
        }

        LimpiaBarraInferior();
        LimpiaDatos();
    }

    private void LimpiaDatos(){
        ObservableList items = FXCollections.observableArrayList(); 
        tablaAerolineas.setItems(items);    
    }
    
    private void LimpiaBarraInferior()
    {
        txt_nombre.clear();
        txt_encargado.clear();
        cb_estado.setValue("");
    }

    @FXML
    private void cb_filtroAction(ActionEvent event) {
        txt_buscar.clear();
        cb_filtroEstado.setPrefWidth(0);    cb_filtroEstado.setVisible(false);
        txt_buscar.setDisable(false);       txt_buscar.setPromptText("Digite lo que desea buscar");
        
        if(cb_filtro.getValue().equals("Todas")) 
           txt_buscar.setDisable(true);
        if (cb_filtro.getValue().equals("Estado")) 
        {
            cb_filtroEstado.setPrefWidth(100);
            cb_filtroEstado.setVisible(true);
            txt_buscar.setDisable(true);
        }  
    }
    
    public class AerolineaC {
    
        Long Id;  
        String Nombre;
        String Encargado;
        String FechaRegistro;
        String FechaModificacion;
        String Estado;
        JFXButton Aviones;
        JFXButton Modificar;

        public AerolineaC(long Id, String Nombre, String Encargado, String FechaRegistro,
                String FechaModificacion, String Estado, JFXButton Aviones, JFXButton Modificar) {
            this.Id = Id;
            this.Nombre = Nombre;
            this.Encargado = Encargado;
            this.FechaRegistro = FechaRegistro;
            this.FechaModificacion = FechaModificacion;
            this.Estado = Estado;
            this.Aviones = Aviones;
            this.Modificar = Modificar;

            Aviones.getStyleClass().add("jfx-buttonStandard1");
            Modificar.getStyleClass().add("jfx-buttonStandard1");
            
            Aviones.setOnAction(e -> {
                for(AerolineaC aerolinea : DatosAerolineas){
                    if(aerolinea.getAviones() == Aviones){
                        AvionController.aerolineaActual = aerolinea;
                        FlowController.getInstance().goView("Avion");
                    }
                }

            });
            
            Modificar.setOnAction(e -> {
                for(AerolineaC aerolinea : DatosAerolineas){
                    if(aerolinea.getModificar() == Modificar){
                        LimpiaBarraInferior();
                        BotonGuardar = false;
                        vb_barraInferior.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
                        vb_barraInferior.setMinSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
                        vb_barraInferior.setVisible(true);
                        
                        AerolineaSeleccionada = aerolinea;
                        txt_nombre.setText(aerolinea.getNombre());
                        txt_encargado.setText(aerolinea.getEncargado());
                        cb_estado.setValue(aerolinea.getEstado());
                    }
                }

            });
        }

        private AerolineaC() {
           
        }
        
        public Long getId() {
            return Id;
        }

        public String getNombre() {
            return Nombre;
        }

        public String getEncargado() {
            return Encargado;
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

        public JFXButton getAviones() {
            return Aviones;
        }

        public JFXButton getModificar() {
            return Modificar;
        }
    }

}
