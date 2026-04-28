package codigo.adapter;

public class CatalogoCETYSSOAP {
    public ResultadoSOAP consultarObra(String codigoCETYS, String formato) {
        return new ResultadoSOAP(codigoCETYS, "Título de ejemplo", "Autor de ejemplo");
    }
}