"use strict";
    
var studentsURL = "http://localhost:8080/students";

var coursesURL = "http://localhost:8080/courses";

var getStudents = function(){
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": studentsURL,
        "method": "GET" ,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        studentTable(ko.mapping.fromJS(response)());
        studentTable().forEach(function(item){
            alert(item.name())//.subscribe(function(){alert("xd")}))         
                  });
    });
}


var getCourses = function(){
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": coursesURL,
        "method": "GET" ,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        courseTable(ko.mapping.fromJS(response)());
    });
}
