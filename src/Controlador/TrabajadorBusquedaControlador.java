/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Excepciones.DAOException;
import Vista.DlgTrabajadoresBusqueda;
import dto.TrabajadorDTO;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import servicio.TrabajadorServicio;

/**
 *
 * @author ilope
 */
public class TrabajadorBusquedaControlador {
    private final TrabajadorServicio servicio;
    private final DlgTrabajadoresBusqueda vista;
    private final TrabajadorControlador controladorPrincipal;

    public TrabajadorBusquedaControlador(TrabajadorServicio servicio,
                                         DlgTrabajadoresBusqueda vista,
                                         TrabajadorControlador controladorPrincipal) {
        this.servicio = servicio;
        this.vista = vista;
        this.controladorPrincipal = controladorPrincipal;
        actualizarComboFiltroPuestos(); 
        configurarTabla();
        inicializarEventos();
        cargarPuestosEnFiltro();
        cargarTodos();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Teléfono", "Correo", "Puesto", "Horarios", "Salario"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vista.getTblTrabajadores().setModel(modelo);
        vista.getTblTrabajadores().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }

    private void inicializarEventos() {
        vista.getCmbFiltroPuesto().addActionListener(e -> buscarPorFiltroPuesto());
        vista.getBtnSeleccionar().addActionListener(e -> seleccionarYVolver());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());

        vista.getTblTrabajadores().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    seleccionarYVolver();
                }
            }
        });
        
    }
     private void actualizarComboFiltroPuestos() {
        JComboBox<String> combo = vista.getCmbFiltroPuesto();
        combo.removeAllItems();
        combo.addItem("Todos");

        try {
            List<String> puestos = servicio.obtenerPuestosUnicos();
            for (String p : puestos) {
                combo.addItem(p);
            }
            combo.setSelectedIndex(0); 
        } catch (DAOException ex) {
            mostrarError("Error al cargar los puestos para el filtro: " + ex.getMessage());
        }
    }
    private void cargarTodos() {
        try {
            List<TrabajadorDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar trabajadores: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<TrabajadorDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblTrabajadores().getModel();
        modelo.setRowCount(0);

        for (TrabajadorDTO dto : lista) {
            modelo.addRow(new Object[]{
                    dto.getId(),
                    dto.getNombre(),
                    dto.getTelefono(),
                    dto.getCorreo(),
                    dto.getPuesto(),
                    dto.getHorarios(),
                    dto.getSalario()
            });
        }
    }

    private void cargarPuestosEnFiltro() {
        try {
            List<String> puestos = servicio.obtenerPuestosUnicos();
            var combo = vista.getCmbFiltroPuesto();
            combo.removeAllItems();
            combo.addItem("Todos");
            for (String p : puestos) {
                combo.addItem(p);
            }
            combo.setSelectedIndex(0);
        } catch (DAOException ex) {
            mostrarError("Error al cargar los puestos en el filtro: " + ex.getMessage());
        }
    }

    private void buscarPorFiltroPuesto() {
        Object sel = vista.getCmbFiltroPuesto().getSelectedItem();
        if (sel == null) {
            return;
        }
        String puestoSeleccionado = sel.toString();

        if (puestoSeleccionado.equals("Todos")) {
            cargarTodos();
            return;
        }

        try {
            List<TrabajadorDTO> lista = servicio.buscarConFiltros(null, puestoSeleccionado, null);
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al buscar por puesto: " + ex.getMessage());
        }
    }

    private void seleccionarYVolver() {
        int fila = vista.getTblTrabajadores().getSelectedRow();
        if (fila < 0) {
            mostrarError("Debe seleccionar un trabajador en la tabla.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) vista.getTblTrabajadores().getModel();

        TrabajadorDTO dto = new TrabajadorDTO();
        dto.setId(modelo.getValueAt(fila, 0).toString());
        dto.setNombre(modelo.getValueAt(fila, 1).toString());
        dto.setTelefono(modelo.getValueAt(fila, 2).toString());
        dto.setCorreo(modelo.getValueAt(fila, 3).toString());
        dto.setPuesto(modelo.getValueAt(fila, 4).toString());
        dto.setHorarios(modelo.getValueAt(fila, 5).toString());
        dto.setSalario(Double.parseDouble(modelo.getValueAt(fila, 6).toString()));

        controladorPrincipal.cargarTrabajadorEnFormulario(dto);
        vista.dispose();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrar() {
        vista.setVisible(true);
    }
     
}