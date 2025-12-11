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

    void actualizar(Produccion produccion) throws DAOException;

    void eliminar(int id) throws DAOException;

    Produccion buscarPorId(int id) throws DAOException;

    List<Produccion> listarTodos() throws DAOException;
    List<Produccion> listarParaAlmacenamiento() throws DAOException;
    
    List<Produccion> buscarConFiltros(
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String idCultivo,
            String destino
    ) throws DAOException;
}