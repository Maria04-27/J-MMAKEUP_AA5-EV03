J\&M Makeup



Evidencia GA7-220501096-AA4-EV03

Componente front-end del proyecto formativo y proyectos de clase.



J\&M Makeup es una tienda virtual de productos de maquillaje que permite a los usuarios registrarse, iniciar sesión, consultar productos, agregar productos al carrito de compras, realizar pedidos mediante diferentes métodos de pago y enviar mensajes a través del formulario de contacto.



**Objetivo**

Desarrollar y codificar un módulo web funcional aplicando buenas prácticas de programación, arquitectura organizada y herramientas de versionamiento.



**Módulo codificado en esta evidencia (AA4-EV03)**

* **Módulo de Contacto:** hasta la evidencia anterior, el formulario de contacto.jsp era solo maquetado (el envío se simulaba con una alerta de JavaScript, sin guardar la información). En esta evidencia se programó el módulo completo:
* **ContactoServlet**: recibe y valida los datos del formulario.
* **MensajeDAO**: guarda el mensaje en la base de datos mediante JDBC.
* **Mensaje**: clase modelo del mensaje de contacto.
* **contacto.jsp:** se actualizó para enviar los datos por POST al servlet y mostrar la confirmación real (éxito o error).



**Tecnologías utilizadas**

* Java 17
* JSP
* Servlets
* JDBC
* MySQL
* Maven
* Bootstrap 5
* HTML5
* CSS3
* JavaScript
* Apache Tomcat 10.1.57
* IntelliJ IDEA
* Git
* GitHub



**Arquitectura del proyecto**

El proyecto utiliza una estructura organizada por capas:

**Modelo**

Contiene las clases que representan las entidades principales del sistema:

* Usuario
* Producto
* Categoría
* Carrito
* Pedido
* Mensaje



**DAO**

Contiene las clases encargadas de realizar las operaciones de acceso a la base de datos:

* UsuarioDAO
* ProductoDAO
* CarritoDAO
* PedidoDAO
* MensajeDAO



**Controladores**

Contiene los Servlets encargados de procesar las solicitudes del usuario:

* LoginServlet
* RegistroServlet
* ProductoServlet
* CarritoServlet
* PedidoServlet
* ContactoServlet



**Conexión**

Contiene la clase encargada de establecer la conexión entre la aplicación y la base de datos MySQL:

* Conexion.java



**Vistas**

Las interfaces del sistema se desarrollaron utilizando JSP:

* Inicio
* Inicio de sesión
* Registro
* Productos
* Carrito de compras
* Pago
* Ofertas
* Contacto



**Funcionalidades**

El sistema contempla las siguientes funcionalidades:

* Visualización de la página principal.
* Registro de usuarios.
* Inicio de sesión.
* Consulta de productos.
* Búsqueda de productos.
* Agregar productos al carrito.
* Actualizar cantidades del carrito.
* Eliminar productos del carrito.
* Vaciar el carrito.
* Visualizar el resumen de compra.
* Seleccionar un método de pago.
* Realizar un pedido.
* Enviar un mensaje desde el formulario de contacto.



**Base de datos**

El proyecto utiliza una base de datos MySQL para almacenar la información de los usuarios, productos, categorías, carritos, pedidos y mensajes de contacto.

La conexión a la base de datos se realiza mediante JDBC y el controlador MySQL Connector/J.



**Requisitos para ejecutar el proyecto**

Para ejecutar el proyecto se requiere:

* Java JDK 17.
* IntelliJ IDEA.
* Apache Tomcat 10.1.57.
* MySQL Server.
* MySQL Workbench.
* Maven.
* Git.



**Autor**

**Nombre del aprendiz**: Maria Carolina Ruiz Aristizabal

**Evidencia**:GA7-220501096-AA4-EV03

**Proyecto**: J\&M Makeup

**Año**:2026

**---**

**Evidencia GA7-220501096-AA5-EV03 — Diseño y desarrollo de servicios web**

En esta evidencia se diseñó y codificó una API REST (paquete api) que expone en formato JSON la misma lógica de negocio del proyecto, reutilizando los modelos y DAO ya existentes. Se agregaron seis servicios web:

/api/productos — catálogo de productos (CRUD)

/api/categorias— categorías de productos (CRUD; se agregó CategoriaDAO)

/api/usuarios— registro, login, logout y perfil

/api/carrito— carrito de compras del usuario autenticado

/api/pedido — generación y consulta de pedidos

/api/mensajes— mensajes del formulario de contacto

La documentación detallada de cada servicio (método, ruta, autenticación, cuerpo de petición, respuesta y códigos de estado) está en el archivo DOCUMENTACION\_API.docx incluido en esta entrega.

