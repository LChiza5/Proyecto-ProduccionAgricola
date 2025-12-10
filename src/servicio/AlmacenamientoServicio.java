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

    /**
     * Registra un nuevo almacenamiento (ingreso de producto al inventario).
     */
    public void registrarAlmacenamiento(AlmacenamientoDTO dto)
            throws DAOException, ValidacionException {

        validar(dto, true);

        Almacenamiento entidad = AlmacenamientoMapper.toEntity(dto);
        dao.crear(entidad);

        // Actualizamos el ID generado en el DTO
        dto.setId(entidad.getId());
    }

    /**
     * Actualiza un registro de almacenamiento existente.
     */
    public void actualizarAlmacenamiento(AlmacenamientoDTO dto)
            throws DAOException, ValidacionException {

        validar(dto, false);

        Almacenamiento entidad = AlmacenamientoMapper.toEntity(dto);
        dao.actualizar(entidad);
    }

    /**
     * Elimina un registro de almacenamiento por ID.
     */
    public void eliminarAlmacenamiento(int id) throws DAOException, ValidacionException {
        if (id <= 0) {
            throw new ValidacionException("El ID de almacenamiento es inválido.");
        }
        dao.eliminar(id);
    }

    /**
     * Lista todos los registros de almacenamiento.
     */
    public List<AlmacenamientoDTO> listarTodos() throws DAOException {
        List<Almacenamiento> entidades = dao.listarTodos();
        List<AlmacenamientoDTO> dtos = new ArrayList<>();

        for (Almacenamiento a : entidades) {
            dtos.add(AlmacenamientoMapper.toDTO(a));
        }
        return dtos;
    }

    /**
     * Busca registros de almacenamiento aplicando filtros opcionales.
     */
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

    /**
     * Obtiene registros de almacenamiento cuya estadía supera cierta cantidad de días.
     * Se usará para las alertas de productos almacenados por tiempo prolongado.
     */
    public List<AlmacenamientoDTO> obtenerAlertasPorEstadiaMayorA(int dias)
            throws DAOException {

        if (dias <= 0) {
            dias = 1;
        }

        LocalDate hoy = LocalDate.now();
        List<AlmacenamientoDTO> alertas = new ArrayList<>();

        // Podemos reutilizar listarTodos y filtrar en memoria
        List<AlmacenamientoDTO> todos = listarTodos();

        for (AlmacenamientoDTO dto : todos) {
            if (dto.getIngreso() == null) {
                continue;
            }

            // Si no tiene fecha de egreso, asumimos que aún está almacenado.
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

        // No permitir fechas de ingreso en el futuro (opcional, pero razonable)
        if (dto.getIngreso().isAfter(LocalDate.now())) {
            throw new ValidacionException("La fecha de ingreso no puede ser futura.");
        }

        if (dto.getEgreso() != null) {
            if (dto.getEgreso().isBefore(dto.getIngreso())) {
                throw new ValidacionException("La fecha de egreso no puede ser anterior a la de ingreso.");
            }
        }
    }
}