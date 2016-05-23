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
import java.util.List;
import java.util.Optional;

@Path("students/{index}/courses")
public class CourseResource {

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Course> getAllCourses(@PathParam("index")int index)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        if (found!=null)
        {
            List<Course> list = found.getCourseList();
            for (Course c : list)
            {
                c.setStudentPath("true");
                c.setCoursePath("false");
            }
            return list;
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
            match.get().setStudentPath("true");
            match.get().setCoursePath("false");
            return match.get();
        }
        else
        {
            throw new NotFoundException();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response addCourse(@PathParam("index") int index, String courseName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        if (found==null)
        {
            String result = "Student not found";
            return Response.status(404).entity(result).build();
        }

        Course foundCourse = datastore.find(Course.class, "courseName =", courseName).get();
        if (foundCourse!=null)
        {
            Optional<Course> match
                    = found.getCourseList().stream()
                    .filter(c -> c.getCourseName().equals(courseName))
                    .findFirst();
            if (match.isPresent())
            {
                String result = "Student already has  the '" + courseName + "' course";
                return Response.status(400).entity(result).build();
            }
            else
            {
                found.addCourse(foundCourse);
                datastore.save(found);
            }
        }
        else
        {
            String result = "Course '" + courseName + "' does not exists. \nCreate it first in '/courses'";
            return Response.status(400).entity(result).build();
        }

        return Response.created(URI.create("students/"+index+"/courses/"+ courseName)).build();
    }


    @DELETE
    @Path("{courseName}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response deleteCourse(@PathParam("courseName") String courseName, @PathParam("index") int index)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        if (found==null)
        {
            String result = "Student not found";
            return Response.status(404).entity(result).build();
        }

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
