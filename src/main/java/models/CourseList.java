package models;

import java.util.ArrayList;
import java.util.Optional;

public class CourseList {
    private static ArrayList<Course> courseList;

    private CourseList() {
    }

    public static ArrayList<Course> getInstance() {
        if (courseList == null) {
            courseList = new ArrayList<>();
        }
        return courseList;
    }

    public static void addCourse(Course course) {
        if (!courseList.contains(course)) courseList.add(course);
    }

    public static Course getCourse(String name) {
        Optional<Course> match
                = courseList.stream()
                .filter(c -> c.getCourseName().equals(name))
                .findFirst();

        if (match.isPresent()) return match.get();
        else return null;
    }
}
