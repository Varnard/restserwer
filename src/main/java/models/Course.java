package models;

import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinkNoFollow;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.*;
import resources.StudentGradeResource;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@Entity("courses")
@Indexes(
        @Index(value = "courseName", fields = @Field("courseName"))
)
@XmlRootElement
public class Course {

    @InjectLinks({
            @InjectLink(resource= resources.StudentResource.class, rel = "All students"),
            @InjectLink(resource= resources.CourseListResource.class, rel = "All courses"),
            @InjectLink(value = "", rel = "self", condition = ""),
          //  @InjectLink(value = "courses/{courseName}", rel ="self", condition = "{index}==null"),
           // @InjectLink(resource = resources.CourseResource.class, rel = "Student courses", condition = "{index}!=null"),
          //  @InjectLink(resource = resources.StudentGradeResource.class, rel= "grades", condition = "{index}!=null"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name ="links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

    private String teacher;
    private String courseName;

    @Id
    ObjectId id;

    @InjectLinkNoFollow
    private ArrayList<Grade> grades;
    private int gradesId;

    public Course() {
    }

    public Course(String teacher, String courseName) {
        this.teacher = teacher;
        this.courseName = courseName;
        this.grades = new ArrayList<>();
        gradesId=0;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @XmlAttribute
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @XmlTransient
    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    public int addGrade(Grade grade)
    {
        int id = gradesId;
        gradesId++;

        grade.setId(id);
        grades.add(grade);

        return id;
    }

    public int addGrade(Grade grade, int studentIndex)
    {
        int id = gradesId;
        gradesId++;

        grade.setId(id);
        grade.setStudentIndex(studentIndex);
        grades.add(grade);

        return id;
    }

    @Override
    public String toString()
    {
        String course="Course name: " + courseName + "\n"
                + "Teacher: " + teacher + "\n";

        return course;
    }

}
