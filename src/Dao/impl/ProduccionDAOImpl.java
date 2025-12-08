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
public class ProduccionDAOImpl implements ProduccionDAO{
    private static final String INSERT_PRODUCCION_SQL =
            "INSERT INTO produccion (fecha, id_cultivo, cantidad, destino, calidad) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, fecha, id_cultivo, cantidad, destino, calidad " +
            "FROM produccion WHERE id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT id, fecha, id_cultivo, cantidad, destino, calidad FROM produccion";

    private static final String UPDATE_PRODUCCION_SQL =
            "UPDATE produccion SET fecha = ?, id_cultivo = ?, cantidad = ?, destino = ?, calidad = ? " +
            "WHERE id = ?";

    private static final String DELETE_PRODUCCION_SQL =
            "DELETE FROM produccion WHERE id = ?";


    @Override
    public void crear(Produccion produccion) throws DAOException {
        Connection cn = null;
        PreparedStatement ps = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            ps = cn.prepareStatement(INSERT_PRODUCCION_SQL);
            ps.setDate(1, java.sql.Date.valueOf(produccion.getFecha()));
            ps.setString(2, produccion.getIdCultivo());
            ps.setDouble(3, produccion.getCantidad());
            ps.setString(4, produccion.getDestino().name());
            ps.setInt(5, produccion.getCalidad());
            ps.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException e2) {}
            }
            throw new DAOException("Error al crear producción.", ex);
        } finally {
            cerrar(ps);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }


    @Override
    public Produccion buscarPorId(int id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduccion(rs);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar producción por ID.", ex);
        }
    }


    @Override
    public List<Produccion> buscarConFiltros(
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String idCultivo,
            String destino,
            Integer calidadMinima,
            Integer calidadMaxima
    ) throws DAOException {

        StringBuilder sql = new StringBuilder(
                "SELECT id, fecha, id_cultivo, cantidad, destino, calidad " +
                "FROM produccion WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (fechaDesde != null) {
            sql.append(" AND fecha >= ?");
            params.add(java.sql.Date.valueOf(fechaDesde));
        }

        if (fechaHasta != null) {
            sql.append(" AND fecha <= ?");
            params.add(java.sql.Date.valueOf(fechaHasta));
        }

        if (idCultivo != null && !idCultivo.isBlank()) {
            sql.append(" AND id_cultivo = ?");
            params.add(idCultivo);
        }

        if (destino != null && !destino.isBlank()) {
            sql.append(" AND destino = ?");
            params.add(destino);
        }

        if (calidadMinima != null) {
            sql.append(" AND calidad >= ?");
            params.add(calidadMinima);
        }

        if (calidadMaxima != null) {
            sql.append(" AND calidad <= ?");
            params.add(calidadMaxima);
        }

        List<Produccion> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToProduccion(rs));
                }
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar producciones con filtros.", ex);
        }

        return lista;
    }


    @Override
    public List<Produccion> listarTodas() throws DAOException {
        List<Produccion> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToProduccion(rs));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al listar todas las producciones.", ex);
        }

        return lista;
    }


    @Override
    public void actualizar(Produccion produccion) throws DAOException {
        Connection cn = null;
        PreparedStatement ps = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            ps = cn.prepareStatement(UPDATE_PRODUCCION_SQL);
            ps.setDate(1, java.sql.Date.valueOf(produccion.getFecha()));
            ps.setString(2, produccion.getIdCultivo());
            ps.setDouble(3, produccion.getCantidad());
            ps.setString(4, produccion.getDestino().name());
            ps.setInt(5, produccion.getCalidad());
            ps.setInt(6, produccion.getId());
            ps.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException e2) {}
            }
            throw new DAOException("Error al actualizar producción.", ex);
        } finally {
            cerrar(ps);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }


    @Override
    public void eliminar(int id) throws DAOException {
        Connection cn = null;
        PreparedStatement ps = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            ps = cn.prepareStatement(DELETE_PRODUCCION_SQL);
            ps.setInt(1, id);
            ps.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException e2) {}
            }
            throw new DAOException("Error al eliminar producción.", ex);
        } finally {
            cerrar(ps);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }


    // =================== Métodos auxiliares ===================

    private Produccion mapResultSetToProduccion(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        String idCultivo = rs.getString("id_cultivo");
        double cantidad = rs.getDouble("cantidad");
        String destinoStr = rs.getString("destino");
        int calidad = rs.getInt("calidad");

        EnuDestino destino = EnuDestino.valueOf(destinoStr);

        return new Produccion(
                id,
                fecha,
                idCultivo,
                cantidad,
                destino,
                calidad
        );
    }


    private void cerrar(AutoCloseable c) {
        if (c != null) {
            try { c.close(); } catch (Exception e) {}
        }
    }

    private void restaurarAutoCommit(Connection cn) {
        if (cn != null) {
            try { cn.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
}
