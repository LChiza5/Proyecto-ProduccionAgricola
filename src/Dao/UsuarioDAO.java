/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;

import Excepciones.DAOException;
import Modelo.Usuario;
import java.util.List;

/**
 * 
 * @author ilope
 */
public interface UsuarioDAO {
    
    Usuario buscarPorId(String id) throws DAOException;

    void crear(Usuario usuario) throws DAOException;
    
    void eliminar(String id) throws DAOException;
    
    void actualizar(Usuario usuario) throws DAOException;
    
    List<Usuario> listarTodos() throws DAOException;
}