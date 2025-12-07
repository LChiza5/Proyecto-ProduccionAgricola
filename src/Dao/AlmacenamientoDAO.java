/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;
import Modelo.Almacenamiento;
import Excepciones.DAOException;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author ilope
 */
public interface AlmacenamientoDAO {
    void crear(Almacenamiento almacenamiento) throws DAOException;

    Almacenamiento buscarPorId(int id) throws DAOException;

    /**
     * Búsqueda con filtros:
     *  - Rango de fecha de ingreso
     *  - idProduccion
     * 
     * Los parámetros pueden ser null para ser ignorados.
     */
    List<Almacenamiento> buscarConFiltros(
            LocalDate fechaIngresoDesde,
            LocalDate fechaIngresoHasta,
            Integer idProduccion
    ) throws DAOException;

    List<Almacenamiento> listarTodos() throws DAOException;

    void actualizar(Almacenamiento almacenamiento) throws DAOException;

    void eliminar(int id) throws DAOException;
}