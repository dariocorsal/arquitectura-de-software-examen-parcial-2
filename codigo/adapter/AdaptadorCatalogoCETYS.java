package codigo.adapter;

public class AdaptadorCatalogoCETYS implements CatalogoBiblioteca {

    private final CatalogoCETYSSOAP catalogoExterno;

    public AdaptadorCatalogoCETYS(CatalogoCETYSSOAP catalogoExterno) {
        this.catalogoExterno = catalogoExterno;
    }

    @Override
    public Libro buscarLibro(String isbn) {
        ResultadoSOAP resultado = catalogoExterno.consultarObra(isbn, "MARC21");
        return new Libro(
            resultado.getCodigoObra(),
            resultado.getNombreObra(),
            resultado.getAutorObra()
        );
    }
}
