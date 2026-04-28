workspace {
    model {
        estudiante    = person "Estudiante"
        bibliotecario = person "Bibliotecario"
        admin         = person "Admin"

        catalogoCETYS         = softwareSystem "Catálogo CETYS"                  "SOAP"
        sistemaPagos          = softwareSystem "Sistema de Pagos Bancario"        "REST"
        directorioEstudiantil = softwareSystem "Directorio Estudiantil"           "LDAP"

        sistemaBiblioteca = softwareSystem "Sistema de Biblioteca CETYS" {

            webApp = container "Aplicación Web" {
                technology "React"
                description "Interfaz de usuario para estudiantes, bibliotecarios y admins. Consume el API Backend vía HTTPS/JSON."
            }

            apiBackend = container "API Backend" {
                technology "Java + Spring Boot"
                description "Núcleo del sistema. Expone endpoints REST, aplica reglas de negocio, orquesta integraciones y registra auditoría."
            }

            baseDatos = container "Base de Datos" {
                technology "PostgreSQL"
                description "Almacena préstamos, reservas, multas, usuarios y log de auditoría."
            }

            workerNotificaciones = container "Worker de Notificaciones" {
                technology "Java + Scheduler"
                description "Envía correos de vencimiento y recordatorios de reserva. Desacoplado del flujo principal para no bloquear al usuario."
            }
        }

        estudiante    -> webApp "Usa la interfaz"       "HTTPS"
        bibliotecario -> webApp "Usa la interfaz"       "HTTPS"
        admin         -> webApp "Administra el sistema" "HTTPS"

        webApp -> apiBackend "Llama a la API" "REST / JSON / HTTPS"

        apiBackend -> baseDatos            "Lee y escribe datos"            "JDBC"
        apiBackend -> catalogoCETYS        "Consulta libros"                "SOAP / HTTPS"
        apiBackend -> sistemaPagos         "Procesa cobros"                 "REST / HTTPS"
        apiBackend -> directorioEstudiantil "Autentica y consulta alumnos" "LDAP"
        apiBackend -> workerNotificaciones "Encola eventos de notificación" "Cola interna"
    }

    views {
        container sistemaBiblioteca "C4-Nivel2-Contenedores" {
            include *
            autolayout lr
        }
    }
}