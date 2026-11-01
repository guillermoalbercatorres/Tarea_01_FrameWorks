# Reservas de aulas

Aplicación académica para gestionar aulas, profesores, franjas horarias y reservas. El backend está desarrollado con Java, Spring Boot, Spring Data JPA y MySQL. El frontend, ubicado en `frontend/`, utiliza exclusivamente HTML, CSS y JavaScript sin frameworks.

## Funcionalidades

- Consultar, crear, editar y eliminar aulas, profesores, franjas horarias y reservas.
- Crear reservas asociando un profesor, un aula y una franja horaria ya existentes.
- Mostrar el estado de conexión con la API y mensajes cuando una operación falla.
- Interfaz adaptable a pantallas estrechas.

## Ejecución

1. Inicia MySQL y crea/configura el usuario y la base de datos indicados en `src/main/resources/application.properties`.
2. Revisa `spring.datasource.url`, `spring.datasource.username` y `spring.datasource.password` para que coincidan con tu instalación local.
3. Inicia Spring Boot desde IntelliJ IDEA o ejecuta `./gradlew bootRun`.
4. Abre [http://localhost:8080](http://localhost:8080). Spring sirve el contenido estático desde `frontend/`.

La aplicación necesita MySQL activo en el host y puerto configurados. Si aparece `Communications link failure` o `Conexión rehusada`, Spring no consigue conectar con el servidor de base de datos; comprueba primero que el servicio esté iniciado y después revisa la URL y credenciales. El controlador no provoca ese error. En el archivo actual figura el usuario local `root` con contraseña `1234`; cámbialos para que coincidan con tu equipo y no uses esos valores por defecto en un despliegue real.

## API

| Recurso | Ruta base |
| --- | --- |
| Aulas | `/aula` |
| Profesores | `/profesor` |
| Franjas horarias | `/franja-horaria` |
| Reservas | `/reserva` |

Cada recurso ofrece consulta individual y de colección, creación, actualización completa y borrado. También hay operaciones `PATCH` para modificar campos concretos. Las reservas requieren IDs válidos de profesor, aula y franja horaria.

### Consultas y validación de reservas

- `GET /aula?ordenadores=true` devuelve aulas con ordenadores; `ordenadores=false` devuelve las que no tienen.
- `GET /aula?capacidad=20` devuelve aulas con capacidad estrictamente superior a 20. Los dos filtros se pueden combinar: `/aula?ordenadores=true&capacidad=20`.
- Al crear una reserva, la fecha de hoy está permitida, pero no una fecha anterior. La aplicación devuelve `400 Bad Request` si falta la fecha o es pasada; devuelve `409 Conflict` si el mismo aula ya está reservada para esa fecha y franja.
- El filtro de aulas usa `/aula` como ruta de colección, acorde con las rutas de la API definidas en este proyecto. El ejemplo del enunciado `/aulas/21?ordenadores=true` mezcla un identificador con un filtro de colección; para filtrar se usa `/aula?ordenadores=true`.
- El campo `ordenadores` se ha añadido a la entidad `Aula`; con `spring.jpa.hibernate.ddl-auto=update`, Hibernate actualizará el esquema al iniciar la aplicación. No se ha ejecutado ningún cambio manual ni destructivo en la base de datos.

## Decisiones y problemas encontrados

- Se eligió JavaScript nativo para respetar el requisito de no usar frameworks y evitar dependencias de instalación.
- Se sirve `frontend/` desde Spring Boot para que la interfaz y la API compartan origen y no necesiten configuración CORS adicional.
- Los controladores se basan en inyección por constructor y devuelven `404` cuando un recurso solicitado no existe.
- Las entidades tienen relaciones bidireccionales. Se omiten las colecciones de reservas en la serialización JSON para evitar ciclos al devolverlas desde la API.
- El primer error de ejecución observado fue `Communications link failure` con `Connection refused`: MySQL no estaba aceptando conexiones en `localhost:3306`. La configuración local usa el esquema `reserva-aulas`; hay que confirmar que el nombre, usuario, contraseña y puerto coinciden con la instalación.
- Borrar un profesor, aula o franja que tenga reservas asociadas puede fallar por las claves foráneas. En ese caso hay que eliminar primero las reservas vinculadas.
- La aplicación usa los valores configurados en `application.properties`; no se deben subir credenciales reales al repositorio.

## Uso de IA durante el desarrollo

Prompts utilizados, resumidos a partir de las peticiones de desarrollo:

- «Haz esto para las demás clases» y una muestra de controlador CRUD para `Aula`.
- «Haz esto 4. Frontend… utilizar únicamente HTML, CSS y JavaScript, sin frameworks. 5. Documentación… AGENTS.md y README.md…».

La IA detectó correctamente que la conexión rechazada venía de MySQL y no del controlador. En una primera respuesta atribuyó demasiado peso a `@Autowired` en un campo `final`: con un único constructor Spring puede inyectar la dependencia por constructor, y la anotación era redundante, no la causa del error de conexión. También se revisaron las rutas copiadas de un ejemplo de alumnos y se adaptaron a aulas.

## Estructura

```text
frontend/                 Interfaz HTML, CSS y JavaScript
src/main/java/            Aplicación Spring Boot, entidades, repositorios y API
src/main/resources/       Configuración de la aplicación
AGENTS.md                 Instrucciones de trabajo para agentes de IA
README.md                 Descripción, ejecución y notas de desarrollo
```
