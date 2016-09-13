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

        ResourceConfig rc = new ResourceConfig(StudentResource.class, CourseResource.class,
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

        Student one = new Student("One", "First", 1 , "1994-12-05");
        Student two = new Student("Two", "Second", 2 ,"1995-02-02");

        one.addCourse(English);
        one.addCourse(Math);

        two.addCourse(Spanish);
        two.addCourse(Physics);
        two.addCourse(Math);

        one.addGrade(new Grade(3.5,"2014-03-10"), "English");
        one.addGrade(new Grade(3.0,"2014-03-11"), "English");
        one.addGrade(new Grade(4.0,"21-04-2014"), "English");

        one.addGrade(new Grade(4.5,"2013-03-20"), "Math");
        one.addGrade(new Grade(3.5,"2013-03-05"), "Math");
        one.addGrade(new Grade(4.5,"2013-06-24"), "Math");
        one.addGrade(new Grade(5.0,"2013-05-17"), "Math");

        two.addGrade(new Grade(2.0,"2014-03-20"), "Spanish");
        two.addGrade(new Grade(3.0,"2014-02-31"), "Spanish");
        two.addGrade(new Grade(2.0,"2014-03-01"), "Spanish");

        two.addGrade(new Grade(4.0,"2013-03-26"), "Physics");
        two.addGrade(new Grade(3.5,"2013-03-15"), "Physics");
        two.addGrade(new Grade(4.5,"2013-06-04"), "Physics");
        two.addGrade(new Grade(5.0,"2013-05-27"), "Physics");

        two.addGrade(new Grade(5.0,"2013-04-28"), "Math");
        two.addGrade(new Grade(3.5,"2013-03-06"), "Math");
        two.addGrade(new Grade(4.0,"2013-04-17"), "Math");
        two.addGrade(new Grade(5.0,"2013-05-09"), "Math");

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
