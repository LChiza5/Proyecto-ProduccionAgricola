/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import Modelo.Produccion;
import Dao.ProduccionDAO;
import dto.ProduccionDTO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import mapper.ProduccionMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author ilope
 */
   public class ProduccionServicio {

    private final ProduccionDAO produccionDAO;

    public ProduccionServicio(ProduccionDAO produccionDAO) {
        this.produccionDAO = produccionDAO;
    }

    // ==================== Operaciones principales ====================

    public void registrarProduccion(ProduccionDTO dto) throws ValidacionException, DAOException {
    validarProduccion(dto);

    int productividad = calcularProductividad(dto);
    dto.setProductividad(productividad);

    Produccion entidad = ProduccionMapper.toEntity(dto);
    produccionDAO.crear(entidad);

    
    dto.setId(entidad.getId());
}

    public void actualizarProduccion(ProduccionDTO dto) throws ValidacionException, DAOException {
    if (dto.getId() == null || dto.getId() <= 0) {
        throw new ValidacionException("El ID de la producción es obligatorio para actualizar.");
    }

    validarProduccion(dto);

    int productividad = calcularProductividad(dto);
    dto.setProductividad(productividad);

    Produccion entidad = ProduccionMapper.toEntity(dto);
    produccionDAO.actualizar(entidad);
}

    public void eliminarProduccion(int id) throws ValidacionException, DAOException {
        if (id <= 0) {
            throw new ValidacionException("Debe indicar un ID de producción válido para eliminar.");
        }
        produccionDAO.eliminar(id);
    }

    public ProduccionDTO buscarPorId(int id) throws DAOException {
        Produccion entidad = produccionDAO.buscarPorId(id);
        return ProduccionMapper.toDTO(entidad);
    }

    public List<ProduccionDTO> listarTodos() throws DAOException {
        return produccionDAO.listarTodos()
                .stream()
                .map(ProduccionMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<ProduccionDTO> listarParaAlmacenamiento() throws DAOException {
        List<Produccion> entidades = produccionDAO.listarParaAlmacenamiento();
        List<ProduccionDTO> lista = new ArrayList<>();
        for (Produccion p : entidades) {
            lista.add(ProduccionMapper.toDTO(p));
        }
        return lista;
    }
    public List<ProduccionDTO> buscarConFiltros(
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String idCultivo,
            String destino
    ) throws DAOException {

        List<Produccion> lista = produccionDAO.buscarConFiltros(
                fechaDesde,
                fechaHasta,
                (idCultivo != null && !idCultivo.isBlank()) ? idCultivo : null,
                (destino != null && !destino.isBlank()) ? destino : null
        );

        return lista.stream()
                .map(ProduccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ==================== Validaciones ====================

    private void validarProduccion(ProduccionDTO dto) throws ValidacionException {
        if (dto.getFecha() == null) {
            throw new ValidacionException("La fecha de la producción es obligatoria.");
        }
        if (dto.getCantProducto() <= 0) {
            throw new ValidacionException("La cantidad de producto en campo debe ser mayor a cero.");
        }
        if (dto.getCalidad() < 0) {
            throw new ValidacionException("La cantidad de producto final (calidad) no puede ser negativa.");
        }
        if (dto.getCalidad() > dto.getCantProducto()) {
            throw new ValidacionException("La cantidad de producto final no puede ser mayor a la cantidad recolectada.");
        }
        if (dto.getDestino() == null || dto.getDestino().isBlank()) {
            throw new ValidacionException("El destino (VENTA o ALMACENAMIENTO) es obligatorio.");
        }
        if (dto.getIdCultivo() == null || dto.getIdCultivo().isBlank()) {
            throw new ValidacionException("Debe seleccionar un cultivo.");
        }
    }

    /**
     * Calcula el porcentaje de productividad como:
     * (calidad / cantProducto) * 100, redondeado al entero más cercano.
     */
    private int calcularProductividad(ProduccionDTO dto) {
        if (dto.getCantProducto() <= 0) {
            return 0;
        }
        double ratio = (double) dto.getCalidad() / (double) dto.getCantProducto();
        return (int) Math.round(ratio * 100);
    }
}