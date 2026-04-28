workspace {
    model {
        catalogoCETYS         = softwareSystem "Catálogo CETYS"           "SOAP"
        sistemaPagos          = softwareSystem "Sistema de Pagos Bancario" "REST"
        directorioEstudiantil = softwareSystem "Directorio Estudiantil"    "LDAP"

        sistemaBiblioteca = softwareSystem "Sistema de Biblioteca CETYS" {

            webApp = container "Aplicación Web" {
                technology "React"
                description "Interfaz de usuario."
            }

            baseDatos = container "Base de Datos" {
                technology "PostgreSQL"
                description "Almacena préstamos, reservas, multas y log de auditoría."
            }

            apiBackend = container "API Backend" {
                technology "Java + Spring Boot"
                description "Núcleo del sistema."

                controladorPrestamos = component "ControladorPrestamos" {
                    technology "Spring REST Controller"
                    description "Recibe peticiones HTTP de préstamos y delega al use case correspondiente."
                }

                registrarPrestamoUseCase = component "RegistrarPrestamoUseCase" {
                    technology "Java — Use Case"
                    description "Orquesta la lógica de negocio: valida datos, llama al repositorio, cobra fianza y registra auditoría."
                }

                fabricaDeUsuarios = component "FabricaDeUsuarios" {
                    technology "Java — Factory"
                    description "Crea instancias de Estudiante, Bibliotecario o Admin según el tipo recibido. Patrón: Factory."
                }

                adaptadorCatalogo = component "AdaptadorCatalogoCETYS" {
                    technology "Java — Adapter"
                    description "Traduce buscarLibro() al formato SOAP del Catálogo CETYS. Patrón: Adapter."
                }

                adaptadorPagos = component "AdaptadorPagosBancario" {
                    technology "Java — Adapter"
                    description "Adapta cobrarFianza() a la API REST del banco. Patrón: Adapter."
                }

                auditoriaLogger = component "AuditoriaLogger" {
                    technology "Java — Singleton"
                    description "Instancia única que registra todas las acciones del sistema. Patrón: Singleton."
                }

                solicitudBuilder = component "SolicitudPrestamoBuilder" {
                    technology "Java — Builder"
                    description "Construye objetos SolicitudPrestamo validando campos obligatorios. Patrón: Builder."
                }

                repositorioPrestamos = component "RepositorioPrestamos" {
                    technology "Java Interface + JPA impl"
                    description "Abstracción de acceso a datos de préstamos. La implementación concreta usa JPA/PostgreSQL."
                }

                servicioAutenticacion = component "ServicioAutenticacion" {
                    technology "Java + LDAP client"
                    description "Autentica usuarios contra el Directorio Estudiantil Institucional."
                }
            }
        }

        webApp -> controladorPrestamos "HTTP REST"
        controladorPrestamos     -> registrarPrestamoUseCase "Delega lógica"
        registrarPrestamoUseCase -> fabricaDeUsuarios        "Obtiene instancia de usuario"
        registrarPrestamoUseCase -> solicitudBuilder         "Construye la solicitud"
        registrarPrestamoUseCase -> adaptadorCatalogo        "Verifica disponibilidad del libro"
        registrarPrestamoUseCase -> adaptadorPagos           "Cobra fianza"
        registrarPrestamoUseCase -> repositorioPrestamos     "Persiste el préstamo"
        registrarPrestamoUseCase -> auditoriaLogger          "Registra la acción"

        adaptadorCatalogo     -> catalogoCETYS         "SOAP"
        adaptadorPagos        -> sistemaPagos          "REST"
        servicioAutenticacion -> directorioEstudiantil "LDAP"
        repositorioPrestamos  -> baseDatos             "JDBC / JPA"
        auditoriaLogger       -> baseDatos             "JDBC"
    }

    views {
        component apiBackend "C4-Nivel3-Componentes-APIBackend" {
            include *
            autolayout lr
        }
    }
}