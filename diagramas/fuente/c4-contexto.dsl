workspace {
    model {
        estudiante    = person "Estudiante"    "Consulta el catálogo, solicita préstamos y reservas, y paga multas."
        bibliotecario = person "Bibliotecario" "Gestiona préstamos, devoluciones y reservas de salas."
        admin         = person "Admin"         "Administra usuarios, consulta reportes y configura el sistema."

        sistemaBiblioteca = softwareSystem "Sistema de Biblioteca CETYS" "Gestiona préstamos de libros, reservas de salas y multas de estudiantes."

        catalogoCETYS         = softwareSystem "Catálogo CETYS"                  "Provee información de libros disponibles. Expone una API SOAP."
        sistemaPagos          = softwareSystem "Sistema de Pagos Bancario"        "Procesa cobros de fianzas y multas. Expone una API REST."
        directorioEstudiantil = softwareSystem "Directorio Estudiantil Institucional" "Autentica usuarios y provee datos de alumnos. Protocolo LDAP."

        estudiante    -> sistemaBiblioteca "Solicita préstamos, reservas y consulta multas"
        bibliotecario -> sistemaBiblioteca "Registra préstamos, devoluciones y reservas"
        admin         -> sistemaBiblioteca "Administra usuarios y consulta reportes"

        sistemaBiblioteca -> catalogoCETYS         "Consulta disponibilidad e información de libros" "SOAP"
        sistemaBiblioteca -> sistemaPagos          "Cobra fianzas y multas"                          "REST"
        sistemaBiblioteca -> directorioEstudiantil "Autentica usuarios y obtiene datos del alumno"   "LDAP"
        sistemaPagos      -> sistemaBiblioteca     "Notifica confirmación de pago"                   "REST/Webhook"
    }

    views {
        systemContext sistemaBiblioteca "C4-Nivel1-Contexto" {
            include *
            autolayout lr
        }
    }
}