package codigo.adapter;

public class ResultadoSOAP {
    private final String codigoObra;
    private final String nombreObra;
    private final String autorObra;

    public ResultadoSOAP(String codigoObra, String nombreObra, String autorObra) {
        this.codigoObra = codigoObra;
        this.nombreObra = nombreObra;
        this.autorObra  = autorObra;
    }

    public String getCodigoObra() { return codigoObra; }
    public String getNombreObra() { return nombreObra; }
    public String getAutorObra()  { return autorObra; }
}
