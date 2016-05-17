package models;

import java.util.ArrayList;
import java.util.Optional;

public class StudentList {
    private static ArrayList<Student> studentList;

    private StudentList(){}

    public static ArrayList<Student> getInstance()
    {
        if (studentList==null)
        {
            studentList= new ArrayList<>();
        }
        return studentList;
    }

    public static void addStudent(Student student)
    {
        if (!studentList.contains(student))studentList.add(student);
    }

    public static Student getStudent(int index)
    {
        Optional<Student> match
                = StudentList.getInstance().stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();
        if (match.isPresent())return match.get();
        else return null;
    }

}
