# Personal time and progress bar

---

Ptpb is a trainee project that synchronize with todoist.com and show what tasks status is.

# Environment
Ptpb uses Java 11 + Maven 3.0.
Environment:
 - Aerospike DB
 - RabbitMq

Development environment is very easy to install and deploy with a docker containers using docker-compose.
Open your favourite Terminal and run these commands:

```sh
$ cd ./ci
$ docker-compose up
```

The components structure is described in the diagram: [components diagram](https://docs.google.com/drawings/d/1JHPsG3Z8cL-KdWP3kaqRyCCQeZLN8T1-wlnb3tJdT5o/edit?usp=sharing)


# Building from sources
For production release run following commands:

```sh
$ mvn package
```

# Installation

Once building from sources is done and environment is running then start the application with using following commands:

```sh
$ java -jar ptpb-handler-1.0-SNAPSHOT.jar
```

Verify the deployment by navigating to your server address in your preferred browser:
```sh
127.0.0.1:8000
```

# License

It is a Free Software

----
