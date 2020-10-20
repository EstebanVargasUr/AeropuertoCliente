package org.una.aeropuertocliente.DTOs;

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
public class TipoServicioDTO {
    
    private Long id; 
    private String nombre;   
    private Long duracion;
}
