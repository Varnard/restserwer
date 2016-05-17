package resources;

import models.Course;
import models.Grade;
import models.Student;
import models.StudentList;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Optional;

@Path("students/{index}/courses/{name}/grades")
public class GradeResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
    public Grade[] getAllGrades(@PathParam("index") int index, @PathParam("name") String name)
    {
        Optional<Student> matchs
                = StudentList.getInstance().stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();

        Optional<Course> matchc
                = matchs.get().getCourseList().stream()
                .filter(c -> c.getCourseName().equals(name))
                .findFirst();

        Course course= matchc.get();

        Grade[] allGrades= new Grade[course.getGrades().size()];

        for (int i=0;i<course.getGrades().size();i++)
        {
            allGrades[i]=course.getGrades().get(i);
        }
        return allGrades;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
    public Grade getGrade(@PathParam("index") int index, @PathParam("name") String name, @PathParam("id") int id)
    {
        Optional<Student> matchs
                = StudentList.getInstance().stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();

        Optional<Course> matchc
                = matchs.get().getCourseList().stream()
                .filter(c -> c.getCourseName().equals(name))
                .findFirst();


        Optional<Grade> match
                = matchc.get().getGrades().stream()
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
    public Response deleteGrade(@PathParam("index") int index, @PathParam("name") String name, @PathParam("id") int id)
    {
        Optional<Student> matchs
                = StudentList.getInstance().stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();

        Optional<Course> matchc
                = matchs.get().getCourseList().stream()
                .filter(c -> c.getCourseName().equals(name))
                .findFirst();


        Optional<Grade> match
                = matchc.get().getGrades().stream()
                .filter(g -> g.getId()== id)
                .findFirst();
        if (match.isPresent())
        {
            String result = "Grade removed";
            matchc.get().getGrades().remove(match.get());
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
    public Response addGrade(@PathParam("index") int index, @PathParam("name") String name, Grade grade)
    {
        Optional<Student> matchs
                = StudentList.getInstance().stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();

        Optional<Course> matchc
                = matchs.get().getCourseList().stream()
                .filter(c -> c.getCourseName().equals(name))
                .findFirst();

        Course course= matchc.get();

        int id = course.addGrade(grade);
        return Response.created(URI.create("students/"+index+"/courses/"+name+"/grades/"+id)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setStudent(@PathParam("index") int index, @PathParam("name") String name, @PathParam("id") int id, Grade grade)
    {

        Optional<Student> matchs
                = StudentList.getInstance().stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();

        Optional<Course> matchc
                = matchs.get().getCourseList().stream()
                .filter(c -> c.getCourseName().equals(name))
                .findFirst();


        Optional<Grade> match
                = matchc.get().getGrades().stream()
                .filter(g -> g.getId()== id)
                .findFirst();
        if (match.isPresent())
        {
            matchc.get().getGrades().remove(match.get());
        }

        matchc.get().getGrades().add(grade);
        String result = "Grade updated";
        return Response.status(200).entity(result).build();

    }

}
