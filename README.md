# 🏹 ACTS — Arrow Command & Tactical System

> *"Esto no es solo código. Es lo que nos da ventaja."* — Felicity Smoak

Sistema de comando táctico del equipo Arrow, diseñado para identificar, clasificar y priorizar amenazas **Mirakuru** en tiempo real. Construido con arquitectura **Hexagonal (Ports & Adapters)** + **Domain-Driven Design** + principios **SOLID**.

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Arquitectura](#-arquitectura)
- [Stack Tecnológico](#-stack-tecnológico)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Persistencia](#-persistencia)
- [API REST](#-api-rest)
- [Cómo ejecutar](#-cómo-ejecutar)
- [Ejemplos de uso](#-ejemplos-de-uso)

---

## 📖 Descripción

El sistema ACTS permite al equipo Arrow:

- **Registrar y clasificar** individuos bajo el efecto del suero Mirakuru
- **Priorizar amenazas** en tiempo real según concentración y comportamiento
- **Planificar y ejecutar misiones** tácticas con validación de invariantes de dominio
- **Procesar inteligencia** de múltiples fuentes externas (ARGUS, cámaras, informantes)

---

## 🏛️ Arquitectura

El proyecto implementa **Arquitectura Hexagonal (Ports & Adapters)**:

```
┌─────────────────────────────────────────────────────────┐
│                    DOMINIO (núcleo)                      │
│                                                          │
│   Models ──► Domain Services ──► Ports (interfaces)     │
│   Enums       ThreatClassification    in / out           │
│   Exceptions  MissionCoordinator                        │
│               ThreatPrioritization                       │
│               IntelligenceAggregator                    │
└───────────────────────┬─────────────────────────────────┘
                        │ implementado por
┌───────────────────────▼─────────────────────────────────┐
│                  APLICACIÓN (adaptadores)               │
│                                                          │
│  Use Cases ──► REST Controllers ──► Persistence Adapters │
│  (orquestación)  SubjectController    MySQL (JPA)        │
│                  MissionController    MongoDB            │
│                  ThreatController                        │
└─────────────────────────────────────────────────────────┘
```

### Principios SOLID aplicados

| Principio | Aplicación |
|-----------|-----------|
| **S** — SRP | Cada servicio de dominio tiene una única responsabilidad |
| **O** — OCP | Los adaptadores pueden reemplazarse sin modificar el dominio |
| **L** — LSP | Todos los adaptadores son intercambiables con sus puertos |
| **I** — ISP | Puertos separados por capacidad (SubjectPort, MissionPort, etc.) |
| **D** — DIP | Los use cases dependen de interfaces (puertos), no de implementaciones |

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Uso |
|------------|---------|-----|
| **Java** | 17 | Lenguaje principal |
| **Spring Boot** | 3.2.5 | Framework de aplicación |
| **Spring Data JPA** | — | Persistencia relacional |
| **MySQL** | 8.x | Base de datos principal |
| **Spring Data MongoDB** | — | Persistencia documental |
| **MongoDB** | 7.x | Base de datos de inteligencia |
| **Lombok** | — | Reducción de boilerplate |
| **Jakarta Validation** | — | Validación de DTOs |

---

## 📁 Estructura del Proyecto

```
src/main/java/com/arrow/acts/
│
├── ActsApplication.java                        ← Punto de entrada
│
├── domain/                                     ← NÚCLEO (sin dependencias externas)
│   ├── models/
│   │   ├── enums/                              ← ThreatLevel, BehavioralStatus...
│   │   ├── subject/MirakuruSubject.java        ← Agregado raíz
│   │   ├── mission/Mission.java                ← Agregado raíz
│   │   ├── team/TeamMember.java                ← Agregado raíz
│   │   └── intelligence/IntelligenceReport.java
│   ├── exceptions/                             ← Excepciones del dominio
│   ├── ports/
│   │   ├── in/                                 ← Casos de uso (interfaces)
│   │   └── out/                                ← Persistencia y notificaciones (interfaces)
│   └── services/                               ← Lógica de negocio pura
│       ├── ThreatClassificationService.java
│       ├── MissionCoordinatorService.java
│       ├── ThreatPrioritizationService.java
│       └── IntelligenceAggregatorService.java
│
└── application/                                ← ADAPTADORES
    ├── usecases/                               ← Implementaciones de casos de uso
    └── adapters/
        ├── api/
        │   ├── controllers/                    ← REST Controllers
        │   ├── request/                        ← DTOs de entrada (@Getter @Setter + Validation)
        │   └── response/                       ← DTOs de salida (Java records)
        ├── persistence/
        │   ├── sql/                            ← Entidades JPA + Repositorios + Adaptadores MySQL
        │   └── mongodb/                        ← Documentos + Repositorios + Adaptadores MongoDB
        └── notifications/                      ← Adaptador de alertas (ConsoleAlertAdapter)
```

---

## 🗄️ Persistencia

### MySQL — Base de datos `felicity_smoak`

| Tabla | Entidad de dominio |
|-------|-------------------|
| `mirakuru_subjects` | `MirakuruSubject` |
| `missions` | `Mission` |
| `team_members` | `TeamMember` |

### MongoDB — Base de datos `acts_intelligence`

| Colección | Entidad de dominio |
|-----------|--------------------|
| `intelligence_reports` | `IntelligenceReport` |

> Las tablas MySQL se crean automáticamente con `spring.jpa.hibernate.ddl-auto=update`.

---

## 🌐 API REST

### Sujetos Mirakuru — `/api/subjects`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/subjects` | Registrar nuevo sujeto |
| `GET` | `/api/subjects` | Listar todos los sujetos |
| `GET` | `/api/subjects/{id}` | Obtener sujeto por ID |
| `PUT` | `/api/subjects/{id}` | Actualizar sujeto |
| `DELETE` | `/api/subjects/{id}` | Eliminar sujeto |

### Análisis de Amenazas — `/api/threats`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/threats` | Lista priorizada (CRITICAL → LOW) |
| `GET` | `/api/threats/critical` | Solo amenazas CRITICAL y HIGH |
| `POST` | `/api/threats/{id}/reclassify` | Reclasificar amenaza |

### Misiones Tácticas — `/api/missions`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/missions` | Crear nueva misión |
| `GET` | `/api/missions` | Listar todas las misiones |
| `GET` | `/api/missions/{id}` | Obtener misión por ID |
| `POST` | `/api/missions/{id}/activate` | Activar misión |
| `POST` | `/api/missions/{id}/agents/{agentId}` | Asignar agente |
| `POST` | `/api/missions/{id}/complete` | Completar misión |
| `POST` | `/api/missions/{id}/abort` | Abortar misión |

### Reglas de negocio del dominio

- Una misión no puede activarse **sin objetivos** definidos
- Una misión `CRITICAL` requiere al menos un agente `FIELD_AGENT` asignado
- La concentración de Mirakuru debe estar en el rango **[0.0, 10.0]**
- No se permiten **alias duplicados** para sujetos

---

## 🚀 Cómo ejecutar

### Requisitos

- Java 17+
- MySQL 8.x corriendo en `localhost:3306`
- MongoDB corriendo en `localhost:27017`

### Configuración

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/felicity_smoak?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA

spring.data.mongodb.uri=mongodb://localhost:27017/acts_intelligence
```

### Ejecutar

```bash
# Con Maven instalado
mvn spring-boot:run

# Con NetBeans (Maven embebido)
"C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" spring-boot:run
```

El sistema levanta en: **http://localhost:8080**

---

## 📡 Ejemplos de uso

### Registrar un sujeto Mirakuru

```bash
curl -X POST http://localhost:8080/api/subjects \
  -H "Content-Type: application/json" \
  -d '{
    "alias": "Slade Wilson",
    "mirakuruConcentration": 9.5,
    "lastKnownLocation": "The Glades - Sector 4",
    "behavioralStatus": "BERSERK",
    "lastSightingSource": "ARGUS-CAM-07"
  }'
```

### Ver amenazas priorizadas

```bash
curl http://localhost:8080/api/threats
```

### Crear una misión

```bash
curl -X POST http://localhost:8080/api/missions \
  -H "Content-Type: application/json" \
  -d '{
    "objective": "Neutralizar amenaza en The Glades",
    "targetSubjectIds": [1],
    "priority": "CRITICAL",
    "operationalZone": "The Glades",
    "scheduledStart": "2024-12-01T22:00:00Z"
  }'
```

---

## 👥 Equipo

| Rol | Descripción |
|-----|-------------|
| `FIELD_AGENT` | Agentes operativos en campo (Arrow, Canary, Arsenal) |
| `TECH_SUPPORT` | Soporte técnico e inteligencia (Felicity Smoak) |
| `ANALYST` | Análisis estratégico y de datos |

---

*Sistema desarrollado para el equipo Arrow. Uso táctico confidencial.*
