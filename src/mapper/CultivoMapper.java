/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.Cultivo;
import dto.CultivoDTO;

/**
 *
 * @author ilope
 */
public class CultivoMapper {
      public static CultivoDTO toDTO(Cultivo entidad) {
        if (entidad == null) {
            return null;
        }
        CultivoDTO dto = new CultivoDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTipo(entidad.getTipo());
        dto.setAreaSembrada(entidad.getAreaSembrada());
        dto.setEstado(entidad.getEstado());
        dto.setFechaSiembra(entidad.getFechaSiembra());
        dto.setFechaCosecha(entidad.getFechaCosecha());
        return dto;
    }

    public static Cultivo toEntity(CultivoDTO dto) {
        if (dto == null) {
            return null;
        }
        Cultivo entidad = new Cultivo();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setTipo(dto.getTipo());
        entidad.setAreaSembrada(dto.getAreaSembrada());
        entidad.setEstado(dto.getEstado());
        entidad.setFechaSiembra(dto.getFechaSiembra());
        entidad.setFechaCosecha(dto.getFechaCosecha());
        return entidad;
    }
}
