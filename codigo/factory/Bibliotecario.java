package codigo.factory;

public class Bibliotecario implements Usuario {
    private final String idUsuario;
    private final String nombre;

    public Bibliotecario(String idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre    = nombre;
    }

    @Override public String  getIdUsuario()        { return idUsuario; }
    @Override public String  getNombre()           { return nombre; }
    @Override public String  getTipo()             { return "BIBLIOTECARIO"; }
    @Override public boolean puedePedirPrestado()  { return false; }
}