## Tech Specification
- Using Springboot 3.1.4 as framework and PostgreSQL as database

## Setup Local Environment Documentation
- Clone repository from github
- Create database in postgreSQL with desired name
- Fill some required field in `application.properties` file with database config in your system and also jwt config
- Run `gradle bootRun` in terminal

# List of API
- `POST /api/v1/auth/register` for register with body is email, password and name of user.
- `POST /api/v1/auth/login` for login user to get bearer token, login using email and password of user and will return bearer token.
- `POST /api/v1/auth/refresh-token` for generate new access token without login using email and password, this method just required bearer token authorization. 
- `GET /api/v1/jobs` for get all jobs, this method just required bearer token for authorization.
- `GET /api/v1/jobs/{id}` for get specific job, this method required job id in parameter and bearer token for authorization.