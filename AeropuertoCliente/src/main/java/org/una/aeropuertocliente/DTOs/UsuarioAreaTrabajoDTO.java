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
public class UsuarioAreaTrabajoDTO {
 
    private Long id; 
    private Date fechaRegistro;
    private UsuarioDTO usuario;
    private AreaTrabajoDTO areaTrabajo;
     
}



