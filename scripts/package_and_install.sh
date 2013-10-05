#!/bin/bash
cd ~/kurki13
mvn install
rm -rf ~/tomcat/webapps/kurki13-1.0-SNAPSHOT
rm -f ~/tomcat/webapps/kurki13-1.0-SNAPSHOT.war
cp ~/kurki13/target/kurki13-1.0-SNAPSHOT.war ~/tomcat/webapps
cp ~/kurki13/apache-tomcat-7.0.42/conf/tomcat-users.xml ~/tomcat/conf/tomcat-users.xml
