/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao.impl;

import Dao.PersonaDAO;
import Modelo.EnuRol;
import Excepciones.DAOException;
import Infraestructura.ConexionBD;
import Modelo.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Luisk
 */
public class PersonaDAOImpl implements PersonaDAO {
    private static final String INSERT_PERSONA_SQL =
            "INSERT INTO persona (id, nombre, telefono, correo, rol) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, telefono, correo, rol FROM persona WHERE id = ?";

    private static final String SELECT_BY_CORREO_SQL =
            "SELECT id, nombre, telefono, correo, rol FROM persona WHERE correo = ?";

    private static final String SELECT_BY_NOMBRE_SQL =
            "SELECT id, nombre, telefono, correo, rol FROM persona WHERE nombre LIKE ?";

    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, telefono, correo, rol FROM persona";

    private static final String UPDATE_PERSONA_SQL =
            "UPDATE persona SET nombre = ?, telefono = ?, correo = ?, rol = ? WHERE id = ?";

    private static final String DELETE_PERSONA_SQL =
            "DELETE FROM persona WHERE id = ?";

    @Override
    public void crear(Persona persona) throws DAOException {
        Connection cn = null;
        PreparedStatement psPersona = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            psPersona = cn.prepareStatement(INSERT_PERSONA_SQL);
            psPersona.setString(1, persona.getId());
            psPersona.setString(2, persona.getNombre());
            psPersona.setString(3, persona.getTelefono());
            psPersona.setString(4, persona.getCorreo());
            psPersona.setString(5, persona.getRol().name());
            psPersona.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException e2) {}
            }
            throw new DAOException("Error al crear persona.", ex);
        } finally {
            cerrar(psPersona);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }

    @Override
    public Persona buscarPorId(String id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPersona(rs);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar persona por ID.", ex);
        }
    }

    @Override
    public Persona buscarPorCorreo(String correo) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_CORREO_SQL)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPersona(rs);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar persona por correo.", ex);
        }
    }

    @Override
    public List<Persona> buscarPorNombre(String nombreParcial) throws DAOException {
        List<Persona> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_NOMBRE_SQL)) {

            ps.setString(1, "%" + nombreParcial + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToPersona(rs));
                }
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar personas por nombre parcial.", ex);
        }

        return lista;
    }

    @Override
    public List<Persona> listarTodas() throws DAOException {
        List<Persona> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToPersona(rs));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al listar todas las personas.", ex);
        }

        return lista;
    }

    @Override
    public void actualizar(Persona persona) throws DAOException {
        Connection cn = null;
        PreparedStatement psPersona = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            psPersona = cn.prepareStatement(UPDATE_PERSONA_SQL);
            psPersona.setString(1, persona.getNombre());
            psPersona.setString(2, persona.getTelefono());
            psPersona.setString(3, persona.getCorreo());
            psPersona.setString(4, persona.getRol().name());
            psPersona.setString(5, persona.getId());
            psPersona.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException e2) {}
            }
            throw new DAOException("Error al actualizar persona.", ex);
        } finally {
            cerrar(psPersona);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }

    @Override
    public void eliminar(String id) throws DAOException {
        Connection cn = null;
        PreparedStatement psPersona = null;

        try {
            cn = ConexionBD.getInstancia().obtenerConexion();
            cn.setAutoCommit(false);

            psPersona = cn.prepareStatement(DELETE_PERSONA_SQL);
            psPersona.setString(1, id);
            psPersona.executeUpdate();

            cn.commit();

        } catch (SQLException ex) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException e2) {}
            }
            throw new DAOException("Error al eliminar persona.", ex);
        } finally {
            cerrar(psPersona);
            restaurarAutoCommit(cn);
            cerrar(cn);
        }
    }


    private Persona mapResultSetToPersona(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String telefono = rs.getString("telefono");
        String correo = rs.getString("correo");
        EnuRol rol = EnuRol.valueOf(rs.getString("rol"));

        return new Persona(
                id,
                nombre,
                telefono,
                correo,
                rol
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
