package org.una.aeropuertocliente.DTOs;

import java.sql.Time;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor 
@ToString
public class HorarioDTO {
 
     private Long id; 
    private Short diaEntrada;   
    private Short diaSalida;
    private Time horaEntrada;
    private Time horaSalida;
    private Date fechaRegistro; 
    private Date fechaModificacion; 
    private Boolean estado; 
    private UsuarioDTO usuario; 
}
