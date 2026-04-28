package codigo.usecase;

import java.util.Date;
import codigo.adapter.CatalogoBiblioteca;
import codigo.adapter.ServicioPagos;
import codigo.singleton.Logger;
import codigo.factory.Estudiante;
import codigo.adapter.Libro;
import codigo.builder.SolicitudPrestamo;


public class RegistrarPrestamoUseCase {

    private final RepositorioPrestamos repositorioPrestamos;
    private final CatalogoBiblioteca   catalogoBiblioteca;
    private final ServicioPagos        servicioPagos;
    private final Logger               logger;

    public RegistrarPrestamoUseCase(
            RepositorioPrestamos repositorioPrestamos,
            CatalogoBiblioteca   catalogoBiblioteca,
            ServicioPagos        servicioPagos,
            Logger               logger) {
        this.repositorioPrestamos = repositorioPrestamos;
        this.catalogoBiblioteca   = catalogoBiblioteca;
        this.servicioPagos        = servicioPagos;
        this.logger               = logger;
    }

    public void ejecutar(Estudiante estudiante, String isbn, Date fechaDevolucion) {
        Libro libro = catalogoBiblioteca.buscarLibro(isbn);
        if (libro == null) {
            throw new IllegalArgumentException("Libro no encontrado: " + isbn);
        }

        SolicitudPrestamo solicitud = new SolicitudPrestamo.Builder()
            .estudiante(estudiante)
            .libro(libro)
            .fechaDevolucion(fechaDevolucion)
            .construir();

        boolean pagoCobrado = servicioPagos.cobrarFianza(estudiante.getIdUsuario(), 50.0);
        if (!pagoCobrado) {
            throw new IllegalStateException("No se pudo cobrar la fianza al estudiante.");
        }

        repositorioPrestamos.guardar(solicitud);
        logger.registrar("PRESTAMO_REGISTRADO", estudiante.getIdUsuario());
    }
}
