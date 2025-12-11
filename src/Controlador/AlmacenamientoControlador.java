/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Vista.DlgAlmacenamientoBusqueda;
import Vista.FrmAlmacenamiento;
import dto.AlmacenamientoDTO;
import dto.ProduccionDTO;
import servicio.AlmacenamientoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import servicio.ProduccionServicio;
/**
 *
 * @author ilope
 */
public class AlmacenamientoControlador {

    private final AlmacenamientoServicio servicio;
    private final ProduccionServicio produccionServicio;   
    private final FrmAlmacenamiento vista;

    public AlmacenamientoControlador(AlmacenamientoServicio servicio,
                                     ProduccionServicio produccionServicio,
                                     FrmAlmacenamiento vista) {
        this.servicio = servicio;
        this.produccionServicio = produccionServicio;
        this.vista = vista;

        inicializarEventos();
        cargarProduccionesEnCombo();   
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnBuscar().addActionListener(e -> abrirDialogoBusqueda());
        vista.getBtnLimpiar().addActionListener(e -> limpiar());
        vista.getBtnRevisarAlertas().addActionListener(e -> revisarAlertasAsync());
    }


    private void guardar() {
        try {
            AlmacenamientoDTO dto = leerDesdeFormulario();
            servicio.registrarAlmacenamiento(dto);

            mostrarMensaje("Registro de almacenamiento guardado correctamente.");
            limpiar();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al guardar el registro de almacenamiento: " + ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            AlmacenamientoDTO dto = leerDesdeFormulario();
            servicio.actualizarAlmacenamiento(dto);

            mostrarMensaje("Registro de almacenamiento actualizado correctamente.");
            limpiar();
        } catch (ValidacionException | DAOException ex) {
            mostrarError("Error al actualizar el registro de almacenamiento: " + ex.getMessage());
        }
    }

    private void eliminar() {
        String idStr = vista.getTxtId().getText().trim();
        if (idStr.isEmpty()) {
            mostrarError("Debe indicar el ID del registro de almacenamiento a eliminar.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            mostrarError("El ID debe ser un número entero.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar el registro de almacenamiento con ID: " + id + " ?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                servicio.eliminarAlmacenamiento(id);
                mostrarMensaje("Registro de almacenamiento eliminado correctamente.");
                limpiar();
            } catch (ValidacionException | DAOException ex) {
                mostrarError("Error al eliminar el registro de almacenamiento: " + ex.getMessage());
            }
        }
    }
    private void cargarProduccionesEnCombo() {
    JComboBox<String> combo = vista.getCmbIdProduccion();
    combo.removeAllItems();

    try {
        List<ProduccionDTO> producciones = produccionServicio.listarTodos();

        for (ProduccionDTO p : producciones) {
            
            combo.addItem(String.valueOf(p.getId()));
        }

        if (combo.getItemCount() > 0) {
            combo.setSelectedIndex(0);
        }

    } catch (DAOException ex) {
        mostrarError("Error al cargar las producciones en el combo: " + ex.getMessage());
    }
}
    private void limpiar() {
        vista.getTxtId().setText("");
        vista.getTxtCantidad().setText("");
        vista.getTxtFechaIngreso().setText("");
        vista.getTxtFechaEgreso().setText("");

        JComboBox<?> combo = vista.getCmbIdProduccion();
        if (combo.getItemCount() > 0) {
            combo.setSelectedIndex(0);
        }
    }

    public void iniciar() {
        vista.setVisible(true);
    }


    private void abrirDialogoBusqueda() {
        DlgAlmacenamientoBusqueda dialogo = new DlgAlmacenamientoBusqueda(vista, true);
        AlmacenamientoBusquedaControlador ctrlBusqueda =
                new AlmacenamientoBusquedaControlador(servicio, dialogo, this);
        ctrlBusqueda.mostrar();
    }

    public void cargarDesdeBusqueda(AlmacenamientoDTO dto) {
    vista.getTxtId().setText(String.valueOf(dto.getId()));
    vista.getTxtCantidad().setText(String.valueOf(dto.getCantidad()));
    vista.getTxtFechaIngreso().setText(dto.getIngreso().toString());
    if (dto.getEgreso() != null) {
        vista.getTxtFechaEgreso().setText(dto.getEgreso().toString());
    } else {
        vista.getTxtFechaEgreso().setText("");
    }

    
    vista.getCmbIdProduccion().setSelectedItem(String.valueOf(dto.getIdProduccion()));
}


    
    private void revisarAlertasAsync() {
        final int DIAS_ALERTA = 30;

        SwingWorker<List<AlmacenamientoDTO>, Void> worker =
                new SwingWorker<>() {
                    @Override
                    protected List<AlmacenamientoDTO> doInBackground() throws Exception {
                        return servicio.obtenerAlertasPorEstadiaMayorA(DIAS_ALERTA);
                    }

                    @Override
                    protected void done() {
                        try {
                            List<AlmacenamientoDTO> alertas = get(); 
                            if (alertas.isEmpty()) {
                                mostrarMensaje("No hay productos con más de " + DIAS_ALERTA + " días almacenados.");
                            } else {
                                mostrarMensaje("Hay " + alertas.size() +
                                        " registros con más de " + DIAS_ALERTA + " días almacenados.\n" +
                                        "Puedes ver el detalle en el diálogo de búsqueda.");
                            }
                        } catch (Exception ex) {
                            mostrarError("Error al revisar alertas en segundo plano: " + ex.getMessage());
                        }
                    }
                };

        worker.execute(); 
    }


    private AlmacenamientoDTO leerDesdeFormulario() throws ValidacionException {
    AlmacenamientoDTO dto = new AlmacenamientoDTO();

    String idStr = vista.getTxtId().getText().trim();
    if (!idStr.isBlank()) {
        try {
            dto.setId(Integer.parseInt(idStr));
        } catch (NumberFormatException ex) {
            throw new ValidacionException("El ID de almacenamiento debe ser numérico.");
        }
    }

    String idProdStr = (String) vista.getCmbIdProduccion().getSelectedItem();
    if (idProdStr == null || idProdStr.isBlank()) {
        throw new ValidacionException("Debe seleccionar una producción.");
    }
    try {
        int idProduccion = Integer.parseInt(idProdStr);
        dto.setIdProduccion(idProduccion);
    } catch (NumberFormatException ex) {
        throw new ValidacionException("El ID de producción seleccionado no es válido.");
    }

    String cantStr = vista.getTxtCantidad().getText().trim();
    if (cantStr.isBlank()) {
        throw new ValidacionException("Debe indicar la cantidad almacenada.");
    }
    try {
        dto.setCantidad(Integer.parseInt(cantStr));
    } catch (NumberFormatException ex) {
        throw new ValidacionException("La cantidad debe ser un número entero.");
    }

    String ingresoStr = vista.getTxtFechaIngreso().getText().trim();
    if (ingresoStr.isBlank()) {
        throw new ValidacionException("Debe indicar la fecha de ingreso.");
    }
    try {
        dto.setIngreso(LocalDate.parse(ingresoStr));
    } catch (DateTimeParseException ex) {
        throw new ValidacionException("La fecha de ingreso debe tener el formato yyyy-MM-dd.");
    }

    String egresoStr = vista.getTxtFechaEgreso().getText().trim();
    if (!egresoStr.isBlank()) {
        try {
            dto.setEgreso(LocalDate.parse(egresoStr));
        } catch (DateTimeParseException ex) {
            throw new ValidacionException("La fecha de egreso debe tener el formato yyyy-MM-dd.");
        }
    }

    return dto;
}

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}