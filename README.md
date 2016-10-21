# JavaSchool t-systems project "eCare"

1. Install java v8, mysql, wildfly
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
4. Create JNDI datasource "jdbc/JS"
5. Run wildfly and deploy appication with
```
mvn wildfly:deploy -Dmaven.test.skip=true
```
