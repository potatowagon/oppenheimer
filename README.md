# Oppenheimer

Java webapp developed with
- Springboot (backend)
- Thymeleaf (frontend)
- mysql (database)
- maven (buildtool)

- [Oppenheimer](#oppenheimer)
  - [Running locally](#running-locally)
  - [Uploading to cloud manually](#uploading-to-cloud-manually)
  - [Development](#development)
    - [Test](#test)
  - [CD](#cd)

## Running locally

1. Create an `application.properties` file at `src\main\resources\application.properties` with the following content

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=<database url>
spring.datasource.username=<database username>
spring.datasource.password=<database password>
server.port=5000
```
Remember to replace the content in <>. Dont commit `application.properties` to git as it contains sensitive information.

2. In a terminal, run
```
./mvnw spring-boot:run
```
This uses the mvnw script to build and run the jar.

3. Open [localhost:5000](http://localhost:5000/) in a browser. The webapp's homepage should appear.
![alt text](/doc_img/homescreen.PNG)

## Uploading to cloud manually
1. Build the Jar
```
./mvnw clean package
```

2. Upload the Jar to cloud

## Development

Source code is in `src\main`

### Test
Unit test will come soon!

Sample CSV to test inserting employees from CSV in `src\test\java\com\example\demo\test_data`. Note: CSVs have to be saved as CSV(Comma delimited), else it wont work!
![alt text](/doc_img/csv.PNG)

Future feature: add support for more csv file types.

## CD
Uses AWS
1. CodePipeline
2. CodeBUild
3. ElasticBeanstalk

for Continuos Deployment.

1. CodePipeline has three main stages configured:
   1. Sync Source Code from Github master branch
   2. Build the jar from source code with CodeBuild
   3. Deploy the built artifacts to ElasticBeanstalk

2. CodeBuild uses `./buildspec.yml` as the build script.
   1. CodeBuild is configured in AWS console to use the Amazon Linux 2 image. The Java runtime for the image is specified in `buildspec.yml`. The image is deployed in a docker container supplied by AWS.
   2. The source code needs the `application.properties` file to build, but it is not committed to the public github repo that holds the source code as it contains sensitive information. The current workaround is to:
      1. Host `application.properties` in a private Github repo.
      2. Store the github access token as an environment variable in AWS build enviroment.
      3. Use the github access token environment variable to download `application.properties` file from the private Github repo to the appropriate path within the synced source code.
        ```
        curl https://$GITHUB_TOKEN@raw.githubusercontent.com/potatowagon/spring-application-properties/master/oppenheimer/application.properties -o ./src/main/resources/application.properties
        ```
   3. Source code is then built using mvn. `./buildspec.yml` specifies to collect the jar file as the artifact.

1. Codepipeline is configured in the AWS console to deploy the jar file to ElasticBeanstalk.

When deployment is successful AWS console will show that
1. CodePipeline deployment is successful
![alt text](/doc_img/codepipeline.PNG)

2. EBS is healthy
![alt text](/doc_img/ebs.PNG)