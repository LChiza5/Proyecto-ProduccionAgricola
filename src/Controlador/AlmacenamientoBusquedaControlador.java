/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Excepciones.DAOException;
import Vista.DlgAlmacenamientoBusqueda;
import dto.AlmacenamientoDTO;
import servicio.AlmacenamientoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
/**
 *
 * @author ilope
 */
public class AlmacenamientoBusquedaControlador {

    private final AlmacenamientoServicio servicio;
    private final DlgAlmacenamientoBusqueda vista;
    private final AlmacenamientoControlador controladorPrincipal;

    public AlmacenamientoBusquedaControlador(AlmacenamientoServicio servicio,
                                             DlgAlmacenamientoBusqueda vista,
                                             AlmacenamientoControlador controladorPrincipal) {
        this.servicio = servicio;
        this.vista = vista;
        this.controladorPrincipal = controladorPrincipal;

        configurarTabla();
        inicializarEventos();
        cargarTodos();
    }

    public void mostrar() {
        vista.setVisible(true);
    }

    private void configurarTabla() {
        String[] columnas = { "ID", "ID Producción", "Cantidad", "Ingreso", "Egreso" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vista.getTblResultados().setModel(modelo);
    }

    private void inicializarEventos() {
        vista.getBtnFiltrar().addActionListener(e -> buscar());
        vista.getBtnSeleccionar().addActionListener(e -> seleccionar());
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
    }

    private void cargarTodos() {
        try {
            List<AlmacenamientoDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar almacenamiento: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<AlmacenamientoDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblResultados().getModel();
        modelo.setRowCount(0);

        for (AlmacenamientoDTO dto : lista) {
            modelo.addRow(new Object[] {
                    dto.getId(),
                    dto.getIdProduccion(),
                    dto.getCantidad(),
                    dto.getIngreso(),
                    dto.getEgreso()
            });
        }
    }

    private void buscar() {
        String fechaDesdeTxt = vista.getTxtFechaDesde().getText().trim();
        String fechaHastaTxt = vista.getTxtFechaHasta().getText().trim();
        

        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;
        Integer idProduccion = null;

        try {
            if (!fechaDesdeTxt.isEmpty()) {
                fechaDesde = LocalDate.parse(fechaDesdeTxt);
            }
            if (!fechaHastaTxt.isEmpty()) {
                fechaHasta = LocalDate.parse(fechaHastaTxt);
            }

            List<AlmacenamientoDTO> lista =
                    servicio.buscarConFiltros(fechaDesde, fechaHasta, idProduccion);
            cargarTabla(lista);

        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha inválido. Use yyyy-MM-dd.");
        } catch (NumberFormatException ex) {
            mostrarError("El ID de producción debe ser numérico.");
        } catch (DAOException ex) {
            mostrarError("Error al buscar almacenamiento: " + ex.getMessage());
        }
    }

    private void seleccionar() {
        int fila = vista.getTblResultados().getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione un registro de la tabla.");
            return;
        }

        JTable tabla = vista.getTblResultados();

        AlmacenamientoDTO dto = new AlmacenamientoDTO();
        dto.setId((int) tabla.getValueAt(fila, 0));
        dto.setIdProduccion((int) tabla.getValueAt(fila, 1));
        dto.setCantidad((int) tabla.getValueAt(fila, 2));

        Object ingreso = tabla.getValueAt(fila, 3);
        Object egreso = tabla.getValueAt(fila, 4);

        if (ingreso != null) {
            dto.setIngreso(LocalDate.parse(ingreso.toString()));
        }
        if (egreso != null) {
            dto.setEgreso(LocalDate.parse(egreso.toString()));
        }

        controladorPrincipal.cargarDesdeSeleccion(dto);
        vista.dispose();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}