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
public class TransaccionDTO {
 
    private Long id; 
    private String informacion; 
    private Boolean estado; 
    private String tipo;
    private Date fechaRegistro;
    private UsuarioDTO usuario;
     
}
