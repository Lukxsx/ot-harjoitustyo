# Fuel logger

A program to keep track of the fuel consumption of a car. It can show interesting charts and data of your car's fuel consumption and cost of ownership. 

## Releases
__HUOM!__ Tarkastajille tietoksi: *Cubblin virtuaalityöasemassa on ollut ongelmia useammalla kurssilaisella SQLiten kanssa. Jostain syystä SQLiteä käyttävät työt antavat virtuaalityöasemassa testattaessa välillä virheilmoituksen* ```[SQLITE_ERROR] SQL error or missing database (no such table...```*, mutta ovat toimineet täysin ongelmitta oikeiden koneiden Cubbli-asennuksilla. Olen testannut ohjelmani Cubbli-virtuaalityöasemassa ja se toimi hyvin, mutta suosittelen testaamaan ei-virtuaalikoneella, jos noita virheilmoituksia tulee.*
* [viikko 6](https://github.com/Lukxsx/ot-harjoitustyo/releases/tag/viikko6)
* [viikko 5](https://github.com/Lukxsx/ot-harjoitustyo/releases/tag/viikko5)

## Documentation
* [Requirements](fuel-logger/documentation/requirements.md)
* [User manual](fuel-logger/documentation/user-manual.md)
* [Architecture](fuel-logger/documentation/architecture.md)
* [Working hours](fuel-logger/documentation/working%20hours.md)
* [Testing report](fuel-logger/documentation/testing.md)

## Commands

### Running tests
```
mvn test
```

### Test coverage
```
mvn test jacoco:report
```

### Running from Maven
```
mvn compile exec:java -Dexec.mainClass=fuellogger.Main
```

### Package jar
```
mvn package
```
Jar file will be generated in _target/fuel-logger-1.0-SNAPSHOT.jar_

### Checkstyle
```
mvn jxr:jxr checkstyle:checkstyle
```
Checkstyle is generated in _target/site/checkstyle.html_

### Javadoc
```
mvn javadoc:javadoc
```
Javadoc is generated in _target/site/apidocs/index.html_
