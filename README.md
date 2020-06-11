
## Codiv-19 API

This is a small API where Covid-19 patient information can be handled easily by basic CRUD operations.

Spring Boot is used to build the API. H2 database is used to store data. Spring Security is used to make API access level security.

### Start Server

```

mvn clean spring-boot:run

```

### Operations

Read All Data

```

Data can be read by both admin and user with the password "password". Data can not be accessed without the valid authentication.

The bellow curl commands will show all the initial data.

Request:
curl localhost:8080/api/covid -u user:password
OR
curl localhost:8080/api/covid -u admin:password

Response:
[{"id":1,"name":"Jack","state":"infected","time":"2011-10-02 18:48:05.0"},{"id":2,"name":"Michael","state":"recovered","time":"2012-10-02 18:48:05.0"},{"id":3,"name":"Cristin","state":"deceased","time":"2013-10-02 18:48:05.0"}]%

```

Insert New Data

```

Data can be inserted only by admin with the password "password". Data can not be inserted without the valid authentication.

The bellow curl command will insert a new data with the current Timestamp.

Request:
curl -X POST localhost:8080/api/covid -H "Content-type:application/json" -d "{\"name\":\"Stive\",\"state\":\"infected\"}" -u admin:password

Response:
{"id":4,"name":"Stive","state":"infected","time":"2020-06-11 10:21:43.447"}%

```

Search Data By ID

```

Data can be searched by both admin and user with the password "password". Data can not be accessed without the valid authentication.

Request:
curl localhost:8080/api/covid/id/1 -u user:password
OR
curl localhost:8080/api/covid/id/1 -u admin:password
Response:
{"id":1,"name":"Jack","state":"infected","time":"2011-10-02 18:48:05.0"}%

```

Search Data By state

```

Data can be searched by both admin and user with the password "password". Data can not be accessed without the valid authentication.

Request:
curl http://localhost:8080/api/covid/state/infected -u user:password
OR
curl http://localhost:8080/api/covid/state/infected -u admin:password

Response:
[{"id":1,"name":"Jack","state":"infected","time":"2011-10-02 18:48:05.0"},{"id":4,"name":"Stive","state":"infected","time":"2020-06-11 10:21:43.447"}]%

```

Search Data By state, startDate and endDate 

```

Data can be searched by both admin and user with the password "password". Data can not be accessed without the valid authentication.

Request:
curl "http://localhost:8080/api/covid/search?state=infected&startDate=2011-10-02%2018:48:05&endDate=2013-10-02%2018:48:05" -u user:password
OR
curl "http://localhost:8080/api/covid/search?state=infected&startDate=2011-10-02%2018:48:05&endDate=2013-10-02%2018:48:05" -u admin:password

Response:
[{"id":1,"name":"Jack","state":"infected","time":"2011-10-02 18:48:05.0"}]%

```

Search Data By startDate and endDate 

```

Data can be searched by both admin and user with the password "password". Data can not be accessed without the valid authentication.

Request:
curl "http://localhost:8080/api/covid/search?startDate=2011-10-02%2018:48:05&endDate=2013-10-02%2018:48:05" -u user:password
OR
curl "http://localhost:8080/api/covid/search?startDate=2011-10-02%2018:48:05&endDate=2013-10-02%2018:48:05" -u admin:password

Response:
[{"id":1,"name":"Jack","state":"infected","time":"2011-10-02 18:48:05.0"},{"id":2,"name":"Michael","state":"recovered","time":"2012-10-02 18:48:05.0"}]%

```

Update Data

```

Data can be updated only by the admin with the password "password". Data can not be updated without the valid authentication.

Request:
curl -X PUT localhost:8080/api/covid/1 -H "Content-type:application/json" -d "{\"name\":\"Jack\",\"state\":\"recovered\",\"time\":\"2011-10-25 18:48:05.0\"}" -u admin:password

Response:
{"id":1,"name":"Jack","state":"recovered","time":"2011-10-25 18:48:05.0"}%

```

Delete Data

```

Data can be deleted only by the admin with the password "password". Data can not be deleted without the valid authentication.

Request:
curl -X DELETE localhost:8080/api/covid/1 -u admin:password

```

### Test

```

mvn test

```
