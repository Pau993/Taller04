package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.Controller.GreetingController;
import com.example.Utils.HttpServer;

class HttpServerTest {

    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;
    
    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream, true);
    }

    /**
     * Prueba para verificar que la solicitud a la ruta "/api/saludo" devuelve una respuesta HTTP 200 OK
     * y contiene el mensaje esperado en el cuerpo de la respuesta.
     */
    @Test
    void testHandleApiRequestSaludo() {
        HttpServer.handleApiRequest("/api/saludo", writer);
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("{\"mensaje\": \"¡Hola desde el servidor!\"}"));
    }

    /**
     * Prueba para verificar que la solicitud a la ruta "/api/fecha" devuelve una respuesta HTTP 200 OK
     * y contiene la fecha en el cuerpo de la respuesta.
     */
    @Test
    void testHandleApiRequestFecha() {
        HttpServer.handleApiRequest("/api/fecha", writer);
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("\"fecha\":"));
    }

    /**
     * Prueba para verificar que la solicitud a una ruta desconocida devuelve una respuesta HTTP 404 Not Found.
     */
    @Test
    void testHandleApiRequestNotFound() {
        HttpServer.handleApiRequest("/api/desconocido", writer);
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 404 Not Found"));
    }

    /**
     * Prueba para verificar que una solicitud POST con un cuerpo JSON se maneja correctamente.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Test
    void testHandleApiPostRequest() throws IOException {
        String input = "{\"mensaje\":\"Hola\"}";
        BufferedReader reader = new BufferedReader(new StringReader(input));
        HttpServer.handleApiPostRequest("/api/enviar", reader, writer);
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Datos recibidos"));
    }
    
    @Test
    void testHandleApiRequestHello() {
        HttpServer.handleApiRequest("/api/hello", writer);
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
    }

    @Test
    void testHandleApiRequestPi() {
        String response = GreetingController.pi("name=World");
        System.out.println("Response for /api/pi: " + response);
        assertTrue(response.contains("3.141592653589793")); // Valor exacto de Math.PI
    }

    /**
     * Prueba para verificar que la solicitud a la ruta "/api/greeting" con un parámetro "name" devuelve una respuesta HTTP 200 OK
     * y contiene el saludo personalizado en el cuerpo de la respuesta.
     */
    @Test
    void testHandleApiRequestGreeting() {
        String response = GreetingController.greeting("Juan");
        System.out.println("Response for /api/greeting?name=Juan: " + response);
        assertTrue(response.contains("Hola Juan"));
    }
    
}

