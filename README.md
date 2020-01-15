# Programming Jokes Generator
A little lambda function that spits out jokes about computer programming!

# Usage

### Build
```
./gradlew clean build
```

### Host Locally
```
serverless offlines start
```

Then hit this endpoint with a GET request:
```
http://localhost:3000/jokes
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

---

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

![](http://share.rowellbelen.com/5WvFH2+)
