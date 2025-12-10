/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao.impl;
import Modelo.Cultivo;
import Dao.CultivoDAO;
import Excepciones.DAOException;
import Infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ilope
 */
public class CultivoDAOImpl implements CultivoDAO {

    private static final String INSERT_SQL =
            "INSERT INTO cultivo (id, nombre, tipo, area_sembrada, estado, fecha_siembra, fecha_cosecha) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, tipo, area_sembrada, estado, fecha_siembra, fecha_cosecha " +
            "FROM cultivo WHERE id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, tipo, area_sembrada, estado, fecha_siembra, fecha_cosecha " +
            "FROM cultivo";

    private static final String UPDATE_SQL =
            "UPDATE cultivo SET nombre = ?, tipo = ?, area_sembrada = ?, estado = ?, " +
            "fecha_siembra = ?, fecha_cosecha = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM cultivo WHERE id = ?";

    // NUEVO: para llenar el ComboBox de tipos en el diálogo
    private static final String SELECT_TIPOS_UNICOS_SQL =
            "SELECT DISTINCT tipo FROM cultivo ORDER BY tipo";

    @Override
    public void crear(Cultivo cultivo) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, cultivo.getId());
            ps.setString(2, cultivo.getNombre());
            ps.setString(3, cultivo.getTipo());
            ps.setDouble(4, cultivo.getAreaSembrada());
            ps.setString(5, cultivo.getEstado());
            ps.setDate(6, Date.valueOf(cultivo.getFechaSiembra()));

            if (cultivo.getFechaCosecha() != null) {
                ps.setDate(7, Date.valueOf(cultivo.getFechaCosecha()));
            } else {
                ps.setNull(7, java.sql.Types.DATE);
            }

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DAOException("Error al crear el cultivo en la base de datos.", ex);
        }
    }

    @Override
    public Cultivo buscarPorId(String id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCultivo(rs);
                }
                return null;
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar el cultivo por ID.", ex);
        }
    }

    @Override
    public List<Cultivo> buscarConFiltros(String nombre, String tipo, String estado)
                                          
            throws DAOException {

        StringBuilder sql = new StringBuilder(
                "SELECT id, nombre, tipo, area_sembrada, estado, fecha_siembra, fecha_cosecha FROM cultivo WHERE 1=1"
        );
        List<Object> parametros = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            sql.append(" AND nombre LIKE ?");
            parametros.add("%" + nombre + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            // Como el tipo viene de un ComboBox de valores únicos, puedes usar = en lugar de LIKE
            sql.append(" AND tipo = ?");
            parametros.add(tipo);
        }
        if (estado != null && !estado.isBlank()) {
            sql.append(" AND estado LIKE ?");
            parametros.add("%" + estado + "%");
        }
        
        

        List<Cultivo> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToCultivo(rs));
                }
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar cultivos con filtros.", ex);
        }

        return lista;
    }

    @Override
    public List<Cultivo> listarTodos() throws DAOException {
        List<Cultivo> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToCultivo(rs));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al listar todos los cultivos.", ex);
        }

        return lista;
    }

    @Override
    public void actualizar(Cultivo cultivo) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, cultivo.getNombre());
            ps.setString(2, cultivo.getTipo());
            ps.setDouble(3, cultivo.getAreaSembrada());
            ps.setString(4, cultivo.getEstado());
            ps.setDate(5, Date.valueOf(cultivo.getFechaSiembra()));

            if (cultivo.getFechaCosecha() != null) {
                ps.setDate(6, Date.valueOf(cultivo.getFechaCosecha()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }

            ps.setString(7, cultivo.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DAOException("Error al actualizar el cultivo.", ex);
        }
    }

    @Override
    public void eliminar(String id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(DELETE_SQL)) {

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DAOException("Error al eliminar el cultivo.", ex);
        }
    }

    // NUEVO: tipos únicos para llenar el ComboBox del filtro en el JDialog
    @Override
    public List<String> obtenerTiposUnicos() throws DAOException {
        List<String> tipos = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_TIPOS_UNICOS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tipos.add(rs.getString("tipo"));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al obtener la lista de tipos de cultivo.", ex);
        }

        return tipos;
    }

    private Cultivo mapResultSetToCultivo(ResultSet rs) throws SQLException {
        Cultivo c = new Cultivo();
        c.setId(rs.getString("id"));
        c.setNombre(rs.getString("nombre"));
        c.setTipo(rs.getString("tipo"));
        c.setAreaSembrada(rs.getDouble("area_sembrada"));
        c.setEstado(rs.getString("estado"));

        Date fSiembra = rs.getDate("fecha_siembra");
        if (fSiembra != null) {
            c.setFechaSiembra(fSiembra.toLocalDate());
        }

        Date fCosecha = rs.getDate("fecha_cosecha");
        if (fCosecha != null) {
            c.setFechaCosecha(fCosecha.toLocalDate());
        }

        return c;
    }
}