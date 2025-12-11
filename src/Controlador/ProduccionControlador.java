/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Vista.FrmProduccion;
import dto.CultivoDTO;
import dto.ProduccionDTO;
import java.awt.Frame;
import servicio.CultivoServicio;
import servicio.ProduccionServicio;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
/**
 *
 * @author ilope
 */
public class ProduccionControlador {

    private final ProduccionServicio produccionServicio;
    private final CultivoServicio cultivoServicio;
    private final FrmProduccion vista;

    public ProduccionControlador(ProduccionServicio produccionServicio,
                                 CultivoServicio cultivoServicio,
                                 FrmProduccion vista) {
        this.produccionServicio = produccionServicio;
        this.cultivoServicio = cultivoServicio;
        this.vista = vista;
        inicializarEventos();
        cargarCultivosEnCombo();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
    }

    private void cargarCultivosEnCombo() {
        JComboBox<String> combo = vista.getCmbCultivo();
        combo.removeAllItems();

        try {
            List<CultivoDTO> cultivos = cultivoServicio.listarTodos();
            for (CultivoDTO c : cultivos) {
                combo.addItem(c.getId());  
            }
            if (cultivos.isEmpty()) {
                combo.setSelectedIndex(-1);
            } else {
                combo.setSelectedIndex(0);
            }
        } catch (DAOException ex) {
            mostrarError("Error al cargar cultivos: " + ex.getMessage());
        }
    }


    private void guardar() {
        try {
            ProduccionDTO dto = leerFormulario(true);
            produccionServicio.registrarProduccion(dto);

            vista.getLblProductividad().setText(String.valueOf(dto.getProductividad()));
            mostrarInfo("Producción registrada. ID: " + dto.getId()
                    + " | Productividad: " + dto.getProductividad() + "%");

            limpiarFormulario();

        } catch (ValidacionException | DAOException ex) {
            mostrarError(ex.getMessage());
        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha inválido. Use yyyy-MM-dd.");
        } catch (NumberFormatException ex) {
            mostrarError("Cantidad y calidad deben ser numéricas.");
        }
    }

    private void actualizar() {
        try {
            ProduccionDTO dto = leerFormulario(false);
            produccionServicio.actualizarProduccion(dto);

            vista.getLblProductividad().setText(String.valueOf(dto.getProductividad()));
            mostrarInfo("Producción actualizada correctamente.");

            limpiarFormulario();

        } catch (ValidacionException | DAOException ex) {
            mostrarError(ex.getMessage());
        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha inválido. Use yyyy-MM-dd.");
        } catch (NumberFormatException ex) {
            mostrarError("Cantidad y calidad deben ser numéricas.");
        }
    }

    private void eliminar() {
        String idTexto = vista.getTxtId().getText().trim();
        if (idTexto.isEmpty()) {
            mostrarError("Seleccione una producción (cargada en el formulario) para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar esta producción?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(idTexto);
            produccionServicio.eliminarProduccion(id);
            mostrarInfo("Producción eliminada correctamente.");
            limpiarFormulario();
        } catch (NumberFormatException ex) {
            mostrarError("ID inválido.");
        } catch (ValidacionException | DAOException ex) {
            mostrarError(ex.getMessage());
        }
    }


    private void abrirDialogoBusqueda() {
        java.awt.Window parent = SwingUtilities.getWindowAncestor(vista);
        Vista.DlgProduccionBusqueda dlg = new Vista.DlgProduccionBusqueda((Frame) parent, true);

        ProduccionBusquedaControlador ctrlBusqueda =
                new ProduccionBusquedaControlador(produccionServicio, dlg, this);

        ctrlBusqueda.mostrar();
    }

    
    public void cargarDesdeSeleccion(ProduccionDTO dto) {
        if (dto == null) return;

        vista.getTxtId().setText(String.valueOf(dto.getId()));
        vista.getCmbCultivo().setSelectedItem(dto.getIdCultivo());
        vista.getTxtFecha().setText(dto.getFecha() != null ? dto.getFecha().toString() : "");
        vista.getTxtCantidad().setText(String.valueOf(dto.getCantProducto()));
        vista.getTxtCalidad().setText(String.valueOf(dto.getCalidad()));
        vista.getCmbDestino().setSelectedItem(dto.getDestino());
        vista.getLblProductividad().setText(String.valueOf(dto.getProductividad()));
    }

    private void limpiarFormulario() {
        vista.getTxtId().setText("");
        vista.getCmbCultivo().setSelectedIndex(-1);
        vista.getTxtFecha().setText("");
        vista.getTxtCantidad().setText("");
        vista.getTxtCalidad().setText("");
        vista.getCmbDestino().setSelectedIndex(0);
        vista.getLblProductividad().setText("");
    }


    private ProduccionDTO leerFormulario(boolean esNuevo) {
        ProduccionDTO dto = new ProduccionDTO();

        if (!esNuevo) {
            String idTexto = vista.getTxtId().getText().trim();
            dto.setId(idTexto.isEmpty() ? 0 : Integer.parseInt(idTexto));
        }

        String idCultivo = (String) vista.getCmbCultivo().getSelectedItem();
        String fechaTexto = vista.getTxtFecha().getText().trim();
        String cantidadTexto = vista.getTxtCantidad().getText().trim();
        String calidadTexto = vista.getTxtCalidad().getText().trim();
        String destino = (String) vista.getCmbDestino().getSelectedItem();

        dto.setIdCultivo(idCultivo);

        if (!fechaTexto.isEmpty()) {
            dto.setFecha(LocalDate.parse(fechaTexto)); 
        }
        if (!cantidadTexto.isEmpty()) {
            dto.setCantProducto(Integer.parseInt(cantidadTexto));
        }
        if (!calidadTexto.isEmpty()) {
            dto.setCalidad(Integer.parseInt(calidadTexto));
        }
        dto.setDestino(destino);

        return dto;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}