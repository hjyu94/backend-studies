```
docker run --name kyh-lec-db-shop \
-p 5432:5432 \
-e POSTGRES_DB=kyh-lec-db-shop \
-e POSTGRES_USER=hjeong \
-e POSTGRES_PASSWORD=test \
-d postgres
```