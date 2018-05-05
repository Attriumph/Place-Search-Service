var express = require('express');
var router = express.Router();
var url = require('url');
var request = require('request');
// var bodyParser = require('body-parser');
// var jsonParser = bodyParser.json();

// router.get('/', jsonParser, function (req, res) {
router.get('/', function (req, res) {
    console.log("this is URL:" + req.url);
    myKey = "AIzaSyAAzyWWYDYdWqPNTCLN8jsfe16MzVsfSNY";
    // var keyword = req.body.keyword;
    // var category = req.body.category;
    // var distance = req.body.distance;
    // var startLoc = req.body.startLoc;
    var queryObj = url.parse(req.url, true).query;
    var endLoc = queryObj.endLoc;
    console.log("2nd qingqiu"+endLoc);
    var url_location = "https://maps.googleapis.com/maps/api/geocode/json?address=" + endLoc + "&key=" + myKey;
    // var geoArr = requestGeo();

    request(url_location, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                var objGeo = JSON.parse(body);
                var lat = objGeo.results[0]['geometry']['location']['lat'];
                var lng = objGeo.results[0]['geometry']['location']['lng'];
                console.log("geo:" + lat + "," + lng);
                res.send([lat,lng]);

            }
        })

});
    // request(url_location, function (error, response, body) {
    //     if (!error && response.statusCode == 200) {
    //         var objGeo = JSON.parse(body);
    //         var lat = objGeo.results[0]['geometry']['location']['lat'];
    //         var lng = objGeo.results[0]['geometry']['location']['lng'];
    //         console.log("222output geo:" + lat + "," + lng);
    //
    //     }
    // })


    // var url_entity = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=" + distance + "&type=" + category + "&keyword=" + keyword + "&key=" + myKey;
    // console.log(url_entity);
    // request(url_entity, function (error, response, body) {
    //     if (!error && response.statusCode == 200) {
    //         var objEntity = JSON.parse(body);
    //
    //         console.log(objEntity);
    //         // var geo =  {'lat':lat, 'lng':lng};
    //
    //     }
    // })
    // if(geoArr != undefined){
    //     console.log("333this is gerArr:"+geoArr);
    //     console.log("lat:"+geoArr[0]);
    //     // res.send("I received form request" + lng+","+ lat);
    //     res.send("I received form request" + geoArr[0]+","+ geoArr[1]);
    // }


    // res.send("I received form request" + keyword + category + distance + startLoc);

// function requestGeo(){
//     var geo = new Array();
//     var url_location = "https://maps.googleapis.com/maps/api/geocode/json?address="+startLoc+"&key="+myKey;
//     console.log(url_location+"111");
//     request(url_location, function (error, response, body) {
//         if (!error && response.statusCode == 200) {
//             var objGeo = JSON.parse(body);
//             var lat = objGeo.results[0]['geometry']['location']['lat'];
//             var lng = objGeo.results[0]['geometry']['location']['lng'];
//              console.log("222output geo:"+lat+","+lng);
//             // var geo =  {'lat':lat, 'lng':lng};
//             geo[0] = lat;
//             geo[1] = lng;
//             // console.log(geo);
//             return geo;
//         }
//     })
//
// }

module.exports = router;