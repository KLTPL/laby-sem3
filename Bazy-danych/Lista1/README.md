# Lista 1

## Setup

### Using docker to launch MySQL

```bash
docker compose up -d
```

### Import sakila

```bash
docker compose exec mysql mysql -uroot -proot -e "SOURCE /sakila/sakila-schema.sql; SOURCE /sakila/sakila-data.sql;"
```

## Working with MySQL

### Executing an .sql file

```bash
docker compose exec -T mysql mysql -uroot -proot -t sakila < Lista1/solution.sql
```

### Interactive mode

```bash
docker compose exec -it mysql mysql -uroot -proot sakila
```
