### Server requirements

- Operating System: Linux
- Docker installed
- Port 8080
  Deployment Steps

1. Obtain the project files
(via Git clone or provided ZIP package)

2. Navigate to the project directory
```bash
    cd event-registration-backend
```
3. Build the Docker image
```bash
    docker build -t event-registration-backend .
```
```bash
    docker run -p 8080:8080 event-registration-backend
```
4. Verify the service is running
Open in browser:

```
    http://SERVER_IP:8080
```

5. Database

```
    http://SERVER_IP:8080/h2-console
```


