FROM openjdk:8
COPY /target/todo-1.jar /var/www/todoapi/
WORKDIR /var/www/todoapi/
EXPOSE 8080
RUN chmod 777 todo-1.jar
CMD ["java","-jar","todo-1.jar"]
