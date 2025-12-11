/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao.impl;

import Dao.AlmacenamientoDAO;
import Excepciones.DAOException;
import Infraestructura.ConexionBD;
import Modelo.Almacenamiento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luisk
 */
public class AlmacenamientoDAOImpl implements AlmacenamientoDAO {
     private static final String INSERT_SQL =
        "INSERT INTO almacenamiento (id_produccion, cantidad, fecha_ingreso, fecha_egreso) " +
        "VALUES (?, ?, ?, ?)";

private static final String SELECT_BY_ID_SQL =
        "SELECT id, id_produccion, cantidad, fecha_ingreso, fecha_egreso " +
        "FROM almacenamiento WHERE id = ?";

private static final String SELECT_ALL_SQL =
        "SELECT id, id_produccion, cantidad, fecha_ingreso, fecha_egreso " +
        "FROM almacenamiento";

private static final String UPDATE_SQL =
        "UPDATE almacenamiento SET id_produccion = ?, cantidad = ?, fecha_ingreso = ?, fecha_egreso = ? " +
        "WHERE id = ?";

private static final String DELETE_SQL =
        "DELETE FROM almacenamiento WHERE id = ?";


    @Override
    public void crear(Almacenamiento a) throws DAOException {
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        cn = ConexionBD.getInstancia().obtenerConexion();
        cn.setAutoCommit(false);

        ps = cn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, a.getIdProduccion());
        ps.setInt(2, a.getCantidad());
        ps.setDate(3, java.sql.Date.valueOf(a.getIngreso()));
        if (a.getEgreso() != null) {
            ps.setDate(4, java.sql.Date.valueOf(a.getEgreso()));
        } else {
            ps.setNull(4, java.sql.Types.DATE);
        }

        ps.executeUpdate();

        rs = ps.getGeneratedKeys();
        if (rs.next()) {
            a.setId(rs.getInt(1));
        }

        cn.commit();

    } catch (SQLException ex) {
        rollback(cn);
        throw new DAOException("Error al crear almacenamiento.", ex);
    } finally {
        cerrar(rs);
        cerrar(ps);
        restaurarAutoCommit(cn);
        cerrar(cn);
    }
}


    @Override
    public Almacenamiento buscarPorId(int id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAlmacenamiento(rs);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar almacenamiento por ID.", ex);
        }
    }


    @Override
    public List<Almacenamiento> buscarConFiltros(
        LocalDate fechaIngresoDesde,
        LocalDate fechaIngresoHasta,
        Integer idProduccion
) throws DAOException {

    StringBuilder sql = new StringBuilder(
            "SELECT id, id_produccion, cantidad, fecha_ingreso, fecha_egreso " +
            "FROM almacenamiento WHERE 1=1"
    );

    List<Object> params = new ArrayList<>();

    if (fechaIngresoDesde != null) {
        sql.append(" AND fecha_ingreso >= ?");
        params.add(java.sql.Date.valueOf(fechaIngresoDesde));
    }

    if (fechaIngresoHasta != null) {
        sql.append(" AND fecha_ingreso <= ?");
        params.add(java.sql.Date.valueOf(fechaIngresoHasta));
    }

    if (idProduccion != null) {
        sql.append(" AND id_produccion = ?");
        params.add(idProduccion);
    }

    List<Almacenamiento> lista = new ArrayList<>();

    try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
         PreparedStatement ps = cn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToAlmacenamiento(rs));
            }
        }

    } catch (SQLException ex) {
        throw new DAOException("Error al buscar almacenamiento con filtros.", ex);
    }

    return lista;
}

    @Override
    public List<Almacenamiento> listarTodos() throws DAOException {
        List<Almacenamiento> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToAlmacenamiento(rs));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al listar todos los registros de almacenamiento.", ex);
        }

        return lista;
    }


    @Override
    public void actualizar(Almacenamiento a) throws DAOException {
        Connection cn = null;
        PreparedStatement ps = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            ps = cn.prepareStatement(UPDATE_SQL);
            ps.setInt(1, a.getIdProduccion());
            ps.setInt(2, a.getCantidad());
            ps.setDate(3, java.sql.Date.valueOf(a.getIngreso()));
            ps.setDate(4, (a.getEgreso() != null) ? java.sql.Date.valueOf(a.getEgreso()) : null);
            ps.setInt(5, a.getId());

            ps.executeUpdate();
            cn.commit();

        } catch (SQLException ex) {
            rollback(cn);
            throw new DAOException("Error al actualizar almacenamiento.", ex);
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

            ps = cn.prepareStatement(DELETE_SQL);
            ps.setInt(1, id);
            ps.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            rollback(cn);
            throw new DAOException("Error al eliminar almacenamiento.", ex);
        } finally {
            cerrar(ps);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }


    // =================== MÉTODOS AUXILIARES ===================

    private Almacenamiento mapResultSetToAlmacenamiento(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    int idProduccion = rs.getInt("id_produccion");
    int cantidad = rs.getInt("cantidad");

    LocalDate ingreso = rs.getDate("fecha_ingreso").toLocalDate();

    LocalDate egreso = null;
    Date egresoDate = rs.getDate("fecha_egreso");
    if (egresoDate != null) {
        egreso = egresoDate.toLocalDate();
    }

    return new Almacenamiento(id, idProduccion, cantidad, ingreso, egreso);
}

    private void cerrar(AutoCloseable c) {
        if (c != null) {
            try { c.close(); } catch (Exception ignored) {}
        }
    }

    private void rollback(Connection cn) {
        if (cn != null) {
            try { cn.rollback(); } catch (SQLException ignored) {}
        }
    }

    private void restaurarAutoCommit(Connection cn) {
        if (cn != null) {
            try { cn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }
}
