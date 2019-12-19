# Oppenheimer

Java webapp developed with
- Springboot (backend)
- Thymeleaf (frontend)
- mysql (database)
- maven (buildtool)

- [Oppenheimer](#oppenheimer)
  - [Running locally](#running-locally)
  - [Development](#development)
    - [Test](#test)

## Running locally

1. Create an `application.properties` file at `src\main\resources\application.properties` with the following content

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=<database url>
spring.datasource.username=<database username>
spring.datasource.password=<database password>
```
Remember to replace the content in <>. Dont commit `application.properties` to git as it contains sensitive information.

2. In a terminal, run
```
./mvnw spring-boot:run
```
This uses the mvnw script to build and run the jar.

1. Open [localhost:8080](http://localhost:8080/) in a browser. The webapp's homepage should appear.
![alt text](/doc_img/homescreen.PNG)

## Development

Source code is in `src\main`

### Test
Unit test will come soon!

Sample CSV to test inserting employees from CSV in `src\test\java\com\example\demo\test_data`. Note: CSVs have to be saved as CSV(Comma delimited), else it wont work!
![alt text](/doc_img/csv.PNG)

Future feature: add support for more csv file types.