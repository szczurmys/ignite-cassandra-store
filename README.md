[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/apache/maven.svg?label=License)](license)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.szczurmys/ignite-cassandra-store.svg?label=Maven%20Central)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.szczurmys%22%20AND%20a%3A%22ignite-cassandra-store%22)

Notice
------
Original repository: **https://github.com/apache/ignite** <br />
Forked repository with type-handler in cassandra-module: https://github.com/szczurmys/ignite

This repository was created by `git filter-branch` forked repository https://github.com/szczurmys/ignite
using commands: 
* `git filter-branch --prune-empty --subdirectory-filter modules/cassandra/store -- <BRANCHES>`
* `git filter-branch --tree-filter 'if [ -d src/main/java/org/apache/ignite/cache/store/cassandra ]; then mkdir -p src/main/java/com/github/szczurmys/ignite/cache/store/cassandra; mv src/main/java/org/apache/ignite/cache/store/cassandra/* src/main/java/com/github/szczurmys/ignite/cache/store/cassandra/; fi' -- <BRANCHES>`
* `git filter-branch --tree-filter 'if [ -d src/test/java/org/apache/ignite/tests ]; then mkdir -p src/test/java/com/github/szczurmys/ignite/tests; mv src/test/java/org/apache/ignite/tests/* src/test/java/com/github/szczurmys/ignite/tests/; fi' -- <BRANCHES>`
* `git filter-branch --tree-filter 'if [ -d src/test/java/org/apache/ignite/testsuites ]; then mkdir -p src/test/java/com/github/szczurmys/ignite/testsuites; mv src/test/java/org/apache/ignite/testsuites/* src/test/java/com/github/szczurmys/ignite/testsuites/; fi' -- <BRANCHES>`
* `git filter-branch --tree-filter "find . -type f -exec sed -i -e 's/org\.apache\.ignite\.cache\.store\.cassandra/com.github.szczurmys.ignite.cache.store.cassandra/g' {} \;" -- <BRANCHES>`
* `git filter-branch --tree-filter "find . -type f -exec sed -i -e 's/org\.apache\.ignite\.tests/com.github.szczurmys.ignite.tests/g' {} \;" -- <BRANCHES>`
* `git filter-branch --tree-filter "find . -type f -exec sed -i -e 's/org\.apache\.ignite\.testsuites/com.github.szczurmys.ignite.testsuites/g' {} \;" -- <BRANCHES>`

<BRANCHES> - specified branches, you can also use --all, but i do not recommend that, because it really slows down the process

Ignite Cassandra Store With Type Handler Module
------------------------

Ignite Cassandra Store With Type Handler module provides CacheStore implementation backed by Cassandra database.

To enable Cassandra Store module when starting a standalone node, move 'optional/ignite-cassandra-store' folder to
'libs' folder and replace `ignite-cassandra-store-<ignite.version>.jar` with jar `ignite-cassandra-store-<version>.jar` from com.github.szczurmys, 
before running 'ignite.{sh|bat}' script. The content of the module folder will be added to classpath in this case.

**Type handler** in cassandra module (similar solution is in iBatis https://ibatis.apache.org/docs/java/dev/com/ibatis/sqlmap/engine/type/TypeHandler.html), that allow use any java type in key|value|pojo and then convert from/to simple cassandra type available in cassandra driver.
E.g. you can use java.time.LocalDateTime insead of use java.util.Date directly, or even use map<key, value> ( https://docs.datastax.com/en/cql/3.1/cql/cql_using/use_map_t.html ), if you like.
It should be compatible with previous persistence-settings file.


Importing Cassandra Store Module In Maven Project
-------------------------------------

If you are using Maven to manage dependencies of your project, you can add Cassandra Store module
dependency like this (replace '${version}' with actual Cassandra Store version you are
interested in):

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                        http://maven.apache.org/xsd/maven-4.0.0.xsd">
    ...
    <dependencies>
        ...
        <dependency>
            <groupId>com.github.szczurmys</groupId>
            <artifactId>ignite-cassandra-store</artifactId>
            <version>${version}</version>
        </dependency>
        ...
    </dependencies>
    ...
</project>
```

Example of use for LocalDateTime:
-------------------------------------

Type hander:
```java
public class LocalDateTimeTypeHandler implements TypeHandler<java.time.LocalDateTime, java.util.Date> {
    @Override 
	public java.time.LocalDateTime toJavaType(Row row, int index) {
        java.util.Date date = row.getTimestamp(index);
        return date == null ? null : convert(date);
    }
    @Override 
	public java.time.LocalDateTime toJavaType(Row row, String col) {
        java.util.Date date = row.getTimestamp(col);
        return date == null ? null : convert(date);
    }
    @Override 
	public java.util.Date toCassandraPrimitiveType(java.time.LocalDateTime javaValue) {
        return javaValue == null ? null : convert(javaValue);
    }
    @Override
    public String getDDLType() {
        return DataType.Name.TIMESTAMP.toString();
    }
    private java.time.LocalDateTime convert(java.util.Date date) {
        return java.time.LocalDateTime.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
    }
    private java.util.Date convert(java.time.LocalDateTime  date) {
        return java.util.Date.from(date.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }
}
```

POJO:
```java
public class TestPojoClass {
	private String name;
	private java.time.LocalDateTime modificationDateTime;
	
	/** getters and setters **/
}

```

persistence-settings:
```xml
<persistence keyspace="test1" table="example_of_use_type_handler">
    <keyPersistence class="java.lang.Long" strategy="PRIMITIVE" column="key"/>
    <valuePersistence class="TestPojoClass"
                      strategy="POJO"
                      serializer="org.apache.ignite.cache.store.cassandra.serializer.JavaSerializer">
        <field name="name" column="name" />
        <field name="modificationDateTime" column="modification_date_time" handlerClass="LocalDateTimeTypeHandler" />
    </valuePersistence>
</persistence>
```
