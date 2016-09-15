"use strict";
    
var studentsURL = "http://localhost:8080/students/";

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
            item.name.subscribe(function(){updateStudent(item)})
            item.lastName.subscribe(function(){updateStudent(item)})
            item.birthdate.subscribe(function(){updateStudent(item)})
                  });
    });
}

var getSearchedStudents = function(){
    
    var params = ko.mapping.toJS(studentSearch.get());
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": studentsURL,
        "data" : params,
        "method": "GET" ,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        studentTable(ko.mapping.fromJS(response)());
        studentTable().forEach(function(item){
            item.name.subscribe(function(){updateStudent(item)})
            item.lastName.subscribe(function(){updateStudent(item)})
            item.birthdate.subscribe(function(){updateStudent(item)})
                  });
    });
}

var getStudentCourses = function(index){
    var path = studentsURL+ index + '/courses/'
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": path,
        "method": "GET" ,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        courseTable(ko.mapping.fromJS(response)()); 
    });
}

var getSearchedStudentCourses = function(index){
    let params = ko.mapping.toJS(studentCoursesSearch.get());
    
    var path = studentsURL+ index + '/courses/'
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": path,
        "method": "GET",
        "data": params,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        courseTable(ko.mapping.fromJS(response)()); 
    });
}

var getStudentGrades = function(index, courseName){
    var path = studentsURL+ index + '/courses/' + courseName + '/grades/'
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": path,
        "method": "GET" ,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
       gradeTable(ko.mapping.fromJS(response)()); 
    });
}

var getSearchedStudentGrades = function(index, courseName){
    let params = ko.mapping.toJS(studentGradesSearch.get());
    
    var path = studentsURL+ index + '/courses/' + courseName + '/grades/';
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": path,
        "method": "GET" ,
        "data" : params,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
       gradeTable(ko.mapping.fromJS(response)()); 
    });
}

var updateStudent = function(student) {
    let index = student.index();
    
    var data = ko.toJS(student);
    delete data.link;
    
    var path = studentsURL + index;
    
    var settings = {
  "async": true,
  "crossDomain": true,
  "url": path,
  "method": "PUT",
  "headers": {
    "content-type": "application/json",
  },
  "processData": false,
  "data": JSON.stringify(data)
}

$.ajax(settings).done(function (response) {
  console.log(response);
    getStudents();
});
}

var addStudent = function() {
   var settings = {
  "async": true,
  "crossDomain": true,
  "url": studentsURL,
  "method": "POST",
  "headers": {
    "content-type": "application/json"
  },
  "processData": false,
  "data": "{}"
}

$.ajax(settings).done(function (response) {
  getStudents();
});
}

var deleteStudent = function(index) {
    var path = studentsURL + index;
    
    var settings = {
  "async": true,
  "crossDomain": true,
  "url": path,
  "method": "DELETE"
}

$.ajax(settings).done(function (response) {
  console.log(response);
  getStudents();
});
}

var coursesURL = "http://localhost:8080/courses/";

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
        courseTable().forEach(function(item){
            item.teacher.subscribe(function(){updateCourse(item)})
            item.courseName.subscribe(function(){updateCourse(item)})
                  });
    });
}


var getSearchedCourses = function(){
    var params = ko.mapping.toJS(courseSearch.get());
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": coursesURL,
        "method": "GET" ,
        "data" : params,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        courseTable(ko.mapping.fromJS(response)());
        courseTable().forEach(function(item){
            item.teacher.subscribe(function(){updateCourse(item)})
            item.courseName.subscribe(function(){updateCourse(item)})
                  });
    });
}


var getCourseGrades = function(courseName){
    var path = coursesURL+ courseName + '/grades/'
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": path,
        "method": "GET" ,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        gradeTable(ko.mapping.fromJS(response)()); 
    });
}

var getSearchedCourseGrades = function(courseName){
    let params = ko.mapping.toJS(courseGradesSearch.get());
    
    var path = coursesURL+ courseName + '/grades/'
    
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": path,
        "method": "GET" ,
        "data" : params,
        "headers": {
        "accept": "application/json"
        }
    }

    $.ajax(settings).done(function (response) {
        gradeTable(ko.mapping.fromJS(response)()); 
    });
}

var updateCourse = function(course) {
    let name = course.courseName();
    
    var data = ko.toJS(course);
    delete data.link;
    
    var path = coursesURL + name;
    
    var settings = {
  "async": true,
  "crossDomain": true,
  "url": path,
  "method": "PUT",
  "headers": {
    "content-type": "application/json",
  },
  "processData": false,
  "data": JSON.stringify(data)
}

$.ajax(settings).done(function (response) {
    console.log(response);
    getCourses();
});
}


var addCourse = function() {
    var settings = {
  "async": true,
  "crossDomain": true,
  "url": coursesURL,
  "method": "POST",
  "headers": {
    "content-type": "application/json"
  },
  "processData": false,
  "data": "{}"
}

$.ajax(settings).done(function (response) {
  getCourses();
});
}

var deleteCourse = function(courseName) {
    var path = coursesURL + courseName;
    
    var settings = {
  "async": true,
  "crossDomain": true,
  "url": path,
  "method": "DELETE"
}

$.ajax(settings).done(function (response) {
  console.log(response);
  getCourses();
});
}





