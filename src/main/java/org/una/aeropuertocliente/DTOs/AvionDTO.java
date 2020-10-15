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
    private boolean estado; 
    private Date fechaRegistro; 
    private Long aerolineaId;
     
}
