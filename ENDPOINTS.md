ENDPOINTS 
 API REST J&M Makeup
 Evidencia GA7-220501096-AA5-EV04

URL BASE (local):
    http://localhost:8080/JMMakeup_war_exploded/

--------------------------------------------------------------------
 1. USUARIOS (autenticación)                    /api/usuarios
--------------------------------------------------------------------
POST    /api/usuarios/registro     Registrar un nuevo usuario
POST    /api/usuarios/login        Iniciar sesión (crea la sesión)
POST    /api/usuarios/logout       Cerrar sesión
GET     /api/usuarios/perfil       Consultar el usuario autenticado

--------------------------------------------------------------------
 2. PRODUCTOS                                   /api/productos
--------------------------------------------------------------------
GET     /api/productos              Listar todos los productos
GET     /api/productos/ofertas      Listar productos en oferta
GET     /api/productos/{id}         Consultar un producto por id
POST    /api/productos              Registrar un producto        (admin)
PUT     /api/productos/{id}         Actualizar un producto        (admin)
DELETE  /api/productos/{id}         Eliminar un producto          (admin)

--------------------------------------------------------------------
 3. CATEGORÍAS                                  /api/categorias
--------------------------------------------------------------------
GET     /api/categorias             Listar todas las categorías
GET     /api/categorias/{id}        Consultar una categoría por id
POST    /api/categorias             Registrar una categoría       (admin)
PUT     /api/categorias/{id}        Actualizar una categoría      (admin)
DELETE  /api/categorias/{id}        Eliminar una categoría        (admin)

--------------------------------------------------------------------
 4. CARRITO DE COMPRAS                          /api/carrito
--------------------------------------------------------------------
GET     /api/carrito                Consultar el carrito activo   (sesión)
POST    /api/carrito                Agregar un producto           (sesión)
PUT     /api/carrito/{idProducto}   Actualizar cantidad            (sesión)
DELETE  /api/carrito/{idProducto}   Eliminar un producto           (sesión)
DELETE  /api/carrito                Vaciar el carrito completo     (sesión)

--------------------------------------------------------------------
 5. PEDIDOS                                     /api/pedidos
--------------------------------------------------------------------
GET     /api/pedidos                Listar mis pedidos             (sesión)
GET     /api/pedidos/{id}           Consultar un pedido específico (sesión)
POST    /api/pedidos                Crear pedido desde el carrito  (sesión)
PUT     /api/pedidos/{id}           Actualizar el estado           (admin)

--------------------------------------------------------------------
 6. MENSAJES DE CONTACTO                        /api/mensajes
--------------------------------------------------------------------
POST    /api/mensajes               Enviar un mensaje de contacto
GET     /api/mensajes               Listar mensajes recibidos     (admin)

====================================================================
 Total: 22 endpoints, agrupados en 6 servicios (recursos) REST.
 Formato de respuesta estándar de todos los servicios:

    {
      "exito": true,
      "mensaje": "texto descriptivo",
      "datos": { ... }
    }


