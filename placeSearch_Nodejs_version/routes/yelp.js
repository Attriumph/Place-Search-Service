'use strict';
var express = require('express');
var router = express.Router();
var url = require('url');
// var request = require('request');


const yelp = require('yelp-fusion');
var apiKey = "sv-_68LVc9O13dqgeUtEqsTJNzeMYQopjXb-YXGPRccsIp1Eq9big0VRNvlQAmTEHIxGDx0vnDCNSPJbSnen8wdQTegvnhkyZzLor2ep6-eRFZe3IDWSTz9g9aq5WnYx";
const client = yelp.client(apiKey);


// router.get('/', jsonParser, function (req, res) {
router.get('/', function (req, res) {
    console.log("this is yelp URL:" + req.url);

    var queryObj = url.parse(req.url, true).query;
    var bzName = queryObj.name;
    var state = queryObj.state;
    var city = queryObj.city;
    var address1 = queryObj.address1;
    var address2 = queryObj.address2;
    // var lat = queryObj.lat;
    // var lng = queryObj.lng;

    console.log("address is :"+address1+"address2:"+address2);
    client.businessMatch('best', {
        name: bzName,
        address1: address1,
        address2: address2,
        city: city,
        state: state,
        country:'US'
    }).then(res_search => {
        if (res_search.jsonBody.businesses.length !== 0) {
        console.log(res_search.jsonBody.businesses[0].id);
        var yelpId = res_search.jsonBody.businesses[0].id;
        client.reviews(yelpId).then(res_review => {
            console.log(res_review.jsonBody.reviews[0].text);
        res.send(res_review.jsonBody.reviews);
    }).catch(review_error => {
            console.log(review_error);
    });
    } else {
        res.send(res_search.jsonBody.businesses);
    }

}).catch(match_error => {
        console.log(match_error);
});
    // // matchType can be 'lookup' or 'best'
    // client.businessMatch('best', {
    //     name: 'Pannikin Coffee & Tea',
    //     city: 'Encinitas',
    //     state: 'CA',
    //     country: 'US'
    // }).then(response1 => {
    //     console.log(JSON.stringify(response1));
    //     var yelpId = response1.jsonBody.businesses[0].id;
    //     console.log(yelpId);
    //      client.reviews(yelpId).then(response2 => {
    //           console.log(response2.jsonBody.reviews[0].text);
    //         res.send(response2);
    //       }).catch(e2 => {
    //           console.log(e2);
    //       });
    //
    // }).catch(e1 => {
    //     console.log(e1);
    // });

    // res.send([lat,lng]);

});


module.exports = router;