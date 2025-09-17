# Survey Application


## Tech Stack / System Requirements

- Language: Java 24 (Oracle JDK)
- Framework: JavaFX 24.0.2
- Build tool: Apache Maven 3.9.8
- Database: SQLite 3.50.3.0
- Operating System: Windows 10/11 (or other OS supported by Java 24)

> [!IMPORTANT]
> - All dependencies, including JavaFX, are bundled in the fat JAR when executing `mvn clean package`.
> - Users only need Maven and compatible Java runtime. Ensure Maven and Java 24 runtime is installed and accessible in PATH.

## Survey Maintenance Application

### Functionalities
- Dashboard for response statistics
- Add/Edit/Delete questions
- Add/Edit/Delete options for each question

### Creating and running the fat JAR
1. Open a terminal or command prompt.
2. Navigate inside `survey-app` subdirectory
```cmd
cd C:\...\survey-app
```
3. Package the application using Maven (a `target` directory will be created)
```cmd
mvn clean package
``` 
4. Navigate to the `target` directory
```cmd
cd C:\...\survey-app\target
```
5. Run the JAR file
```cmd
java -jar "survey-app-1.0-SNAPSHOT.jar"
```

### Application Screenshots
![Dashboard](.\sample_ss\sm1.PNG "Dashboard")
![Management](.\sample_ss\sm2.PNG "Management")
![Add Question](.\sample_ss\sm-add.PNG "Add Question")
![Edit Question](.\sample_ss\sm-edit.PNG "Edit Question")




## User Application

### Functionalities
- User can take the survey and submit their answers
- Next and previous navigation buttons
- Data validation for response completeness

### Creating and running the fat JAR
1. Open a terminal or command prompt.
2. Navigate inside `survey-client` subdirectory
```cmd
cd C:\...\survey-client
```
3. Package the application using Maven (a `target` directory will be created)
```cmd
mvn clean package
``` 
4. Navigate to the `target` directory
```cmd
cd C:\...\survey-client\target
```
5. Run the JAR file
```cmd
java -jar "survey-client-1.0.jar"
```

### Application Screenshots
![Home](.\sample_ss\user1.PNG "Home")
![Validation](.\sample_ss\user2.PNG "Validation")
![Summary](.\sample_ss\user3.PNG "Summary")
