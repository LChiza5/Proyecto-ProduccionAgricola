/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import Dao.UsuarioDAO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Modelo.Usuario;
import Util.HashUtil;
import dto.UsuarioDTO;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import mapper.UsuarioMapper;

/**
 *
 * @author sebas
 */
public class UsuarioServicio {
    private UsuarioDAO DAO;

    public UsuarioServicio(UsuarioDAO DAO) {
        this.DAO = DAO;
    }
    
    public void agregarUsuario(UsuarioDTO dto) throws ValidacionException, DAOException {
        validarUsuario(dto);
        Usuario entidad = UsuarioMapper.toEntity(dto);
        DAO.crear(entidad);
    }
     
    public void eliminarUsuario(String id) throws ValidacionException, DAOException {
        if (id == null || id.isBlank()) {
            throw new ValidacionException("Debe proporcionar la cédula/ID del trabajador a eliminar.");
        } 
        
        DAO.eliminar(id);
    }
          
    public void actualizarUsuario(UsuarioDTO dto) throws ValidacionException, DAOException {
         validarUsuario(dto);
         Usuario entidad = UsuarioMapper.toEntity(dto);
         DAO.actualizar(entidad);
    }
    
        public List<UsuarioDTO> listarTodos() throws DAOException {
        List<Usuario> lista = DAO.listarTodos();
        return lista.stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
    
        // ================== Validaciones de negocio ==================

    private void validarUsuario(UsuarioDTO dto) throws ValidacionException {
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new ValidacionException("El Id del usuario es obligatorio.");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ValidacionException("El nombre del usuario es obligatorio.");
        }
        if (dto.getTelefono() == null || dto.getTelefono().isBlank()) {
            throw new ValidacionException("El teléfono del usuario es obligatorio.");
        }
        if (dto.getCorreo() == null || dto.getCorreo().isBlank()) {
            throw new ValidacionException("El correo del usuario es obligatorio.");
        }
        if (!esCorreoValido(dto.getCorreo())) {
            throw new ValidacionException("El formato del correo no es válido.");
        }
    }

    private boolean esCorreoValido(String correo) {
        // Validación simple, suficiente para el proyecto
        String regex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        return Pattern.matches(regex, correo);
    }
}
