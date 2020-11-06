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
public class VueloDTO {
 
    private Long id; 
    private Float duracion; 
    private String aeropuerto;   
    private Date fechaSalida; 
    private Date fechaLlegada; 
    private Long distancia; 
    private Boolean estado;
    private AvionDTO avion;
}
