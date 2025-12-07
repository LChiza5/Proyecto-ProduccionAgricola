/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;

import Excepciones.DAOException;
import Modelo.Persona;
import java.util.List;

/**
 *
 * @author ilope
 */
public interface PersonaDAO {
    void crear(Persona persona) throws DAOException;

    Persona buscarPorId(String id) throws DAOException;

    Persona buscarPorCorreo(String correo) throws DAOException;

    /**
     * Búsqueda por nombre (ej. LIKE '%texto%').
     */
    List<Persona> buscarPorNombre(String nombreParcial) throws DAOException;

    List<Persona> listarTodas() throws DAOException;

    void actualizar(Persona persona) throws DAOException;

    void eliminar(String id) throws DAOException;
}
