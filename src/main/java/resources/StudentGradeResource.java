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

@Path("students/{index}/courses/{courseName}/grades")
public class StudentGradeResource {

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public ArrayList<Grade> getAllGrades(@PathParam("index") int index, @PathParam("courseName") String courseName,
                                         @DefaultValue("2") @QueryParam("higherThan") double higherValue,
                                         @DefaultValue("5") @QueryParam("lowerThan") double lowerValue)
    {
        ArrayList<Grade> list = new ArrayList<>();

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        if (found==null)throw new NotFoundException();
        else
        {
            for (Grade grade : found.getGrades())
            {
                if (grade.getStudentIndex()==index && grade.getMark()>higherValue && grade.getMark()<lowerValue)
                {
                    grade.setCoursePath("false");
                    grade.setStudentPath("true");
                    list.add(grade);
                }
            }
        }

        if (list.isEmpty())throw new NotFoundException();
        else return list;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Grade getGrade(@PathParam("index") int index, @PathParam("courseName") String courseName, @PathParam("id") int id)
    {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        Optional<Grade> match
                = found.getGrades().stream()
                .filter(g -> g.getId()== id)
                .findFirst();

        if (match.isPresent() && match.get().getStudentIndex()==index)
        {
            match.get().setStudentPath("true");
            match.get().setCoursePath("flase");
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
    public Response deleteGrade(@PathParam("index") int index, @PathParam("courseName") String courseName, @PathParam("id") int id)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Course found = datastore.find(Course.class, "courseName =", courseName).get();

        Optional<Grade> match
                = found.getGrades().stream()
                .filter(g -> g.getId()== id)
                .findFirst();

        if (match.isPresent() && match.get().getStudentIndex()==index)
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
    public Response addGrade(@PathParam("index") int index, @PathParam("courseName") String courseName, Grade grade)
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
            int id = match.get().addGrade(grade);
            datastore.save(found);
            return Response.created(URI.create("students/" + index + "/courses/" + courseName + "/grades/" + id)).build();
        }

        else throw new NotFoundException();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setStudent(@PathParam("index") int index, @PathParam("courseName") String courseName, @PathParam("id") int id, Grade grade)
    {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        Optional<Course> matchc
                = found.getCourseList().stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst();

        Optional<Grade> match
                = matchc.get().getGrades().stream()
                .filter(g -> g.getId()== id)
                .findFirst();

        if (match.isPresent() && match.get().getStudentIndex()==index)
        {
            matchc.get().getGrades().remove(match.get());
        }

        matchc.get().getGrades().add(grade);
        datastore.save(found);
        String result = "Grade updated";
        return Response.status(201).entity(result).build();

    }

}
