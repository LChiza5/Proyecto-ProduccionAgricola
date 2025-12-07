/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectofinal.produccionagricola;

import Controlador.CultivoControlador;
import Dao.CultivoDAO;
import Dao.impl.CultivoDAOImpl;
import Vista.FrmCultivos;
import servicio.CultivoServicio;

/**
 *
 * @author Luisk
 */
public class ProyectoFinalProduccionAgricola {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       CultivoDAO cultivoDAO = new CultivoDAOImpl();
        CultivoServicio servicio = new CultivoServicio(cultivoDAO);

        // Crear la vista
        FrmCultivos vista = new FrmCultivos();

        // Crear el controlador y conectar todo
        CultivoControlador controlador = new CultivoControlador(servicio, vista);

        // Iniciar la ventana
        controlador.iniciar();
    }
    
}
