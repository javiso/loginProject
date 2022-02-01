# BASIC LOGIN PROJECT

In this file, you are going to find how to run the project as well as the details about its use. 

# REQUIREMENTS
- Java >= V11
- Docker >= 20.10.2
- Docker-compose >= 2.2.3

# RUN THE PROJECT
There are two ways of running the project: from Docker or local

- Run the project by using docker
You simply have to run the file docker-compose.yaml. This file is in the project. The project's container listens on the port 9000.  

- Run the project locally.
You have to clone the project and then run only the service 'postgres' in docker-compose.yaml. This service must be run before running the project. The server listens on the port 8080

You will find in the project the requests (with examples) so that they can be imported to postman (They are inside the folder 'resources'). The requests are already set in the port 9000
You need to change the ports to 8080 in case of the project is run locally.

# USE
# Create an user
The first thing you will have to do is to create an user. To do so, use the following endpoint:
- POST /project/v1/user/signup
- Body:

{
    
    "username": "mufasa",

    "password": "reyleon",
    
    "email": "mufasa@gmail.com"
}

- Response example:
{
    "id": 1,
    "username": "javis",
    "password": "hola",
    "email": "xx@xx.com"
}

# Log in
Now, you should log in. Call the endpoint:
- POST /project/api/login
- Parameters: They must be input in: Body --> x-www-form-urlencoded. Example: username: mufasa, password: reyleon
- Response: It returns a token example: 
{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZpcyIsImlzcyI6Ii9wcm9qZWN0L2FwaS9sb2dpbiIsImV4cCI6MTY0Mzc1MTE2M30.pA2YC-cGSTrdxC9dPFADbUmJnhl6zgk8Atsxqgf9T_I"
}
Note: The token expires in 3' from the time of creation. 

# Sum and logurl
Just then, you already can use the services sum and logurl. To use them, it is really important that you inputs the token in the header. In postman, got to headers and, in the key column, write: Authorization. As its value, input: Bearer token. Note the speace between Bearer and the token.
- SUM: 
    GET /project/v1/user/sum?value1=2&value2=5.
    Response example: 7
- LOG URLS:
    GET /project/v1/logUrl?page=0&size=3  (Through page and size, you can configure the result. By default, size = 3). The json result was set up in order to appreciate the result better.
    Response example:
    
    {
    "items": [
        {
            "id": 1,
            "url": "http://localhost:8080/project/v1/user/signup"
        },
        {
            "id": 2,
            "url": "http://localhost:8080/project/api/login"
        },
        {
            "id": 3,
            "url": "http://localhost:8080/project/v1/logUrl"
        }
    ],
    "paging": {
        "page": 0,
        "size": 3,
        "total": 3
    }
}

- LOGOUT
GET /project/api/logout
Result: Ok
It's necessary to put the token in headers in order to logout. 
