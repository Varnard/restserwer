"use strict";

var studentModel = function() {
    this.name = ko.observable('');
    this.lastName = ko.observable('');
    this.index = ko.observable();
    this.birthdate = ko.observable('');
};

var courseModel = function() {
    this.courseName = ko.observable('');
    this.teacher = ko.observable('');
}

var gradeModel = function() {
    this.mark=ko.observable(2);
    this.studentIndex=ko.observable();
    this.date=ko.observable('');
}

var studentTable = ko.observableArray();

var courseTable = ko.observableArray();;

var gradeTable = ko.observableArray();

var studentSearch = {
    searchName : ko.observable(''),
    searchLastName : ko.observable(''),
    searchIndex : ko.observable(),
    searchBirthdate : ko.observable('')
};

studentSearch.searchName.subscribe(function(){getSearchedStudents(studentSearch)});
studentSearch.searchLastName.subscribe(function(){getSearchedStudents(studentSearch)});
studentSearch.searchIndex.subscribe(function(){getSearchedStudents(studentSearch)});
studentSearch.searchBirthdate.subscribe(function(){getSearchedStudents(studentSearch)});

studentSearch.get = function(){
    let student = new studentModel();
    student.name(this.searchName());
    student.lastName(this.searchLastName());
    student.index(this.searchIndex());
    student.birthdate(this.searchBirthdate());
    
    return student;
}

var courseSearch = {
    searchCourseName : ko.observable(''),
    searchTeacher : ko.observable('')
};


courseSearch.searchCourseName.subscribe(function(){getSearchedCourses(courseSearch)});
courseSearch.searchTeacher.subscribe(function(){getSearchedCourses(courseSearch)});

courseSearch.get = function() {
    let course = new courseModel();
    course.courseName(this.searchCourseName());
    course.teacher(this.searchTeacher());
    
    return course;
}

var studentCoursesSearch = {
    searchCourseName : ko.observable(''),
    searchTeacher : ko.observable('')
};


studentCoursesSearch.searchCourseName.subscribe(function(){getSearchedStudentCourses(activeStudent,studentCoursesSearch)});
studentCoursesSearch.searchTeacher.subscribe(function(){getSearchedStudentCourses(activeStudent,studentCoursesSearch)});

studentCoursesSearch.get = function() {
    let course = new courseModel();
    course.courseName(this.searchCourseName());
    course.teacher(this.searchTeacher());
    
    return course;
}

var studentGradesSearch = {
    searchMark : ko.observable(''),
    searchDate : ko.observable('')
};


studentGradesSearch.searchMark.subscribe(function(){getSearchedStudentGrades(activeStudent,activeCourse)});
studentGradesSearch.searchDate.subscribe(function(){getSearchedStudentGrades(activeStudent,activeCourse)});

studentGradesSearch.get = function() {
    let grade = new gradeModel();    
    grade.studentIndex(activeStudent);
    grade.mark(this.searchMark);
    grade.date(this.searchDate);
    
    return grade;
}


var courseGradesSearch = {
    searchMark : ko.observable(''),
    searchDate : ko.observable(''),
    searchStudentIndex : ko.observable()
};


courseGradesSearch.searchMark.subscribe(function(){getSearchedCourseGrades(activeCourse,courseGradesSearch)});
courseGradesSearch.searchDate.subscribe(function(){getSearchedCourseGrades(activeCourse,courseGradesSearch)});
courseGradesSearch.searchStudentIndex.subscribe(function(){getSearchedCourseGrades(activeCourse,courseGradesSearch)});

courseGradesSearch.get = function() {
    let grade = new gradeModel();
    grade.studentIndex(this.searchStudentIndex);
    grade.mark(this.searchMark);
    grade.date(this.searchDate);
    
    return grade;
}

function addStudent(){
    studentTable.push(new studentModel())
}

function addCourse(){
    courseTable.push(new courseModel())
}

function addGrade(){
    gradeTable.push(new gradeModel())
}


