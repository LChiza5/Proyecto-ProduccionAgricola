/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao.impl;
import Modelo.EnuRol;
import Modelo.Trabajador;
import Dao.TrabajadorDAO;
import Excepciones.DAOException;
import Infraestructura.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 *  
 * Maneja inserciones y actualizaciones tanto en persona como en trabajador.
 * @author ilope
 */
public class TrabajadorDAOImpl implements TrabajadorDAO {

    private static final String INSERT_PERSONA_SQL =
            "INSERT INTO persona (id, nombre, telefono, correo, rol) VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_TRABAJADOR_SQL =
            "INSERT INTO trabajador (id, puesto, horarios, salario) VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT p.id, p.nombre, p.telefono, p.correo, p.rol, " +
            "       t.puesto, t.horarios, t.salario " +
            "FROM persona p " +
            "JOIN trabajador t ON p.id = t.id " +
            "WHERE p.id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT p.id, p.nombre, p.telefono, p.correo, p.rol, " +
            "       t.puesto, t.horarios, t.salario " +
            "FROM persona p " +
            "JOIN trabajador t ON p.id = t.id";

    private static final String UPDATE_PERSONA_SQL =
            "UPDATE persona SET nombre = ?, telefono = ?, correo = ?, rol = ? " +
            "WHERE id = ?";

    private static final String UPDATE_TRABAJADOR_SQL =
            "UPDATE trabajador SET puesto = ?, horarios = ?, salario = ? " +
            "WHERE id = ?";

    private static final String DELETE_TRABAJADOR_SQL =
            "DELETE FROM trabajador WHERE id = ?";

    private static final String DELETE_PERSONA_SQL =
            "DELETE FROM persona WHERE id = ?";

    @Override
    public void crear(Trabajador trabajador) throws DAOException {
        Connection cn = null;
        PreparedStatement psPersona = null;
        PreparedStatement psTrabajador = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false); 

            
            psPersona = cn.prepareStatement(INSERT_PERSONA_SQL);
            psPersona.setString(1, trabajador.getId());
            psPersona.setString(2, trabajador.getNombre());
            psPersona.setString(3, trabajador.getTelefono());
            psPersona.setString(4, trabajador.getCorreo());
            psPersona.setString(5, trabajador.getRol().name());
            psPersona.executeUpdate();

            
            psTrabajador = cn.prepareStatement(INSERT_TRABAJADOR_SQL);
            psTrabajador.setString(1, trabajador.getId());
            psTrabajador.setString(2, trabajador.getPuesto());
            psTrabajador.setString(3, trabajador.getHorarios());
            psTrabajador.setDouble(4, trabajador.getSalario());
            psTrabajador.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException e2) {
                   
                }
            }
            throw new DAOException("Error al crear trabajador: " + ex.getMessage(), ex);

        } finally {
            cerrar(psTrabajador);
            cerrar(psPersona);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }

    @Override
    public Trabajador buscarPorId(String id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTrabajador(rs);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar trabajador por ID.", ex);
        }
    }

    @Override
    public List<Trabajador> buscarConFiltros(String nombreParcial,
                                             String puesto,
                                             String horarios) throws DAOException {

        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.nombre, p.telefono, p.correo, p.rol, " +
                "       t.puesto, t.horarios, t.salario " +
                "FROM persona p " +
                "JOIN trabajador t ON p.id = t.id " +
                "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (nombreParcial != null && !nombreParcial.isBlank()) {
            sql.append(" AND p.nombre LIKE ?");
            params.add("%" + nombreParcial + "%");
        }
        if (puesto != null && !puesto.isBlank()) {
            sql.append(" AND t.puesto LIKE ?");
            params.add("%" + puesto + "%");
        }
        if (horarios != null && !horarios.isBlank()) {
            sql.append(" AND t.horarios LIKE ?");
            params.add("%" + horarios + "%");
        }

        List<Trabajador> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToTrabajador(rs));
                }
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar trabajadores con filtros.", ex);
        }

        return lista;
    }

    @Override
    public List<Trabajador> listarTodos() throws DAOException {
        List<Trabajador> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToTrabajador(rs));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al listar todos los trabajadores.", ex);
        }

        return lista;
    }

    @Override
    public void actualizar(Trabajador trabajador) throws DAOException {
        Connection cn = null;
        PreparedStatement psPersona = null;
        PreparedStatement psTrabajador = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            // Actualizar persona
            psPersona = cn.prepareStatement(UPDATE_PERSONA_SQL);
            psPersona.setString(1, trabajador.getNombre());
            psPersona.setString(2, trabajador.getTelefono());
            psPersona.setString(3, trabajador.getCorreo());
            psPersona.setString(4, trabajador.getRol().name());
            psPersona.setString(5, trabajador.getId());
            psPersona.executeUpdate();

            // Actualizar trabajador
            psTrabajador = cn.prepareStatement(UPDATE_TRABAJADOR_SQL);
            psTrabajador.setString(1, trabajador.getPuesto());
            psTrabajador.setString(2, trabajador.getHorarios());
            psTrabajador.setDouble(3, trabajador.getSalario());
            psTrabajador.setString(4, trabajador.getId());
            psTrabajador.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException e2) {
                    
                }
            }
            throw new DAOException("Error al actualizar trabajador.", ex);
        } finally {
            cerrar(psTrabajador);
            cerrar(psPersona);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }

    @Override
    public void eliminar(String id) throws DAOException {
        Connection cn = null;
        PreparedStatement psTrabajador = null;
        PreparedStatement psPersona = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            // Borrar de trabajador
            psTrabajador = cn.prepareStatement(DELETE_TRABAJADOR_SQL);
            psTrabajador.setString(1, id);
            psTrabajador.executeUpdate();

            // Borrar de persona
            psPersona = cn.prepareStatement(DELETE_PERSONA_SQL);
            psPersona.setString(1, id);
            psPersona.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException e2) {
                    
                }
            }
            throw new DAOException("Error al eliminar trabajador.", ex);
        } finally {
            cerrar(psTrabajador);
            cerrar(psPersona);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }

    // =================== Métodos auxiliares ===================

    private Trabajador mapResultSetToTrabajador(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String telefono = rs.getString("telefono");
        String correo = rs.getString("correo");
        String rolStr = rs.getString("rol");
        EnuRol rol = EnuRol.valueOf(rolStr);

        String puesto = rs.getString("puesto");
        String horarios = rs.getString("horarios");
        double salario = rs.getDouble("salario"); 

        
        return new Trabajador(
                puesto,
                horarios,
                salario,  
                id,
                nombre,
                telefono,
                correo,
                rol
        );
    }

    private void cerrar(AutoCloseable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
               
            }
        }
    }

    private void restaurarAutoCommit(Connection cn) {
        if (cn != null) {
            try {
                cn.setAutoCommit(true);
            } catch (SQLException e) {
               
            }
        }
    }
}