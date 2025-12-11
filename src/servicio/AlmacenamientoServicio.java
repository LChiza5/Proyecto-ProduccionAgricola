/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import Dao.AlmacenamientoDAO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import Modelo.Almacenamiento;
import dto.AlmacenamientoDTO;
import mapper.AlmacenamientoMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author ilope
 */
public class AlmacenamientoServicio {
     private final AlmacenamientoDAO dao;

    public AlmacenamientoServicio(AlmacenamientoDAO dao) {
        this.dao = dao;
    }

    // ===================== Métodos públicos =====================

    
    public void registrarAlmacenamiento(AlmacenamientoDTO dto)
            throws DAOException, ValidacionException {

        validar(dto, true);

        Almacenamiento entidad = AlmacenamientoMapper.toEntity(dto);
        dao.crear(entidad);

        dto.setId(entidad.getId());
    }

    public void actualizarAlmacenamiento(AlmacenamientoDTO dto)
            throws DAOException, ValidacionException {

        validar(dto, false);

        Almacenamiento entidad = AlmacenamientoMapper.toEntity(dto);
        dao.actualizar(entidad);
    }

    public void eliminarAlmacenamiento(int id) throws DAOException, ValidacionException {
        if (id <= 0) {
            throw new ValidacionException("El ID de almacenamiento es inválido.");
        }
        dao.eliminar(id);
    }


    public List<AlmacenamientoDTO> listarTodos() throws DAOException {
        List<Almacenamiento> entidades = dao.listarTodos();
        List<AlmacenamientoDTO> dtos = new ArrayList<>();

        for (Almacenamiento a : entidades) {
            dtos.add(AlmacenamientoMapper.toDTO(a));
        }
        return dtos;
    }

    public List<AlmacenamientoDTO> buscarConFiltros(
            LocalDate fechaIngresoDesde,
            LocalDate fechaIngresoHasta,
            Integer idProduccion
    ) throws DAOException {

        List<Almacenamiento> entidades =
                dao.buscarConFiltros(fechaIngresoDesde, fechaIngresoHasta, idProduccion);

        List<AlmacenamientoDTO> dtos = new ArrayList<>();
        for (Almacenamiento a : entidades) {
            dtos.add(AlmacenamientoMapper.toDTO(a));
        }
        return dtos;
    }

    
    public List<AlmacenamientoDTO> obtenerAlertasPorEstadiaMayorA(int dias)
            throws DAOException {

        if (dias <= 0) {
            dias = 1;
        }

        LocalDate hoy = LocalDate.now();
        List<AlmacenamientoDTO> alertas = new ArrayList<>();

        List<AlmacenamientoDTO> todos = listarTodos();

        for (AlmacenamientoDTO dto : todos) {
            if (dto.getIngreso() == null) {
                continue;
            }

            LocalDate fechaFin = dto.getEgreso() != null ? dto.getEgreso() : hoy;

            long diasEntre = java.time.temporal.ChronoUnit.DAYS.between(dto.getIngreso(), fechaFin);

            if (diasEntre >= dias) {
                alertas.add(dto);
            }
        }

        return alertas;
    }

    // ===================== Validaciones internas =====================

    private void validar(AlmacenamientoDTO dto, boolean esNuevo) throws ValidacionException {

        if (dto == null) {
            throw new ValidacionException("Los datos de almacenamiento no pueden ser nulos.");
        }

        if (!esNuevo && dto.getId() <= 0) {
            throw new ValidacionException("El ID del almacenamiento es obligatorio para actualizar.");
        }

        if (dto.getIdProduccion() <= 0) {
            throw new ValidacionException("Debe especificar una producción válida.");
        }

        if (dto.getCantidad() <= 0) {
            throw new ValidacionException("La cantidad debe ser mayor a cero.");
        }

        if (dto.getIngreso() == null) {
            throw new ValidacionException("La fecha de ingreso es obligatoria.");
        }

        
        if (dto.getIngreso().isAfter(LocalDate.now())) {
        JOptionPane.showMessageDialog(null,
            "Advertencia: la fecha de ingreso es futura.",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
}

        if (dto.getEgreso() != null) {
            if (dto.getEgreso().isBefore(dto.getIngreso())) {
                throw new ValidacionException("La fecha de egreso no puede ser anterior a la de ingreso.");
            }
        }
    }
}