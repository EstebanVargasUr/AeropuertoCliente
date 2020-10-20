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
public class ParametroSistemaDTO {
 
    private Long id; 
    private String nombre;
    private String valor;
    private String Descripcion;
    private Date fechaRegistro; 
    private Date fechaModificacion; 
    private boolean estado;
     
}

