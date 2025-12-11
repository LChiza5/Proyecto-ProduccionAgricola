/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Excepciones.DAOException;
import Util.ReporteProduccionPDF;
import Vista.DlgProduccionBusqueda;
import dto.ProduccionDTO;
import servicio.ProduccionServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
/**
 *
 * @author ilope
 */
public class ProduccionBusquedaControlador {

    private final ProduccionServicio servicio;
    private final DlgProduccionBusqueda vista;
    private final ProduccionControlador controladorPrincipal;

    public ProduccionBusquedaControlador(ProduccionServicio servicio,
                                         DlgProduccionBusqueda vista,
                                         ProduccionControlador controladorPrincipal) {
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
        String[] columnas = { "ID", "Cultivo", "Fecha", "Cantidad", "Calidad", "Destino", "Productividad (%)" };
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
        vista.getBtnGenerarReporte().addActionListener(e -> generarReporte());
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
    }

    private void cargarTodos() {
        try {
            List<ProduccionDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar producciones: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<ProduccionDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblResultados().getModel();
        modelo.setRowCount(0);

        for (ProduccionDTO dto : lista) {
            modelo.addRow(new Object[] {
                    dto.getId(),
                    dto.getIdCultivo(),
                    dto.getFecha(),
                    dto.getCantProducto(),
                    dto.getCalidad(),
                    dto.getDestino(),
                    dto.getProductividad()
            });
        }
    }

    private void buscar() {
        String fechaDesdeTxt = vista.getTxtFechaDesde().getText().trim();
        String fechaHastaTxt = vista.getTxtFechaHasta().getText().trim();        
        String destino = (String) vista.getCmbDestino().getSelectedItem();
        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;
        String idCultivo = null;
        String destinoFiltro = null;

        try {
            if (!fechaDesdeTxt.isEmpty()) {
                fechaDesde = LocalDate.parse(fechaDesdeTxt);
            }
            if (!fechaHastaTxt.isEmpty()) {
                fechaHasta = LocalDate.parse(fechaHastaTxt);
            }
            if (destino != null && !destino.isBlank()) {
                destinoFiltro = destino;
            }

            List<ProduccionDTO> lista =
                    servicio.buscarConFiltros(fechaDesde, fechaHasta, idCultivo, destinoFiltro);
            cargarTabla(lista);

        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha inválido. Use yyyy-MM-dd.");
        } catch (DAOException ex) {
            mostrarError("Error al buscar producciones: " + ex.getMessage());
        }
    }

    private void seleccionar() {
        int fila = vista.getTblResultados().getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione una producción de la tabla.");
            return;
        }

        JTable tabla = vista.getTblResultados();

        ProduccionDTO dto = new ProduccionDTO();
        dto.setId((int) tabla.getValueAt(fila, 0));
        dto.setIdCultivo(tabla.getValueAt(fila, 1).toString());

        Object fecha = tabla.getValueAt(fila, 2);
        if (fecha != null) {
            dto.setFecha(LocalDate.parse(fecha.toString()));
        }

        dto.setCantProducto((int) tabla.getValueAt(fila, 3));
        dto.setCalidad((int) tabla.getValueAt(fila, 4));
        dto.setDestino(tabla.getValueAt(fila, 5).toString());

        Object prod = tabla.getValueAt(fila, 6);
        if (prod != null) {
            dto.setProductividad((int) Double.parseDouble(prod.toString()));
        }

        controladorPrincipal.cargarDesdeSeleccion(dto);
        vista.dispose();
    }

    private void generarReporte() {
    int fila = vista.getTblResultados().getSelectedRow();
    if (fila == -1) {
        mostrarError("Seleccione una producción de la tabla para generar el reporte.");
        return;
    }

    JTable tabla = vista.getTblResultados();

    // Armar el DTO a partir de la fila seleccionada
    ProduccionDTO dto = new ProduccionDTO();
    dto.setId((int) tabla.getValueAt(fila, 0));
    dto.setIdCultivo(tabla.getValueAt(fila, 1).toString());

    Object fecha = tabla.getValueAt(fila, 2);
    if (fecha != null) {
        dto.setFecha(LocalDate.parse(fecha.toString()));
    }

    dto.setCantProducto((int) tabla.getValueAt(fila, 3));
    dto.setCalidad((int) tabla.getValueAt(fila, 4));
    dto.setDestino(tabla.getValueAt(fila, 5).toString());

    Object prod = tabla.getValueAt(fila, 6);
    if (prod != null) {
        dto.setProductividad((int) Double.parseDouble(prod.toString()));
    }

    // Selector de archivo para PDF
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Guardar reporte PDF de producción");
    chooser.setSelectedFile(new File("reporte_produccion_" + dto.getId() + ".pdf"));

    // (Opcional) filtrar solo PDF
    javax.swing.filechooser.FileNameExtensionFilter filtroPdf =
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf");
    chooser.setFileFilter(filtroPdf);

    int opcion = chooser.showSaveDialog(vista);
    if (opcion == JFileChooser.APPROVE_OPTION) {
        File archivo = chooser.getSelectedFile();

        // Asegurar extensión .pdf
        if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
            archivo = new File(archivo.getAbsolutePath() + ".pdf");
        }

        try {
            // Llamada a tu generador de PDF
            ReporteProduccionPDF.generar(dto, archivo);

            JOptionPane.showMessageDialog(
                    vista,
                    "Reporte PDF generado en:\n" + archivo.getAbsolutePath(),
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            mostrarError("Error al generar reporte: " + ex.getMessage());
        }
        }
        }

          private void mostrarError(String mensaje) {
       JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            }

    
}