package codigo.usecase;

import codigo.builder.SolicitudPrestamo;

import java.util.List;

// Capa: Use Cases / Dominio
public interface RepositorioPrestamos {
    void guardar(SolicitudPrestamo solicitud);
    SolicitudPrestamo buscarPorId(String identificador);
    List<SolicitudPrestamo> buscarPorEstudiante(String idEstudiante);
}