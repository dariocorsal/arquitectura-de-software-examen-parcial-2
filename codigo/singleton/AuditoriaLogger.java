package codigo.singleton;

public class AuditoriaLogger implements Logger {

    private static volatile AuditoriaLogger instancia;

    private AuditoriaLogger() { }

    public static AuditoriaLogger obtenerInstancia() {
        if (instancia == null) {
            synchronized (AuditoriaLogger.class) {
                if (instancia == null) {
                    instancia = new AuditoriaLogger();
                }
            }
        }
        return instancia;
    }

    @Override
    public void registrar(String evento, String usuario) {
        String entrada = "[" + java.time.LocalDateTime.now() + "] "
                       + "Usuario: " + usuario + " | Evento: " + evento;
        System.out.println(entrada);
    }
}