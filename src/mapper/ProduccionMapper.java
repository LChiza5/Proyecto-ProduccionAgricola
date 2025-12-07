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
    public static ProduccionDTO toDTO(Produccion entidad) {
        if (entidad == null) {
            return null;
        }
        ProduccionDTO dto = new ProduccionDTO();
        dto.setId(entidad.getId());
        dto.setFecha(entidad.getFecha());
        dto.setCantProducto(entidad.getCantProducto());
        dto.setCalidad(entidad.getCalidad());
        dto.setProductividad(entidad.getProductividad());
        dto.setDestino(entidad.getDestino());
        dto.setIdCultivo(entidad.getIdCultivo());
        return dto;
    }

    public static Produccion toEntity(ProduccionDTO dto) {
        if (dto == null) {
            return null;
        }
        Produccion entidad = new Produccion(
            dto.getId(),
            dto.getFecha(),
            dto.getCantProducto(),
            dto.getCalidad(),
            dto.getProductividad(), // este no va a BD pero sí en memoria
            dto.getDestino(),
            dto.getIdCultivo()
        );
        return entidad;
    }
}
