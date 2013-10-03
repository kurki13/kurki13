#!/bin/bash
scp -i scripts/id_rsa target/kurki13-1.0-SNAPSHOT.war mkctammi@users.cs.helsinki.fi:/home/mkctammi/tomcat/webapps/
chromium-browser http://t-mkctammi.users.cs.helsinki.fi/kurki13-1.0-SNAPSHOT/servlet/index
