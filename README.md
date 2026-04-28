# Sistema de Biblioteca Universitaria: CETYS Universidad

**Materia:** Arquitectura de Software
**Alumno:** Darío Córdova S.
**Matricula:** m040754

---

## Sección 1. Modelo C4

### Pregunta 1A. Diagrama de Contexto

Archivo fuente:
`./diagramas/fuente/c4-contexto.dsl`

---

### Pregunta 1B. Diagrama de Contenedores

Archivo fuente:
`./diagramas/fuente/c4-contenedores.dsl`

| Contenedor               | Tecnología         | Justificación                                                 |
| ------------------------ | ------------------ | ------------------------------------------------------------- |
| Aplicación Web           | React              | SPA liviana, fácil de mantener, consume REST                  |
| API Backend              | Java + Spring Boot | Ecosistema maduro para SOAP, REST y LDAP; lenguaje del equipo |
| Base de Datos            | PostgreSQL         | Relacional, confiable; soporta auditoría con tablas simples   |
| Worker de Notificaciones | Java + Scheduler   | Desacoplado del request principal; no bloquea al usuario      |

---

### Pregunta 1C. Diagrama de Componentes del API Backend

Archivo fuente:
`./diagramas/fuente/c4-componentes.dsl`

---

## Sección 2. Patrones de Diseño

### Pregunta 2A. Singleton: Registro de Auditoría

#### 2A.1 Implementación y 2A.2 Exponer el método registrar

Codigo fuente:
`./codigo/singleton/`

**Justificación**

El constructor es private para impedir que cualquier clase externa instancie AuditoriaLogger. El método obtenerInstancia() usa double-checked locking con volatile para garantizar una sola instancia en entornos multihilo. AuditoriaLogger implementa la interfaz Logger, lo que permite que los use cases dependan de la abstracción y no de la clase concreta.

#### 2A.3 Diagrama de clases

Codigo fuente:
`./diagramas/fuente/singleton.puml`

#### 2A.4 Reflexión

**¿Qué problema concreto de este sistema resuelve tener una sola instancia?**
RegistrarPrestamoUseCase, AdaptadorPagosBancario y ControladorPrestamos escriben eventos de auditoría de forma concurrente. Con una sola instancia se garantiza un único punto de escritura, sin entradas duplicadas ni fragmentadas.

**¿Qué pasaría si hubiera dos instancias simultáneas?**
Cada componente escribiría a su propia copia del logger. El registro quedaría dividido en dos flujos independientes, haciendo imposible reconstruir la secuencia real de eventos, lo que viola directamente el requisito de un único registro centralizado.

---

### Pregunta 2B. Factory: Creación de Usuarios

#### 2B.1 Interfaz de Usuario y 2B.2 Implementación de Estudiante, Bibliotecario, Admin

Codigo fuente:
`./codigo/factory/`

#### 2B.3 Diagrama de clases

Codigo fuente:
`./diagramas/fuente/factory.puml`

#### 2B.4 Agregar el tipo Posgrado en el futuro

Codigo fuente:
`./codigo/Factory/Posgrado.java`

```java
FabricaDeUsuarios.registrarTipo("POSGRADO", (id, nombre) -> new Posgrado(id, nombre));
```

**Qué principio SOLID garantiza esto?**

El principio SOLID que garantiza esto es el Open/Closed Principle (OCP). La fábrica usa un Map de lambdas creadoras en lugar de un switch cerrado. Esto permite que FabricaDeUsuarios esté cerrada para modificación y abierta para extensión. Agregar Posgrado en el futuro requiere únicamente crear la clase concreta y registrarla en el arranque, ninguna línea de la fábrica cambia.

---

### Pregunta 2C. Adapter: Integración con el Catálogo CETYS

#### 2C.1 Implementación

Codigo fuente:
`./codigo/adapter/`

#### 2C.2 Diagrama de clases. Adapter Catálogo

