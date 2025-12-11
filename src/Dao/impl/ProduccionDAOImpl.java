/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao.impl;

import Dao.ProduccionDAO;
import Excepciones.DAOException;
import Infraestructura.ConexionBD;
import Modelo.Produccion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Luisk
 */
public class ProduccionDAOImpl implements ProduccionDAO {

    private static final String INSERT_SQL =
            "INSERT INTO produccion (fecha, cant_producto, calidad, destino, id_cultivo) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE produccion SET fecha=?, cant_producto=?, calidad=?, destino=?, id_cultivo=? WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM produccion WHERE id=?";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM produccion WHERE id=?";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM produccion";

    @Override
    public void crear(Produccion produccion) throws DAOException {
    String sql = "INSERT INTO produccion (fecha, cant_producto, calidad, destino, id_cultivo) " +
                 "VALUES (?, ?, ?, ?, ?)";

    try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
         PreparedStatement stmt = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setDate(1, java.sql.Date.valueOf(produccion.getFecha()));
        stmt.setInt(2, produccion.getCantProducto());
        stmt.setInt(3, produccion.getCalidad());
        stmt.setString(4, produccion.getDestino());
        stmt.setString(5, produccion.getIdCultivo());

        stmt.executeUpdate();

        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                produccion.setId(idGenerado);
            }
        }

    } catch (SQLException e) {
        throw new DAOException("Error al crear producción: " + e.getMessage(), e);
    }
}

    @Override
    public void actualizar(Produccion p) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(UPDATE_SQL)) {

            ps.setDate(1, Date.valueOf(p.getFecha()));
            ps.setInt(2, p.getCantProducto());
            ps.setInt(3, p.getCalidad());
            ps.setString(4, p.getDestino());
            ps.setString(5, p.getIdCultivo());
            ps.setInt(6, p.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DAOException("Error al actualizar producción", ex);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DAOException("Error al eliminar producción", ex);
        }
    }

    @Override
    public Produccion buscarPorId(int id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar producción", ex);
        }
    }

    @Override
    public List<Produccion> listarTodos() throws DAOException {
        List<Produccion> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(map(rs));

        } catch (SQLException ex) {
            throw new DAOException("Error al listar producciones", ex);
        }

        return lista;
    }

    @Override
    public List<Produccion> buscarConFiltros(LocalDate fechaDesde,
                                             LocalDate fechaHasta,
                                             String idCultivo,
                                             String destino) throws DAOException {

        StringBuilder sql = new StringBuilder("SELECT * FROM produccion WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (fechaDesde != null) {
            sql.append(" AND fecha >= ?");
            params.add(Date.valueOf(fechaDesde));
        }

        if (fechaHasta != null) {
            sql.append(" AND fecha <= ?");
            params.add(Date.valueOf(fechaHasta));
        }

        if (idCultivo != null) {
            sql.append(" AND id_cultivo = ?");
            params.add(idCultivo);
        }

        if (destino != null && !destino.isBlank()) {
            sql.append(" AND destino = ?");
            params.add(destino);
        }

        List<Produccion> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al filtrar producción", ex);
        }

        return lista;
    }

    private Produccion map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        int cantProducto = rs.getInt("cant_producto");
        int calidad = rs.getInt("calidad");
        String destino = rs.getString("destino");
        String idCultivo = rs.getString("id_cultivo");

        int productividad = cantProducto > 0
                ? (int) Math.round((double) calidad / cantProducto * 100)
                : 0;

        return new Produccion(id, fecha, cantProducto, calidad, productividad, destino, idCultivo);
    }
}


