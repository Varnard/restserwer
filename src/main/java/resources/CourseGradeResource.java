package resources;

import com.mongodb.MongoClient;
import models.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

@Path("courses/{courseName}/grades")
public class CourseGradeResource {

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public ArrayList<Grade> getAllGrades(@PathParam("courseName") String courseName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        if (found!=null)
        {
            return found.getGrades();
        }
        else throw new NotFoundException();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Grade getGrade(@PathParam("index") int index, @PathParam("courseName") String courseName, @PathParam("id") int id)
    {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        if (found==null)
        {
            throw new NotFoundException();
        }

        Optional<Grade> match
                = found.getGrades().stream()
                .filter(g -> g.getId()== id)
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteGrade(@PathParam("courseName") String courseName, @PathParam("id") int id)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        if (found==null)
        {
            throw new NotFoundException();
        }

        Optional<Grade> match
                = found.getGrades().stream()
                .filter(g -> g.getId()== id)
                .findFirst();

        if (match.isPresent())
        {
            String result = "Grade removed";
            found.getGrades().remove(match.get());
            datastore.save(found);
            return Response.ok().entity(result).build();
        }
        else
        {
            String result = "Grade not found";
            return Response.status(404).entity(result).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGrade(@PathParam("courseName") String courseName, Grade grade)
    {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        if (found!=null)
        {
            int id = found.addGrade(grade);
            datastore.save(found);
            return Response.created(URI.create("courses/" + courseName + "/grades/" + id)).build();
        }
        else throw new NotFoundException();


    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setStudent(@PathParam("courseName") String courseName, @PathParam("id") int id, Grade grade)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        if (found==null)
        {
            throw new NotFoundException();
        }

        Optional<Grade> match
                = found.getGrades().stream()
                .filter(g -> g.getId()== id)
                .findFirst();

        if (match.isPresent())
        {
            found.getGrades().remove(match.get());
        }

        found.getGrades().add(grade);
        datastore.save(found);
        String result = "Grade updated";
        return Response.status(201).entity(result).build();

    }

}