Codigo fuente:
`./diagramas/fuente/adapter.puml`

#### 2C.3 Reflexión

**Si mañana CETYS cambia de proveedor de catálogo a uno con una interfaz completamente diferente, ¿cuánto código habría que modificar? ¿Por qué?** Solo hay que crear un nuevo adaptador que implemente CatalogoBiblioteca. Todo el código interno seguirá usando la interfaz sin ningún cambio, cero modificaciones al código existente.

---

### Pregunta 2D. Builder: Solicitudes de Préstamo

#### 2D.1 Implementación

Codigo fuente:
`./codigo/builder/`

#### 2D.2 Tres ejemplos de uso

```java
// 1. Solo campos obligatorios
SolicitudPrestamo basica = new SolicitudPrestamo.Builder()
    .estudiante(estudiante)
    .libro(libro)
    .fechaDevolucion(new Date())
    .construir();

// 2. Con nota especial
SolicitudPrestamo conNotas = new SolicitudPrestamo.Builder()
    .estudiante(estudiante)
    .libro(libro)
    .fechaDevolucion(new Date())
    .notasEspeciales("Libro para tesis, requiere manejo especial")
    .construir();

// 3. Con renovacion automatica
SolicitudPrestamo renovable = new SolicitudPrestamo.Builder()
    .estudiante(estudiante)
    .libro(libro)
    .fechaDevolucion(new Date())
    .renovacionAutomatica(true)
    .numRenovaciones(3)
    .notasEspeciales("Alumno de intercambio")
    .construir();
```

#### 2D.3 Diagrama de clases

Codigo fuente:
`./diagramas/fuente/builder.puml`

#### Reflexión

**¿Por qué conviene que SolicitudPrestamo sea inmutable una vez construida? ¿Qué problemas evitamos?**
SolicitudPrestamo es inmutable porque todos sus campos son final y no expone setters. Una vez registrado un préstamo, sus datos no pueden alterarse desde ninguna otra parte del código. Si libro o fechaDevolucion fueran mutables, cualquier componente podría modificar un préstamo ya procesado y auditado, generando inconsistencias entre el log y el estado real del sistema. La inmutabilidad garantiza que lo que se auditó es exactamente lo que ocurrió.

---

## Sección 3. Buenas Prácticas y Principios SOLID

### Pregunta 3A. Análisis de violaciones

#### 3A.1 Principios violados por GestorBiblioteca

**1. Single Responsibility Principle (SRP)**
GestorBiblioteca concentra al menos seis responsabilidades distintas: gestionar préstamos y devoluciones, registrar multas, enviar notificaciones por email, generar reportes PDF, autenticar usuarios y consultar el catálogo externo. Cada una es una razón independiente para cambiar: si el proveedor de email cambia, se toca la misma clase que gestiona los préstamos.

**2. Open/Closed Principle (OCP)**
Agregar un nuevo canal de notificación (SMS, push) o un nuevo formato de reporte implica modificar GestorBiblioteca directamente. La clase no está cerrada para modificación.

**3. Dependency Inversion Principle (DIP)**
GestorBiblioteca instancia directamente CatalogoCETYSSOAP en lugar de depender de la abstracción CatalogoBiblioteca. Depende de una implementación concreta de infraestructura, no de una interfaz de dominio.

#### 3A.2 Refactorización propuesta

Cada responsabilidad pasa a una clase propia. Las dependencias se invierten: las clases de dominio dependen de interfaces, no de implementaciones concretas.

Codigo fuente:
`./diagramas/fuente/refactor-solid.puml`

#### 3A.3 Relación con la Dependency Rule de Clean Architecture

La Dependency Rule establece que las dependencias deben apuntar siempre hacia adentro, hacia el dominio. GestorBiblioteca la viola porque mezcla reglas de negocio con detalles de infraestructura en la misma clase. Después de refactorizar, ServicioPrestamos depende de la interfaz CatalogoBiblioteca (dominio), no de CatalogoCETYSSOAP (infraestructura).

