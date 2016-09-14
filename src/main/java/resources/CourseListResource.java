package resources;

import com.mongodb.MongoClient;
import models.Course;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("courses")
public class CourseListResource {
    public boolean indexExist;

    public CourseListResource()
    {
        indexExist=false;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Course> getAllCourses(@DefaultValue("") @QueryParam("teacher") String teacherName,
                                      @DefaultValue("") @QueryParam("courseName") String courseName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        List<Course> list = new ArrayList<>();

        for (Course c : datastore.find(Course.class).asList())
        {
            if (c.getTeacher().toLowerCase().contains(teacherName.toLowerCase()) &&
                    c.getCourseName().toLowerCase().contains(courseName.toLowerCase()))
            {
                c.setStudentPath("false");
                c.setCoursePath("true");
                list.add(c);
            }

        }

        /*if (list.isEmpty())throw new NotFoundException();
        else */
        return list.stream()
                .sorted((c1,c2)->new StringComparator().compare(c1.getCourseName(),c2.getCourseName()))
                .collect(Collectors.toList());
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

    @POST
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response createNewCourse(Course course) {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", course.getCourseName()).get();
        if (found!=null)
        {
            String result = "Course '" + course.getCourseName() + "' already exists.";
            return Response.status(400).entity(result).build();
        }
        else
        {
            datastore.save(course);
            return Response.created(URI.create("courses/"+ course.getCourseName())).build();
        }
    }

    @PUT
    @Path("{courseName}")
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response setCourse(@PathParam("courseName") String courseName, Course course) {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        datastore.delete(datastore.find(Course.class, "courseName =", courseName));
        datastore.save(course);

        if (courseName.equals(course.getCourseName()))
        {

            String result = "Course: '" + course.getCourseName() + "' updated";
            return Response.status(200).entity(result).build();
        }
        else
        {
            String result = "Course: '" + courseName + "' moved to /" +course.getCourseName();
            return Response.status(200).entity(result).build();
        }

    }


    @DELETE
    @Path("{courseName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCourse(@PathParam("courseName") String courseName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found =  datastore.find(Course.class, "courseName =", courseName).get();

        if (found!=null)
        {
            datastore.delete(found);
            String result = "Course: " + found.getCourseName()+" was removed";
            return Response.ok().entity(result).build();
        }
        else
        {
            String result = "Course not found";
            return Response.status(404).entity(result).build();
        }
    }


}
