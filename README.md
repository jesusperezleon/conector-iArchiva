# Conector iArchiva

## 📌 Resumen del Enunciado

El objetivo del ejercicio es desarrollar una solución compuesta por:

### 1️⃣ Creación de un servicio web REST

Se implementa un servicio REST con los siguientes endpoints:

| Entrada | Salida |
|----------|----------|
| CIF | Nombre, Email y código interno del proveedor |
| CIF + fechaDesde + fechaHasta | Facturas (número de factura, fecha, importe) del proveedor en ese rango de fechas |

### 2️⃣ Creación de un cliente REST

Se desarrolla un segundo proyecto que actúa como cliente del servicio anterior e incluye:

- Test unitarios con datos correctos
- Test unitarios con datos incorrectos

---

## ⚙️ Stack Tecnológico

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
