package resources;

import com.mongodb.MongoClient;
import models.Course;
import models.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

@Path("students/{index}/courses")
public class CourseResource {

    public boolean indexExist;
    public final String qwerty = "students/{index}/courses";

    public CourseResource()
    {
        indexExist=true;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public ArrayList<Course> getAllCourses(@PathParam("index")int index)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        if (found!=null)
        {
            return found.getCourseList();
        }
        else throw new NotFoundException();
    }


    @GET
    @Path("{courseName}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Course getCourse(@PathParam("index") int index, @PathParam("courseName") String courseName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        if (found==null)
        {
            throw new NotFoundException();
        }

        Optional<Course> match
                = found.getCourseList().stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst();
        if (match.isPresent())
        {
            return match.get();
        }
        else
        {
            throw new NotFoundException();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON )
    public Response addCourse(@PathParam("index") int index, Course course)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        if (found==null)
        {
            throw new NotFoundException();
        }

        found.getCourseList().add(course);
        datastore.save(found);

        return Response.created(URI.create("students/"+index+"/courses/"+ course.getCourseName())).build();
    }


    @PUT
    @Path("{courseName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setStudent(@PathParam("courseName") String courseName, @PathParam("index") int index, Course course)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        course.setCourseName(courseName);
        Optional<Course> match
                = found.getCourseList().stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst();
        if (match.isPresent())
        {
            found.getCourseList().remove(match.get());
        }

        found.getCourseList().add(course);
        datastore.save(found);

        String result = "Course: " + course + " updated";
        return Response.status(201).entity(result).build();

    }

    @DELETE
    @Path("{courseName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCourse(@PathParam("courseName") String courseName, @PathParam("index") int index)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        Optional<Course> match
                = found.getCourseList().stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst();
        if (match.isPresent())
        {
            String result = "Course: " + match.get().getCourseName()+" was removed";
            found.getCourseList().remove(match.get());
            datastore.save(found);
            return Response.ok().entity(result).build();
        }
        else
        {
            String result = "Course not found";
            return Response.status(404).entity(result).build();
        }
    }
}
