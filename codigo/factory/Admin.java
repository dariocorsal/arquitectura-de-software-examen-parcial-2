package codigo.factory;

public class Admin implements Usuario {
    private final String idUsuario;
    private final String nombre;

    public Admin(String idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre    = nombre;
    }

    @Override public String  getIdUsuario()        { return idUsuario; }
    @Override public String  getNombre()           { return nombre; }
    @Override public String  getTipo()             { return "ADMIN"; }
    @Override public boolean puedePedirPrestado()  { return false; }
}
