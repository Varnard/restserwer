"use strict";

getStudents();
getCourses();

var urlTreeModel = [{
path : ko.observable(''),
url : ko.observable('')
                    },
                   {
path : ko.observable(''),
url : ko.observable('')
                    },
                   {
path : ko.observable(''),
url : ko.observable('')
                    }]


var setUrlTree = function (newPath, newUrl)
{
    urlTreeModel[0].path(newPath);
    urlTreeModel[0].url(newUrl);
}

var activeStudent=0;

var activeCourse='';

var selectStudent = function(data){
    activeStudent=data.index();
    studentTable.valueHasMutated();
}

var selectCourse = function(data){
    activeCourse=data.courseName();
    courseTable.valueHasMutated();
}

ko.applyBindings(studentTable, document.getElementById("studentsTableBody"));
ko.applyBindings(courseTable, document.getElementById("coursesTableBody"));
ko.applyBindings(courseTable, document.getElementById("studentCoursesTableBody"));
ko.applyBindings(gradeTable, document.getElementById("gradesTableBody"));
ko.applyBindings(gradeTable, document.getElementById("studentGradesTableBody"));

ko.applyBindings(studentSearch, document.getElementById("studentsTableHead"));
ko.applyBindings(courseSearch, document.getElementById("coursesTableHead"));
ko.applyBindings(studentCoursesSearch, document.getElementById("studentCoursesTableHead"));
ko.applyBindings(courseGradesSearch, document.getElementById("gradesTableHead"));
ko.applyBindings(studentGradesSearch, document.getElementById("studentGradesTableHead"));