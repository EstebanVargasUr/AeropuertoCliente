/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuertocliente.DataSource;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.una.aeropuertocliente.DTOs.PrecioDTO;
import org.una.aeropuertocliente.DTOs.ServicioDTO;
import org.una.aeropuertocliente.WebService.PrecioWebService;
import org.una.aeropuertocliente.WebService.ServicioWebService;
import org.una.aeropuertocliente.utility.FlowController;

/**
 *
 * @author Daniel
 */

public class DataServiciosFecha implements JRDataSource{
    private final List<ServicioDTO> list;   
    List<PrecioDTO> precios;
    List<PrecioDTO> preciosAux= new ArrayList<PrecioDTO>();
    PrecioDTO precio;
    private int index=0;
    private String token;
    private String EstadoCobro;
    float num=1000;
    int cont=0;
    public DataServiciosFecha() throws InterruptedException, ExecutionException, IOException {
        index=-1;
        token = FlowController.getInstance().authenticationResponse.getJwt();
        list = ServicioWebService.getServicioByFechaRegistroBetweenAndTipoServicioId(FlowController.getInstance().FechaIni, FlowController.getInstance().FechaFinal,FlowController.getInstance().idTipoServicio, token); 
        precios=PrecioWebService.getPrecioByTipoServicioId(list.get(0).getTipoServicio().getId(), token);
     
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
  
        case "Precio":
            value =  precios.get(precios.toArray().length-1).getMonto()+""; 
             break;
        case "Factura":
            value = list.get(index).getFactura()+"";                
            break;
        case "Duracion":
            value = list.get(index).getTipoServicio().getDuracion()+"";                
            break;
        case "Encargado":
            value = list.get(index).getNombreResponsable()+"";                
            break;
        case "FechaRegistro":
            value = list.get(index).getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()+"";                
            break;
        case "FechaIni":
            value = FlowController.getInstance().FechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()+"";               
            break;
        case "FechaFinal":
            value =  FlowController.getInstance().FechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()+"";                
            break;
        case "EstadoCobro":
            if (list.get(index).getEstadoCobro().toString().equals("true")) 
                EstadoCobro = "Activo";
            else EstadoCobro = "Inactivo";
            value =  EstadoCobro+"";                
            break;
        case "TipoServicio":
            value = list.get(index).getTipoServicio().getNombre()+"";
            break;
        case "Imprimidor":
            value =  FlowController.getInstance().authenticationResponse.getUsuario().getNombreCompleto()+"";
            break;
        case "Rol":
            value = FlowController.getInstance().authenticationResponse.getUsuario().getRol().getNombre()+"";
            break;
        }
        return value;
    }
    
    public static JRDataSource getDatasSource() throws InterruptedException, ExecutionException, IOException{
        return new DataServiciosFecha();
    }

}