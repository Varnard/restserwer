package resources;


import com.mongodb.MongoClient;
import models.Student;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("students")
public class StudentResource {

    @GET
    @Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public List<Student> getAllStudents(@DefaultValue("") @QueryParam("name") String studentName,
                                        @DefaultValue("") @QueryParam("last name") String studentLastName,
                                        @DefaultValue("01-01-1970") @QueryParam("bornAfter") String bDateAfter,
                                        @DefaultValue("01-01-2070") @QueryParam("bornBefore") String bDateBefore,
                                        @DefaultValue("") @QueryParam("born") String bDate)
    {
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        List<Student> list = new ArrayList<>();
        for (Student s : datastore.find(Student.class).asList())
        {
            try {
                if (    (studentName.equals("") || s.getName().equals(studentName)) &&
                        (studentLastName.equals("") || s.getLastName().equals(studentLastName)) &&
                        s.getBirthDate().after(sdf.parse(bDateAfter)) &&
                        s.getBirthDate().before(sdf.parse(bDateBefore)) &&
                        (bDate.equals("") || s.getBirthDate().equals(sdf.parse(bDate))))
                {
                    list.add(s);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response createNewStudent(Student student) {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        int newIndex = datastore.find(Student.class).order("-index").limit(1).get().getIndex()+1;

        student.setIndex(newIndex);

        datastore.save(student);

        return Response.created(URI.create("students/"+ student.getIndex())).build();
    }

    @PUT
    @Path("{index}")
    @Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
    public Response setStudent(@PathParam("index") int index, Student student) {

        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost", 8004), "morphia_example");

        student.setIndex(index);
        datastore.delete(datastore.createQuery(Student.class).filter("index =", index));
        datastore.save(student);

        String result = "Student: " + student + " updated";
        return Response.status(200).entity(result).build();

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
