"use strict";

var oArray = ko.observableArray();

var studentModel = function() {
    this.name = ko.observable('nameasdf');
    this.lastName = ko.observable('lastnameqwerty');
    this.index = ko.observable(0);
    this.birthdate = ko.observable('01-01-1901');
};

var student1 = new studentModel();
var student2 = new studentModel();

var tab = [student1,student2];

var studentModel2 = {
    name: ko.observable('Bobt'),
    lastName: ko.observable('123'),
    index: ko.observable(5),
    birthdate: ko.observable('12-05-1994')
};

ko.applyBindings(tab);

var studentsUrl = "http://localhost:8080/students/";

var studentTableHeader ="<tr><th>Name<form><input type=\"text\"placeholder=\"search\"></form></th><th>Last Name<form><input type=\"text\" placeholder=\"search\"></form></th><th>Index<form><input type=\"number\" placeholder=\"search\"></form></th><th>Birth date<form><input type=\"date\"></form></th></tr>";

var studentsArray = ko.observableArray();
var resource;

function get() {
    
var settings = {
  "url": "http://localhost:8080/students",
  "method": "GET",
  "headers": {
    "accept": "application/json"
  },
  "data": ""
}

$.ajax(settings).done(function (response) {
 resource=response;
});
    
}

function makeStudentTable(){
    get(studentsUrl,studentsArray);
    var myTable = studentTableHeader;
    
    studentsArray()[0].forEach(function(item){myTable+=studentRow(item);})
    
    document.getElementById('studentsTable').innerHTML = myTable;
}

function studentRow(newStudent) {
    var row = "<tr><td><form><input type=\"text\" value="+newStudent.name+" placeholder=\"First name\" required></form></td><td><form><input type=\"text\" value="+newStudent.lastName+" placeholder=\"Last name\" required></form></td><td><form> <input type=\"number\" value="+newStudent.index+" readonly></form></td><td><form><input type=\"date\" value="+newStudent.birthDate+"></form></td></tr>";
    return row;
}

