# Programming Jokes Generator
A little lambda function that spits out jokes about computer programming!

# Usage

### Build
```
./gradlew clean build
```

### Host Locally
```
serverless offline start
```

Then hit this endpoint with a GET request:
```
http://localhost:3000/jokes
```

This should return a single random joke.

These routes also exist for testing purposes:
```
http://localhost:3000/healthCheck
http://localhost:3000/now
```

_Note: You may also need to run these commands to get serverless offline up and running:_
```
npm i -g serverless
sls plugin install -n serverless-offline
```

## Deploy Live
```
serverless deploy
```

_Note: you will need to have the aws cli configured in order to deploy._


# Live Endpoints
This project is hosted live! It's a public HTTPS api so no api keys are required, but each client is rate limited to only 50 requests per day.

### endpoints:
  - GET - https://r2iol78q44.execute-api.us-east-1.amazonaws.com/dev/healthCheck
  - GET - https://r2iol78q44.execute-api.us-east-1.amazonaws.com/dev/now
  - GET - https://r2iol78q44.execute-api.us-east-1.amazonaws.com/dev/jokes


# Motivation
I love coding in Clojure, and having a simple go-to recipe for building highly scalable serverless api-calling microservice that runs on NodeJs but is _written in ClojureScript_ is just awesomeee! 😍

---

Proudly scaffolded with the serverless "aws-clojurescript-gradle" template.

# AWS Clojurescript Gradle Template

This project compiles **Clojurescript** to a [NodeJS](https://nodejs.org/en/) module using the [Gradle Clojure Plugin](https://gradle-clojure.github.io/gradle-clojure/index.html).

### NodeJS Support

Rudimentary support for loading/using **NodeJS** modules is provided.

See [functions.cljs](./src/main/clojurescript/serverless/functions.cljs) as an example.

To include **NodeJS** dependencies, modify [build.gradle](./build.gradle) and add the module to the `closurescript .. npmDeps` section.

### Prerequisites

- Create an [Amazon Web Services](https://aws.amazon.com) account
- Install and set-up [Serverless Framework CLI](https://serverless.com)
- Install [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- Install [NPM](https://www.npmjs.com/get-npm)
- Install [Clojure](https://clojure.org/guides/getting_started)
- Install [Gradle](https://gradle.org/install/)

### Build and Deploy

- To build, run `./gradlew clean build`
- To deploy, run `serverless deploy`

### Using the Repl in IntelliJ Cursive IDE

This project contains a [script](./scripts/node_repl.clj) the must be initialized in order to use the **Repl** in **IntelliJ**.
