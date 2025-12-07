/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;
import Modelo.Admin;
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
    void crear(Admin admin) throws DAOException;

    Admin buscarPorId(String id) throws DAOException;

    Admin buscarPorCorreo(String correo) throws DAOException;

    List<Admin> listarTodos() throws DAOException;

    void actualizar(Admin admin) throws DAOException;

    void eliminar(String id) throws DAOException;
}

