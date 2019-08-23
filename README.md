# ReportAPI

----------------To Run the jar--------------------------------------------------------------------------- 

1. make sure you have installed java 8 (I am using 1.8.0_77)and have set the JAVA_HOME accordingly
2. make sure you have installed maven (I am using 3.3.9) and have set the MAVEN_HOME accordingly

Steps to run the report api jar file in Windows
1. place the jar file in any folder on your windows file system
2. go to command prompt and change directory to the one where you have placed the jar
3. now, run the command java -jar report-api-0.0.1-SNAPSHOT.jar 
   this will start the spring boot application at port 8085
4. go to web browser, say chrome and enter this link - http://localhost:8085/report-api/swagger-ui.html
   you should see a swagger page for the report api.
5. On the swagger page,click on report-controller 
   * click on GET 
   * click on Try it Out button on right side
   * click on execute button
     this will execute the API and will return the report as an output
   * scroll down a little and you should see server reponse
   * it will show code as 200 and in front of it, you will see a link Download file as a response body	
   * click on the Download file link and you will get Output.csv which is nothing but the Summary report
6. The folder in which you have placed your jar, Log file will be generated in the same folder.   
 
 
----------------------To chage Settings-----------------------------------------------------------------------
1. make sure you have installed java 8 (I am using 1.8.0_77)and have set the JAVA_HOME accordingly
2. make sure you have installed maven (I am using 3.3.9) and have set the MAVEN_HOME accordingly

Few points about settings configured in application.properties file :
1. The Input.txt is placed inside the resources in source folder of the project.
2. If you want to use any other input file, then place it in the same folder and update the name of the input file in application.properties
   property name is :  input.file.name
3. you can also change the name of the Output.csv, It is configured in application properties
   property name is : output.file.name
4. Currently the report api is configured to run on the port 8085. 
   If you already have any other service running on this port, you can change the port for the report api in application.properties file.
   property name is : server.port 
5. ** after performing all these configuration changes, you must generate the jar again. 
   for that, go to command prompt and go to project directory.
   run the command : mvn clean install
   you will get the project jar in the target folder of the project.
6. follow the steps mentioned above to run this updatd jar   
