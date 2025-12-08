/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.Usuario;
import dto.AdminDTO;

/**
 *
 * @author Luisk
 */
public class AdminMapper {
    public static AdminDTO toDTO(Usuario entidad) {
        if (entidad == null) {
            return null;
        }
        AdminDTO dto = new AdminDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTelefono(entidad.getTelefono());
        dto.setCorreo(entidad.getCorreo());
        dto.setRol(entidad.getRol());
        dto.setContrasena(entidad.getContrasenaHash());
        return dto;
    }

    public static Usuario toEntity(AdminDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario entidad = new Usuario(
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
