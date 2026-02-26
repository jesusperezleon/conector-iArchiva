# Conector iArchiva

## 📌 Índice
1. [Resumen del Enunciado](#resumen-del-enunciado)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Instrucciones de Despliegue](#instrucciones-de-despliegue)
4. [Arquitectura del Sistema](#arquitectura-del-sistema)
5. [Modelo de Datos: Entidades y Relaciones](#modelo-de-datos-entidades-y-relaciones)
6. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
7. [Detalle de Endpoints (Servicio REST)](#detalle-de-endpoints-servicio-rest)
8. [Pruebas y Cliente REST](#pruebas-y-cliente-rest)

---

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

## Instrucciones de Despliegue

### Requisitos previos
- Java 1.8 instalado
- No es necesario instalar Gradle (se usa Gradle Wrapper incluido)

### Clonar el proyecto
```bash
git clone <url>
cd conector-iArchiva
```

### Compilar el proyecto
Para compilar el proyecto se debe ejecutar el siguiente comando en la terminal desde la raíz del proyecto.
#### Linux / macOS
```bash
./gradlew build
```

#### Windows (CMD o PowerShell)
```bash
gradlew.bat build
```

### Levantar el Servicio Web
Para ejecutar el servicio **(Servicio-Web)**, se debe ejecutar el siguiente comando en la terminal desde la raíz del proyecto.
#### Linux / macOS
```bash
./gradlew bootRun
```

#### Windows (CMD o PowerShell)
```bash
gradlew.bat bootRun
```

El servicio quedará disponible en:

```bash
http://localhost:8080
```

### Base de datos

La base de datos es H2 en memoria, por lo que no requiere instalación adicional.

Para acceder a la consola H2:

1. Levanta el Servicio-Web
2. Accede a: http://localhost:8080/h2-console

| Campo      | Valor                  |
|------------|------------------------|
| JDBC URL   | jdbc:h2:file:./data/mydb    |
| Usuario    | sa                     |
| Contraseña | (vacía)                |

---

## Arquitectura del Sistema

El proyecto sigue un patrón de **Arquitectura por Capas** (Layered Architecture), asegurando una separación clara de responsabilidades y facilitando el mantenimiento y la escalabilidad del conector.

### Capas del Proyecto:

1.  **Capa de Presentación (Controllers):**
    * Expone los endpoints REST.
    * Gestiona las peticiones HTTP y valida los parámetros de entrada.
2.  **Capa de Negocio (Services):**
    * Contiene la lógica principal del conector.
    * Transforma las Entidades en **DTOs** para asegurar que solo se expone la información necesaria.
3.  **Capa de Persistencia (Repositories):**
    * Utiliza **Spring Data JPA** para la comunicación con la base de datos.
    * Implementa consultas derivadas para filtrar por el CIF del proveedor y el rango temporal.
4.  **Capa de Modelo (Entities/DTOs):**
    * **Entities:** Mapeo de tablas mediante Hibernate (Proveedor y Factura).
    * **DTOs:** Objetos de transferencia para desacoplar la API de la estructura interna.

---

## Modelo de Datos: Entidades y Relaciones

La persistencia se gestiona mediante una base de datos **H2** en memoria, ideal para entornos de prueba y desarrollo rápido.

* **Proveedor:** Entidad principal identificada por su **Código de Proveedor**. 
    * Atributos: `codProveedor`, `CIF`, `nombre` y `email`.
* **Factura:** Entidad que identica una factura mediante **codFactura** y declara una relación con Proveedor.
    * Atributos: `codFactura`, `proveedor`, `fecha` e `importe`.
    * La relación con Proveedor se establece mediante `@ManyToOne` y la columna `codProveedor` como Foreign Key.

---

## Data Transfer Objects (DTOs)

Se ha implementado un DTO para evitar la exposición directa de las entidades JPA y prevenir problemas de recursividad en las relaciones:
* **`FacturaDTO`**: Devuelve la información formateada de las facturas (número, fecha e importe).

---

## Detalle de Endpoints (Servicio REST)

### 1. Consulta de Proveedor
* **URL:** `GET /proveedores/{cif}`
* **Descripción:** Recupera los datos básicos de un proveedor.
* **Salida:** JSON con `nombre`, `email`, `cif` y `codigoInterno`.

### 2. Consulta de Facturas por Fechas
* **URL:** `GET /facturas`
* **Parámetros:**
    * `cif` - **Obligatorio**.
    * `fechaDesde` - Opcional. El formato debe ser YYYY-MM-DD.
    * `fechaHasta` - Opcional. El formato debe ser YYYY-MM-DD.
* **Lógica:** Según los parámetros que se introduzcan se devuelven distintos resultados:
  
    | Parámetros Enviados | Resultado de la Consulta |
    | :--- | :--- |
    | **Solo CIF** | Devuelve todas las facturas históricas del proveedor. |
    | **CIF + FechaDesde** | Devuelve las facturas desde esa fecha hasta hoy. |
    | **CIF + FechaHasta** | Devuelve todas las facturas anteriores a esa fecha. |
    | **CIF + FechaDesde + FechaHasta** | Devuelve las facturas en el rango exacto solicitado. |

---

## Pruebas y Cliente REST

Se incluye un segundo proyecto que actúa como **Cliente REST** para validar la integración.

### Pruebas realizadas:

### 1. Tests Unitarios (Service Layer con Mockito)
Estas pruebas validan la lógica del cliente REST de forma aislada, simulando las respuestas del servidor.

#### **Servicio de Proveedores (`ProveedorServiceTest`)**
* **Test 1.** CIF válido y existente: el mock devuelve el proveedor esperado.
* **Test 2.** CIF inexistente: el mock lanza 404 Not Found.
* **Test 3.** CIF con formato inválido: el mock lanza 400 Bad Request.
* **Test 4.** CIF vacío: el servicio lanza excepción antes de llamar a la API.

#### **Servicio de Facturas (`FacturaServiceTest`)**
* **Test 1.** CIF válido sin fechas: retorna facturas del proveedor.
* **Test 2.** CIF inexistente: lanza 404 Not Found.
* **Test 3.** CIF con formato inválido: lanza 400 Bad Request.
* **Test 4.** CIF vacío: lanza IllegalArgumentException sin llamar al servidor.
* **Test 5.** CIF nulo: lanza IllegalArgumentException sin llamar al servidor.
* **Test 6.** CIF válido con ambas fechas correctas: retorna facturas en el rango.
* **Test 7.** Solo fechaDesde: retorna facturas desde esa fecha.
* **Test 8.** Solo fechaHasta: retorna facturas hasta esa fecha.
* **Test 9.** Formato de fechaDesde inválido: lanza IllegalArgumentException sin llamar al servidor.
* **Test 10.** Formato de fechaHasta inválido: lanza IllegalArgumentException sin llamar al servidor.
* **Test 11.** fechaDesde posterior a fechaHasta: lanza IllegalArgumentException sin llamar al servidor.

### Ejecución de los tests unitarios:
Para ejecutar los test unitarios desde la terminal, abra el proyecto `cliente-rest` y desde la raíz ejecute el siguiente comando:
```bash
./gradlew test
```

---

### 2. Tests de Integración (`@SpringBootTest`)
Estas pruebas validan la comunicación real entre el cliente y el servidor, verificando el flujo completo de datos.

#### **Integración de Proveedores (`ProveedorServiceIT`)**
* **Test 1.** CIF Válido y existente: Retorna datos del proveedor.
* **Test 2.** CIF Válido pero inexistente: Retorna 404 Not Found.
* **Test 3.** CIF con formato inválido: Retorna 400 Bad Request.
* **Test 4.** CIF vacío: Retorna 404 por ruta inexistente (validación local).

#### **Integración de Facturas (`FacturaServiceIT`)**
* **Test 1.** CIF válido sin fechas: retorna facturas.
* **Test 2.** CIF inexistente: retorna 404.
* **Test 3.** CIF con formato inválido: retorna 400.
* **Test 4.** CIF vacío: lanza IllegalArgumentException.
* **Test 5.** CIF válido con ambas fechas correctas: retorna facturas.
* **Test 6.** Solo fechaDesde: retorna facturas desde esa fecha.
* **Test 7.** Solo fechaHasta: retorna facturas hasta esa fecha.
* **Test 8.** Formato fechaDesde inválido: lanza IllegalArgumentException.
* **Test 9.** Formato fechaHasta inválido: lanza IllegalArgumentException.
* **Test 10.** fechaDesde posterior a fechaHasta: lanza IllegalArgumentException.

### Ejecución de los tests de integración:
Para ejecutar los test de integración desde la terminal, primero de levantar el servidor ya que el cliente intentará comunicarse con el. Después y de igual manera, abra la raíz del proyecto y ejecute el siguiente comando:
```bash
./gradlew integrationTest
```
> [!WARNING]
> El servidor debe de estar levantado para realizar las pruebas de integración
---

Para ejecutar ambos test de forma simultanea, ejecute el siguiente comando:
```bash
./gradlew test integrationTest
```

Para ver los resultados de ambos test, ejecute desde la terminal los dos siguientes comandos para ver en formato web los resultado de los test unitarios y de integración respectivamente:
```bash
xdg-open build/reports/tests/test/index.html
xdg-open build/reports/tests/integrationTest/index.html
```
