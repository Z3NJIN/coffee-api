# ☕ Coffee APP

API REST para registrar recetas de café de especialidad, llevar una bitácora de preparaciones con notas de cata, y ver la evolución de una receta en el tiempo.

Proyecto de portafolio construido para practicar y demostrar un stack backend en Java, a partir de un problema real: llevo años anotando mis recetas de café (V60, aeropress, prensa francesa, moka pot) en notas sueltas — este proyecto reemplaza esas notas con algo estructurado.

## Funcionalidades

- **Registro de recetas por método** (V60, aeropress, prensa francesa, moka pot), con cálculo automático de ratio café:agua
- **Bitácora de preparaciones**, con notas de cata (acidez, dulzor, cuerpo, tags de sabor) y rating
- **Evolución de una receta en el tiempo**, para ver cómo mejora con ajustes sucesivos
- **Sugerencia de ajustes** basada en reglas simples a partir de las notas de cata (ej. "salió amargo" → sugiere molienda más gruesa)

## Stack técnico

| Capa | Tecnología |
|---|---|
| Backend | Java 21, Spring Boot 4.1 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | H2 (desarrollo) / PostgreSQL (producción) |
| Validación | Jakarta Bean Validation |
| Documentación de API | Springdoc OpenAPI (Swagger) |
| Testing | JUnit 5 + Mockito |
| Build | Gradle |

## Cómo correr el proyecto localmente

Requiere JDK 21.

```bash
git clone https://github.com/tu-usuario/coffee-api.git
cd coffee-api
./gradlew bootRun
```

Por defecto corre con el perfil `dev`, que usa una base de datos H2 en memoria — no necesitas instalar ni configurar nada más para empezar a probar.

- API disponible en: `http://localhost:8080`
- Consola de H2: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:coffeeapp`, usuario `sa`, sin contraseña)
- Documentación interactiva (Swagger): `http://localhost:8080/swagger-ui.html`

### Con PostgreSQL local (opcional)

Si prefieres probar contra Postgres en vez de H2:

```bash
docker compose up -d
```

Y ajusta el datasource en `application-dev.yml` según las instrucciones comentadas en ese mismo archivo.

## Probar la API

Se incluye una colección de Postman (`coffee-app.postman_collection.json`) con los 11 endpoints listos para importar, incluyendo variables `baseUrl`, `recipeId` y `brewId`.

### Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/recipes` | Crear receta |
| GET | `/api/recipes` | Listar recetas (filtro opcional `?method=`) |
| GET | `/api/recipes/{id}` | Obtener receta por id |
| PUT | `/api/recipes/{id}` | Actualizar receta |
| DELETE | `/api/recipes/{id}` | Eliminar receta |
| POST | `/api/brews` | Registrar preparación |
| GET | `/api/brews/{id}` | Obtener preparación por id |
| GET | `/api/recipes/{recipeId}/brews` | Bitácora de una receta |
| GET | `/api/recipes/{recipeId}/brews/evolution` | Historial cronológico |
| GET | `/api/recipes/{recipeId}/brews/average-rating` | Rating promedio de una receta |

## Estructura del proyecto

```
src/main/java/dev/brewlog/coffee
├── controller/     # Endpoints REST
├── dto/            # Contratos de entrada/salida (records)
├── model/          # Entidades JPA
├── repository/     # Spring Data JPA
├── service/        # Lógica de negocio
├── mapper/         # Conversión entidad ↔ DTO
└── exception/      # Manejo centralizado de errores
```

## Tests

```bash
./gradlew test
```

Cobertura actual: services (lógica de negocio, incluida la regla de sugerencia de ajustes) y controllers (códigos de estado HTTP, validación, manejo de errores), con JUnit 5 + Mockito.

## Próximos pasos

- [ ] Conectar frontend en React
- [ ] Agregar Testcontainers para tests de integración contra Postgres real
- [ ] Autenticación con Spring Security + JWT (modo compartir receta con link público)
- [ ] Filtro por origen del grano (finca, tueste)

## Autor

Marlon Vera — [LinkedIn](#) · [GitHub](#)
