"use strict";

var settings = {
  "async": true,
  "crossDomain": true,
  "url": "http://localhost:8080/students",
  "method": "GET",
  "headers": {
    "accept": "application/json",
    "cache-control": "no-cache",
    "postman-token": "f93b5045-a63f-3db8-22dc-0674c255eaac"
  }
}

$.ajax(settings).done(function (response) {
  console.log(response);
});