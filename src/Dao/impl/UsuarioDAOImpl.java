/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao.impl;

import Dao.UsuarioDAO;
import Excepciones.DAOException;
import Infraestructura.ConexionBD;
import Modelo.EnuRol;
import Modelo.Usuario;
import java.sql.*;
/**
 *
 * @author Luisk
 */
public class UsuarioDAOImpl implements UsuarioDAO  {

    private static final String INSERT_USUARIO_SQL =
            "INSERT INTO usuario (id, nombre, telefono, correo, contrasena_hash, rol) VALUES (?, ?, ?, ?, ?, ?)";
    
        private static final String INSERT_PERSONA_SQL =
            "INSERT INTO persona (id, nombre, telefono, correo, rol) VALUES (?, ?, ?, ?, ?)";
        
    private static final String SELECT_SQL =
            "SELECT u.id, u.nombre, u.telefono, u.correo, u.contrasena_hash, u.rol " +
            "FROM usuario u WHERE u.id = ?";

    private static final String UPDATE_SQL = 
            "UPDATE usuario SET nombre = ?, telefono = ?, correo = ?, contrasena_hash = ?, " +
            "rol = ? WHERE id = ?";
   
    private static final String DELETE_USUARIO_SQL =
            "DELETE FROM usuario WHERE id = ?";

        private static final String DELETE_PERSONA_SQL =
            "DELETE FROM persona WHERE id = ?";
    
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
                    String hash = rs.getString("contrasena_hash");
                    EnuRol rol = EnuRol.valueOf(rs.getString("rol"));
                    
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
                psUsuario.setString(2, usuario.getNombre());
                psUsuario.setString(3, usuario.getTelefono());
                psUsuario.setString(4, usuario.getCorreo());
                psUsuario.setString(5, usuario.getContrasenaHash());
                psUsuario.setString(6, usuario.getRol().name());
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

    @Override
    public void eliminar(Usuario u) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion()) {
            cn.setAutoCommit(false);

            try (PreparedStatement psPersona = cn.prepareStatement(DELETE_PERSONA_SQL);
                 PreparedStatement psUsuario = cn.prepareStatement(DELETE_USUARIO_SQL)) {

                // Delete en PERSONA
                psPersona.setString(1, u.getId());
                psPersona.executeUpdate();

                // Delete en USUARIO
                psUsuario.setString(1, u.getId());
                psUsuario.executeUpdate();

                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al eliminar usuario", ex);
        }
    }

    @Override
    public void actualizar(Usuario u) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, u.getNombre());
                ps.setString(2, u.getTelefono());
                ps.setString(3, u.getCorreo());
                ps.setString(4, u.getContrasenaHash());
                ps.setString(5,u.getRol().name() );
                ps.setString(6, u.getId());
                ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DAOException("Error al actualizar el usuario.", ex);
        }
    }
}