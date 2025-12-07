/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Modelo.Cultivo;
import Excepciones.DAOException;
import java.time.LocalDate;
import java.util.List;
/**
 * DAO para la entidad Cultivo.
 * 
 * Incluye CRUD y una búsqueda con filtros avanzados.
 * @author ilope
 */
public interface CultivoDAO {
    void crear(Cultivo cultivo) throws DAOException;

    Cultivo buscarPorId(String id) throws DAOException;

    /**
     * Búsqueda avanzada por filtros.
     * Cualquier parámetro puede ser null o vacío; el DAO lo ignora.
     */
    List<Cultivo> buscarConFiltros(
            String nombre,
            String tipo,
            String estado,
            LocalDate fechaSiembraDesde,
            LocalDate fechaSiembraHasta,
            LocalDate fechaCosechaDesde,
            LocalDate fechaCosechaHasta
    ) throws DAOException;

    List<Cultivo> listarTodos() throws DAOException;

    void actualizar(Cultivo cultivo) throws DAOException;

    void eliminar(String id) throws DAOException;
}
