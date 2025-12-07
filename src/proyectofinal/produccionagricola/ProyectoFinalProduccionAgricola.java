/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectofinal.produccionagricola;

import Controlador.CultivoControlador;
import Controlador.TrabajadorControlador;
import Dao.CultivoDAO;
import Dao.TrabajadorDAO;
import Dao.impl.CultivoDAOImpl;
import Dao.impl.TrabajadorDAOImpl;
import Vista.FrmCultivos;
import Vista.FrmTrabajadores;
import servicio.CultivoServicio;
import servicio.TrabajadorServicio;

/**
 *
 * @author Luisk
 */
public class ProyectoFinalProduccionAgricola {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TrabajadorDAO dao = new TrabajadorDAOImpl();
        TrabajadorServicio servicio = new TrabajadorServicio(dao);
        FrmTrabajadores vista = new FrmTrabajadores();
        TrabajadorControlador controlador = new TrabajadorControlador(servicio, vista);
        controlador.iniciar();
    }
    
}
