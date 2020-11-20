/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuertocliente.DataSource;

/**
 *
 * @author Daniel
 */
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutionException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.una.aeropuertocliente.DTOs.AreaTrabajoAvionDTO;
import org.una.aeropuertocliente.WebService.AreaTrabajoAvionWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 *
 * @author Daniel
 */

public class DataRecorridosAviones implements JRDataSource{
    private final List<AreaTrabajoAvionDTO> list;   
    private int index=0;
    private String token;
    private String EstadoCobro;
    public DataRecorridosAviones() throws InterruptedException, ExecutionException, IOException {
    index=-1;
    token = FlowController.getInstance().authenticationResponse.getJwt();
    list = AreaTrabajoAvionWebService.getAreaTrabajoAvionByFechaRegistroAndAerolineaAndZona(FlowController.getInstance().FechaIni, 
            FlowController.getInstance().FechaFinal, FlowController.getInstance().idAerolinea, FlowController.getInstance().idZona, token); 
    System.out.println(list.toString());
    }
    
    @Override
    public boolean next() throws JRException {
       index++;
       return (index < list.toArray().length);
    }
       
    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        
        Object value = null;
        String fieldName = jrf.getName();
      
        switch(fieldName){
            case "Aerolinea":
                value = list.get(index).getAvion().getAerolinea().getNombreAerolinea()+"";        
                break;
            case "Matricula":
                value = list.get(index).getAvion().getMatricula()+"";                
                break;
            case "TipoAvion":
                value = list.get(index).getAvion().getTipoAvion()+"";                
                break;
            case "Zona":
                value = list.get(index).getAreaTrabajo().getNombreArea()+"";                
                 break;
            case "FechaRegistro":
                value = list.get(index).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()+"";                
                break;
            case "EstadoAvion":
                if (list.get(index).getAvion().getEstado().toString().equals("true")) 
                EstadoCobro = "Activo";
                else EstadoCobro = "Inactivo";
                value =  EstadoCobro+"";                
                break;
            case "FechaIni":
                 value = FlowController.getInstance().FechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()+"";               
                break;
            case "FechaFinal":
                value =  FlowController.getInstance().FechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()+"";                
                break;
            case "Nombre":
                  value =  FlowController.getInstance().authenticationResponse.getUsuario().getNombreCompleto()+"";
                  break;
            case "Rol":
                  value = FlowController.getInstance().authenticationResponse.getUsuario().getRol().getNombre()+"";
            break;
            }
            return value;
    }
    
    public static JRDataSource getDatasSource() throws InterruptedException, ExecutionException, IOException{
        return new DataRecorridosAviones();
    }

}
