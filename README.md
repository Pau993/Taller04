# Desarrollo de marcos web para servicios REST y gestión de archivos estáticos

En este laboratorio exploraremos el desarrollo de frameworks web para servicios REST. Para ello, utilizaremos los recursos obtenidos en el Taller 01, integrándolos a los requisitos principales de este nuevo proyecto.

Este framework incluirá herramientas que permitirán definir servicios REST mediante funciones Lambda, gestionar valores en las consultas (Query Parameters) y especificar la ubicación de archivos estáticos.

Este proyecto nos ayudará a comprender los fundamentos del desarrollo de frameworks web para servicios REST, permitiéndonos:

* Aplicar los conceptos del Taller 01 para construir una solución más robusta.
* Explorar el uso de funciones Lambda en la definición de servicios REST.
* Manejar parámetros de consulta (Query Parameters) para personalizar las respuestas del servicio.
* Especificar la ubicación de archivos estáticos, facilitando el acceso a recursos como imágenes, scripts y hojas de estilo.

## Descripción de la aplicación 📖

La aplicación es un microframework en Java que configura y ejecuta un servidor HTTP simple. Este microframework proporciona una forma sencilla de configurar y ejecutar un servidor HTTP con rutas básicas y soporte para archivos estáticos.

## Diagrama de Arquitectura

* Usuario (User):

Es quien realiza solicitudes HTTP a través de un navegador web.
* Navegador (Browser):

Actúa como intermediario entre el usuario y el servidor HTTP.
Realiza solicitudes HTTP al servidor en busca de recursos como archivos HTML, JavaScript, CSS o imágenes.
* Servidor HTTP (HttpServer):

Es el servidor que recibe y procesa las solicitudes HTTP enviadas por el navegador.
Se encuentra dentro de un "grupo genérico", lo que indica que puede formar parte de una infraestructura más amplia.

El navegador envía varias solicitudes HTTP al servidor en el puerto 35000 para diferentes rutas:

* /script.js: Solicitud para obtener un archivo de JavaScript.
* /index.html: Solicitud para cargar el archivo principal de la página web.
* /estilos.css: Solicitud para cargar el archivo de estilos CSS.
* /Imagen/Chill.jpg: Solicitud para obtener una imagen ubicada en una ruta específica.
* Recursos (Archivos estáticos): Almacenados en el servidor, servidos a través de rutas específicas.
* Comunicación: Protocolo HTTP entre el cliente y el servidor.

El servidor procesa estas solicitudes y responde con los recursos correspondientes desde su sistema de archivos.

* Servidor HTTP: Clase HttpServer que maneja solicitudes HTTP.
* Manejo de rutas: Clase Route para mapear rutas específicas a manejadores.
* Clases de soporte:
* Request y Response para manejar y estructurar las solicitudes y respuestas.
* Utils con funciones auxiliares
* Archivos estáticos: Recursos en la carpeta resources/Files (HTML, CSS, imágenes, etc.).
* API REST: Endpoints definidos en HttpServer para manejar /api/saludo, /api/fecha, etc.
* Pruebas: Clases de prueba con JUnit para validar el comportamiento del servidor.

![image](https://github.com/user-attachments/assets/ee3336ae-1ca3-40f2-883f-e43ccb439cce)


## Diagrama de Clase

Este diagrama de clases representa la arquitectura de un microframework para servicios REST, dividiendo la funcionalidad en varias clases e interfaces.

Las clases principales (Request, Response, HttpServer) manejan las solicitudes, respuestas y la lógica del servidor, mientras que las interfaces (Route) definen cómo implementar rutas personalizadas.

![image](https://github.com/user-attachments/assets/26319417-3811-4ad4-8b30-28ff1de7ccc5)


## Comenzando 🚀

Las siguientes instrucciones le permitirán obtener una copia del proyecto en funcionamiento en su máquina local para fines de desarrollo y prueba.

### Tecnologías usadas ⚙️

* [Maven](https://maven.apache.org/) : Gestor de dependencias y automatización de construcción para Java.
* [JavaScript](https://nodejs.org/) : Lenguaje de programación para interactividad en la web.
* [Java](https://www.java.com/es/) : Lenguaje de programación robusto para backend y aplicaciones empresariales.

```
* Versión Maven: 3.9.9
* Versión Java: 21
```

### Instalación 📦

Realice los siguientes pasos para clonar el proyecto en su máquina local.

```
git clone https://github.com/Pau993/Taller02.git
cd Taller02
git checkout Taller02
mvn clean compile
```

### Ejecutando la aplicación ⚙️

Para ejecutar la aplicación, ejecute el siguiente comando:

```
mvn exec:java -Dexec.mainClass="com.example.HttpServer"

```

El anterior comando limpiará las contrucciones previas, compilará y empaquetará el código en un jar y luego ejecutará la aplicación.

Diríjase a su navegador de preferencia y vaya a la siguiente dirección: http://localhost:35000/ para ver la aplicación en funcionamiento.

## Ejecutando las pruebas ⚙️

Para ejecutar las pruebas, ejecute el siguiente comando:

Las pruebas realizadas en este proyecto se enfocan en la validación y verificación de requisitos relacionados con el proceso de gestión de solicitudes, asegurando su correcto funcionamiento y cumplimiento de especificaciones.

```
mvn test
```
![image](https://github.com/user-attachments/assets/d51f5c64-3d0b-4b87-bec3-d6c058bc4675)


## Descripción de las pruebas

* testHandleApiRequestSaludo 🛠️

Verifica que la solicitud a la ruta /api/saludo responde con HTTP 200 OK y contiene el mensaje JSON esperado.
* testHandleApiRequestFecha 📅

Valida que la solicitud a /api/fecha devuelve HTTP 200 OK y contiene una clave "fecha" en la respuesta.
* testHandleApiRequestNotFound ❌

Comprueba que una ruta inexistente, como /api/desconocido, devuelve HTTP 404 Not Found.
* testHandleApiPostRequest 📤

Evalúa que una solicitud POST a /api/enviar con un cuerpo JSON sea procesada correctamente y responda con HTTP 200 OK y el mensaje

* testHandleApiRequestHello
Esta prueba verifica que el servidor HTTP maneje correctamente una solicitud a la ruta "/api/hello".

## Características principales: ⚙️

1. Interfaz moderna y responsiva:

* Un diseño minimalista con un esquema de colores que incluye degradados de tonos morados, creando una experiencia visual sofisticada.
* Totalmente adaptable a diferentes dispositivos gracias a su diseño responsivo.
* Panel de busqueda de archivos, el cual permite leer cualquier tipo de archivo localmente.
  
2. Gestión de archivos: ⚙️

* Incluye botones interactivos que permiten abrir y visualizar archivos clave como:
* Archivos JavaScript (script.js).
* Hojas de estilo CSS (estilos.css).
* Documentos HTML (index.html).
* Imágenes (Chill.jpg).

## Muestra de la aplicación 🧩


https://github.com/user-attachments/assets/7a3b86a6-4797-40f4-b374-aaf98f08355d


## Autores ✒️

* **Paula Natalia Paez Vega* - *Initial work* - [Paulinguis993](https://github.com/Paulinguis993)

## Licencia 📄

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Agradecimientos 🎁

Agradecimientos al profeso Daniel Benavides por brindarme sus conocimientos.
