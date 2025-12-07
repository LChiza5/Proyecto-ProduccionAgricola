/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;

import Excepciones.DAOException;
import Modelo.Persona;

/**
 * DAO especializado para autenticación y validación de usuarios.
 * 
 * La implementación se encargará de:
 *  - Verificar correo y contraseña.
 *  - Consultar las tablas correspondientes (persona, admin, trabajador).
 * @author ilope
 */
public interface UsuarioDAO {
 
    /**
     * Busca un usuario por sus credenciales.
     * 
     * @param correo Correo ingresado en el login.
     * @param contrasenaEnTextoPlano Contraseña en texto plano tal como la escribe el usuario.
     *        La implementación se encargará de aplicar el mismo algoritmo de hash
     *        que se usó al guardar la contraseña en la base de datos.
     * @return Persona (con rol ADMIN o TRABAJADOR) si las credenciales son válidas;
     *         null si no coinciden.
     */
    Persona buscarPorCredenciales(String correo, String contrasenaEnTextoPlano) throws DAOException;

    /**
     * Verifica si ya existe un usuario con ese correo electrónico.
     */
    boolean existeCorreo(String correo) throws DAOException;
}