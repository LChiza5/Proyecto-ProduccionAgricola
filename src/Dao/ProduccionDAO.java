/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;
import Modelo.Produccion;
import Excepciones.DAOException;
import java.time.LocalDate;
import java.util.List;
/**
 * DAO para la entidad Produccion.
 * 
 * Recordar: el campo 'productividad' se calcula en la lógica de negocio
 * y NO se almacena directamente en la base de datos.
 * @author ilope
 */
public interface ProduccionDAO {
    void crear(Produccion produccion) throws DAOException;

    Produccion buscarPorId(int id) throws DAOException;

    /**
     * Búsqueda con filtros combinados:
     *  - Rango de fechas
     *  - Cultivo
     *  - Destino (VENTA / ALMACENAMIENTO)
     *  - Rango de calidad
     * 
     * Los parámetros pueden ser null para ser ignorados.
     */
    List<Produccion> buscarConFiltros(
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String idCultivo,
            String destino,
            Integer calidadMinima,
            Integer calidadMaxima
    ) throws DAOException;

    List<Produccion> listarTodas() throws DAOException;

    void actualizar(Produccion produccion) throws DAOException;

    void eliminar(int id) throws DAOException;
}