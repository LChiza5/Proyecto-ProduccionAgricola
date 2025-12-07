/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import Modelo.Cultivo;
import Dao.CultivoDAO;
import dto.CultivoDTO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import mapper.CultivoMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Capa de servicio para la gestión de cultivos.
 * Aplica validaciones y reglas de negocio antes de llamar al DAO.
 * 
 * @author ilope
 */
public class CultivoServicio {
    private final CultivoDAO cultivoDAO;

    public CultivoServicio(CultivoDAO cultivoDAO) {
        this.cultivoDAO = cultivoDAO;
    }

    public void crearCultivo(CultivoDTO dto) throws ValidacionException, DAOException {
        validarCultivo(dto);

        Cultivo entidad = CultivoMapper.toEntity(dto);
        cultivoDAO.crear(entidad);
    }

    public void actualizarCultivo(CultivoDTO dto) throws ValidacionException, DAOException {
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new ValidacionException("El ID del cultivo es obligatorio para actualizar.");
        }
        validarCultivo(dto);

        Cultivo entidad = CultivoMapper.toEntity(dto);
        cultivoDAO.actualizar(entidad);
    }

    public void eliminarCultivo(String id) throws ValidacionException, DAOException {
        if (id == null || id.isBlank()) {
            throw new ValidacionException("Debe proporcionar el ID del cultivo a eliminar.");
        }
        cultivoDAO.eliminar(id);
    }

    public CultivoDTO buscarPorId(String id) throws DAOException {
        Cultivo entidad = cultivoDAO.buscarPorId(id);
        return CultivoMapper.toDTO(entidad);
    }

    public List<CultivoDTO> listarTodos() throws DAOException {
        List<Cultivo> lista = cultivoDAO.listarTodos();
        return lista.stream()
                .map(CultivoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CultivoDTO> buscarConFiltros(
            String nombre,
            String tipo,
            String estado,
            LocalDate fechaSiembraDesde,
            LocalDate fechaSiembraHasta,
            LocalDate fechaCosechaDesde,
            LocalDate fechaCosechaHasta
    ) throws DAOException {

        List<Cultivo> lista = cultivoDAO.buscarConFiltros(
                nombre, tipo, estado,
                fechaSiembraDesde, fechaSiembraHasta,
                fechaCosechaDesde, fechaCosechaHasta
        );

        return lista.stream()
                .map(CultivoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Reglas de negocio básicas para validar datos de cultivo.
     */
    private void validarCultivo(CultivoDTO dto) throws ValidacionException {
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new ValidacionException("El ID del cultivo es obligatorio.");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ValidacionException("El nombre del cultivo es obligatorio.");
        }
        if (dto.getTipo() == null || dto.getTipo().isBlank()) {
            throw new ValidacionException("El tipo de cultivo es obligatorio.");
        }
        if (dto.getAreaSembrada() <= 0) {
            throw new ValidacionException("El área sembrada debe ser mayor a cero.");
        }
        if (dto.getFechaSiembra() == null) {
            throw new ValidacionException("La fecha de siembra es obligatoria.");
        }
        if (dto.getFechaCosecha() != null &&
                dto.getFechaCosecha().isBefore(dto.getFechaSiembra())) {
            throw new ValidacionException("La fecha de cosecha no puede ser anterior a la de siembra.");
        }
    }
}