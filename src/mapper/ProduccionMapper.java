/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.Produccion;
import dto.ProduccionDTO;

/**
 *
 * @author Luisk
 */
public class ProduccionMapper {

    public static ProduccionDTO toDTO(Produccion p) {
        if (p == null) return null;

        return new ProduccionDTO(
                p.getId(),              
                p.getFecha(),
                p.getCantProducto(),
                p.getCalidad(),
                p.getProductividad(),
                p.getDestino(),
                p.getIdCultivo()
        );
    }

    public static Produccion toEntity(ProduccionDTO dto) {
        if (dto == null) return null;

        
        Produccion p = new Produccion(
                dto.getFecha(),
                dto.getCantProducto(),
                dto.getCalidad(),
                dto.getProductividad(),
                dto.getDestino(),
                dto.getIdCultivo()
        );

        
        if (dto.getId() != null && dto.getId() > 0) {
            p.setId(dto.getId());
        }

        return p;
    }
}

