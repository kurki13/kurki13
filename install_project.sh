#!/bin/bash

wanna-tomcat
stop-tomcat
git clone https://github.com/kurki13/kurki13.git
cd kurki13
git pull origin master
mvn package
mv ~/kurki13/target/kurki13-1.0-SNAPSHOT.war ~/tomcat/webapps
mv ~/kurki13/apache-tomcat-7.0.42/conf/tomcat-users.xml ~/tomcat/conf/tomcat-users.xml
start-tomcat
