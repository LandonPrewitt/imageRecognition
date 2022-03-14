# imageRecognition
Spring Service to Interface with Images and have them processed for recognized Objects

- Dockerfile : served to create an image which will run the spring application within a container
- docker-compose : used to set up the local environment and run the full application e2e with all dependcies
- Spin up a single Postgres container for testing locally: >
> docker run --name postgresqldb -p5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=imagedb postgres
