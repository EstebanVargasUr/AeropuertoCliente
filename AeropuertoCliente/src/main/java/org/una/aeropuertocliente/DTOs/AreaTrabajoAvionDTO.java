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
public class AreaTrabajoAvionDTO {
    
    private Long id;
    private AreaTrabajoDTO areaTrabajo;
    private AvionDTO avion;
    private Date fechaRegistro;
}

