# Conector iArchiva

## Resumen del Enunciado

El objetivo del ejercicio es desarrollar una solución compuesta por:

### Creación de un servicio web REST

Se implementa un servicio REST con los siguientes endpoints:

| Entrada | Salida |
|----------|----------|
| CIF | Nombre, Email y código interno del proveedor |
| CIF + fechaDesde + fechaHasta | Facturas (número de factura, fecha, importe) del proveedor en ese rango de fechas |

### Creación de un cliente REST

Se desarrolla un segundo proyecto que actúa como cliente del servicio anterior e incluye:

- Test unitarios con datos correctos
- Test unitarios con datos incorrectos

---

## Stack Tecnológico

- Java 1.8
- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA
- H2 Database
- Gradle
- JUnit 5
- Lombok

Se garantiza compatibilidad explícita con Java 1.8 mediante:

```gradle
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
```

---

## Arquitectura del Sistema

El proyecto sigue un patrón de **Arquitectura por Capas** (Layered Architecture), asegurando una separación clara de responsabilidades y facilitando el mantenimiento y la escalabilidad del conector.

### Capas del Proyecto:

1.  **Capa de Presentación (Controllers):**
    * Expone los endpoints REST.
    * Gestiona las peticiones HTTP y valida los parámetros de entrada.
3.  **Capa de Negocio (Services):**
    * Contiene la lógica principal del conector.
    * Transforma las Entidades en **DTOs** para asegurar que solo se expone la información necesaria.
5.  **Capa de Persistencia (Repositories):**
    * Utiliza **Spring Data JPA** para la comunicación con la base de datos.
    * Implementa consultas derivadas para filtrar por el CIF del proveedor y el rango temporal.
7.  **Capa de Modelo (Entities/DTOs):**
    * **Entities:** Mapeo de tablas mediante Hibernate (Proveedor y Factura).
    * **DTOs:** Objetos de transferencia para desacoplar la API de la estructura interna.

---

## Modelo de Datos: Entidades y Relaciones

La persistencia se gestiona mediante una base de datos **H2** en memoria, ideal para entornos de prueba y desarrollo rápido.

* **Proveedor:** Entidad principal identificada por su **Codigo de Proveedor**. 
    * Atributos: `nombre`, `email`, `codigoInterno` y `CIF`.
* **Factura:** Entidad relacionada con un proveedor mediante una relación `@ManyToOne`.
    * Atributos: `numeroFactura`, `fecha`, `importe` y `cod_Proveedor` que actua de `Foreing Key` para establecer la relación con una relación muchos a uno con la entidad `Proveeedor`.

---

## Data Transfer Objects (DTOs)

Se ha implementado un DTO para evitar la exposición directa de las entidades JPA y prevenir problemas de recursividad en las relaciones:
* **`FacturaDTO`**: Devuelve la información formateada de las facturas (número, fecha e importe).

---

## Detalle de Endpoints (Servicio REST)

### 1. Consulta de Proveedor
* **URL:** `GET /proveedores/{cif}`
* **Descripción:** Recupera los datos básicos de un proveedor.
* **Salida:** JSON con `nombre`, `email`, `CIF` y `codigoInterno`.

### 2. Consulta de Facturas por Rango
* **URL:** `GET /facturas`
* **Parámetros:**
    * `cif` - **Obligatorio**.
    * `fechaDesde` - Opcional.
    * `fechaHasta` - Opcional.
* **Lógica:** Segun los paremetros que se introduzcan se devuelven distintos resultados:
  
    | Parámetros Enviados | Resultado de la Consulta |
    | :--- | :--- |
    | **Solo CIF** | Devuelve todas las facturas históricas del proveedor. |
    | **CIF + fechaDesde** | Devuelve las facturas desde esa fecha hasta hoy. |
    | **CIF + fechaHasta** | Devuelve todas las facturas anteriores a esa fecha. |
    | **CIF + Desde + Hasta** | Devuelve las facturas en el rango exacto solicitado. |

---

## Pruebas y Cliente REST

Se incluye un segundo proyecto que actúa como **Cliente REST** para validar la integración.

### Pruebas realizadas:
* **Unitarias (Datos Correctos):** Verificación de flujo positivo, recuperación de proveedores existentes y filtrado correcto de facturas en rangos válidos.
* **Unitarias (Datos Incorrectos):** Validación de comportamiento ante CIFs inexistentes, formatos de fecha erróneos o parámetros obligatorios ausentes.

### Ejecución de los tests:
Para ejecutar la suite de pruebas desde la terminal:
```bash
./gradlew test
