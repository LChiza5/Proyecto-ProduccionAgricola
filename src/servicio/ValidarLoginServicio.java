/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import Dao.AdminDAO;
import Dao.UsuarioDAO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Modelo.Usuario;
import Util.HashUtil;

/**
 *
 * @author sebas
 */
public class ValidarLoginServicio {
    private final UsuarioDAO usuarioDAO;

    public ValidarLoginServicio(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario iniciarSesion(String id, String contrasenaPlano)
            throws ValidacionException, DAOException {

        if (id == null || id.isBlank()) {
            throw new ValidacionException("Debe ingresar la cédula/ID.");
        }
        if (contrasenaPlano == null || contrasenaPlano.isBlank()) {
            throw new ValidacionException("Debe ingresar la contraseña.");
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new ValidacionException("Usuario no encontrado o sin acceso al sistema.");
        }
   
        String hashIngresado = HashUtil.sha256(contrasenaPlano);
        if (!hashIngresado.equals(usuario.getContrasenaHash())) {
            throw new ValidacionException("Contraseña incorrecta.");
        }

        
        return usuario;
    }
    
}
