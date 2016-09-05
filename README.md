# JavaSchool t-systems project "eCare"

1. Install java v8, mysql, tomcat
2. Create new database
``` sql
CREATE DATABASE 'eCare';
```
3. Create new user for db
``` sql
CREATE USER 'ecare_user'@'localhost' IDENTIFIED BY 'keinerverstehtdeutsch';
GRANT ALL PRIVILEGES ON eCare . * TO 'ecare_user'@'localhost';
FLUSH PRIVILEGES;
```
4. Creare tabes from db.sql
5. Run from IDE
6. If database is empty, you can start with new tariffs adding
