# Usa una imagen base de Java
FROM openjdk:22-jdk-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia los archivos compilados y dependencias
COPY target/classes /app/classes
COPY target/dependency /app/dependency
COPY src/main/resources/Files /app/src/main/resources/Files

# Comando de ejecución
CMD ["java", "-cp", "classes:dependency/*", "com.example.Microframework"]
