/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Infraestructura;
import Excepciones.DAOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * * Maneja la conexión a la base de datos utilizando el patrón Singleton.
 *
 * @author ilope
 */
public class ConexionBD {
    private static ConexionBD instancia;

    private static final String URL = "jdbc:mysql://localhost:3306/agricola"
            + "?useSSL=false&useUnicode=true&characterEncoding=utf8";
    private static final String USUARIO = "Admin";
    private static final String PASSWORD = "Admin123@";

    /**
     * Constructor privado (patrón Singleton).
     * Carga el driver JDBC al iniciar la instancia.
     */
    private ConexionBD() throws DAOException {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException ex) {
            throw new DAOException("No se pudo cargar el driver JDBC de la base de datos.", ex);
        }
    }

    
    public static synchronized ConexionBD getInstancia() throws DAOException {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection obtenerConexion() throws DAOException {
        try {
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException ex) {
            throw new DAOException("Error al obtener la conexión con la base de datos.", ex);
        }
    }
}
