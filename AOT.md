# Compilar o app Spring boot para nativo

1. Compilar para JAR
2. Rodar o agente nativo para detectar os hints
```shell
java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image  \
    -Dspring.profiles.active=dev-pg \
    -jar target/mumuca-diet-0.0.1-SNAPSHOT.jar
```

3. Teste a aplicação e dê Ctrl+C

4. Compile para nativo (VAI USAR MUITOS RECURSOS DA MAQUINA)
```shell
mvn -Pnative native:compile -Dspring.profiles.active=dev-pg -DskipTests
```

5. RESOLVER O PROBLEMA DOS REPOSITORIOS JPA NÃO INSTANCIAREM  
Problema no [Github](https://github.com/spring-projects/spring-boot/releases/tag/v3.2.3)
