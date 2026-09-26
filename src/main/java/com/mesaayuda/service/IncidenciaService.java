package com.mesaayuda.service;

import com.mesaayuda.dto.IncidenciaRequest;
import com.mesaayuda.dto.IncidenciaResponse;
import com.mesaayuda.entity.Incidencia;
import com.mesaayuda.entity.TipoIncidencia;
import com.mesaayuda.entity.Usuario;
import com.mesaayuda.enums.EstadoIncidencia;
import com.mesaayuda.enums.Rol;
import com.mesaayuda.repository.AccionIncidenciaRepository;
import com.mesaayuda.repository.IncidenciaRepository;
import com.mesaayuda.repository.TipoIncidenciaRepository;
import com.mesaayuda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoIncidenciaRepository tipoIncidenciaRepository;
    private final HistorialIncidenciaService historialIncidenciaService;
    private final AccionIncidenciaRepository accionIncidenciaRepository;

    public IncidenciaService(
            IncidenciaRepository incidenciaRepository,
            UsuarioRepository usuarioRepository,
            TipoIncidenciaRepository tipoIncidenciaRepository,
            HistorialIncidenciaService historialIncidenciaService, AccionIncidenciaRepository accionIncidenciaRepository) {

        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoIncidenciaRepository = tipoIncidenciaRepository;
        this.historialIncidenciaService = historialIncidenciaService;
        this.accionIncidenciaRepository = accionIncidenciaRepository;
    }
    public List<Incidencia> listarTodas() {
        return incidenciaRepository.findAll();
    }
    public Optional<Incidencia> buscarPorId(Long id) {
        return incidenciaRepository.findById(id);
    }
    public IncidenciaResponse crear(
            IncidenciaRequest request,
            String email,
            String rol) {

        Usuario usuario;

        if ("USUARIO".equals(rol)) {

            usuario = usuarioRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuario autenticado no encontrado"
                            )
                    );

            if (Boolean.FALSE.equals(usuario.getEstado())) {
                throw new RuntimeException(
                        "El usuario se encuentra desactivado"
                );
            }

        } else if ("ADMIN".equals(rol)) {

            if (request.getUsuarioId() == null) {
                throw new RuntimeException(
                        "Debe indicar el usuario para registrar la incidencia"
                );
            }

            usuario = usuarioRepository
                    .findById(request.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuario no encontrado"
                            )
                    );

            if (usuario.getRol() != Rol.USUARIO) {
                throw new RuntimeException(
                        "La incidencia debe registrarse para un usuario solicitante"
                );
            }

        } else {

            throw new RuntimeException(
                    "No tiene permisos para registrar incidencias"
            );
        }
        TipoIncidencia tipo =
                tipoIncidenciaRepository
                        .findById(request.getTipoIncidenciaId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tipo de incidencia no encontrado"
                                )
                        );

        if (Boolean.FALSE.equals(tipo.getEstado())) {
            throw new RuntimeException(
                    "El tipo de incidencia se encuentra deshabilitado"
            );
        }
        Incidencia incidencia = new Incidencia();

        incidencia.setTitulo(
                request.getTitulo()
        );

        incidencia.setDescripcion(
                request.getDescripcion()
        );

        incidencia.setUbicacion(
                request.getUbicacion()
        );

        incidencia.setPrioridad(
                request.getPrioridad()
        );

        incidencia.setUsuario(
                usuario
        );

        incidencia.setTipoIncidencia(
                tipo
        );

        incidencia.setFechaRegistro(
                LocalDateTime.now()
        );

        incidencia.setEstado(
                EstadoIncidencia.REGISTRADA
        );
        Incidencia guardada =
                incidenciaRepository.save(incidencia);
        String codigo =
                String.format(
                        "INC-%04d",
                        guardada.getId()
                );

        guardada.setCodigo(codigo);

        guardada =
                incidenciaRepository.save(guardada);
        historialIncidenciaService.registrar(
                guardada,
                usuario,
                "CREACION",
                null,
                EstadoIncidencia.REGISTRADA.name(),
                "Incidencia registrada"
        );

        return convertirAResponse(guardada);
    }
    public Incidencia asignarTecnico(
            Long incidenciaId,
            Long tecnicoId) {

        Incidencia incidencia =
                incidenciaRepository
                        .findById(incidenciaId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Incidencia no encontrada"
                                )
                        );

        Usuario tecnico =
                usuarioRepository
                        .findById(tecnicoId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Técnico no encontrado"
                                )
                        );
        if (tecnico.getRol() != Rol.TECNICO) {
            throw new RuntimeException(
                    "El usuario seleccionado no es técnico"
            );
        }
        if (Boolean.FALSE.equals(tecnico.getEstado())) {
            throw new RuntimeException(
                    "El técnico seleccionado se encuentra desactivado"
            );
        }
        if (incidencia.getEstado()
                != EstadoIncidencia.EN_REVISION) {

            throw new RuntimeException(
                    "La incidencia debe estar en estado EN_REVISION "
                            + "antes de asignar un técnico"
            );
        }
        if (incidencia.getTecnico() != null) {
            throw new RuntimeException(
                    "La incidencia ya tiene un técnico asignado"
            );
        }

        EstadoIncidencia estadoAnterior =
                incidencia.getEstado();

        incidencia.setTecnico(tecnico);

        incidencia.setEstado(
                EstadoIncidencia.ASIGNADA
        );

        incidencia.setFechaAsignacion(
                LocalDateTime.now()
        );

        Incidencia actualizada =
                incidenciaRepository.save(incidencia);
        historialIncidenciaService.registrar(
                actualizada,
                tecnico,
                "ASIGNACION_TECNICO",
                estadoAnterior.name(),
                EstadoIncidencia.ASIGNADA.name(),
                "Incidencia asignada al técnico "
                        + tecnico.getNombre()
                        + " "
                        + tecnico.getApellido()
        );

        return actualizada;
    }
    public Incidencia cambiarEstadoSegunUsuario(
            Long incidenciaId,
            EstadoIncidencia nuevoEstado,
            String email,
            String rol) {

        Incidencia incidencia =
                incidenciaRepository
                        .findById(incidenciaId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Incidencia no encontrada"
                                )
                        );

        EstadoIncidencia estadoActual =
                incidencia.getEstado();
        if ("ADMIN".equals(rol)) {

            boolean permitido =
                    (estadoActual == EstadoIncidencia.REGISTRADA
                            && nuevoEstado == EstadoIncidencia.EN_REVISION)

                            || (estadoActual == EstadoIncidencia.RESUELTA
                            && nuevoEstado == EstadoIncidencia.CERRADA)

                            || (nuevoEstado == EstadoIncidencia.CANCELADA
                            && estadoActual != EstadoIncidencia.CERRADA
                            && estadoActual != EstadoIncidencia.CANCELADA);

            if (!permitido) {
                throw new RuntimeException(
                        "El ADMIN no puede realizar el cambio de estado: "
                                + estadoActual
                                + " -> "
                                + nuevoEstado
                );
            }

            return aplicarCambioEstado(
                    incidencia,
                    nuevoEstado
            );
        }
        if ("TECNICO".equals(rol)) {
            if (incidencia.getTecnico() == null) {

                throw new RuntimeException(
                        "La incidencia no tiene técnico asignado"
                );
            }
            if (!incidencia
                    .getTecnico()
                    .getEmail()
                    .equals(email)) {

                throw new RuntimeException(
                        "La incidencia está asignada a otro técnico"
                );
            }
            boolean permitido =
                    (estadoActual == EstadoIncidencia.ASIGNADA
                            && nuevoEstado == EstadoIncidencia.EN_ATENCION)

                            ||

                            (estadoActual == EstadoIncidencia.EN_ATENCION
                                    && nuevoEstado == EstadoIncidencia.RESUELTA);

            if (!permitido) {

                throw new RuntimeException(
                        "El TECNICO no puede realizar el cambio de estado: "
                                + estadoActual
                                + " -> "
                                + nuevoEstado
                );
            }
            if (nuevoEstado == EstadoIncidencia.RESUELTA) {

                boolean tieneAcciones =
                        accionIncidenciaRepository
                                .existsByIncidenciaId(
                                        incidencia.getId()
                                );

                if (!tieneAcciones) {

                    throw new RuntimeException(
                            "Debe registrar al menos una acción antes de resolver la incidencia"
                    );
                }
            }

            return aplicarCambioEstado(
                    incidencia,
                    nuevoEstado
            );
        }
        throw new RuntimeException(
                "No tiene permisos para cambiar el estado"
        );
    }
    private Incidencia aplicarCambioEstado(
            Incidencia incidencia,
            EstadoIncidencia nuevoEstado) {

        EstadoIncidencia estadoAnterior =
                incidencia.getEstado();

        if (!transicionValida(
                estadoAnterior,
                nuevoEstado)) {

            throw new RuntimeException(
                    "Transición de estado no permitida: "
                            + estadoAnterior
                            + " -> "
                            + nuevoEstado
            );
        }

        incidencia.setEstado(nuevoEstado);

        LocalDateTime ahora =
                LocalDateTime.now();

        switch (nuevoEstado) {

            case EN_ATENCION:

                if (incidencia
                        .getFechaInicioAtencion() == null) {

                    incidencia.setFechaInicioAtencion(
                            ahora
                    );
                }

                break;

            case RESUELTA:

                if (incidencia
                        .getFechaResolucion() == null) {

                    incidencia.setFechaResolucion(
                            ahora
                    );
                }

                break;

            case CERRADA:

                if (incidencia
                        .getFechaCierre() == null) {

                    incidencia.setFechaCierre(
                            ahora
                    );
                }

                break;

            default:
                break;
        }

        Incidencia actualizada =
                incidenciaRepository.save(incidencia);

        Usuario responsable;

        if (incidencia.getTecnico() != null) {

            responsable =
                    incidencia.getTecnico();

        } else {

            responsable =
                    incidencia.getUsuario();
        }

        historialIncidenciaService.registrar(
                actualizada,
                responsable,
                "CAMBIO_ESTADO",
                estadoAnterior.name(),
                nuevoEstado.name(),
                "Cambio de estado de la incidencia"
        );

        return actualizada;
    }
    public IncidenciaResponse convertirAResponse(
            Incidencia incidencia) {

        IncidenciaResponse response =
                new IncidenciaResponse();

        response.setId(
                incidencia.getId()
        );

        response.setCodigo(
                incidencia.getCodigo()
        );

        response.setTitulo(
                incidencia.getTitulo()
        );

        response.setDescripcion(
                incidencia.getDescripcion()
        );

        response.setUbicacion(
                incidencia.getUbicacion()
        );

        response.setPrioridad(
                incidencia.getPrioridad()
        );

        response.setEstado(
                incidencia.getEstado()
        );

        response.setFechaRegistro(
                incidencia.getFechaRegistro()
        );

        response.setFechaAsignacion(
                incidencia.getFechaAsignacion()
        );

        response.setFechaInicioAtencion(
                incidencia.getFechaInicioAtencion()
        );

        response.setFechaResolucion(
                incidencia.getFechaResolucion()
        );

        response.setFechaCierre(
                incidencia.getFechaCierre()
        );
        if (incidencia.getUsuario() != null) {

            response.setUsuarioId(
                    incidencia
                            .getUsuario()
                            .getId()
            );

            response.setUsuarioNombre(
                    incidencia
                            .getUsuario()
                            .getNombre()
                            + " "
                            + incidencia
                            .getUsuario()
                            .getApellido()
            );
        }
        if (incidencia.getTipoIncidencia() != null) {

            response.setTipoIncidenciaId(
                    incidencia
                            .getTipoIncidencia()
                            .getId()
            );

            response.setTipoIncidenciaNombre(
                    incidencia
                            .getTipoIncidencia()
                            .getNombre()
            );
        }
        if (incidencia.getTecnico() != null) {

            response.setTecnicoId(
                    incidencia
                            .getTecnico()
                            .getId()
            );

            response.setTecnicoNombre(
                    incidencia
                            .getTecnico()
                            .getNombre()
                            + " "
                            + incidencia
                            .getTecnico()
                            .getApellido()
            );
        }

        return response;
    }
    public List<Incidencia> listarSegunUsuario(
            String email,
            String rol) {
        if ("ADMIN".equals(rol)) {
            return incidenciaRepository.findAll();
        }
        if ("TECNICO".equals(rol)) {

            return incidenciaRepository
                    .findByTecnicoEmail(email);
        }
        if ("USUARIO".equals(rol)) {

            return incidenciaRepository
                    .findByUsuarioEmail(email);
        }

        throw new RuntimeException(
                "Rol no autorizado"
        );
    }
    public Optional<Incidencia> buscarPorIdSegunUsuario(
            Long id,
            String email,
            String rol) {
        if ("ADMIN".equals(rol)) {

            return incidenciaRepository
                    .findById(id);
        }
        if ("TECNICO".equals(rol)) {

            return incidenciaRepository
                    .findByIdAndTecnicoEmail(
                            id,
                            email
                    );
        }
        if ("USUARIO".equals(rol)) {

            return incidenciaRepository
                    .findByIdAndUsuarioEmail(
                            id,
                            email
                    );
        }

        return Optional.empty();
    }
    public boolean puedeAccederIncidencia(
            Long incidenciaId,
            String email,
            String rol) {

        if ("ADMIN".equals(rol)) {

            return incidenciaRepository
                    .existsById(incidenciaId);
        }

        if ("TECNICO".equals(rol)) {

            return incidenciaRepository
                    .findByIdAndTecnicoEmail(
                            incidenciaId,
                            email
                    )
                    .isPresent();
        }

        if ("USUARIO".equals(rol)) {

            return incidenciaRepository
                    .findByIdAndUsuarioEmail(
                            incidenciaId,
                            email
                    )
                    .isPresent();
        }

        return false;
    }
    private boolean transicionValida(
            EstadoIncidencia actual,
            EstadoIncidencia nuevo) {

        switch (actual) {

            case REGISTRADA:

                return nuevo == EstadoIncidencia.EN_REVISION
                        || nuevo == EstadoIncidencia.CANCELADA;

            case EN_REVISION:

                return nuevo == EstadoIncidencia.ASIGNADA
                        || nuevo == EstadoIncidencia.CANCELADA;

            case ASIGNADA:

                return nuevo == EstadoIncidencia.EN_ATENCION
                        || nuevo == EstadoIncidencia.CANCELADA;

            case EN_ATENCION:

                return nuevo == EstadoIncidencia.RESUELTA
                        || nuevo == EstadoIncidencia.CANCELADA;

            case RESUELTA:

                return nuevo == EstadoIncidencia.CERRADA;

            case CERRADA:
                return false;

            case CANCELADA:
                return false;

            default:
                return false;
        }
    }
}