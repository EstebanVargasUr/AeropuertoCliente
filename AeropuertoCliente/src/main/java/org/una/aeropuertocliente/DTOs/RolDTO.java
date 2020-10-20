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
public class RolDTO {
 
    private Long id; 
    private String tipo;   
    private Date fechaRegistro; 
    private Date fechaModificacion; 
    private boolean estado; 
     
}
