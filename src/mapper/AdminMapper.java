/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.Admin;
import dto.AdminDTO;

/**
 *
 * @author Luisk
 */
public class AdminMapper {
    public static AdminDTO toDTO(Admin entidad) {
        if (entidad == null) {
            return null;
        }
        AdminDTO dto = new AdminDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTelefono(entidad.getTelefono());
        dto.setCorreo(entidad.getCorreo());
        dto.setRol(entidad.getRol());
        dto.setContrasena(entidad.getContrasena());
        return dto;
    }

    public static Admin toEntity(AdminDTO dto) {
        if (dto == null) {
            return null;
        }
        Admin entidad = new Admin(
            dto.getContrasena(),
            dto.getId(),
            dto.getNombre(),
            dto.getTelefono(),
            dto.getCorreo(),
            dto.getRol()
        );
        return entidad;
    }
}
