# Quizzer
A dashboard application where the teacher can manage quizzes. 
This application provides an opportunity for the teacher to list, create, delete, edit and mark 
a status for the quizzes and questions.  Additionally, the app includes authentication functionality, 
allowing the teachers and students to sign up, log in, and access secure pages based on their roles.

## Team members:
- [Denis Chuvakov](https://github.com/DenisHki "Github page")
- [Maksim Minenko](https://github.com/madaraDance "Github page")
- [Phat Trong Nguyen](https://github.com/padwhen "Github page")
- [Tatiana Lyubavskaya](https://github.com/lTanjal "Github page")
- [Un Kuan Che](https://github.com/arielunkuanche "Github page")

## Documentation:
<https://github.com/orgs/https-github-com-DenisHki/projects/1>

### Link to the deployed application:
<https://quizzer-app.onrender.com/>


#### Run Program Via Jar file.
 1. Download the target folder to your local machine.
 2. Open the terminal in the target folder and write: java -jar target/quizzer-0.0.1-SNAPSHOT.jar.
 3. It will start the server which can be accessed from http://localhost:8080 url.

## Developer guide:
**1. How to start the application**

1. The project uses Spring Boot version 3, which requires Java version 17 or higher. Before you begin, you should check your current Java installation by running the following command: **$ java -version**.
2. Start the application by running: **./mvnw spring-boot:run** command on the command-line in project folder. 
3. To stop the application running simply press *Ctrl+C* in your terminal.
4. Or you can install Maven to run the application in your IDE. Spring Boot is compatible with Apache Maven 3.6.3 or later, you can follow the instructions at [maven.apache.org](https://maven.apache.org/). 
5. Once the application has started, visit [http://localhost:8080](http://localhost:8080) in a web browser to use the application.


**2. How to generate and run the application using the JAR file**

1. Generate a JAR file by running **./mvnw package** in the terminal.
2. Check for your project root folder, under the target folder, you can find JAR file: **quizzer-0.0.1-SNAPSHOT.jar**.
3. You should now to stop your IDE running status of the application.
4. Then run command: **java -jar target/quizzer-0.0.1-SNAPSHOT.jar** to run the application with the JAR file.
5. Open the application in [http://localhost:8080](http://localhost:8080).


**3. URL of the backend application**

<https://quizzer-app.onrender.com/>


**4. The purpose of the project’s branches**

- **Isolation of Work** :Team members to work on different features or bug fixes without interfering with each other's work.
- **Parallel Development** :Team members to work on different tasks simultaneously. This can significantly speed up the development process.
- **Code Review** :Team members can review each other's changes in a dedicated branch before merging them into the main codebase. And to resolve conflicts that may arise when multiple team members make changes to the same files.
- **Backup and Recovery** :If something goes wrong, team members can revert to a previous branch or commit to recover lost work.