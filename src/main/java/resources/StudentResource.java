package resources;


import com.mongodb.MongoClient;
import models.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("students")
public class StudentResource {

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Student> getAllStudents(@DefaultValue("") @QueryParam("name") String studentName,
                                        @DefaultValue("") @QueryParam("last name") String studentLastName)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        List<Student> list = new ArrayList<>();
        for (Student s : datastore.find(Student.class).asList())
        {
            if ((studentName.equals("") || s.getName().equals(studentName)) &&
                (studentLastName.equals("") || s.getLastName().equals(studentLastName)))
            {
                list.add(s);
            }
        }

        if (list.isEmpty())throw new NotFoundException();
        else return list;
    }

    @GET
    @Path("{index}")
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Student getStudent(@PathParam("index") int index)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found = datastore.find(Student.class, "index =", index).get();

        if (found!=null)
        {
            return found;
        }
        else throw new NotFoundException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewStudent(Student student) {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        datastore.save(student);

        return Response.created(URI.create("students/"+ student.getIndex())).build();
    }

    @PUT
    @Path("{index}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setStudent(@PathParam("index") int index, Student student) {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        student.setIndex(index);
        datastore.delete(datastore.createQuery(Student.class).filter("index =", index));
        datastore.save(student);

        String result = "Student: " + student + " updated";
        return Response.status(201).entity(result).build();

    }



    @DELETE
    @Path("{index}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteStudent(@PathParam("index") int index)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        Student found =  datastore.find(Student.class, "index =", index).get();

        if (found!=null)
        {
            datastore.delete(found);
            String result = "Student with index: " + found.getIndex()+" was removed";
            return Response.ok().entity(result).build();
        }
        else
        {
            String result = "Student not found";
            return Response.status(404).entity(result).build();
        }
    }
}
