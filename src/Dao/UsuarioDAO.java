/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;

import Excepciones.DAOException;
import Modelo.Usuario;

/**
 * 
 * @author ilope
 */
public interface UsuarioDAO {

    Usuario buscarPorId(String id) throws DAOException;

    void crear(Usuario usuario) throws DAOException;
}