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
     * Obtiene el usuario (persona + admin) por su ID (persona.id).
     * Devuelve null si no existe o no tiene acceso.
     */
    UsuarioDAO buscarPorId(String id) throws DAOException;
}
