// NPM 
npm install --> install all dependencies described from package.json file, into node_modules directory
npm install --save \[dependency@version\] --> install new dependency
npm run build --> build FE application, create build directory
HOST=0.0.0.0 npm start --> run FE project


// Java
java -jar file.jar --> running jar file, from target folder


// Maven .mvnw (maven wrapper)
./mvnw clean --> delete target folder
./mvnw install --> create target folder, maven will use the active profile to build the project, even if the profile includes to build the FE project too.
./mvnw clean install --> combine above commands, maven will use the active profile to build the project, even if the profile includes to build the FE project too.


// Maven, Profiles and Automate FE and BE bundling
https://github.com/eirslett/frontend-maven-plugin --> this plugin use npm to install and build FE project, it is started when maven run install command into active maven profile described into pom.xml file
https://maven.apache.org/plugins/maven-resources-plugin/plugin-info.html --> automate copy FE build folder into static folder in our BE project, as part of the maven profile and above maven plugin


// Docker and Jib
https://github.com/GoogleContainerTools/jib --> Jib builds optimized Docker and OCI (Open Container Initiative) images for our Java applications without Docker daemon.

./mvnw jib:dockerBuild -Djib.to.image=image-name:version --> Jib build LOCAL DOCKER IMAGE from our application
./mvnw clean install jib:dockerBuild -Djib.to.image=image-name:version --> If the jar file is missing, first create jar file and then build LOCAL DOCKER IMAGE

./mvnw clean install jib:build -Djib.to.image=bdostumski/spring-react-fullstack:v1 --> build jar file, containerized it into docker image and push it to the docker hub repository
./mvnw clean install jib:build -Djib.to.image=bdostumski/spring-react-fullstack:latest -Djib.to.auth.username=bdostumski -Djib.to.auth.password=**** --> same as above but with login credentials

./mvnw help:active-profiles --> show all active profiles

./mvnw clean install -P bundle-backend-and-frontend -P jib-build-docker-image-and-push-it-to-docker-hub -Dapp.image.tag=2.3 --> build jar file, dockerized it and push it to docker hub repository
./mvnw clean install -P bundle-backend-and-frontend -P jib-build-local-docker-image -Dapp.image.tag=2.3 --> build jar file, and create docker image

// bundle FE and BE locally and build docker image with Jib and push it to the DockerHub repository
./mvnw clean install -P bundle-backend-and-frontend -P jib-build-docker-image-and-push-it-to-docker-hub -Dapp.image.tag=3 -- use -P (for profile) bundle-backend-and-frontend to bundle FE and BE locally, and then run -P (for profile) bundle-backend-and-frontend to use Jib to create docker image and to push it into DockerHub repository, and then set up the version of the docker image -Dapp.image.tag=3 (where app.image.tag is environment variable)

docker login --> login into docker repository
docker image ls | docker images --> show images in our local machine 
docker ps --> show running containers 
docker ps -a --> show all containers 
docker rm -f --> force the removal of a running containers (uses SIGKILL) 
docker run --name image-name -p 8080:8080 image:version  
docker run --rm --name image-name -p 8080:8080 image:version
docker pull image:version --> pull image from remove repository

// Docker and databases
docker network create db
docker network rm db
docker run --name db -p 5555:5432 --network=db -v "/path/to/database-dir:/var/lib/postgresql/data" -e POSTGRES_PASSWORD=password -d postgres:alpine // when use $PWD you must be in the directory, that you want to be used for database
docker run -it --rm --network=db postgres:alpine psql -h db -U postgres 
docker run -it --rm postgres:alpine psql -h aa9320n4muma7h.celswdmxhcr1.eu-west-1.rds.amazonaws.com -U amigoscode -d postgres // connect to AWS RDS database, after setup AWS Security Group to allow external login to it. -h this is the path to the database that can be found into Elastic Beanstalk, log into environment and click configuration, find database tab copy Endpoint without the port





