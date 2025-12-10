/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import dto.CultivoDTO;
import Excepciones.DAOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import servicio.CultivoServicio;
import Vista.DlgCultivosBusqueda;
/**
 *
 * @author ilope
 */
public class CultivoBusquedaControlador {
    
    private final CultivoServicio servicio;
    private final DlgCultivosBusqueda vista;
    private final CultivoControlador controladorPrincipal;

    public CultivoBusquedaControlador(CultivoServicio servicio,
                                      DlgCultivosBusqueda vista,
                                      CultivoControlador controladorPrincipal) {
        this.servicio = servicio;
        this.vista = vista;
        this.controladorPrincipal = controladorPrincipal;

        configurarTabla();
        inicializarEventos();
        cargarTiposEnFiltro();
        cargarTodos();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Tipo", "Área sembrada", "Estado", "Fecha siembra", "Fecha cosecha"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vista.getTblCultivos().setModel(modelo);
        vista.getTblCultivos().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }

    private void inicializarEventos() {
       
        vista.getCmbFiltroTipo().addActionListener(e -> filtrarPorTipo());
        vista.getBtnSeleccionar().addActionListener(e -> seleccionarYVolver());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
        vista.getTblCultivos().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    seleccionarYVolver();
                }
            }
        });
        
    }

    private void cargarTodos() {
        try {
            List<CultivoDTO> lista = servicio.listarTodos();
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al listar cultivos: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<CultivoDTO> lista) {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblCultivos().getModel();
        modelo.setRowCount(0);

        for (CultivoDTO dto : lista) {
            modelo.addRow(new Object[]{
                    dto.getId(),
                    dto.getNombre(),
                    dto.getTipo(),
                    dto.getAreaSembrada(),
                    dto.getEstado(),
                    dto.getFechaSiembra(),
                    dto.getFechaCosecha()
            });
        }
    }

    private void cargarTiposEnFiltro() {
        try {
            
            List<String> tipos = servicio.obtenerTiposUnicos();
            var combo = vista.getCmbFiltroTipo();
            combo.removeAllItems();
            combo.addItem("Todos");
            for (String t : tipos) {
                combo.addItem(t);
            }
            combo.setSelectedIndex(0);
        } catch (DAOException ex) {
            mostrarError("Error al cargar los tipos de cultivo: " + ex.getMessage());
        }
    }

    private void filtrarPorTipo() {
        Object sel = vista.getCmbFiltroTipo().getSelectedItem();
        if (sel == null) return;

        String tipoSeleccionado = sel.toString();
        if (tipoSeleccionado.equals("Todos")) {
            cargarTodos();
            return;
        }

        try {
            
            List<CultivoDTO> lista = servicio.buscarConFiltros(null, tipoSeleccionado, null);
            cargarTabla(lista);
        } catch (DAOException ex) {
            mostrarError("Error al filtrar por tipo: " + ex.getMessage());
        }
    }

    private void seleccionarYVolver() {
        int fila = vista.getTblCultivos().getSelectedRow();
        if (fila < 0) {
            mostrarError("Debe seleccionar un cultivo en la tabla.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) vista.getTblCultivos().getModel();

        CultivoDTO dto = new CultivoDTO();
        
        dto.setId((String) modelo.getValueAt(fila, 0)); 
        dto.setNombre(modelo.getValueAt(fila, 1).toString());
        dto.setTipo(modelo.getValueAt(fila, 2).toString());
        dto.setAreaSembrada(Double.parseDouble(modelo.getValueAt(fila, 3).toString()));
        dto.setEstado(modelo.getValueAt(fila, 4).toString());
        dto.setFechaSiembra((java.time.LocalDate) modelo.getValueAt(fila, 5));   
        dto.setFechaCosecha((java.time.LocalDate) modelo.getValueAt(fila, 6));   

       
        controladorPrincipal.cargarCultivoEnFormulario(dto);

        
        vista.dispose();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrar() {
        vista.setVisible(true);
    }
}