package resources;

import com.mongodb.MongoClient;
import models.Course;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("courses")
public class CourseListResource {
    public boolean indexExist;

    public CourseListResource()
    {
        indexExist=false;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Course> getAllCourses(@DefaultValue("") @QueryParam("teacher") String teacherName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        List<Course> list = new ArrayList<>();

        for (Course c : datastore.find(Course.class).asList())
        {
            if (teacherName.equals("") || c.getTeacher().equals(teacherName))
            {
                c.setStudentPath("false");
                c.setCoursePath("true");
                list.add(c);
            }

        }

        if (list.isEmpty())throw new NotFoundException();
        else return list;
    }

    @GET
    @Path("{courseName}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public  Course getCourse(@PathParam("courseName") String courseName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        if (found!=null)
        {
            found.setCoursePath("true");
            found.setStudentPath("false");
            return found;
        }
        else throw new NotFoundException();
    }


}
