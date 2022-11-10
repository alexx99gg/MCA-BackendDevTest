# MCA-BackendDevTest
Technical test from MCA
View exercise statement at: https://github.com/dalogax/backendDevTest

Dockerized deployment instructions:
1. `cd similar-products-api`
2. `mvn clean package`
3. `cd ..`
4. `docker-compose build`
5. `docker-compose up`

Api exposed at localhost:5000

Swagger available at http://localhost:5000/swagger-ui/index.html

If you decide to not run the api without the docker-compose setup, the api will also be exposed at localhost:5000

To execute K6 tests:


If you deployed the api via docker-compose up:\
`docker-compose run --rm k6 run scripts/test.js`

If you deployed the api without the docker-compose up:\
`docker-compose run --rm k6 run scripts/test-local.js`

Note:
All similar-products-api are sef-contained, no need to run other services in background.
For testing, port 5500 and 3301 will be used.

This application uses Java 19 version.