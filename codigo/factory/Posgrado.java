package codigo.factory;

public class Posgrado implements Usuario {
    private final String idUsuario;
    private final String nombre;

    public Posgrado(String idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre    = nombre;
    }

    @Override public String  getIdUsuario()        { return idUsuario; }
    @Override public String  getNombre()           { return nombre; }
    @Override public String  getTipo()             { return "POSGRADO"; }
    @Override public boolean puedePedirPrestado()  { return true; }
}