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
public class AreaTrabajoDTO {
 
    private Long id;  
    private String nombreArea;   
    private boolean estado; 
    private Date fechaRegistro; 
    private String Responsable; 
     
}
