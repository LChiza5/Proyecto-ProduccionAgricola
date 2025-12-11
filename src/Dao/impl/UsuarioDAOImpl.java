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
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Luisk
 */
public class UsuarioDAOImpl implements UsuarioDAO  {

    private static final String INSERT_PERSONA_SQL =
        "INSERT INTO persona (id, nombre, telefono, correo, rol) VALUES (?, ?, ?, ?, ?)";
    
    private static final String INSERT_USUARIO_SQL =
        "INSERT INTO usuario (id, contrasena_hash) VALUES (?, ?)";
    
    private static final String SELECT_SQL =
        "SELECT p.id, p.nombre, p.telefono, p.correo, p.rol, u.contrasena_hash FROM persona p "
        + "JOIN usuario u ON u.id = p.id WHERE p.id = ?";

    private static final String UPDATE_PERSONA_SQL = 
        "UPDATE persona SET nombre = ?, telefono = ?, correo = ?, rol = ? WHERE id = ?";
    
    private static final String UPDATE_USUARIO_SQL = 
        "UPDATE usuario SET contrasena_hash = ? WHERE id = ?";
   
    private static final String DELETE_SQL =
        "DELETE FROM persona WHERE id = ?";
    
    private static final String SELECT_ALL_SQL =
        "SELECT p.id, p.nombre, p.telefono, p.correo, p.rol, u.contrasena_hash FROM persona p JOIN usuario u ON u.id = p.id";
    
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

                psPersona.setString(1, usuario.getId());
                psPersona.setString(2, usuario.getNombre());
                psPersona.setString(3, usuario.getTelefono());
                psPersona.setString(4, usuario.getCorreo());
                psPersona.setString(5, usuario.getRol().name());
                psPersona.executeUpdate();

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

    @Override
    public void eliminar(String id) throws DAOException {
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
         PreparedStatement ps = cn.prepareStatement(DELETE_SQL)) {

        ps.setString(1, id);
        ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DAOException("Error al eliminar usuario", ex);
        }
    }

    @Override
    public void actualizar(Usuario u) throws DAOException {
                try (Connection cn = ConexionBD.getInstancia().obtenerConexion()) {
            cn.setAutoCommit(false);

            try (PreparedStatement psPersona = cn.prepareStatement(UPDATE_PERSONA_SQL);
                 PreparedStatement psUsuario = cn.prepareStatement(UPDATE_USUARIO_SQL)) {

                psPersona.setString(1, u.getNombre());
                psPersona.setString(2, u.getTelefono());
                psPersona.setString(3, u.getCorreo());
                psPersona.setString(4, u.getRol().name());
                psPersona.setString(5, u.getId());
                
                psPersona.executeUpdate();

                psUsuario.setString(1, u.getContrasenaHash());
                psUsuario.setString(2, u.getId());
                psUsuario.executeUpdate();

                cn.commit();
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al actualizar el usuario", ex);
        }
    }

    @Override
    public List<Usuario> listarTodos() throws DAOException {
        List<Usuario> lista = new ArrayList<>();
        
        try (Connection cn = ConexionBD.getInstancia().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToUsuario(rs));
            }

        } catch (SQLException ex) {
            throw new DAOException("Error al listar todos los usuarios.", ex);
        }
        
        return lista;
    }
    
    
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String telefono = rs.getString("telefono");
        String correo = rs.getString("correo");
        String contrasenaHash = rs.getString("contrasena_hash");
        String rolStr = rs.getString("rol");
        EnuRol rol = EnuRol.valueOf(rolStr);

        return new Usuario(
                id,
                nombre,
                telefono,  
                correo,
                contrasenaHash,
                rol
        );
    }
}