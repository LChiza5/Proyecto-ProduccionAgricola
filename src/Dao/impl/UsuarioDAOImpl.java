/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao.impl;

import Dao.UsuarioDAO;
import Excepciones.DAOException;
import Infraestructura.ConexionBD;
import Modelo.Almacenamiento;
import Modelo.EnuRol;
import Modelo.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Luisk
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    private static final String SELECT_SQL =
            "SELECT p.id, p.nombre, p.telefono, p.correo, p.rol, u.contrasena_hash " +
            "FROM persona p " +
            "JOIN usuario u ON p.id = u.id " +
            "WHERE p.id = ?";

    private static final String INSERT_PERSONA_SQL =
            "INSERT INTO persona (id, nombre, telefono, correo, rol) VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_USUARIO_SQL =
            "INSERT INTO usuario (id, contrasena_hash) VALUES (?, ?)";

    @Override
    public Usuario buscarPorId(String id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_SQL)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String telefono = rs.getString("telefono");
                    String correo = rs.getString("correo");
                    EnuRol rol = EnuRol.valueOf(rs.getString("rol"));
                    String hash = rs.getString("contrasena_hash");

                    return new Usuario(id, nombre, telefono, correo, hash, rol);
                }
            }

            return null;

        } catch (SQLException ex) {
            throw new DAOException("Error al buscar usuario por ID", ex);
        }
    }

    @Override
    public void crear(Usuario usuario) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion()) {
            cn.setAutoCommit(false);

            try (PreparedStatement psPersona = cn.prepareStatement(INSERT_PERSONA_SQL);
                 PreparedStatement psUsuario = cn.prepareStatement(INSERT_USUARIO_SQL)) {

                // Insert en PERSONA
                psPersona.setString(1, usuario.getId());
                psPersona.setString(2, usuario.getNombre());
                psPersona.setString(3, usuario.getTelefono());
                psPersona.setString(4, usuario.getCorreo());
                psPersona.setString(5, usuario.getRol().name());
                psPersona.executeUpdate();

                // Insert en USUARIO
                psUsuario.setString(1, usuario.getId());
                psUsuario.setString(2, usuario.getContrasenaHash());
                psUsuario.executeUpdate();

                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al crear usuario", ex);
        }
    }
}