---

### Pregunta 3B. Diseño de la capa de Use Cases (Clean Architecture)

#### 3B.1 Interfaz del repositorio RepositorioPrestamos

Codigo fuente:
`./codigo/usecase/RepositorioPrestamos.java`

La interfaz vive en la capa de Use Cases porque es una abstracción que el caso de uso necesita. La implementación concreta vive en infraestructura y apunta hacia esta interfaz.

#### 3B.2 Implementación de clase RegistrarPrestamoUseCase

Codigo fuente:
`./codigo/usecase/RegistrarPrestamoUseCase.java`

#### 3B.3 ¿Cómo garantiza la Dependency Rule el cambio de MySQL a MongoDB?

RegistrarPrestamoUseCase solo conoce RepositorioPrestamos (interfaz). Cambiar la base de datos es sustituir únicamente la implementación concreta en infraestructura. El use case no se toca. La Dependency Rule garantiza esto porque las dependencias apuntan hacia adentro y ningún detalle de infraestructura está referenciado en la capa de dominio.

#### 3B.4 ¿Qué patrón de los estudiados en clase aparece implícitamente en este diseño?

**Inyección de Dependencias (Dependency Inversion):** el use case recibe todos sus colaboradores por constructor en lugar de instanciarlos. Esto permite sustituir cualquier implementación (base de datos, logger, etc.) sin modificar el caso de uso.

---

## Sección 4. Integración y Síntesis

### Pregunta 4. Flujo completo: solicitud de préstamo con cobro de fianza

#### 4.1 Diagrama de secuencia

Codigo fuente:
`./diagramas/fuente/secuencia.puml`

#### 4.2 Dónde actúa cada patrón

| Patrón        | Momento en el flujo                                                                                                    |
| ------------- | ---------------------------------------------------------------------------------------------------------------------- |
| **Factory**   | Al inicio: ControladorPrestamos crea la instancia correcta de Estudiante sin conocer la clase concreta             |
| **Adapter**   | Dos veces: AdaptadorCatalogoCETYS traduce la llamada SOAP; AdaptadorPagosBancario adapta la llamada REST del banco |
| **Builder**   | Después de verificar el libro: SolicitudPrestamo.Builder valida campos obligatorios y construye el objeto inmutable  |
| **Singleton** | Al cierre del flujo: AuditoriaLogger registra la acción; instancia única garantizada en todo el sistema              |

#### 4.3 Nivel C4 de cada interacción

| Interacción                                       | Nivel C4               |
| ------------------------------------------------- | ---------------------- |
| Estudiante -> Sistema de Biblioteca                | Nivel 1 - Contexto     |
| Estudiante -> Aplicación Web -> API Backend         | Nivel 2 - Contenedores |
| ControladorPrestamos -> UseCase -> Builder -> Logger | Nivel 3 - Componentes  |
| AdaptadorCatalogoCETYS -> Catálogo CETYS (SOAP)    | Nivel 3 - Componentes  |
| AdaptadorPagosBancario -> Sistema de Pagos (REST)  | Nivel 3 - Componentes  |

#### 4.4 Decisión arquitectónica

**Decisión:** los adaptadores AdaptadorCatalogoCETYS y AdaptadorPagosBancario son componentes separados, y el use case depende exclusivamente de sus interfaces (CatalogoBiblioteca y ServicioPagos), nunca de las implementaciones concretas.

**Justificación:** combinarlos en un único componente de integración violaría SRP; una misma clase tendría razones distintas para cambiar (cambio de proveedor de catálogo vs cambio del banco). Con adaptadores independientes, cada uno puede evolucionar, reemplazarse o testearse en aislamiento. El use case queda completamente desacoplado de los detalles de infraestructura, lo que es la aplicación directa de la Dependency Rule de Clean Architecture.
