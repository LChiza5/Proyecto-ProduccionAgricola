/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import Modelo.EnuRol;
import Modelo.Usuario;
import dto.UsuarioDTO;

/**
 *
 * @author Luisk
 */
public class UsuarioMapper {
    public static UsuarioDTO toDTO(Usuario e) {
        if (e == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO(
        e.getId(),
        e.getNombre(),
        e.getTelefono(),
        e.getCorreo(),
        e.getContrasenaHash(),        
        e.getRol().toString()
        );
        return dto;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario entidad = new Usuario(
            dto.getId(),
            dto.getNombre(),
            dto.getTelefono(),
            dto.getCorreo(),
            dto.getContrasenaHash(),    
            EnuRol.valueOf(dto.getRol())
        );
        return entidad;
    }
}
