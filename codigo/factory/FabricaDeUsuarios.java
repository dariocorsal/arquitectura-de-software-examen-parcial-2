package codigo.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class FabricaDeUsuarios {

    private static final Map<String, BiFunction<String, String, Usuario>> creadores = new HashMap<>();

    static {
        creadores.put("ESTUDIANTE",    (id, nombre) -> new Estudiante(id, nombre));
        creadores.put("BIBLIOTECARIO", (id, nombre) -> new Bibliotecario(id, nombre));
        creadores.put("ADMIN",         (id, nombre) -> new Admin(id, nombre));
    }

    public static void registrarTipo(String tipo, BiFunction<String, String, Usuario> creador) {
        creadores.put(tipo.toUpperCase(), creador);
    }

    public static Usuario crear(String tipo, String idUsuario, String nombre) {
        BiFunction<String, String, Usuario> creador = creadores.get(tipo.toUpperCase());
        if (creador == null) {
            throw new IllegalArgumentException("Tipo de usuario no reconocido: " + tipo);
        }
        return creador.apply(idUsuario, nombre);
    }
}
