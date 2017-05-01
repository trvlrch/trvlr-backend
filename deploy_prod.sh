# build project
gradlew build

# copy war file to production machine
rsync -avz  -e "ssh -p 7454" build/libs/trvlr-0.0.1-SNAPSHOT.war centos@5.148.165.52:/usr/share/tomcat/webapps/trvlr-0.0.1-SNAPSHOT.war