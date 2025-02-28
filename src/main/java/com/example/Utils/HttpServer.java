package com.example.Utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.Anotation.GetMapping;
import com.example.Anotation.RequestParam;
import com.example.Anotation.RestController;



/**
 *   @author Paula Paez 
 */
public class HttpServer {
    public static Map<String, Method> services = new HashMap();
    private static boolean primeraPeticion = true; //Revisa si la primera petición es verdadera
    private static int PORT = 35000; // Puerto donde se inicia el programa 
    private static final String BASE_DIRECTORY = System.getProperty("user.dir"); 
    public static final Utils staticFiles = new Utils();
    private static boolean running = true;
    private static ServerSocket serverSocket;
    private static ExecutorService threadPool; 

    // String que almacena la ruta de los archivos Controlador
    // Route es una interfaz funcional que se utiliza para manejar las rutas, la Respuesta
    //static Map<String, Route> routes = new HashMap<>(); // Rutas registradas en el servidor

    /**
     * Constructor de la clase HttpServer
     * @throws Exception
     */
    public HttpServer(){
        
    }

    public static void run() throws IOException {
        loadComponents();
        ServerSocket serverSocket = new ServerSocket(PORT); 
        threadPool = Executors.newFixedThreadPool(10); //Crear un pool de hulos concurrentes
        System.out.println("Servidor iniciado en el puerto " + PORT);

        //Escucha simultaneamente por puerto
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleRequest(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error al aceptar la conexión: " + e.getMessage());
                }
            }
        }
        threadPool.shutdown();

        try{
            if(!threadPool.awaitTermination(60,TimeUnit.SECONDS)){
                threadPool.shutdownNow();
            }
        } catch(InterruptedException e){
            threadPool.shutdownNow();
        }
        if (serverSocket != null && !serverSocket.isClosed()){
            serverSocket.close();
        }
        System.out.println("Servidor detenido");
    }

    /**
     * Este método maneja la solicitud HTTP recibida y envía una respuesta al cliente.
     * @param clientSocket es el socket del cliente que realiza la solicitud
     */
    static void handleRequest(Socket clientSocket) {
        // Abre un BufferedReader para leer la entrada del cliente
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Abre un PrintWriter para enviar exto al cliente
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            // Abre un Buffere BufferedOutputStream para enviar datos binarios al cliente
             BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
             // Abre un OutputStream para enviar datos al cliente
             OutputStream dataOut = clientSocket.getOutputStream();
        ) {
            //Valor de cada invocación de BufferedReader
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            System.out.println("Solicitud recibida: " + requestLine);
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0]; // GET, POST, PUT, DELETE, etc. Metodos que se están invocando
            String path = requestParts[1]; // Ruta del recurso solicitado
            System.out.println(path);

            if (method.equals("GET") && !primeraPeticion) {
                serveStaticFile(path, out, dataOut, bos);
                } else if (primeraPeticion){
                    serveStaticFile(path, out, dataOut, bos);
                } else {
                sendResponse(out, 405, "Method Not Allowed", "Método no permitido.");
                }
        } catch (IOException e) {
            System.err.println("Error al manejar la solicitud: " + e.getMessage());
        }
    }

    /**
     * // Sirve un archivo estático al cliente.
     * @param path es la ruta del recurso solicitado
     * @param out es el flujo de salida para enviar la respuesta HTTP
     * @param dataOut es el flujo de salida para enviar los datos del archivo
     * @throws IOException es una excepción que se lanza si ocurre un error de entrada/salida
     */
    private static void serveStaticFile(String path, PrintWriter out, OutputStream dataOut, BufferedOutputStream bos) throws IOException {
        //Se obtiene el valor de la variable almacenar
        String almacenar = URI.create(path).getQuery();
        if(almacenar != null){
            path = "/" + almacenar.split("=")[1];
        }
        System.out.println(path);
        String filePath = "/app/src/main/resources/Files" + (path.equals("/") ? "/index.html" : path);
        File file = new File(filePath);
        System.out.println(filePath);
        
        boolean pathVer = file.exists();
        if (services.containsKey(path)) {
            
            Method response = services.get(path);
            String responseValue = "";
            try {
                 responseValue = handleControllers(response, almacenar);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(responseValue);
            
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/plain");
                out.println();
                out.println(responseValue);
                out.flush();
    
        }else if (file.exists() && !file.isDirectory() && !services.containsKey(path)) {
            String contentType = Files.probeContentType(file.toPath());
            System.out.println(contentType);
            
                byte[] fileData = Files.readAllBytes(file.toPath());
    
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + contentType);
                out.println("Content-Length: " + fileData.length);
               out.println();
                out.flush();
    
                dataOut.write(fileData, 0, fileData.length);
                dataOut.flush();
        } else {
            sendResponse(out, 404, "Not Found", "Archivo no encontrado.");
        }
    }

    /**
     * // Maneja una solicitud a la API.
     * @param path
     * @param out
     */
    public static void handleApiRequest(String path, PrintWriter out) {
        Arrays.toString( getParams(path));
        if (path.equals("/api/saludo")) {
            sendResponse(out, 200, "OK", "{\"mensaje\": \"¡Hola desde el servidor!\"}");
        } else if (path.equals("/api/fecha")) {
            sendResponse(out, 200, "OK", "{\"fecha\": \"" + new Date() + "\"}");
        } else if (path.equals("/api/hello")) {
            sendResponse(out, 200, "OK", "{\"mensaje\\\": \\\"¡Hola desde el servidor!\\\"}");
        } else if (path.equals("/index.html")) {
            sendResponse(out, 200, "OK", "{\"mensaje\\\": \\\"¡Hola desde el servidor!\\\"}");
        } else {
            sendResponse(out, 404, "Not Found", "{\"error\": \"Recurso no encontrado\"}");
        }
    }

    /**
     * // Obtiene los parámetros de una solicitud.
     * @param path
     * @return
     */
    public static String[] getParams(String path) {
        String[] parts = path.split("\\?");
        if (parts.length == 1) return new String[0];
        return parts[1].split("&");
    }

    /**
     * // Maneja una solicitud POST a la API.
     * @param path
     * @param in
     * @param out
     * @throws IOException
     */
    public static void handleApiPostRequest(String path, BufferedReader in, PrintWriter out) throws IOException {
        if (path.startsWith("/api/enviar")) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                body.append(line);
            }

            sendResponse(out, 200, "OK", "{\"mensaje\": \"Datos recibidos: " + body + "\"}");
        } else {
            sendResponse(out, 404, "Not Found", "{\"error\": \"Recurso no encontrado\"}");
        }
    }


    /**
     * Envía una respuesta HTTP al cliente.
     * @param out
     * @param statusCode
     * @param statusMessage
     * @param body
     */
    private static void sendResponse(PrintWriter out, int statusCode, String statusMessage, String body) {
        out.printf("HTTP/1.1 %d %s\r\n", statusCode, statusMessage);
        out.println("Content-Type: application/json");
        out.println("Content-Length: " + body.length());
        out.println();
        out.println(body);
    }

    /**
     * Establece el puerto en el que escuchará el servidor.
     * @param port
     */
    public static void port(int port) {
        PORT = port;
    }

    /** 
     * 
     * @param args
     */
    public static void loadComponents() {
        File filesController = new File("src\\main\\java\\com\\example\\Controller");
        if(filesController.exists() && filesController.isDirectory()){
            for(File doc : filesController.listFiles()){
                if(doc.getName().endsWith(".java")){
                    try {
                        String nameClass = "com.example.Controller." + doc.getName().replace(".java", "");
                        //Intenta cargar la clase especificada por el primer argumento
                        Class c = Class.forName(nameClass);
                        //verifica si la clase cargada esta anotada con @RestController
                        if (!c.isAnnotationPresent(RestController.class)) {
                            //Si no está anotada con @RestController, cierra el progama
                            System.exit(0);
                        }
                        //Itera sobre los métodos declarados de la clase cargada
                        for (Method m : c.getDeclaredMethods()) {
                            // Verifica si el método esta anotado con @GetMapping
                            if (m.isAnnotationPresent(GetMapping.class)) {
                                // Recupera la anotación @GetMapping
                                GetMapping a = m.getAnnotation(GetMapping.class);
                                // Mapea el valor de la anotación al método en el mapa sevices
                                services.put(a.value(), m);
                            }
                        }
                        System.out.println(services.keySet().toArray().toString());
                    } catch(Exception ex) {
                        Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
            
        }
        
    }

    public static Object convertToType(Class<?> type, String value) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else {
            return value; 
        }
    }

    public static String handleControllers(Method serviceMethod, String query){
        
        if (serviceMethod != null) {
            try {
                Map<String, String> queryParams = parseQueryParams(query);
                Object[] parameters = new Object[serviceMethod.getParameterCount()];
                Class<?>[] parameterTypes = serviceMethod.getParameterTypes();
                Annotation[][] annotations = serviceMethod.getParameterAnnotations();

                for (int i = 0; i < annotations.length; i++) {
                    for (Annotation annotation : annotations[i]) {
                        if (annotation instanceof RequestParam) {
                            RequestParam requestParam = (RequestParam) annotation;
                            String paramName = requestParam.value();
                            String paramValue = queryParams.get(paramName);

                            if (paramValue == null || paramValue.isEmpty()) {
                                parameters[i] = convertToType(parameterTypes[i], requestParam.defaultValue());
                            } else {
                                parameters[i] = convertToType(parameterTypes[i], paramValue);
                            }
                        } else {
                            parameters[i] = null; 
                        }
                    }
                }
                String variable = serviceMethod.invoke(null, parameters).toString();
                return (String) serviceMethod.invoke(null, parameters);
            } 
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return "";
                
    }

    public static Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    public void stop() {
        running = false;
        threadPool.shutdown();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("Servidor apagado correctamente.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}



