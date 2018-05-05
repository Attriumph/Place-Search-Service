var app = angular.module('searchApp', ['ngResource', 'ngAnimate']);
var from;
var end;
var directionsDisplay;
var markers = [];
var panorama;
var entityLat;
var entityLng;
var resultsStack = new Array();

console.log("初始长度：" + resultsStack.length);
// create angular controller and pass in $scope and $http
app.controller('resultController',
    ["$scope", "$http", "$timeout", function ($scope, $http, $timeout) {
        //var formData = {};
        // process the form
        // $scope.infoPart = true;
        // $scope.photoPart = true;
        // $scope.mapPart = true;
        // $scope.reviewPart = true;
        var mydate = new Date();
        $scope.curDay = mydate.getDay();

        $scope.isLeft = false;
        $scope.pageNum = 0;
        $scope.highlightArray = [];
        for (var page = 0; page < Math.max((localStorage.length / 20 + 1), 3); page++) {
            $scope.highlightArray[page] = new Array();
            for (var i = 0; i < 20; i++) {
                $scope.highlightArray[page][i] = false;
            }
        }

        $scope.favor_arr = [];


        var autocomplete1;

        $scope.initAutocomplete = function () {

            autocomplete1 = new google.maps.places.Autocomplete((document.getElementById('id_location')), {types: ['geocode']});
            autocomplete1.addListener('place_changed', fillInAddress);
        }

        function fillInAddress() {
            // Get the place details from the autocomplete object.
            var place = autocomplete1.getPlace();

            console.log("zidong jieguo" + place.formatted_address);
            $scope.input_location = place.formatted_address;
            console.log(document.getElementById('id_location').value);

        }


        $scope.geolocate = function () {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    var geolocation = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    };
                    var circle = new google.maps.Circle({
                        center: geolocation,
                        radius: position.coords.accuracy
                    });
                    autocomplete1.setBounds(circle.getBounds());
                });
            }
        }


        $scope.submit = function () {
            $scope.favorHide = true;

            //set to default
            $scope.noPhoto = true;
            $scope.noReview = true;
            $scope.detailHide = true;

            $scope.failedPanel = true;
            $scope.favorHide = true;
            $scope.isLeft = false;
            console.log("hight array is " + $scope.highlightArray);
            console.log("run this method");
            $scope.noRecord = true;
            $scope.progressHide = false;
            $scope.preHide = true;
            $scope.favorBtn = "btn btn-default";
            $scope.resultBtn = "btn btn-primary";
            for (var page = 0; page < Math.max((localStorage.length / 20 + 1), 3); page++) {
                $scope.highlightArray[page] = new Array();
                for (var i = 0; i < 20; i++) {
                    $scope.highlightArray[page][i] = false;
                }
            }
            if ($scope.distance == undefined) {
                $scope.distance = 10;
            }
            if ($scope.startLoc == 'false') { // from other
                console.log("star loc:" + $scope.input_location);
                $http({
                    method: 'GET',
                    url: '/search',
                    params: {
                        keyword: $scope.keyword,
                        category: $scope.category,
                        distance: $scope.distance,
                        startLoc: $scope.input_location
                    },  // pass in data as strings
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then(function successCallback(response) {

                    $scope.progressHide = true;
                    console.log("this is response from backedn:" + JSON.stringify(response.data.results));
                    if (response.data.results.length !== 0) {

                        $scope.resultHide = false;
                        $scope.entities = response.data.results;
                        // resultsStack.push($scope.entities);
                        resultsStack.push(response.data);
                        console.log("search之后的长度" + resultsStack.length);

                        if (response.data.next_page_token !== undefined) {

                            $scope.pagination = response.data.next_page_token;
                            console.log("search后pagination is :" + $scope.pagination);
                            $scope.nextHide = false;

                        } else {
                            $scope.nextHide = true;
                        }
                    } else {
                        $scope.resultHide = true;
                        $scope.noRecord = false;
                    }

                }, function errorCallback(response) {
                    console.log("error request");
                    $scope.progressHide = true;
                    $scope.failedPanel = false;
                });
            } else {
                // console.log(myLat + "," + myLng);
                $http({
                    method: 'GET',
                    url: '/search',
                    params: {
                        keyword: $scope.keyword,
                        category: $scope.category,
                        distance: $scope.distance,
                        startLat: myLat,
                        startLng: myLng
                    },  // pass in data as strings
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then(function successCallback(response) {
                    $scope.progressHide = true;
                    console.log("response length:" + response.data.results.length);
                    if (response.data.results.length !== 0) {
                        console.log("response length:" + response.data.results.length);
                        $scope.resultHide = false;
                        $scope.entities = response.data.results;
                        // resultsStack.push($scope.entities);
                        resultsStack.push(response.data);
                        console.log("search之后的长度" + resultsStack.length);

                        if (response.data.next_page_token !== undefined) {
                            $scope.pagination = response.data.next_page_token;
                            console.log("search pagination is :" + $scope.pagination);
                            $scope.nextHide = false;
                        } else {
                            $scope.nextHide = true;
                        }
                    } else {
                        $scope.noRecord = false;
                    }
                    // console.log("this is response from backedn:" + JSON.stringify(response.data.results));

                }, function errorCallback(response) {
                    $scope.progressHide = true;
                    $scope.failedPanel = false;
                    console.log("error request");
                });
            }

        };
        $scope.removeAll = function () {
            // localStorage.clear();
            $scope.favorBtn = "btn btn-default";
            $scope.resultBtn = "btn btn-primary";
            $scope.chooseOther = false;
            $scope.input_location = "";
            document.getElementById("myForm").reset();
            // $scope.keyword = "";
            // $scope.category = "default";
            // $scope.distance = "";

            document.getElementById("here_radio").checked = "true";
            $scope.detailHide = true;
            $scope.favorHide = true;
            $scope.resultHide = true;
            $scope.noRecord = true;
        }

        $scope.nextPage = function () {
            $scope.pageNum += 1;
            console.log("pagination is :" + $scope.pagination);

            $http({
                method: 'GET',
                url: '/search/next',
                params: {
                    token: $scope.pagination
                },  // pass in data as strings
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function successCallback(response) {
                console.log("this is response from next page req:" + JSON.stringify(response.data.results));
                $scope.resultHide = false;
                $scope.entities = response.data.results;
                // resultsStack.push($scope.entities)
                resultsStack.push(response.data);
                console.log("next 数组长度：" + resultsStack.length);
                $scope.preHide = false;
                if (response.data.next_page_token !== undefined) {
                    $scope.pagination = response.data.next_page_token;
                    console.log("pagination is :" + $scope.pagination);
                    $scope.nextHide = false;
                } else {
                    $scope.nextHide = true;
                }

            }, function errorCallback(response) {
                $scope.detailHide = true;
                $scope.resultHide = true;
                $scope.failedPanel = false;
                console.log("error");
            });
        }

        $scope.prePage = function () {
            $scope.pageNum -= 1;
            console.log("pop is :" + resultsStack.pop());
            var currentEntities = resultsStack[resultsStack.length - 1];
            $scope.entities = currentEntities.results;
            $scope.pagination = currentEntities.next_page_token;
            $scope.nextHide = false;
            console.log("arr len：" + resultsStack.length);
            if (resultsStack.length === 1) {
                console.log("arr len：" + resultsStack.length);
                $scope.preHide = true;
            } else {
                $scope.preHide = false;
            }
        }
        $scope.detailed = function () {
            for (var page = 0; page < Math.max((localStorage.length / 20 + 1), 3); page++) {
                for (var i = 0; i < 20; i++) {
                    if ($scope.highlightArray[page][i] === true) {
                        return false;
                    }
                }
            }
            return true;
        }

        $scope.getDetails2 = function () {
            console.log("run getDeails");
            $scope.resultHide = true;
            $scope.detailHide = false;
        }
        $scope.getDetails3 = function () {
            console.log("run getDeails");
            $scope.favorHide = true;
            $scope.detailHide = false;
        }
        $scope.requestDetails = function (entity, index) {
            // $scope.showInfo();
            $scope.isLeft = true;
            $scope.favorHide = true;

            //set to default
            $scope.googleHide = false;
            $scope.phoneHide = false;
            $scope.priceHide = false;
            $scope.addressHide = false;
            $scope.rateHide = false;
            $scope.webHide = false;
            $scope.hourHide = false;

            //set to default
            $scope.noPhoto = true;
            $scope.noReview = true;
            $scope.detailHide = true;
            $scope.picUrl = "http://cs-server.usc.edu:45678/hw/hw8/images/Pegman.png";
            // $scope.reviewGoogle = true;
            // $scope.reviewYelp = true;
            // $scope.mapPart = true;
            // $scope.photoPart = true;
            document.getElementById("map-tab").setAttribute("class", "nav-item nav-link p-2");
            document.getElementById("photo-tab").setAttribute("class", "nav-item nav-link p-2");
            document.getElementById("review-tab").setAttribute("class", "nav-item nav-link p-2");
            document.getElementById("info-tab").setAttribute("class", "nav-item nav-link active p-2");
            document.getElementById("dropdownMenu1").innerText = "Google Review";
            document.getElementById("dropdownMenu2").innerText = "Default Order";
            $scope.travelMode = "DRIVING";

            console.log("index is :" + index);
            for (var page = 0; page < Math.max((localStorage.length / 20 + 1), 3); page++) {
                $scope.highlightArray[page] = new Array();
                for (var i = 0; i < 20; i++) {
                    $scope.highlightArray[page][i] = false;
                }
            }
            // console.log("hight array is "+$scope.highlightArray);
            // console.log(" highlightArray[index]:"+ highlightArray[index]);
            // console.log("the progress is :" + $scope.progressHide);
            // console.log("entity is " + JSON.stringify(entity));
            entityLat = entity['geometry']['location']['lat'];
            entityLng = entity['geometry']['location']['lng'];
            // console.log("entity lant and lng" + entityLat + "," + entityLng);
            entityName = entity['name'];
            console.log("entity Name is " + entityName);
            // var detailsDiv = document.createElement("div");
            // detailsDiv.setAttribute("id","detailsDiv");
            var request = {
                placeId: entity.place_id
            };

            var service = new google.maps.places.PlacesService(document.createElement("div"));
            service.getDetails(request, callback);
            $scope.highlightArray[$scope.pageNum][index] = true;

            // $scope.infoPart = false;

            function callback(place, status) {
                if (status == google.maps.places.PlacesServiceStatus.OK) {
                    $scope.showInfo();
                    console.log(place);

                    $scope.details = {
                        name: place.name,
                        address: place.formatted_address,
                        phone: place.international_phone_number,
                        price: place.price_level,
                        rating: place.rating,
                        website: place.website,
                        google_page: place.url,
                        photos: place.photos,
                        reviews: place.reviews,
                        vicinity: place.formatted_address,
                        icon: place.icon,
                        hours: place.opening_hours,
                        place_id: place.place_id

                    };
                    // if (place.opening_hours) {
                    //     console.log("has openinghous");
                    //     $scope.hourHide = true;
                    // }
                    if (place.formatted_address) {
                        console.log("add");
                        $scope.addressHide = true;
                    }
                    console.log("phone" + place.international_phone_number);
                    if (place.international_phone_number) {
                        $scope.phoneHide = true;
                    }
                    if (place.url) {
                        $scope.googleHide = true;
                    }
                    console.log("place website" + place.website);
                    if (place.website) {
                        $scope.webHide = true;
                    }
                    if (place.price_level) {
                        console.log("has prc");
                        $scope.priceHide = true;
                        var price_text = "";
                        for (var i = 0; i < place.price_level; i++) {
                            price_text += "$";
                        }
                        var timer2 = $timeout(function () {

                        }, 1000);

                        timer2.then(function () {
                            document.getElementById("price").innerHTML = price_text;
                        });
                        //document.getElementById("price").innerHTML = price_text;

                    }
                    if (place.rating) {
                        console.log("has rate");
                        //prepare for rating
                        $scope.rateHide = true;
                        var tempReview = $scope.details.reviews;
                        var star_text = "";
                        for (var i = 0; i < Math.ceil($scope.details.rating); i++) {
                            star_text += "<i class='star star_under fa fa-star'><i class='star star_over fa fa-star'></i></i>";
                        }
                        // console.log("111"+star_text);

                        var timer1 = $timeout(function () {

                        }, 1000);

                        timer1.then(function () {
                            document.getElementById("ratingIndex").innerHTML = star_text;
                            rateStyle($scope.details.rating, 'ratingIndex');
                        });

                        // document.getElementById("ratingIndex").innerHTML = star_text;
                        // console.log("222inner完" + document.getElementById("ratingIndex").innerHTML);
                        // rateStyle($scope.details.rating, 'ratingIndex');

                    }


                    // console.log("photos:"+place.photos);
                    //sort photos
                    if (place.photos) {
                        var orgPhotos = place.photos;
                        // console.log("orgPh"+orgPhotos);
                        $scope.photos = sortPhotos(orgPhotos);
                    } else {
                        $scope.photos = [];
                        $scope.noPhoto = false;
                    }


                    if (place.reviews) {
                        //prepare for review

                        for (var i = 0; i < tempReview.length; i++) {
                            tempReview[i]['time'] = moment.utc(new moment("1970-01-01 00:00:00")).add(tempReview[i]['time'], 's').format('YYYY-MM-DD HH:mm:ss');
                            console.log(tempReview[i]['time']);
                        }
                        $scope.reviewsG = tempReview.slice(0);
                        $scope.savedReviewG = $scope.reviewsG.slice(0);
                        // $scope.noReview = false;
                    } else {
                        console.log("No review");
                        $scope.reviewsG = [];
                        $scope.noReview = false;
                    }
                    $scope.status = "Open now:";
                    if (place.opening_hours) {
                        $scope.hourHide = true;

                        $scope.dates = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
                        var index = 0;
                        var tempDates = [];
                        for (var i = 0; i < 7; i++) {
                            if (i === $scope.curDay - 1) {
                                for (var j = i; j < 7; j++) {
                                    tempDates[index] = $scope.dates[j];
                                    index++;
                                }
                            }
                        }
                        console.log(tempDates);
                        for (var i = 0; index <= 6; i++) {
                            tempDates[index] = $scope.dates[i];
                            index++;
                        }
                        console.log(tempDates);
                        $scope.dates = tempDates;

                        $scope.openPeriods = [];
                        console.log(place.opening_hours.weekday_text[0].split(':'));
                        if (place.opening_hours.weekday_text[0].split(':').length !== 2) {
                            var index = 0;
                            for (var i = 0; i < place.opening_hours.weekday_text.length; i++) {
                                if (i === $scope.curDay - 1) {
                                    for (var j = i; j < 7; j++) {
                                        var splitedArr = place.opening_hours.weekday_text[j].split(":");
                                        $scope.openPeriods[index] = splitedArr[1] + ":" + splitedArr[2] + ":" + splitedArr[3];
                                        console.log("period:" + $scope.openPeriods[i]);
                                        index++;
                                    }
                                }
                            }
                            for (var i = 0; index <= 6; i++) {
                                var splitedArr = place.opening_hours.weekday_text[i].split(":");
                                $scope.openPeriods[index] = splitedArr[1] + ":" + splitedArr[2] + ":" + splitedArr[3];
                                index++;
                            }

                            $scope.curPeriod = $scope.openPeriods[$scope.curDay - 1];
                        } else if (place.opening_hours.weekday_text[6].split(':')[1].trim() === "Open 24 hours") {
                            for (var i = 0; i < place.opening_hours.weekday_text.length; i++) {
                                $scope.openPeriods[i] = "Open 24 hours";
                            }
                            $scope.curPeriod = "Open 24 hours";
                        }
                        // else {
                        //     console.log("closed on weekend");
                        //     var index = 0;
                        //     for (var i = 0; i < 5; i++) {
                        //         if ( i === $scope.curDay - 1) {
                        //             for (var j = i; j < 5; j++) {
                        //                 var splitedArr = place.opening_hours.weekday_text[j].split(":");
                        //                 $scope.openPeriods[index] = splitedArr[1] + ":" + splitedArr[2] + ":" + splitedArr[3];
                        //                 console.log("period:" + $scope.openPeriods[i]);
                        //             }
                        //         }
                        //
                        //     }
                        //     $scope.openPeriods[5] = "Closed";
                        //     $scope.openPeriods[6] = "Closed";
                        //     $scope.curPeriod = $scope.openPeriods[$scope.curDay - 1];
                        //  }

                        if (place.opening_hours.open_now == false) {
                            $scope.curPeriod = "";
                            $scope.status = "Closed";
                        }
                    }


                    //prepare for price level
                    // var price_text = "";
                    // for(var i = 0; i < place.price_level; i++) {
                    //     price_text += "$";
                    // }
                    // document.getElementById("price").innerHTML = price_text;
                    // var tempReview = $scope.details.reviews;
                    // //prepare for rating
                    //  var star_text = "";
                    //  for (var i = 0; i < Math.ceil($scope.details.rating); i++) {
                    //      star_text += "<i class='star star_under fa fa-star'><i class='star star_over fa fa-star'></i></i>";
                    //  }
                    //  // console.log("111"+star_text);
                    //  document.getElementById("ratingIndex").innerHTML = star_text;
                    //  // console.log("222inner完" + document.getElementById("ratingIndex").innerHTML);
                    //  rateStyle($scope.details.rating, 'ratingIndex');

                    //prepare for review
                    //  for (var i = 0; i < tempReview.length; i++) {
                    //      tempReview[i]['time'] = moment.utc(new moment("1970-01-01 00:00:00")).add(tempReview[i]['time'], 's').format('YYYY-MM-DD HH:mm:ss');
                    //      console.log(tempReview[i]['time']);
                    //  }
                    //  $scope.reviewsG = tempReview.slice(0);
                    //  $scope.savedReviewG = $scope.reviewsG.slice(0);
                    // console.log("photo url is  2222 " + place.photos[0].getUrl({'maxWidth': place.photos[0].width, 'maxHeight': place.photos[0].height}));
                    // console.log(JSON.stringify($scope.details));
                    // $scope.status = "Open";
                    // if (place.opening_hours){
                    //     if (place.opening_hours.open_now == false) {
                    //         $scope.status = "Closed";
                    //     }
                    // }


                }
                // else {
                //     $scope.resultHide = true;
                //     $scope.failedPanel = false;
                // }
            }


            var timer = $timeout(function () {
                console.log('details ready');
            }, 700);

            timer.then(function () {
                // $scope.mapDiv = true;
                // $scope.reviewDiv = true;
                // $scope.photoDiv = true;
                // if ($scope.entities.length !== 0){
                if ($scope.details) {
                    $scope.resultHide = true;
                    $scope.detailHide = false;
                } else {
                    $scope.resultHide = true;
                    $scope.failedPanel = false;
                }
                // $scope.resultHide = true;
                // $scope.detailHide = false;


                // $scope.info = false;
            });


        }


        $scope.setStar = function (num) {
            return new Array(num);
        }


        $scope.goBack = function () {
            // $scope.photoPart = true;
            // $scope.mapPart = true;
            // $scope.reviewPart = true;
            // $scope.infoPart = false;
            //
            // //

            if (document.getElementById("resultsBtn").getAttribute("class") == "btn btn-primary") {
                //on result view
                $scope.detailHide = true;
                $scope.resultHide = false;
            } else {
                $scope.detailHide = true;
                $scope.favorHide = false;
            }
            // $scope.detailHide = true;
            // $scope.resultHide = false;
        }
        $scope.showReview = function () {

            $scope.photoPart = true;
            $scope.mapPart = true;
            $scope.infoPart = true;
            $scope.reviewPart = false;
            $scope.noReview = true;
            // if($scope.savedReviewG){
            if ($scope.reviewsG && $scope.reviewsG.length !== 0) {
                // $scope.noReview = true;
                $scope.reviewYelp = true;
                $scope.reviewGoogle = false;
            } else {
                console.log("No review");
                $scope.reviewYelp = true;
                $scope.reviewGoogle = true;
                $scope.noReview = false;
            }

            // $scope.infoPart = true;
            // $scope.photoPart = true;
            // $scope.mapPart = true;
            // $scope.reviewPart = false;

        }
        //
        $scope.showInfo = function () {

            $scope.photoPart = true;
            $scope.mapPart = true;
            $scope.reviewPart = true;
            $scope.infoPart = false;
        }
        $scope.showPhoto = function () {
            console.log("show photo");

            $scope.mapPart = true;
            $scope.reviewPart = true;
            $scope.infoPart = true;
            $scope.photoPart = false;
            console.log($scope.photoPart);
        }

        $scope.showMap = function () {
            console.log("review" + JSON.stringify($scope.details.reviews));
            $scope.reviewPart = true;
            $scope.infoPart = true;
            $scope.photoPart = true;
            $scope.mapPart = false;
            $scope.toWhere = entityName + "," + $scope.details.address;
            if ($scope.startLoc == 'false') { // from other
                $scope.fromWhere = $scope.input_location;
            } else {
                $scope.fromWhere = "Your Location";
            }

            // console.log("from statLoc" + $scope.startLoc)
            // if ($scope.startLoc == 'true') {
            //     from = {lat: myLat, lng: myLng};
            //     // console.log("dingyi le from" + from);
            //     createMap();
            // } else {
            $http({
                method: 'GET',
                url: '/latLng',
                params: {
                    endLoc: $scope.details.address
                    // startLoc: $scope.details.address
                },  // pass in data as strings
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function successCallback(response) {
                console.log("latLng :" + JSON.stringify(response.data));
                console.log("this is from" + response.data[0]);
                // from = {lat: response.data[0], lng: response.data[1]};
                end = {lat: response.data[0], lng: response.data[1]};
                createMap();

            }, function errorCallback(response) {
                $scope.detailHide = true;
                $scope.resultHide = true;
                $scope.failedPanel = false;
                console.log("error");
            });


            // }

        }
        $scope.showDir = function () {
            console.log("from statLoc" + $scope.startLoc);
            if ($scope.startLoc == 'true') {
                from = {lat: myLat, lng: myLng};
            } else {
                $http({
                    method: 'GET',
                    url: '/latLng',
                    params: {
                        endLoc: $scope.input_location
                    },  // pass in data as strings
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then(function successCallback(response) {
                    console.log("latLng :" + JSON.stringify(response.data));
                    console.log("this is from" + response.data[0]);
                    // from = {lat: response.data[0], lng: response.data[1]};
                    from = {lat: response.data[0], lng: response.data[1]};

                }, function errorCallback(response) {
                    $scope.detailHide = true;
                    $scope.resultHide = true;
                    $scope.failedPanel = false;
                    console.log("error");
                });
            }

            var start_lat = from.lat;
            var start_lng = from.lng;
            var start;
            var travel_mode;
            deleteMarkers();
            console.log("fromWhere is :" + $scope.fromWhere);
            if ($scope.fromWhere == "Your Location" || $scope.fromWhere == "My location") {
                start = new google.maps.LatLng(start_lat, start_lng);
            } else {
                console.log("fromWhere is :" + $scope.fromWhere);
                start = $scope.fromWhere;
            }
            // travelMode
            // if ($scope.travelMode == undefined) {
            //     travel_mode = 'DRIVING';
            // } else {
            //     travel_mode = $scope.travelMode;
            // }
            travel_mode = $scope.travelMode;
            var end = new google.maps.LatLng(entityLat, entityLng);
            var request = {
                origin: start,
                destination: end,
                travelMode: travel_mode,
                provideRouteAlternatives: true
            };
            console.log("show Dir time22");
            var directionsService = new google.maps.DirectionsService;

            directionsService.route(request, function (result, status) {
                if (status == 'OK') {
                    console.log("show Dir time333");
                    directionsDisplay.setDirections(result);
                }
            });
        }
        $scope.picUrl = "http://cs-server.usc.edu:45678/hw/hw8/images/Pegman.png";
        $scope.toggleStreetView = function () {
            console.log("see the street view");
            $scope.picUrl = "http://cs-server.usc.edu:45678/hw/hw8/images/Map.png";
            var toggle = panorama.getVisible();

            if (toggle == false) {
                panorama.setVisible(true);
                $scope.picUrl = "http://cs-server.usc.edu:45678/hw/hw8/images/Map.png";
            } else {
                panorama.setVisible(false);
                $scope.picUrl = "http://cs-server.usc.edu:45678/hw/hw8/images/Pegman.png";
            }
        }

        $scope.toHere = function () {
            // document.getElementById("id_location").required = false;
            $scope.chooseOther = false;
            document.getElementById("id_location").value = "";
            document.getElementById("id_location").disabled = true;
            $scope.startLoc = 'true';
        }

        $scope.toOther = function () {
            // document.getElementById("id_location").required = true;
            document.getElementById("id_location").value = "";
            $scope.chooseOther = true;
            console.log(document.getElementById("id_location").required);
            document.getElementById("id_location").disabled = false;
            $scope.startLoc = 'false';

        }

        function clearMarkers() {
            setMapOnAll(null);
        }

        function deleteMarkers() {
            clearMarkers();
            markers = [];
        }

        function setMapOnAll(map) {
            for (var i = 0; i < markers.length; i++) {
                markers[i].setMap(map);
            }
        }

        function createMap() {
            console.log("this is from" + from);

            // var markers = [];
            directionsDisplay = new google.maps.DirectionsRenderer;
            var map = new google.maps.Map(document.getElementById("map"), {
                zoom: 14,
                center: end
            });
            var marker = new google.maps.Marker({
                position: end,
                map: map
            });
            var end_view = {lat: entityLat, lng: entityLng};
            markers.push(marker);
            directionsDisplay.setMap(map);
            panorama = map.getStreetView();
            panorama.setPosition(end_view);
            panorama.setPov(/** @type {google.maps.StreetViewPov} */({
                heading: 265,
                pitch: 0
            }));

            directionsDisplay.setPanel(document.getElementById('route_panel'));
            $scope.mapDiv = false;
        }

        var autocomplete2;


        $scope.initAutocomplete2 = function () {

            autocomplete2 = new google.maps.places.Autocomplete((document.getElementById('start_location')), {types: ['geocode']});
            autocomplete2.addListener('place_changed', fillInAddress2);
        }

        function fillInAddress2() {
            // Get the place details from the autocomplete object.
            var place = autocomplete2.getPlace();
            console.log("zidong jieguo" + JSON.stringify(place));
            console.log("zidong jieguo" + place.formatted_address);
            $scope.fromWhere = place.formatted_address;
            // console.log("zidong jieguo"+{{$scope.input_location}});

        }

        $scope.geolocate2 = function () {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    var geolocation = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    };
                    var circle = new google.maps.Circle({
                        center: geolocation,
                        radius: position.coords.accuracy
                    });
                    autocomplete2.setBounds(circle.getBounds());
                });
            }
        }

        // $scope.favorBtnChange = function (event) {
        //
        //     if (event.currentTarget.children[0].getAttribute("class") === 'fa fa-star-o'){
        //         event.currentTarget.children[0].setAttribute("class", "fa fa-star")
        //         event.currentTarget.children[0].style.color = "tomato";
        //
        //     } else { // delete favor
        //         event.currentTarget.children[0].setAttribute("class", "fa fa-star-o")
        //         event.currentTarget.children[0].style.color = "black";
        //     }
        //
        //     console.log(event.currentTarget);
        // }
        $scope.favorChange = function (entity, event) {

            if (event.currentTarget.children[0].getAttribute("class") === 'fa fa-star-o noFavor') { //add favor
                event.currentTarget.children[0].setAttribute("class", "fa fa-star favorStar")
                // event.currentTarget.children[0].style.color = "yellow";
                $scope.favor_arr[entity.place_id] = true;
                console.log(entity.place_id);
                console.log($scope.favor_arr[entity.place_id]);
                // localStorage.setItem(entity['place_id'], JSON.stringify(entity));
                var curtime = new Date().getTime();
                localStorage.setItem(entity['place_id'], JSON.stringify({val: entity, time: curtime}));
                console.log("add " + entity['name'] + " as favor");
            } else { // delete favor
                event.currentTarget.children[0].setAttribute("class", "fa fa-star-o noFavor")
                // event.currentTarget.children[0].style.color = "black";
                localStorage.removeItem(entity['place_id']);
                $scope.favor_arr[entity.place_id] = false;
                console.log("delete " + entity['name'] + " from favor");
            }

            // console.log(event.currentTarget);
        }
        // $scope.addFavor = function (entity,event) {
        //     console.log(event.currentTarget.children[0].getAttribute("class"));
        //     console.log("entity is "+JSON.stringify(entity));
        //     $scope.favor_arr[entity.place_id] = true;
        //     console.log(entity.place_id);
        //     console.log($scope.favor_arr[entity.place_id]);
        //     localStorage.setItem(entity['place_id'], JSON.stringify(entity));
        //     console.log("add" + entity['name'] + "as favor");
        // }
        // $scope.isFavor = function (place_id) {
        //     var favors = new Array();
        //     for (var i = 0; i < localStorage.length; i++) {
        //         console.log(localStorage.key(i));
        //         var value = localStorage.getItem(localStorage.key(i));
        //         var jsonValue = JSON.parse(value);
        //         favors[i] = jsonValue;
        //     }
        //
        //     for (var i = 0; i < favors.length; i++) {
        //         if (favors[i]['place_id']== place_id) {
        //             return true;
        //         }
        //     }
        //
        //     return false;
        // }
        $scope.deleteFavor = function (entity) {
            // event.target.setAttribute("class", "far fa-star");
            var curFavorPage = 0;
            for (var i = 0; i < $scope.favorsArray.length; i++) {
                for (var j = 0; j < $scope.favorsArray[i].length; j++) {
                    console.log("cur id:" + $scope.favorsArray[i][j]['val']['place_id']);
                    if ($scope.favorsArray[i][j]['val']['place_id'] === entity['place_id']) {
                        console.log("find page" + i);
                        curFavorPage = i;
                    }
                }
            }

            localStorage.removeItem(entity['place_id']);
            console.log("remove:" + entity['place_id']);
            $scope.favor_arr[entity.place_id] = false;
            // $scope.showFavor();
            //form new sorted Array
            var favors = new Array();
            for (var i = 0; i < localStorage.length; i++) {
                console.log(localStorage.key(i));
                var value = localStorage.getItem(localStorage.key(i));
                var jsonValue = JSON.parse(value);
                favors[i] = jsonValue;
                console.log("val:" + JSON.stringify(jsonValue.val));
                console.log("val:" + jsonValue.val);

            }


            favors.sort(function (a, b) {
                if (a.time > b.time) {
                    return 1;
                }
                if (a.time < b.time) {
                    return -1;
                }

                return 0;
            });
            if (favors.length === 0) {
                $scope.favorHide = true;
                $scope.noRecord = false;

            } else {
                $scope.favorsArray = packFavors(favors);
                if ($scope.favorsArray[curFavorPage]) {
                    $scope.favors = $scope.favorsArray[curFavorPage];
                } else {
                    $scope.favors = $scope.favorsArray[curFavorPage - 1];
                }

                console.log("CUr favors pagae cotent:" + JSON.stringify($scope.favors));
                $scope.favorHide = false;
            }

        }
        $scope.showFavor = function () {

            $scope.detailHide = true;
            $scope.favorBtn = "btn btn-primary";
            // console.log($scope.favorBtn);
            $scope.resultBtn = "btn btn-default";

            for (var page = 0; page < Math.max((localStorage.length / 20 + 1), 3); page++) {
                $scope.highlightArray[page] = new Array();
                for (var i = 0; i < 20; i++) {
                    $scope.highlightArray[page][i] = false;
                }
            }
            // $scope.detailHide = true;
            if (localStorage.length === 0) {
                $scope.favorHide = true;
                $scope.detailHide = true;
                $scope.resultHide = true;
                $scope.noRecord = false;

                console.log("lenth == 0");
            } else {
                console.log("lenth != 0");
                var favors = new Array();
                for (var i = 0; i < localStorage.length; i++) {
                    console.log(localStorage.key(i));
                    var value = localStorage.getItem(localStorage.key(i));
                    var jsonValue = JSON.parse(value);
                    favors[i] = jsonValue;
                    console.log("val:" + JSON.stringify(jsonValue.val));
                    console.log("val:" + jsonValue.val);

                }


                favors.sort(function (a, b) {
                    if (a.time > b.time) {
                        return 1;
                    }
                    if (a.time < b.time) {
                        return -1;
                    }

                    return 0;
                });

                $scope.favorsArray = packFavors(favors);
                // $scope.savedFavors = favors.slice(0);
                console.log("favorsArray[0]:" + $scope.favorsArray[0]);
                $scope.favors = $scope.favorsArray[0];
                console.log($scope.favors);
                // $scope.favLenSaved = favors.length;
                // $scope.favorLen = favors.length;
                $scope.pageNum = 0;
                console.log($scope.favors);
                console.log($scope.favors.length);
                $scope.resultHide = true;
                $scope.favorHide = false;

                // $scope.favorsArray = packFavors (favors);
                // // $scope.savedFavors = favors.slice(0);
                // $scope.favors = $scope.favorsArray[0];
                // console.log($scope.favors);
                // // $scope.favLenSaved = favors.length;
                // // $scope.favorLen = favors.length;
                // $scope.pageNum = 0;
                // console.log($scope.favors);
                // console.log($scope.favors.length);
                // $scope.resultHide = true;
                // $scope.favorHide = false;
            }


        }


        $scope.favorNextPage = function () {
            $scope.pageNum += 1;
            $scope.favors = $scope.favorsArray[$scope.pageNum];
            // $scope.indexStart += 20;
            // $scope.favors = $scope.savedFavors.slice($scope.indexStart,$scope.indexStart+20)
        }

        $scope.favorPrePage = function () {
            $scope.pageNum -= 1;
            $scope.favors = $scope.favorsArray[$scope.pageNum];

        }
        $scope.showResult = function (event) {
            $scope.favorBtn = "btn btn-default";
            $scope.resultBtn = "btn btn-primary";
            $scope.favorHide = true;
            $scope.detailHide = true;
            $scope.noRecord = true;
            if ($scope.entities !== undefined) {
                $scope.resultHide = false;
            } else {
                $scope.noRecord = true
            }


        }
        $scope.googleReview = function () {
            $scope.noReview = true;
            $scope.reviewYelp = true;
            if ($scope.savedReviewG) {
                $scope.reviewGoogle = false;
            } else {
                $scope.noReview = false;
            }

            document.getElementById("dropdownMenu1").innerText = "Google Review";

        }
        $scope.yelpReview = function () {
            $scope.reviewGoogle = true;
            $scope.noReview = true;
            document.getElementById("dropdownMenu1").innerText = "Yelp Review";
            console.log("request yelp");
            var arr = ($scope.details.address).split(",");
            console.log(arr);
            var address1 = arr[0];
            var address2 = arr[1] + "," + arr[2];
            var city = arr[arr.length - 3].trim();
            var arr2 = arr[arr.length - 2].split(" ");
            var state = arr2[1];
            console.log($scope.details.name + "," + city + "," + state);
            $http({
                method: 'GET',
                // url: '/yelp/'+entityName+'/'+address1+'/'+address2+'/'+city+'/'+state+'/US',
                url: '/yelp',
                params: {
                    name: entityName,
                    address: $scope.details.address,
                    city: city,
                    state: state,
                    address1: address1,
                    address2: address2
                },  // pass in data as strings
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function successCallback(response) {
                console.log(response.data);
                if (response.data.length !== 0) {
                    console.log("this is response from yelp:" + JSON.stringify(response));
                    console.log("this is response from yelp:" + JSON.stringify(response.data));
                    $scope.reviewGoogle = true;
                    $scope.noReview = true;
                    $scope.reviewsY = response.data;
                    $scope.savedReviewY = $scope.reviewsY.slice(0);
                    console.log(JSON.stringify($scope.reviewsY));
                    $scope.reviewYelp = false;
                } else {
                    console.log("No record");
                    $scope.reviewGoogle = true;
                    $scope.noReview = false;
                }

            }, function errorCallback(response) {
                $scope.detailHide = true;
                $scope.resultHide = true;
                $scope.failedPanel = false;
                console.log("yelp Error");

            });
        }
        $scope.defaultSort = function () {
            document.getElementById("dropdownMenu2").innerText = "Default Order";
            if ($scope.reviewGoogle === true) { //sort yelp review
                $scope.reviewsY = $scope.savedReviewY.slice(0);
                $scope.reviewsG = $scope.savedReviewG.slice(0);
            } else {
                $scope.reviewsG = $scope.savedReviewG.slice(0);
            }
        }

        $scope.rateHighSort = function () {
            document.getElementById("dropdownMenu2").innerText = "Highest Rating";
            if ($scope.reviewGoogle === true) { //sort yelp review
                var tempReview = $scope.reviewsY;
                tempReview.sort(function (a, b) {
                    if (a.rating > b.rating) {
                        return -1;
                    }
                    if (a.rating < b.rating) {
                        return 1;
                    }
                    return 0;
                });
                $scope.reviewsY = tempReview;

                var tempReview2 = $scope.reviewsG;
                tempReview2.sort(function (a, b) {
                    if (a.rating > b.rating) {
                        return -1;
                    }
                    if (a.rating < b.rating) {
                        return 1;
                    }
                    return 0;
                });
                $scope.reviewsG = tempReview2;
            } else {
                var tempReview = $scope.details.reviews;
                tempReview.sort(function (a, b) {
                    if (a.rating > b.rating) {
                        return -1;
                    }
                    if (a.rating < b.rating) {
                        return 1;
                    }
                    return 0;
                });
                $scope.reviewsG = tempReview;
            }
        }

        $scope.rateLowSort = function () {
            document.getElementById("dropdownMenu2").innerText = "Lowest Rating";
            if ($scope.reviewGoogle === true) { //sort yelp review
                var tempReview = $scope.reviewsY;
                tempReview.sort(function (a, b) {
                    if (a.rating > b.rating) {
                        return 1;
                    }
                    if (a.rating < b.rating) {
                        return -1;
                    }

                    return 0;
                });
                $scope.reviewsY = tempReview;

                var tempReview2 = $scope.reviewsG;
                tempReview2.sort(function (a, b) {
                    if (a.rating > b.rating) {
                        return 1;
                    }
                    if (a.rating < b.rating) {
                        return -1;
                    }

                    return 0;
                });
                $scope.reviewsG = tempReview2;
            } else {
                var tempReview = $scope.details.reviews;
                tempReview.sort(function (a, b) {
                    if (a.rating > b.rating) {
                        return 1;
                    }
                    if (a.rating < b.rating) {
                        return -1;
                    }

                    return 0;
                });
                $scope.reviewsG = tempReview;
            }
        }

        $scope.mostSort = function () {
            document.getElementById("dropdownMenu2").innerText = "Most Recent";
            if ($scope.reviewGoogle === true) { //sort yelp review
                var tempReview = $scope.reviewsY;
                tempReview.sort(function (a, b) {
                    if (a.time_created.valueOf() > b.time_created.valueOf()) {
                        return -1;
                    }
                    if (a.time_created.valueOf() < b.time_created.valueOf()) {
                        return 1;
                    }
                    return 0;
                });
                $scope.reviewsY = tempReview;

                var tempReview2 = $scope.reviewsG;
                tempReview2.sort(function (a, b) {
                    if (a.time.valueOf() > b.time.valueOf()) {
                        return -1;
                    }
                    if (a.time.valueOf() < b.time.valueOf()) {
                        return 1;
                    }
                    return 0;
                });
                $scope.reviewsG = tempReview2;
            } else {
                var tempReview = $scope.details.reviews;
                tempReview.sort(function (a, b) {
                    if (a.time.valueOf() > b.time.valueOf()) {
                        return -1;
                    }
                    if (a.time.valueOf() < b.time.valueOf()) {
                        return 1;
                    }

                    return 0;
                });
                $scope.reviewsG = tempReview;
            }
        }

        $scope.leastSort = function () {
            document.getElementById("dropdownMenu2").innerText = "Least Rating";
            if ($scope.reviewGoogle === true) { //sort yelp review
                var tempReview = $scope.reviewsY;
                tempReview.sort(function (a, b) {
                    if (a.time_created.valueOf() > b.time_created.valueOf()) {
                        return 1;
                    }
                    if (a.time_created.valueOf() < b.time_created.valueOf()) {
                        return -1;
                    }
                    return 0;
                });
                $scope.reviewsY = tempReview;

                var tempReview2 = $scope.reviewsG;
                tempReview2.sort(function (a, b) {
                    if (a.time.valueOf() > b.time.valueOf()) {
                        return 1;
                    }
                    if (a.time.valueOf() < b.time.valueOf()) {
                        return -1;
                    }
                    return 0;
                });
                $scope.reviewsG = tempReview2;
            } else {
                var tempReview = $scope.details.reviews;
                tempReview.sort(function (a, b) {
                    if (a.time.valueOf() > b.time.valueOf()) {
                        return 1;
                    }
                    if (a.time.valueOf() < b.time.valueOf()) {
                        return -1;
                    }

                    return 0;
                });
                $scope.reviewsG = tempReview;
            }
        }

        $scope.formCheck = function () {
            if (!$scope.chooseOther) {
                $scope.myForm.input_location.$setPristine();
                $scope.myForm.input_location.$setUntouched();
                if ($scope.myForm.keyword.$invalid || myLat === undefined) {
                    return true;
                }
            }
            else if ($scope.chooseOther) {
                if ($scope.myForm.keyword.$invalid || $scope.myForm.input_location.$invalid) {
                    return true;
                }
            }
        }

        $scope.dirFormCheck = function () {
            if ($scope.dirForm.start_location.$invalid) {
                return true;
            } else {
                return false;
            }
        }


        function rateStyle(num, divID) {

            var ratingRounded = Math.floor(num);
            console.log("333" + document.getElementById(divID).innerHTML);
            var starArray = document.getElementById(divID).querySelectorAll(".star_over");
            console.log(starArray);
            for (var i = 0; i < ratingRounded; i++) {
                // starArray[i].classList.add("star_visible");
                starArray[i].style.color = "#f80";
            }
            console.log("444" + document.getElementById(divID).innerHTML);
            var finalStar = (num - ratingRounded) * 100;
            console.log(finalStar);
            if (finalStar !== 0) {
                // starArray[ratingRounded].classList.add("star_visible");

                // starArray[ratingRounded].style.width=finalStar+"%";
                starArray[Math.ceil(num) - 1].style.width = finalStar + "%";
                starArray[Math.ceil(num) - 1].style.color = "#f80";
            }
        }

        function packFavors(favors) {
            var packedFavors = new Array();
            var parts = 0;
            if (favors.length % 20 === 0) {
                parts = favors.length / 20;
            } else {
                parts = favors.length / 20 + 1;
            }
            var indexStart = 0;
            for (var i = 0; i < parts - 1; i++) {
                packedFavors[i] = favors.slice(indexStart, indexStart + 20);
                indexStart += 20;
            }
            packedFavors[parts - 1] = favors.slice(indexStart);
            console.log("packed favores");
            console.log(packedFavors);

            return packedFavors;
        }

        function sortPhotos(orgPhotos) {
            var sortedPhotos = new Array();
            var photoIndex = 0;
            for (var i = 0; i < orgPhotos.length; i++) {
                if (i % 4 === 0) {
                    sortedPhotos[photoIndex] = orgPhotos[i];
                    photoIndex++;
                }
            }
            for (var i = 0; i < orgPhotos.length; i++) {
                if (i % 4 === 1) {
                    sortedPhotos[photoIndex] = orgPhotos[i];
                    photoIndex++;
                }
            }
            for (var i = 0; i < orgPhotos.length; i++) {
                if (i % 4 === 2) {
                    sortedPhotos[photoIndex] = orgPhotos[i];
                    photoIndex++;
                }
            }
            for (var i = 0; i < orgPhotos.length; i++) {
                if (i % 4 === 3) {
                    sortedPhotos[photoIndex] = orgPhotos[i];
                    photoIndex++;
                }
            }
            return sortedPhotos;

        }


    }]);

window.onload = function () {

    document.getElementById("searchBtn").disabled = true;
    var script = document.createElement("script");
    script.type = "text/javascript";
    url = "http://ip-api.com/json/?format=jsonp&callback=getLoc";
    script.setAttribute('src', url);
    document.getElementsByTagName("head")[0].appendChild(script);

    if (document.getElementById("here_radio").checked) {
        document.getElementById("id_location").disabled = "true";
    }

};

function getLoc(response) {
    myLat = response.lat;
    myLng = response.lon;
    // console.log("myLat:"+myLat);
    // document.getElementById("searchBtn").disabled = false;
}


// function Stack() {
//     this._size = 0;
//     this._storage = {};
// }
//
// Stack.prototype.push = function(data) {
//     var size = ++this._size;
//     this._storage[size] = data;
// };
//
// Stack.prototype.pop = function() {
//     var size = this._size,
//         deletedData;
//
//     if (size) {
//         deletedData = this._storage[size];
//
//         delete this._storage[size];
//         this._size--;
//
//         return deletedData;
//     }
// };


// function generateTable(jsonObj_entity) {
//     if (jsonObj_entity == "" || jsonObj_entity.results.length == 0) {
//         var div_text = "<table border='2' width = '600px' style = 'text-align:center;margin:auto;'>";
//         div_text += "<tr><td bgcolor='silver'>No records have been found</td></tr></table>";
//         document.getElementById("result").innerHTML = div_text;
//         document.getElementById("result").style.display = "block";
//     } else {
//         div_text = "<table border='2' style = 'text-align:center;margin:auto;'>";
//         div_text += "<tr>";
//         div_text += "<th>" + "Category" + "</th>";
//         div_text += "<th>" + "Name" + "</th>";
//         div_text += "<th>" + "Address" + "</th>";
//         div_text += "</tr>";
//
//
//         var results = jsonObj_entity.results;
//
//
//         for (i = 0; i < results.length; i++) {
//             var nameId = results[i]["place_id"];
//             div_text += "<tr id = '" + nameId + "'>";
//             div_text += "<td><img src = ' " + results[i]["icon"] + "' width='" + 30 + "'height =' " + 15 + "'></td>";
//             var temp1 = nameId + "details";
//             div_text += "<td id = '" + temp1 + "'>" + results[i]["name"] + "</td>";
//             var temp2 = nameId + "map";
//             div_text += "<td onmouseover='changeCol_text(this)'  onmouseout='restoreCol_text(this)'' id = '" + temp2 + "'>" + results[i]["vicinity"] + "</td>";
//             div_text += "</tr>";
//
//
//         }
//
//         div_text += "</table>";
//         document.getElementById("result").innerHTML = div_text;
//     }
// }


