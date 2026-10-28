# Instrucciones para agentes

- Mantén el frontend en `frontend/` y usa únicamente HTML, CSS y JavaScript sin frameworks ni gestores de paquetes.
- La API REST de Spring Boot usa las rutas `/aula`, `/profesor`, `/franja-horaria` y `/reserva`.
- Conserva los nombres de propiedades definidos por las entidades Java (por ejemplo, `horaInicio`, `fechaInsercion` y `franjaHoraria`).
- No incluyas contraseñas ni otros secretos en el repositorio. La configuración local de MySQL se encuentra en `src/main/resources/application.properties`.
- Explica en `README.md` cualquier limitación conocida, decisión de diseño o cambio importante generado con ayuda de IA.
- No añadas dependencias de frontend ni ejecutes cambios destructivos sobre la base de datos.
