/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import Modelo.EnuRol;
import Modelo.Trabajador;
import Dao.TrabajadorDAO;
import dto.TrabajadorDTO;
import Excepciones.DAOException;
import Excepciones.ValidacionException;
import mapper.TrabajadorMapper;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 *
 * @author ilope
 */
public class TrabajadorServicio {

    private final TrabajadorDAO trabajadorDAO;

    public TrabajadorServicio(TrabajadorDAO trabajadorDAO) {
        this.trabajadorDAO = trabajadorDAO;
    }

    public void crearTrabajador(TrabajadorDTO dto) throws ValidacionException, DAOException {
        if (dto.getRol() == null) {
            dto.setRol(EnuRol.TRABAJADOR);
        }
        validarTrabajador(dto);

        Trabajador entidad = TrabajadorMapper.toEntity(dto);
        trabajadorDAO.crear(entidad);
    }

    public void actualizarTrabajador(TrabajadorDTO dto) throws ValidacionException, DAOException {
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new ValidacionException("La cédula/ID del trabajador es obligatoria para actualizar.");
        }
        if (dto.getRol() == null) {
            dto.setRol(EnuRol.TRABAJADOR);
        }
        validarTrabajador(dto);

        Trabajador entidad = TrabajadorMapper.toEntity(dto);
        trabajadorDAO.actualizar(entidad);
    }

    public void eliminarTrabajador(String id) throws ValidacionException, DAOException {
        if (id == null || id.isBlank()) {
            throw new ValidacionException("Debe proporcionar la cédula/ID del trabajador a eliminar.");
        }
        trabajadorDAO.eliminar(id);
    }

    public TrabajadorDTO buscarPorId(String id) throws DAOException {
        Trabajador entidad = trabajadorDAO.buscarPorId(id);
        return TrabajadorMapper.toDTO(entidad);
    }

    public List<TrabajadorDTO> listarTodos() throws DAOException {
        List<Trabajador> lista = trabajadorDAO.listarTodos();
        return lista.stream()
                .map(TrabajadorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TrabajadorDTO> buscarConFiltros(
            String nombreParcial,
            String puesto,
            String horarios
    ) throws DAOException {

        List<Trabajador> lista = trabajadorDAO.buscarConFiltros(
                nombreParcial,
                puesto,
                horarios
        );

        return lista.stream()
                .map(TrabajadorMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<String> obtenerPuestosUnicos() throws DAOException {
    return trabajadorDAO.listarTodos().stream()
            .map(t -> t.getPuesto())
            .filter(p -> p != null && !p.isBlank())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
}
    // ================== Validaciones de negocio ==================

    private void validarTrabajador(TrabajadorDTO dto) throws ValidacionException {
        if (dto.getId() == null || dto.getId().isBlank()) {
            throw new ValidacionException("La cédula/ID del trabajador es obligatoria.");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new ValidacionException("El nombre del trabajador es obligatorio.");
        }
        if (dto.getTelefono() == null || dto.getTelefono().isBlank()) {
            throw new ValidacionException("El teléfono del trabajador es obligatorio.");
        }
        if (dto.getCorreo() == null || dto.getCorreo().isBlank()) {
            throw new ValidacionException("El correo del trabajador es obligatorio.");
        }
        if (!esCorreoValido(dto.getCorreo())) {
            throw new ValidacionException("El formato del correo no es válido.");
        }
        if (dto.getPuesto() == null || dto.getPuesto().isBlank()) {
            throw new ValidacionException("El puesto del trabajador es obligatorio.");
        }
        if (dto.getSalario() <= 0) {
            throw new ValidacionException("El salario debe ser mayor a cero.");
        }
    }

    private boolean esCorreoValido(String correo) {
        String regex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        return Pattern.matches(regex, correo);
    }
}