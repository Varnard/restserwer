package models;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import resources.StudentGradeResource;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;


@Embedded("grades")
@Indexes(
        @Index(value = "date", fields = @Field("date"))
)
@XmlRootElement
public class Grade {
    @InjectLinks({
            @InjectLink(value = "students/{index}/courses/{courseName}", rel = "Parent course"),
            @InjectLink(resource = StudentGradeResource.class, rel = "all Grades"),
            @InjectLink(value = "students/{index}/courses/{courseName}/grades/{id}", rel = "self"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name ="links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

    private int studentIndex;
    private double mark;
    private String date;
    private int id;

    public Grade(double mark, String date) {
        this.mark = mark;
        this.date = date;
    }

    public Grade() {
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //@XmlTransient
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentIndex() {
        return studentIndex;
    }

    public void setStudentIndex(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    @Override
    public String toString()
    {
       String grade="StudentIndex: " + studentIndex  + "\n"
                + "Mark: " + mark + "\n"
                + "Date: " + date + "\n"
                + "Id: " + id + "\n";



        return grade;
    }

}
