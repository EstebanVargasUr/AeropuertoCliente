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
public class HoraMarcajeDTO {
 
    private Long id; 
    private Date horaEntrada;
    private Date horaSalida;
    private Date fechaRegistro; 
    private Date fechaModificacion; 
    private UsuarioDTO usuario;
     
}
