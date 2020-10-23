package org.una.aeropuertocliente.DTOs;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Esteban Vargas
 */
@Data
@AllArgsConstructor
@NoArgsConstructor 
@ToString
public class PrecioDTO {
    
    private Long id; 
    private Float monto;   
    private Date fechaRegistro;
    private boolean estado;
    private TipoServicioDTO tipoServicio;
}
