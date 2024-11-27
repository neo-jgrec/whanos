# Project Samples

Thoses are example of projects you can upload to test compilation and even deployment!

# C project

For this the mandatory setup is to have a Makefile in the root of the project, must be able to compile the project with the `make` command. And the executable must be named `compiled-app` (and be found in the root of the project).

This is what the file architecture could look like for a C project:

```
.
├── Makefile
├── src
│   └── hello.c
```

Here are the content of the files:

```make
# Makefile
NAME = compiled-app

SRC = app/hello.c

all: $(NAME)

$(NAME):
	gcc -o $(NAME) $(SRC)

clean:
	rm -f *.o

fclean: clean
	rm -f $(NAME)

re: fclean all
```

```c
// src/hello.c
#include <stdio.h>

int main()
{
    puts("Hello world!");
    return 0;
}
```

# Befunge project

The only mandatory setup is to have a file named `main.bf` in a app folder in the root of the project.

This is what the file architecture could look like for a Befunge project:

```
.
├── app
│   └── main.bf
```

Example of a Befunge program:

```befunge
>              v
v  ,,,,,"Hello"<
>48*,          v
v,,,,,,"World!"<
>25*,@
```

# Java project

For this the mandatory setup is to a `pom.xml` file in the app folder in the root of the project.

This is what the file architecture could look like for a Java project:

```
.
├── app
│   ├── src
│   │   └── main
│   │       └── java
│   │           └── com
│   │               └── example
│   │                   └── App.java
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── example
│   │                   └── AppTest.java
│   └── pom.xml
```

Here are the content of the files:

```xml
<!-- app/pom.xml -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>

```

```java
// app/src/main/java/com/example/App.java
package com.example;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

```java
// app/src/test/java/com/example/AppTest.java
package com.example;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AppTest {
    @Test
    public void testApp() {
        assertTrue(true);
    }
}
```

# JavaScript project

For this the mandatory setup is to have a `package.json` file in the root of the project.

This is what the file architecture could look like for a JavaScript project:

```
.
├── app
│   ├── app.js
├── .dockerignore
├── package-lock.json
└── package.json
```

Here are the content of the files:

```json
// package.json
{
  "name": "hello-world",
  "description": "Hello World",
  "version": "0.0.1",
  "private": true,
  "dependencies": {
    "express": "4.17.1"
  },
  "main": "app/app.js"
}

```

```javascript
// app/app.js
var express = require('express');
var app = express();

// Routes
app.get('/', function(req, res) {
  res.send('Hello World!');
});

// Listen
var port = process.env.PORT || 3000;
console.log("Starting hello-world server");
app.listen(port, () => console.log('Listening on localhost:'+ port));
```

# Python project

For this the mandatory setup is to have a `requirements.txt` file in the root of the project.

This is what the file architecture could look like for a Python project:

```
.
├── app
│   └── __init__.py
│   └── __main__.py
├── requirements.txt
```

Here are the content of the files:

```python
# requirements.txt
Flask==3.0.0
```

```python
# app/__main__.py
from flask import Flask

app = Flask(__name__)

@app.route('/')
def helloIndex():
    return 'Hello world!'

app.run(host='0.0.0.0', port=8080)
```

(we execute running `python -m app`)

# Project with a deployment

For the sake of the example, we will use a TypeScript project with a Dockerfile to deploy it.  

He're the file architecture:

```
.
├── app
│   ├── app.ts
├── .dockerignore
├── Dockerfile
├── package-lock.json
├── package.json
├── tsconfig.json
├── whanos.yml
```

Here are the content of the important files:

```Dockefile
# Dockerfile
FROM whanos-javascript

RUN npm install -g typescript@4.4.3

RUN tsc

RUN find . -name "*.ts" -type f -not -path "./node_modules/*" -delete
```

```yaml
# whanos.yml
deployment:
  replicas: 3
  resources:
    limits:
      memory: "128M"
    requests:
      memory: "64M"
  ports:
    - 3000
```

Remember that the `whanos.yml` file is mandatory for the deployment to work, it'll use thoses values to deploy the project in the kubernetes cluster.
See your cluster IP and port 3000 in our case to access the project.

Here more information about the `whanos.yml` file:

The whanos.yml file can contain a deployment root property, which itself can contain:
- replicas -> number of replicas to have (default: 1; 2 replicas means that 2 instances of the resulting
pod must be running at the same time in the cluster);
- resources -> resource needs, corresponding to [Kubernetes’ own resource specifications](https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#resource-requests-and-limits-of-pod-and-container) (default:
no specifications; the syntax expected here is the same as the given link);
- ports -> an integer list of ports needed by the container to be forwarded to it (default: no ports
forwarded).

# Conclusion

Now you now how to create a project for the Jenkins server, you can now link your repository to a project and start using it !
