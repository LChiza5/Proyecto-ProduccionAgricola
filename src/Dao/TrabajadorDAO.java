/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;
import Modelo.Trabajador;
import Excepciones.DAOException;
import java.util.List;
/**
 *
 * @author ilope
 */
public interface TrabajadorDAO {
     void crear(Trabajador trabajador) throws DAOException;

    Trabajador buscarPorId(String id) throws DAOException;

    /**
     * Búsqueda con filtros:
     *  - nombre (parcial)
     *  - puesto
     *  - horarios
     */
    List<Trabajador> buscarConFiltros(
            String nombreParcial,
            String puesto,
            String horarios
    ) throws DAOException;

    List<Trabajador> listarTodos() throws DAOException;

    void actualizar(Trabajador trabajador) throws DAOException;

    void eliminar(String id) throws DAOException;
}