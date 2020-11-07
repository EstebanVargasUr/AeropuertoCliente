package org.una.aeropuertocliente.DTOs;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor 
@ToString
public class AvionDTO {
 
    private Long id; 
    private String matricula; 
    private String tipoAvion;   
    private Boolean estado; 
    private Date fechaRegistro; 
    private Date fechaModificacion;
    private Integer recorrido;
    private Integer recorridoMaximo;
    private AerolineaDTO aerolinea;
     
}
