/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.Trabajador;
import dto.TrabajadorDTO;

/**
 *
 * @author Luisk
 */
public class TrabajadorMapper {
    public static TrabajadorDTO toDTO(Trabajador entidad) {
        if (entidad == null) {
            return null;
        }
        TrabajadorDTO dto = new TrabajadorDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTelefono(entidad.getTelefono());
        dto.setCorreo(entidad.getCorreo());
        dto.setRol(entidad.getRol());
        dto.setPuesto(entidad.getPuesto());
        dto.setHorarios(entidad.getHorarios());
        dto.setSalario(entidad.getSalario());
        return dto;
    }

    public static Trabajador toEntity(TrabajadorDTO dto) {
        if (dto == null) {
            return null;
        }
        Trabajador entidad = new Trabajador(
            dto.getPuesto(),
            dto.getHorarios(),
            dto.getSalario(),
            dto.getId(),
            dto.getNombre(),
            dto.getTelefono(),
            dto.getCorreo(),
            dto.getRol()
        );
        return entidad;
    }
}
