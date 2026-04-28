package codigo.builder;

import java.util.Date;
import codigo.factory.Estudiante;
import codigo.adapter.Libro;

public class SolicitudPrestamo {

    private final Estudiante estudiante;
    private final Libro      libro;
    private final Date       fechaDevolucion;
    private final String     notasEspeciales;
    private final boolean    renovacionAutomatica;
    private final int        numRenovaciones;

    private SolicitudPrestamo(Builder builder) {
        this.estudiante           = builder.estudiante;
        this.libro                = builder.libro;
        this.fechaDevolucion      = builder.fechaDevolucion;
        this.notasEspeciales      = builder.notasEspeciales;
        this.renovacionAutomatica = builder.renovacionAutomatica;
        this.numRenovaciones      = builder.numRenovaciones;
    }

    public Estudiante getEstudiante()          { return estudiante; }
    public Libro      getLibro()               { return libro; }
    public Date       getFechaDevolucion()     { return fechaDevolucion; }
    public String     getNotasEspeciales()     { return notasEspeciales; }
    public boolean    isRenovacionAutomatica() { return renovacionAutomatica; }
    public int        getNumRenovaciones()     { return numRenovaciones; }

    public static class Builder {

        private Estudiante estudiante;
        private Libro      libro;
        private Date       fechaDevolucion;

        private String  notasEspeciales      = null;
        private boolean renovacionAutomatica = false;
        private int     numRenovaciones      = 1;

        public Builder estudiante(Estudiante val)       { this.estudiante           = val; return this; }
        public Builder libro(Libro val)                 { this.libro                = val; return this; }
        public Builder fechaDevolucion(Date val)        { this.fechaDevolucion      = val; return this; }
        public Builder notasEspeciales(String val)      { this.notasEspeciales      = val; return this; }
        public Builder renovacionAutomatica(boolean val){ this.renovacionAutomatica = val; return this; }
        public Builder numRenovaciones(int val)         { this.numRenovaciones      = val; return this; }

        public SolicitudPrestamo construir() {
            if (estudiante      == null) throw new IllegalStateException("El estudiante es obligatorio.");
            if (libro           == null) throw new IllegalStateException("El libro es obligatorio.");
            if (fechaDevolucion == null) throw new IllegalStateException("La fecha de devolución es obligatoria.");
            return new SolicitudPrestamo(this);
        }
    }
}