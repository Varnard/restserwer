package models;

import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinkNoFollow;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.*;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity("students")
@Indexes(
        @Index(value = "Last Name", fields = @Field("lastName"))
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
    ObjectId id;

    private int index;

    private String birthDate;

   // @Reference
    @InjectLinkNoFollow
    private ArrayList<Course> courseList;

    public Student(){}

    public Student(String name, String lastName, int index, String birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.index = index;
        this.birthDate = birthDate;
        this.courseList=new ArrayList<>();
    }

    public Student(String name, String lastName, int index, String birthDate, ArrayList<Course> courseList) {
        this.name = name;
        this.lastName = lastName;
        this.index = index;
        this.birthDate = birthDate;
        this.courseList = courseList;
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


    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
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
