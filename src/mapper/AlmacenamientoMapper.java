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

        return new AlmacenamientoDTO(
                entidad.getId(),
                entidad.getIdProduccion(),
                entidad.getCantidad(),
                entidad.getIngreso(),
                entidad.getEgreso()
        );
    }

    public static Almacenamiento toEntity(AlmacenamientoDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Almacenamiento(
                dto.getId(),
                dto.getIdProduccion(),
                dto.getCantidad(),
                dto.getIngreso(),
                dto.getEgreso()
        );
    }
}
