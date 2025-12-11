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
        String[] columnas = {"ID", "ID Producción", "Cantidad", "Ingreso", "Egreso"};
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

        if (vista.getBtnVerAlertas() != null) {
            vista.getBtnVerAlertas().addActionListener(e -> verAlertas());
        }
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
            modelo.addRow(new Object[]{
                    dto.getId(),
                    dto.getIdProduccion(),
                    dto.getCantidad(),
                    dto.getIngreso() != null ? dto.getIngreso().toString() : "",
                    dto.getEgreso() != null ? dto.getEgreso().toString() : ""
            });
        }
    }

    private void buscar() {

        String fechaDesdeTxt = vista.getTxtFechaDesde().getText().trim();
        String fechaHastaTxt = vista.getTxtFechaHasta().getText().trim();

        LocalDate desde = null;
        LocalDate hasta = null;

        try {
            if (!fechaDesdeTxt.isEmpty()) {
                desde = LocalDate.parse(fechaDesdeTxt);
            }
            if (!fechaHastaTxt.isEmpty()) {
                hasta = LocalDate.parse(fechaHastaTxt);
            }

            
            List<AlmacenamientoDTO> lista =
                    servicio.buscarConFiltros(desde, hasta, null);

            cargarTabla(lista);

        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha incorrecto. Use: yyyy-MM-dd");
        } catch (DAOException ex) {
            mostrarError("Error al buscar almacenamiento: " + ex.getMessage());
        }
    }

    private void seleccionar() {
        int fila = vista.getTblResultados().getSelectedRow();
        if (fila == -1) {
            mostrarError("Debe seleccionar un registro de la tabla.");
            return;
        }

        JTable tabla = vista.getTblResultados();

        AlmacenamientoDTO dto = new AlmacenamientoDTO();

        dto.setId((int) tabla.getValueAt(fila, 0));
        dto.setIdProduccion((int) tabla.getValueAt(fila, 1));
        dto.setCantidad((int) tabla.getValueAt(fila, 2));

        
        String ingresoStr = tabla.getValueAt(fila, 3).toString();
        if (!ingresoStr.isBlank()) {
            dto.setIngreso(LocalDate.parse(ingresoStr));
        }

        
        String egresoStr = tabla.getValueAt(fila, 4).toString();
        if (!egresoStr.isBlank()) {
            dto.setEgreso(LocalDate.parse(egresoStr));
        }

        controladorPrincipal.cargarDesdeBusqueda(dto);
        vista.dispose();
    }

    private void verAlertas() {
        String diasStr = JOptionPane.showInputDialog(
                vista,
                "Ingrese el número de días para alerta:",
                "30"
        );

        if (diasStr == null) return;

        int dias;
        try {
            dias = Integer.parseInt(diasStr);
            if (dias <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            mostrarError("Debe ingresar un número entero positivo.");
            return;
        }

        try {
            List<AlmacenamientoDTO> alertas = servicio.obtenerAlertasPorEstadiaMayorA(dias);

            if (alertas.isEmpty()) {
                mostrarError("No hay productos con más de " + dias + " días almacenados.");
            } else {
                cargarTabla(alertas);
                JOptionPane.showMessageDialog(
                        vista,
                        "Se encontraron " + alertas.size() + " registros en alerta.",
                        "Alertas",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (DAOException ex) {
            mostrarError("Error al obtener alertas: " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}