# imageRecognition
Spring Service to ingests user images, analyzes them for object detection, and returns the enhanced content

<hr />

### Requirements
Make sure you meet all the following requirements
* You have [Java](https://www.java.com/en/download/manual.jsp) Installed on your PC
* You Have [Docker](https://www.docker.com/products/docker-desktop/) installed with Daemon Running Successfully
* Read the 'Options & Notes' sections for details on running the app outside of docker.

<hr />

### Options & Notes
* <b>Easter Egg ;) </b> - There is an easter egg in the code, hopefully you find it!
* <b>Object Detection Accuracy</b> - In the [properties file](src/main/resources/application.yml), you can configure the 'imagga.threshold' value to set a threshold for how certain it knows the provided object is in the image.
  * If running via Docker: For changes to take effect, you must 1) Reinstall mvn package -- 2) Rebuild the application image -- 3) re-run docker-compose.
* <b>POST /images</b>
  * Image File vs URL Post - When using the POST - /images request, URL is configured to take priority; so if both are provided, the image being posted will ignore the File being sent. 
  * Also, make sure to read the additional notes in the next section about properly executing this request. 

<hr />

### Get Started!
<h4 align="center"> --- Run App Via Docker --- </h4>

First let's get the environment set up and Everything running locally. Execute the following Commands in Order in your terminal. 
1. Run maven install in the project root directory
> mvn clean install
2. Run Docker-Compose to start the ImageRecognition App & Postgres Containers ([Docker Compose File](docker-compose.yml))
> docker-compose up -d

<br/>

<h4 align="center"> --- Run App Outside Docker (IDE/Terminal/etc) --- </h4>

1. If you've already ran docker-compose, ensure the application container is killed, otherwise it will hog the 8080 port on your local machine.  
> docker stop $container_name
2. If you've already ran docker-compose, you should already have a postgres container running, otherwise, run the following.
> docker run --name postgresqldb -p5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=imagedb postgres
3. When running the app locally,  make sure your active profile is set to 'local'. This is to ensure the postgres url is pointing to local port and not to docker Container

<br />

<h4 align="center"> --- Use --- </h4>

The App should be running successfully at this point. Now let's interact with it.
* Visit the [Swagger](http://localhost:8080/swagger-ui.html) and execute some Endpoints there
* Refer to the [Project Definition](src/main/resources/static/readmeImages/CodingExerciseRequirements.pdf) for additional understanding of the endpoints
* <b>Important! -</b> Regarding the POST - /images method. This will not work via Swagger. You must use an application, such as PostMan, to execute it properly if uploading an Image.
  Refer to the below example on how to properly execute and test this method if uploading a file. <u>If you wish to use a URL</u> simply add the URL of the image to the JSON property in the JSON of the image Key (Which is empty in the 2nd screenshot). 

![](src/main/resources/static/readmeImages/postman_pt1.png)
![](src/main/resources/static/readmeImages/postman_pt2.png)
