/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;
import Modelo.Usuario;
import Excepciones.DAOException;
import java.util.List;
/**
 * DAO para los administradores del sistema.
 * 
 * NOTA: Se asume que el campo contrasena de Admin está
 *       en el formato que se guardará en la BD (idealmente hash)
 * @author ilope
 */
public interface AdminDAO {
    void crear(Usuario admin) throws DAOException;

    Usuario buscarPorId(String id) throws DAOException;

    Usuario buscarPorCorreo(String correo) throws DAOException;
    
    Usuario buscarPorNombre(String nombre) throws DAOException;

    List<Usuario> listarTodos() throws DAOException;

    void actualizar(Usuario admin) throws DAOException;

    void eliminar(String id) throws DAOException;
}

