import com.mongodb.MongoClient;
import models.Course;
import models.Grade;
import models.Student;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import resources.*;

import java.io.IOException;
import java.net.URI;

public class RESTserwer {

    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig(StudentResource.class, CourseResource.class,
                StudentGradeResource.class, CourseListResource.class, CourseGradeResource.class);
        rc.packages("org.glassfish.jersey.examples.linking").register(DeclarativeLinkingFeature.class);
        rc.register(CustomHeaders.class);
        //rc.register(DateParamConverterProvider.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        final Morphia morphia = new Morphia();

    // tell Morphia where to find your classes
    // can be called multiple times with different packages or classes
        morphia.mapPackage("org.tpsi.rest.models");


    // create the Datastore connecting to the default port on the local host
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");
        datastore.getDB().dropDatabase();

        datastore.ensureIndexes();


        Course English = new Course("Mr. Englishman","English");
        Course Math = new Course("The scary old Profesor", "Math");
        Course Physics = new Course("Albert Heisenberg", "Physics");
        Course Physics2 = new Course("Albert Heisenberg", "Physics2");
        Course Spanish = new Course("Senor Spaniard","Spanish");

        Student one = new Student("One", "First", 1 , "05-12-1994");
        Student two = new Student("Two", "Second", 2 ,"02-02-1995");

        one.addCourse(English);
        one.addCourse(Math);

        two.addCourse(Spanish);
        two.addCourse(Physics);
        two.addCourse(Math);

        one.addGrade(new Grade(3.5,"10-03-2014"), "English");
        one.addGrade(new Grade(3.0,"11-03-2014"), "English");
        one.addGrade(new Grade(4.0,"21-04-2014"), "English");

        one.addGrade(new Grade(4.5,"20-03-2013"), "Math");
        one.addGrade(new Grade(3.5,"05-03-2013"), "Math");
        one.addGrade(new Grade(4.5,"24-06-2013"), "Math");
        one.addGrade(new Grade(5.0,"17-05-2013"), "Math");

        two.addGrade(new Grade(2.0,"20-03-2014"), "Spanish");
        two.addGrade(new Grade(3.0,"31-02-2014"), "Spanish");
        two.addGrade(new Grade(2.0,"01-03-2014"), "Spanish");

        two.addGrade(new Grade(4.0,"26-03-2013"), "Physics");
        two.addGrade(new Grade(3.5,"15-03-2013"), "Physics");
        two.addGrade(new Grade(4.5,"04-06-2013"), "Physics");
        two.addGrade(new Grade(5.0,"27-05-2013"), "Physics");

        two.addGrade(new Grade(5.0,"28-04-2013"), "Math");
        two.addGrade(new Grade(3.5,"06-03-2013"), "Math");
        two.addGrade(new Grade(4.0,"17-04-2013"), "Math");
        two.addGrade(new Grade(5.0,"09-05-2013"), "Math");

        if (datastore.getCount(Student.class)>0 || datastore.getCount(Course.class)>0)
        {

        }
        else
        {
            datastore.save(English);
            datastore.save(Spanish);
            datastore.save(Math);
            datastore.save(Physics);
            datastore.save(Physics2);

            datastore.save(one);
            datastore.save(two);
        }

    }
}
