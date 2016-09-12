"use strict";

var studentModel = function() {
    this.name = ko.observable('');
    this.lastName = ko.observable('');
    this.index = ko.observable(0);
    this.birthdate = ko.observable('');
};

var courseModel = function() {
    this.courseName = ko.observable('');
    this.teacher = ko.observable('');
}

var gradeModel = function() {
    this.mark=ko.observable(2);
    this.mindex=ko.observable(0);
    this.date=ko.observable('');
}


var student1 = new studentModel();
var student2 = new studentModel();

var course1 = new courseModel();
var course2 = new courseModel();

var grade1 = new gradeModel();
var grade2 = new gradeModel();

var studentTable = ko.observableArray();
studentTable.push(student1);
studentTable.push(student2);

var courseTable = ko.observableArray();
courseTable.push(course1);
courseTable.push(course2);

var gradeTable = ko.observableArray();
gradeTable.push(grade1);
gradeTable.push(grade2);


function addStudent(){
    studentTable.push(new studentModel())
}

function addCourse(){
    courseTable.push(new courseModel())
}

function addGrade(){
    gradeTable.push(new gradeModel())
}

//gradeModel.mark.subscribe(function(newValue) {
//    //alert("new Grade: " + newValue);
//});


