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
    this.studentIndex=ko.observable(0);
    this.date=ko.observable('');
}


var studentTable = ko.observableArray();

var courseTable = ko.observableArray();;

var gradeTable = ko.observableArray();

function addStudent(){
    studentTable.push(new studentModel())
}

function addCourse(){
    courseTable.push(new courseModel())
}

function addGrade(){
    gradeTable.push(new gradeModel())
}


