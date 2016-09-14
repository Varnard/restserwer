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


ko.applyBindings(studentTable, document.getElementById("students"));
ko.applyBindings(courseTable, document.getElementById("courses"))
ko.applyBindings(courseTable, document.getElementById("studentCourses"));
ko.applyBindings(gradeTable, document.getElementById("grades"));
ko.applyBindings(gradeTable, document.getElementById("studentGrades"));