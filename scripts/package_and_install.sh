#!/bin/bash
cd ~/kurki13
mvn package
cp ~/kurki13/target/kurki13-1.0-SNAPSHOT.war ~/tomcat/webapps
cp ~/kurki13/apache-tomcat-7.0.42/conf/tomcat-users.xml ~/tomcat/conf/tomcat-users.xml
