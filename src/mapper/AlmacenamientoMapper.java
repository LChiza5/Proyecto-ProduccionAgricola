/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.Almacenamiento;
import dto.AlmacenamientoDTO;

/**
 *
 * @author Luisk
 */
public class AlmacenamientoMapper {
    public static AlmacenamientoDTO toDTO(Almacenamiento entidad) {
        if (entidad == null) {
            return null;
        }
        AlmacenamientoDTO dto = new AlmacenamientoDTO();
        dto.setId(entidad.getId());
        dto.setIdProduccion(entidad.getIdProduccion());
        dto.setCantidad(entidad.getCantidad());
        dto.setIngreso(entidad.getIngreso());
        dto.setEgreso(entidad.getEgreso());
        return dto;
    }

    public static Almacenamiento toEntity(AlmacenamientoDTO dto) {
        if (dto == null) {
            return null;
        }
        Almacenamiento entidad = new Almacenamiento(
            dto.getId(),
            dto.getIdProduccion(),
            dto.getCantidad(),
            dto.getIngreso(),
            dto.getEgreso()
        );
        return entidad;
    }
}
