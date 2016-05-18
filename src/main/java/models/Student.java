package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinkNoFollow;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.*;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity("students")
@Indexes(
        @Index(value = "Index", fields = @Field("index"))
)
@XmlRootElement
public class Student {

    @InjectLinks({
            @InjectLink(resource= resources.StudentResource.class, rel = "All students"),
            @InjectLink(value = "students/{index}/", rel = "self"),
            @InjectLink(resource = resources.CourseResource.class, rel = "courses"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name ="links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

    private String name;

    private String lastName;

    @Id
    private int index;

    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern="dd-MM-yyyy",timezone="CET")
    private Date birthDate;

    @Reference
    @InjectLinkNoFollow
    private ArrayList<Course> courseList;

    public Student(){}

    public Student(String name, String lastName, int index, String birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.index = index;
        this.courseList=new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        try
        {
            this.birthDate = sdf.parse(birthDate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlAttribute
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @XmlTransient
    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public void addGrade(Grade grade, String courseName)
    {
        Optional<Course> match
                = courseList.stream()
                .filter(c -> c.getCourseName().equals(courseName))
                .findFirst();
        if (match.isPresent())
        {
            grade.setStudentIndex(this.index);
           match.get().addGrade(grade);
        }

    }

    public void addCourse(Course course)
    {
        Optional<Course> match
                = courseList.stream()
                .filter(c -> c.equals(course))
                .findFirst();
        if (!match.isPresent())courseList.add(course);

    }

    @XmlTransient
    public Object getId() {
        return index;
    }

    protected void setId_(Object id) {
        index = (int)processId_(id);
    }

    protected static Object processId_(Object id) {
        return Integer.parseInt(id.toString());
    }

    @Override
    public String toString()
    {
        String student="Name: " + name + "\n"
                     + "Last name: " + lastName + "\n"
                     + "Index: " + index + "\n"
                     + "Born: " + birthDate + "\n";
        return student;
    }
}
