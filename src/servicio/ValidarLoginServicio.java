/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import Dao.AdminDAO;
import Excepciones.DAOException;
import Modelo.Usuario;
import Util.HashUtil;

/**
 *
 * @author sebas
 */
public class ValidarLoginServicio {
    private AdminDAO dao;

    public ValidarLoginServicio() {
        
    }
    
    public boolean validarLogIn(String u, String contra){
        try {
            Usuario usuario = dao.buscarPorNombre(u);

            if (usuario == null) return false;

            String contraHash = HashUtil.sha256(contra);

            if (usuario.getContrasenaHash().equals(contraHash)) {
                return true;
            }
            return false;
        } catch (DAOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
