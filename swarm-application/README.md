# Swarm and JEE7 Example II

## About

Example of JEE7 code using JAXRS with WELD, packaged and deployed in docker container as wildfly-swarm.
Database backend is PostgreSQL 9.x, with predifiend schema runing in docker container.

## Running the code

1. Build and deploy database container

```
$ cd database-container && docker build -t db_backend . 
$ docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d db_backend
```
Build the code with:
```
$ mvn clean install ## will just build code and run tests
$ mvn clean install -Pdocker ## will build swarm jar & docker container as well
```

To run the code:
```
## 
## Database host can be specified with -DdbHost=IP
## Database port can be specified wtih -DdbPort=PORT
## Database name can be specified wtih -DdbName=DB_NAME
## Database user and password can be specified with -DdbUser=USER (-DdbPassword=PASSWORD)
##
## Default values, if non of the above is specified: dbHost=localhost, dbPort=5432, dbName=swarmapp, dbUser=postgres, dbPassword=mysecretpassword
##

$ java -jar payment-backend/target/payment-backend-1.0.1-SNAPSHOT-swarm.jar
```

Or run the container with usual docker run ....

Happy hacking,
Dejan