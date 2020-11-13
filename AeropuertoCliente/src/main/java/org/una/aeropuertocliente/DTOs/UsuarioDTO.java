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
public class UsuarioDTO {
 
    private Long id; 
    private String cedula; 
    private String nombreCompleto;   
    private String telefono;
    private Boolean estado; 
    private String passwordEncriptado;
    private Date fechaRegistro; 
    private Date fechaModificacion;
    private RolDTO rol;
    private UsuarioDTO usuarioJefe;
     
}

