/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.Persona;

/**
 *
 * @author Luisk
 */
public class PersonaMapper {
   public static PersonaDTO toDTO(Persona entidad) {
        if (entidad == null) {
            return null;
        }
        PersonaDTO dto = new PersonaDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTelefono(entidad.getTelefono());
        dto.setCorreo(entidad.getCorreo());
        dto.setRol(entidad.getRol());
        return dto;
    }

    public static Persona toEntity(PersonaDTO dto) {
        if (dto == null) {
            return null;
        }
        Persona entidad = new Persona(
            dto.getId(),
            dto.getNombre(),
            dto.getTelefono(),
            dto.getCorreo(),
            dto.getRol()
        );
        return entidad;
    }
}
