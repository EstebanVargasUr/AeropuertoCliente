package org.una.aeropuertocliente.controllersView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.una.aeropuertocliente.DTOs.AerolineaDTO;
import org.una.aeropuertocliente.DTOs.AvionDTO;
import org.una.aeropuertocliente.WebService.AerolineaWebService;
import org.una.aeropuertocliente.WebService.AutenticationWebService;
import org.una.aeropuertocliente.WebService.AvionWebService;
import org.una.aeropuertocliente.utility.FlowController;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyEvent;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;

public class AerolineaAvionController extends Controller implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private JFXTextField txt_buscar;

    @FXML
    private JFXButton btnBuscar;

    @FXML
    private JFXComboBox<String> cb_filtro;

    @FXML
    private TreeTableView<Modelo> tabla;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_Aerolinea;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_Id;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_Encargado;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_FechaRegistro;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_FechaModificacion;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_Estado;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_TipoAvion;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_Recorrido;

    @FXML
    private TreeTableColumn<Modelo, String> tbc_RecorridoMaximo;

  @FXML
    void buscar(MouseEvent event) {
        
        if ( cb_filtro.getValue().equals("Todo")) {
            tabla.getRoot().getChildren().clear();
            ModeloAerolinea.getChildren().clear();
            try {CargarDatos();} 
            catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaAvionController.class.getName()).log(Level.SEVERE, null, ex);}
        }
        
        if ( cb_filtro.getValue().equals("Id de Aerolinea")) {
            tabla.getRoot().getChildren().clear();
            ModeloAerolinea.getChildren().clear();
            try {FiltraPorIdAerolinea();} 
            catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaAvionController.class.getName()).log(Level.SEVERE, null, ex);}
        }
        
        if ( cb_filtro.getValue().equals("Nombre de Aerolinea")) {
            tabla.getRoot().getChildren().clear();
            ModeloAerolinea.getChildren().clear();
            try {FiltraPorNombreAerolinea();} 
            catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaAvionController.class.getName()).log(Level.SEVERE, null, ex);}
        }
        
        if (cb_filtro.getValue().equals("Nombre de Responsable")) {
            tabla.getRoot().getChildren().clear();
            ModeloAerolinea.getChildren().clear();
            try {FiltraPorNombreResponsable();} 
            catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaAvionController.class.getName()).log(Level.SEVERE, null, ex);}
        }
         
        if (cb_filtro.getValue().equals("Estado de Aerolinea")) {
            tabla.getRoot().getChildren().clear();
            ModeloAerolinea.getChildren().clear();
            try {FiltraPorNombreResponsable();} 
            catch (InterruptedException | ExecutionException | IOException ex) {Logger.getLogger(AerolineaAvionController.class.getName()).log(Level.SEVERE, null, ex);}
        }
        
       
    }

    AutenticationWebService Login= new AutenticationWebService();
    TreeItem<Modelo> ModeloPrimAerolinea = new TreeItem<>(new Modelo("Aerolinea","-","-","-","-","-","-","-","-"));
    TreeItem<Modelo> ModeloAerolinea = new TreeItem<>(new Modelo("Aerolinea","-","-","-","-","-","-","-","-"));
    TreeItem<Modelo> ModeloAvion = new TreeItem<>(new Modelo("Avión","-","-","-","-","-","-","-","-"));
    TreeItem<Modelo> treeRoot = new TreeItem<>(new Modelo("Aerolineas","-","-","-","-","-","-","-","-"));

    TreeItem<Modelo> aerolinea;
    TreeItem<Modelo> avion;
    TreeItem<Modelo> aero;

    String EstadoAerolinea;
    String EstadoAvion;
    
    AuthenticationResponse authenticationResponse = FlowController.getInstance().authenticationResponse;

    void CargarDatos() throws InterruptedException, ExecutionException, IOException{
        List<AerolineaDTO> aerolineasModel = AerolineaWebService.getAllAerolineas(FlowController.getInstance().token);
        for (int i = 0; i < aerolineasModel.toArray().length; i++)
        {
            if (aerolineasModel.get(i).getEstado().toString().equals("true"))
                EstadoAerolinea = "Activo";
            else EstadoAerolinea = "Inactivo";
            aerolinea = new TreeItem<>(new Modelo(aerolineasModel.get(i).getNombreAerolinea(),
                aerolineasModel.get(i).getId()+"", aerolineasModel.get(i).getNombreResponsable(),aerolineasModel.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                aerolineasModel.get(i).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,"-","-","-"));

            aero = new TreeItem<>(new Modelo("Aviones Matricula","-","-","-","-","-","-","-","-"));
            aerolinea.getChildren().add(aero);

            List<AvionDTO> aviones = AvionWebService.getAvionByAerolineaId(aerolineasModel.get(i).getId(),FlowController.getInstance().token);
            for(int j = 0; j < aviones.toArray().length; j++){
                if (aviones.get(j).getEstado().toString().equals("true"))
                    EstadoAvion = "Activo";
                else EstadoAvion = "Inactivo";
                avion = new TreeItem<>(new Modelo(aviones.get(j).getMatricula(),aviones.get(j).getId()+"",
                        "-",aviones.get(j).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                        aviones.get(j).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAvion,aviones.get(j).getTipoAvion()+"",aviones.get(j).getRecorrido()+"",aviones.get(j).getRecorridoMaximo()+""));
                aero.getChildren().add(avion);

            }
            ModeloAerolinea.getChildren().add(aerolinea);
        }
    }
    
    void FiltraPorIdAerolinea() throws InterruptedException, ExecutionException, IOException{
        long num = Long.parseLong(txt_buscar.getText());
        AerolineaDTO aerolineasModel = AerolineaWebService.getAerolineaById(num,FlowController.getInstance().token);

        if (aerolineasModel.getEstado().toString().equals("true"))
            EstadoAerolinea = "Activo";
        else EstadoAerolinea = "Inactivo";
        aerolinea = new TreeItem<>(new Modelo(aerolineasModel.getNombreAerolinea(),
            aerolineasModel.getId()+"", aerolineasModel.getNombreResponsable(),aerolineasModel.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
            aerolineasModel.getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,"-","-","-"));

        aero = new TreeItem<>(new Modelo("Aviones Matricula","-","-","-","-","-","-","-","-"));
        aerolinea.getChildren().add(aero);

        List<AvionDTO> aviones = AvionWebService.getAvionByAerolineaId(aerolineasModel.getId(),FlowController.getInstance().token);
        for(int j = 0; j < aviones.toArray().length; j++){
            if (aviones.get(j).getEstado().toString().equals("true"))
                EstadoAvion = "Activo";
            else EstadoAvion = "Inactivo";
            avion = new TreeItem<>(new Modelo(aviones.get(j).getMatricula(),aviones.get(j).getId()+"",
                    "-",aviones.get(j).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                    aviones.get(j).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAvion,aviones.get(j).getTipoAvion()+"",aviones.get(j).getRecorrido()+"",aviones.get(j).getRecorridoMaximo()+""));
            aero.getChildren().add(avion);
        }
        ModeloAerolinea.getChildren().add(aerolinea);
    }
     
      void FiltraPorNombreAerolinea() throws InterruptedException, ExecutionException, IOException{
        List<AerolineaDTO> aerolineasModel = AerolineaWebService.getAerolineaByNombreAerolinea(txt_buscar.getText(),FlowController.getInstance().token);
        for (int i = 0; i < aerolineasModel.toArray().length; i++)
        {
            if (aerolineasModel.get(i).getEstado().toString().equals("true"))
                EstadoAerolinea = "Activo";
            else EstadoAerolinea = "Inactivo";
            aerolinea = new TreeItem<>(new Modelo(aerolineasModel.get(i).getNombreAerolinea(),
                    aerolineasModel.get(i).getId()+"", aerolineasModel.get(i).getNombreResponsable(),
                    aerolineasModel.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),aerolineasModel.get(i).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,"-","-","-"));

            aero = new TreeItem<>(new Modelo("Aviones Matricula","-","-","-","-","-","-","-","-"));
            aerolinea.getChildren().add(aero);

            List<AvionDTO> aviones = AvionWebService.getAvionByAerolineaId(aerolineasModel.get(i).getId(),authenticationResponse.getJwt());
            for(int j = 0; j < aviones.toArray().length; j++){
                if (aviones.get(j).getEstado().toString().equals("true"))
                    EstadoAvion = "Activo";
                else EstadoAvion = "Inactivo";
                avion = new TreeItem<>(new Modelo(aviones.get(j).getMatricula(),aviones.get(j).getId()+"",
                        "-",aviones.get(j).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                        aviones.get(j).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAvion,aviones.get(j).getTipoAvion()+"",aviones.get(j).getRecorrido()+"",aviones.get(j).getRecorridoMaximo()+""));
                aero.getChildren().add(avion);

            }

            ModeloAerolinea.getChildren().add(aerolinea);
        }
    }
      
      void FiltraPorNombreResponsable() throws InterruptedException, ExecutionException, IOException
        {
        List<AerolineaDTO> aerolineasModel = AerolineaWebService.getAerolineaByNombreResponsable(txt_buscar.getText(),FlowController.getInstance().token);
        for (int i = 0; i < aerolineasModel.toArray().length; i++)
        {
            if (aerolineasModel.get(i).getEstado().toString().equals("true"))
                EstadoAerolinea = "Activo";
            else EstadoAerolinea = "Inactivo";
            aerolinea = new TreeItem<>(new Modelo(aerolineasModel.get(i).getNombreAerolinea(),
                    aerolineasModel.get(i).getId()+"", aerolineasModel.get(i).getNombreResponsable(),
                    aerolineasModel.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),aerolineasModel.get(i).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,"-","-","-"));

            aero = new TreeItem<>(new Modelo("Aviones Matricula","-","-","-","-","-","-","-","-"));
            aerolinea.getChildren().add(aero);

            List<AvionDTO> aviones = AvionWebService.getAvionByAerolineaId(aerolineasModel.get(i).getId(),FlowController.getInstance().token);
            for(int j = 0; j < aviones.toArray().length; j++){
                if (aviones.get(j).getEstado().toString().equals("true"))
                    EstadoAvion = "Activo";
                else EstadoAvion = "Inactivo";
                avion = new TreeItem<>(new Modelo(aviones.get(j).getMatricula(),aviones.get(j).getId()+"",
                        "-",aviones.get(j).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                        aviones.get(j).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAvion,aviones.get(j).getTipoAvion()+"",aviones.get(j).getRecorrido()+"",aviones.get(j).getRecorridoMaximo()+""));
                aero.getChildren().add(avion);

            }

            ModeloAerolinea.getChildren().add(aerolinea);
        }
    }
      
    void FiltraPorEstado() throws InterruptedException, ExecutionException, IOException
    {
    boolean estado= Boolean.getBoolean(txt_buscar.getText());
    List<AerolineaDTO> aerolineasModel = AerolineaWebService.getAerolineaByEstado(estado,FlowController.getInstance().token);
    for (int i = 0; i < aerolineasModel.toArray().length; i++)
    {
        if (aerolineasModel.get(i).getEstado().toString().equals("true"))
            EstadoAerolinea = "Activo";
        else EstadoAerolinea = "Inactivo";
        aerolinea = new TreeItem<>(new Modelo(aerolineasModel.get(i).getNombreAerolinea(),
                aerolineasModel.get(i).getId()+"", aerolineasModel.get(i).getNombreResponsable(),
                aerolineasModel.get(i).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),aerolineasModel.get(i).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAerolinea,"-","-","-"));

        aero = new TreeItem<>(new Modelo("Aviones Matricula","-","-","-","-","-","-","-","-"));
        aerolinea.getChildren().add(aero);

        List<AvionDTO> aviones = AvionWebService.getAvionByAerolineaId(aerolineasModel.get(i).getId(),FlowController.getInstance().token);
        for(int j = 0; j < aviones.toArray().length; j++){
            if (aviones.get(j).getEstado().toString().equals("true"))
                EstadoAvion = "Activo";
            else EstadoAvion = "Inactivo";
            avion = new TreeItem<>(new Modelo(aviones.get(j).getMatricula(),aviones.get(j).getId()+"",
                    "-",aviones.get(j).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                    aviones.get(j).getFechaModificacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),EstadoAvion,aviones.get(j).getTipoAvion()+"",aviones.get(j).getRecorrido()+"",aviones.get(j).getRecorridoMaximo()+""));
            aero.getChildren().add(avion);

        }

        ModeloAerolinea.getChildren().add(aerolinea);
    }
    }
     

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_filtro.getItems().add("Todo");
        cb_filtro.getItems().add("Id de Aerolinea");
        cb_filtro.getItems().add("Nombre de Aerolinea");
        cb_filtro.getItems().add("Nombre de Responsable");
        cb_filtro.getItems().add("Estado de Aerolinea");
        cb_filtro.getItems().add("Nombre de Aerolinea");
        cb_filtro.getItems().add("Nombre de Responsable");
        cb_filtro.getItems().add("Estado de Aerolinea");
        
        try {
            CargarDatos();
            tbc_Aerolinea.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getAerolineaProperty());
            tbc_Id.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getIdPropertyProperty());
            tbc_Encargado.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getEncargadoProperty());
            tbc_FechaRegistro.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getFechaRegistroProperty());
            tbc_FechaModificacion.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getFechaModificacionProperty());
            tbc_Estado.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getEstadoProperty());
            tbc_TipoAvion.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getTipoAvionProperty());
            tbc_Recorrido.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getRecorridoProperty());
            tbc_RecorridoMaximo.setCellValueFactory((TreeTableColumn.CellDataFeatures<Modelo, String> param) -> param.getValue().getValue().getRecorridoMaximoProperty());
            treeRoot.getChildren().setAll(ModeloAerolinea);
            tabla.setRoot(ModeloAerolinea);
            tabla.setShowRoot(false);

        } catch (InterruptedException | ExecutionException | IOException ex) { Logger.getLogger(AerolineaAvionController.class.getName()).log(Level.SEVERE, null, ex); }
    }

    @Override
    public void initialize() {

    }

    @Override
    public Node getRoot() {
        return null;
    }

    @FXML
    private void buscar(KeyEvent event) {
    }

    public class Modelo {

        private SimpleStringProperty aerolineaProperty;
        private SimpleStringProperty idProperty;
        private SimpleStringProperty encargadoProperty;
        private SimpleStringProperty fechaRegistroProperty;
        private SimpleStringProperty fechaModificacionProperty;
        private SimpleStringProperty estadoProperty;
        private SimpleStringProperty tipoAvionProperty;
        private SimpleStringProperty recorridoProperty;
        private SimpleStringProperty recorridoMaximoProperty;

        public Modelo(String aerolinea, String id, String encargado, String fechaRegistro,
                      String fechaModificacion, String estado, String tipoAvion, String recorrido, String recorridoMax) {
            this.aerolineaProperty = new SimpleStringProperty(aerolinea);
            this.idProperty = new SimpleStringProperty(id);
            this.encargadoProperty = new SimpleStringProperty(encargado);
            this.fechaRegistroProperty = new SimpleStringProperty(fechaRegistro);
            this.fechaModificacionProperty = new SimpleStringProperty(fechaModificacion);
            this.estadoProperty = new SimpleStringProperty(estado);
            this.tipoAvionProperty = new SimpleStringProperty(tipoAvion);
            this.recorridoProperty= new SimpleStringProperty(recorrido);
            this.recorridoMaximoProperty = new SimpleStringProperty(recorridoMax);
        }

        public SimpleStringProperty getAerolineaProperty() {
            return aerolineaProperty;
        }

        public SimpleStringProperty getIdPropertyProperty() {
            return idProperty;
        }

        public SimpleStringProperty getEncargadoProperty() {
            return encargadoProperty;
        }

        public SimpleStringProperty getFechaRegistroProperty() {
            return fechaRegistroProperty;
        }

        public SimpleStringProperty getFechaModificacionProperty() {
            return fechaModificacionProperty;
        }

        public SimpleStringProperty getEstadoProperty() {
            return estadoProperty;
        }

        public SimpleStringProperty getTipoAvionProperty() {
            return tipoAvionProperty;
        }

        public SimpleStringProperty getRecorridoProperty() {
            return recorridoProperty;
        }

        public SimpleStringProperty getRecorridoMaximoProperty() {
            return recorridoMaximoProperty;
        }


    }


}
