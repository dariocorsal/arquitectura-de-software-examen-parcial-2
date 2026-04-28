package codigo.factory;

public class Estudiante implements Usuario {
    private final String idUsuario;
    private final String nombre;

    public Estudiante(String idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre    = nombre;
    }

    @Override public String  getIdUsuario()        { return idUsuario; }
    @Override public String  getNombre()           { return nombre; }
    @Override public String  getTipo()             { return "ESTUDIANTE"; }
    @Override public boolean puedePedirPrestado()  { return true; }
}