var express = require('express');


var app = express();
var searchRouter = require('./routes/search');
var latLngRouter = require('./routes/latLng');
var yelpRouter = require('./routes/yelp');
var indexRouter = require('./routes/index');
var detailRouter = require('./routes/details');

app.use('/public',express.static(__dirname +"/public"));
app.use('/search', searchRouter);
app.use('/latLng',latLngRouter);
app.use('/yelp',yelpRouter);
app.use('/details',detailRouter);
app.use('/', indexRouter);


app.listen(8081);





//
// var server = http.createServer(function (req, resp) {
//     if (req.url != "/favicon.ico") {
//         var queryObj = url.parse(req.url, true).query;
//         var keyword = queryObj.keyword;
//         var category = queryObj.category;
//         var distance = queryObj.distance;
//         var startLoc = queryObj.startLoc;
//         resp.writeHead(200, {'ContentType': 'text/html;charset=utf-8'});
//         resp.end("I received form request"+keyword+category+distance+startLoc);
//     }
//
// });
//
//


// var request = require('request');
// request('http://www.baidu.com', function (error, response, body) {
//   if (!error && response.statusCode == 200) {
//     console.log(body) // Show the HTML for the baidu homepage.
//   }
// })
