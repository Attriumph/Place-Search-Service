<?php

  $myKey = "AIzaSyAAzyWWYDYdWqPNTCLN8jsfe16MzVsfSNY";
  $lat = null;
  $lng = null;
   if(isset($_POST["keyword"]) && !isset($_POST["place_id_details"]) && !isset($_POST["end_place"])) {
      global $lat;
      global $lng;
      $distance = $_POST["distance"];
      $keyword =  $_POST["keyword"];
      $category = $_POST["category"];
      $keyword = str_replace('_', '+', $keyword);
       if (isset($_POST["location"])) {
         // global $lat;
         // global $lng;
          $location =  $_POST["location"];
          $location = urlencode($location);
          // call geocode api
          $url_location = "https://maps.googleapis.com/maps/api/geocode/json?address="."$location"."&key=$myKey";
          $jsonOb_geocode = file_get_contents($url_location);
          // echo "jsonObj_geo is :".$jsonOb_geocode;
          $obj_geocode = json_decode($jsonOb_geocode);
          // var_dump($obj_geocode);
          // echo $obj_geocode->{'results'}[0]->{'formatted_address'};
          // echo "<br>";
           if (count($obj_geocode->{'results'}) == 0) {
             $obj_geocode = json_encode($obj_geocode);
             echo $obj_geocode;
             return;
            } else {
            $lat = $obj_geocode->{'results'}[0]->{'geometry'}->{'location'}->{'lat'};
             // echo $lat;
            // echo "<br>";
             $lng = $obj_geocode->{'results'}[0]->{'geometry'}->{'location'}->{'lng'};
            // $place_id = $obj_geocode->{'results'}[0]->{'geometry'}->{'place_id'};
             // echo $lng;
            // echo "<br>";
          }
        } else {
           // global $lat;
           // global $lng;
           $lat = $_POST["latitude"];
           $lng = $_POST["longitude"];
         }

        // call nearbysearch api
        $distance = $distance * 1609.34;
        $url_entity = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="."$lat,"."$lng"."&radius=$distance"."&type=$category"."&keyword=$keyword"."&key=$myKey";
        //get the json object
        // echo $url_entity;
        $jsonOb_entity = file_get_contents($url_entity);
        // $obj_entity = json_decode($jsonOb_entity);
        // var_dump($obj_entity);

        echo $jsonOb_entity;
        return;
        // $url_details = "https://maps.googleapis.com/maps/api/place/details/json?placeid=".$place_id."&key=$myKey";
        // $jsonOb_details = file_get_contents($url_details);
        // echo $jsonOb_details;
        //

          // }

   }

  if(isset($_POST["place_id_details"])){
    $place_id = $_POST["place_id_details"];
    $url_details = "https://maps.googleapis.com/maps/api/place/details/json?placeid=".$place_id."&key=$myKey";
    $jsonOb_details = file_get_contents($url_details);
    $details = json_decode($jsonOb_details,true);
    $place_name = $details['result']['name'];
    $json_pack = array();
    $reviews = array();
    $photos = array();

    if (isset($details['result']['reviews'])){

      $place_reviews = $details['result']['reviews'];


      // $reviews = array();
      for ($i = 0; $i < 5 && $i < count($place_reviews); $i++) {
         global $reviews;
        $tempt_name = $place_reviews[$i]["author_name"];
        if (isset($place_reviews[$i]["profile_photo_url"])){
          $tempt_profile = $place_reviews[$i]["profile_photo_url"];
        } else {
          $tempt_profile = "";
        }

        $tempt_text = $place_reviews[$i]["text"];
        $temp_review = array("author_name"=>"$tempt_name","profile_photo_url"=>"$tempt_profile","text"=>"$tempt_text");
        $reviews[$i] = $temp_review;
      }


    }
      if (isset($details['result']['photos']) ){
          $place_photos = $details['result']['photos'];
          // $photos = array();
           foreach (glob("*.png") as $photoName) {
            unlink($photoName);
             }

           for ($i = 0; $i < 5 && $i < count($place_photos); $i++) {
              global $photos;
             $temp_width = $place_photos[$i]["width"];
             $temp_reference = $place_photos[$i]["photo_reference"];
             $url_photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=".$temp_width."&photoreference=".$temp_reference."&key=".$myKey;
             $photo_name = $place_id.$i.".png";
             file_put_contents($photo_name,file_get_contents($url_photo));
             $photos[$i] = $photo_name;
           }
      }

    // $json_pack = array();
    $json_pack['name'] = $place_name;
    $json_pack['reviews'] = $reviews;
    $json_pack['photos'] = $photos;
    $json_pack_string = json_encode($json_pack);

    echo $json_pack_string;
    return;
  }

  if(isset($_POST["end_place"])) {
     $lat = null;
     $lng = null;
    if (isset($_POST["location"])) {
      // global $lat;
      // global $lng;
       $location =  $_POST["location"];
       $location = urlencode($location);
       // call geocode api
       $url_location = "https://maps.googleapis.com/maps/api/geocode/json?address="."$location"."&key=$myKey";
       $jsonOb_geocode = file_get_contents($url_location);
       // echo "jsonObj_geo is :".$jsonOb_geocode;
       $obj_geocode = json_decode($jsonOb_geocode);
       // var_dump($obj_geocode);
       // echo $obj_geocode->{'results'}[0]->{'formatted_address'};
       // echo "<br>";
        $lat = $obj_geocode->{'results'}[0]->{'geometry'}->{'location'}->{'lat'};
        // echo $lat;
       // echo "<br>";
        $lng = $obj_geocode->{'results'}[0]->{'geometry'}->{'location'}->{'lng'};
       // $place_id = $obj_geocode->{'results'}[0]->{'geometry'}->{'place_id'};
        // echo $lng;
       // echo "<br>";
    } else {
      // global $lat;
      // global $lng;
      $lat = $_POST["latitude"];
      $lng = $_POST["longitude"];
    }

    $end_place =  $_POST["end_place"];
    $end_place = urlencode($end_place);
    // call geocode api
    $url_end_place = "https://maps.googleapis.com/maps/api/geocode/json?address="."$end_place"."&key=$myKey";
    $jsonOb_geocode_end = file_get_contents($url_end_place);
    $obj_geocode_end = json_decode($jsonOb_geocode_end);

    $end_lat = $obj_geocode_end->{'results'}[0]->{'geometry'}->{'location'}->{'lat'};
    $end_lng = $obj_geocode_end->{'results'}[0]->{'geometry'}->{'location'}->{'lng'};

    $location_pack = array();
    $location_pack['start_lat'] = $lat;
    $location_pack['start_lng'] = $lng;
    $location_pack['end_lat'] = $end_lat;
    $location_pack['end_lng'] = $end_lng;

    $location_pack_string = json_encode($location_pack);

    echo $location_pack_string;
    return;

    }

  // if(isset($_POST["start_lat"]) && isset($_POST["start_lon"]) && isset($_POST["end_placeId"])){
  //   $start_lat = $_POST["start_lat"];
  //   $start_lon = $_POST["start_lon"];
  //   $end_placeId = $_POST["end_placeId"];
  //   $mode = $_POST["mode"];
  //   $url_dir = "https://maps.googleapis.com/maps/api/directions/json?origin="+$start_lat+","+$start_lon+"&destination=place_id:"+$end_placeId+"&mode="+$mode+"&key="+$myKey;
  //   echo $url_dir;
  //   $jsonOb_dir = file_get_contents($url_dir);
  //
  //   echo $jsonOb_dir;
  //   return;
  //
  //     }
?>


<!DOCTYPE HTML>
<html>
  <title>Travel and Entertainment Serach </title>
  <head>
    <meta charset="UTF-8">
    <style type="text/css">
       #container {
         weight:100%;
         height:600px;
         margin: auto;
       }
       #form_section {
         border-style: solid;
         border-width: 3px;
         border-color: gray;
         background-color: white;
         height: 200px;
         width: 600px;
         margin: auto;
       }
       #result {
        padding-top: 40px;
        margin: 20px auto;
       }
       #map {
        height: 200px;
        width: 300px;
       }

    </style>
    <script>
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
             if(document.getElementById("not_here").checked) {
               document.getElementById("id_location").disabled = "false";
             }
         };
         function getLoc(response) {

            myLat = response.lat;
            myLon = response.lon;
            document.getElementById("searchBtn").disabled = false;
        }

   </script>

   <script>
    var xmlhttp;
    var curX;
    var curY;
    document.addEventListener("click",getCursorLoc);
    function getCursorLoc(event){
           curX = event.pageX;
           curY = event.pageY;
       }
    function pre_search() {
      if (document.getElementById("here_radio").checked && document.getElementById("id_keyword").value != "") {
        document.getElementById("id_location").required = false;
        beginSearch();
      }
      if (document.getElementById("not_here").checked){
          document.getElementById("id_location").required = true;
          if(document.getElementById("id_keyword").value != "" && document.getElementById("id_location").value !="" ) {
              beginSearch();
          }
      }
    }
    function beginSearch() {
      // if (document.getElementById("id_keyword").required == true){
        // document.getElementById("id_keyword").required = true;
        var location_value;
        var keyword_value;
        var category_value;
        var distance_value;
        if (document.getElementById("map").style.display != "none"){
          document.getElementById("mode_panel").style.display = "none";
          document.getElementById("map").style.display ="none";
        }

        xmlhttp = new XMLHttpRequest();

        keyword_value = document.getElementById("id_keyword").value;
        keyword_value = keyword_value.replace(/[^A-Z0-9]+/ig, "_");
        category_value = document.getElementById("id_category").value;

        if(document.getElementById("id_distance").value == "") {
           distance_value = 10;
        }else {
          distance_value = document.getElementById("id_distance").value;
        }


        if(document.getElementById("here_radio").checked){
          latitude_value = myLat;
          longitude_value = myLon;
          xmlhttp.open("POST","/page.php",true);
          xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
          sendString = "longitude="+longitude_value+"&latitude="+latitude_value+"&distance="+distance_value+"&category="+category_value+"&keyword="+keyword_value;
          // alert(sendString);
          xmlhttp.send(sendString);

        }else {
          location_value =  document.getElementById("id_location").value;
          xmlhttp.open("POST","/page.php",true);
          // xmlhttp.send(myJsonString);
          xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
          sendString = "location="+location_value+"&distance="+distance_value+"&category="+category_value+"&keyword="+keyword_value;
          // alert(sendString);
          xmlhttp.send(sendString);
        }


     xmlhttp.onreadystatechange = function() {
           if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

             var response_text = xmlhttp.responseText;
              // document.getElementById("test").innerHTML = response_text;
             jsonObj_entity = JSON.parse(response_text);
             generateTable(jsonObj_entity);
          }
       };



   // xmlhttp.open("POST","/page.php",true);
   // // xmlhttp.send(myJsonString);
   // xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
   // sendString = "location="+location_value+"&distance="+distance_value+"&category="+category_value+"&keyword="+keyword_value;
   // // alert(sendString);
   // xmlhttp.send(sendString);
 }

   function generateTable(jsonObj_entity) {
       if (jsonObj_entity.length == 0 || jsonObj_entity.results.length == 0) {
         div_text = "<table border='2' width = '600px' style = 'text-align:center;margin:auto;'>";
         div_text += "<tr><td bgcolor='silver'>No records have been found</td></tr></table>";
         document.getElementById("result").innerHTML = div_text;
         document.getElementById("result").style.display = "block";
       } else {
         div_text = "<table border='2' style = 'text-align:center;margin:auto;'>";
         div_text += "<tr>";
         div_text += "<th>" + "Category" + "</th>";
         div_text += "<th>" + "Name" + "</th>";
         div_text += "<th>" + "Address" + "</th>";
         div_text += "</tr>";


         var results = jsonObj_entity.results;


         for(i = 0; i < results.length; i++) {
            var nameId = results[i]["place_id"];
            div_text += "<tr id = '" + nameId + "'>";
            div_text += "<td><img src = ' " + results[i]["icon"] + "' width='" + 30 + "'height =' " + 15 + "'></td>";
            var temp1 = nameId + "details";
            div_text +=  "<td id = '" + temp1 + "'>" +  results[i]["name"] + "</td>";
            var temp2 = nameId + "map";
            div_text += "<td onmouseover='changeCol_text(this)'  onmouseout='restoreCol_text(this)'' id = '" + temp2 + "'>" +  results[i]["vicinity"] + "</td>";
            div_text += "</tr>";


         }

       div_text += "</table>";

       document.getElementById("result").innerHTML = div_text;
       document.getElementById("result").style.display = "block";
       for(i = 0; i < results.length; i++) {
         var detail_nameId = results[i]["place_id"] + "details";
         var map_nameId = results[i]["place_id"] + "map";
         document.getElementById(detail_nameId).addEventListener("click", getDetails);
         document.getElementById(map_nameId).addEventListener("click", getMap);
       }
       }

   }

  </script>

  <script>
  var placeID;
        function removeAll() {
          document.getElementById("mode_panel").style.display = "none";
          document.getElementById("map").style.display ="none";
           document.getElementById("result").style.display = "none";
           document.getElementById("result").innerHTML = "";
           document.getElementById("id_keyword").value ="";
           document.getElementById("id_category").value = "default";
           document.getElementById("id_distance").value="";
           document.getElementById("id_location").value ="";
           document.getElementById("id_location").disabled = "true";
           document.getElementById("here_radio").checked ="true";


        }

        function getDetails() {
          // alert("my palce id is :"+this.id);
          if (document.getElementById("map").style.display != "none"){
            document.getElementById("mode_panel").style.display = "none";
            document.getElementById("map").style.display ="none";
          }
          xmlhttp2 = new XMLHttpRequest();

          placeID = this.parentElement.id;
          xmlhttp2.onreadystatechange = function() {
                if (xmlhttp2.readyState == 4 && xmlhttp2.status == 200) {
                  // alert("details ready!!!!!");
                  var response_text2 = xmlhttp2.responseText;
                   // document.getElementById("test2").innerHTML = response_text2;
                   jsonObj_details = JSON.parse(response_text2);
                    generateDetails(jsonObj_details);
               }
            };
          xmlhttp2.open("POST","/page.php",true);
          xmlhttp2.setRequestHeader("Content-type","application/x-www-form-urlencoded");
          xmlhttp2.send("place_id_details="+placeID);
        }

        function generateDetails(jsonObj_details) {

          details_text = "<h2 style='text-align:center;'>";
          details_text += jsonObj_details['name'];
          details_text += "</h2>";
          details_text += "<p id = 'reviews' style='text-align:center;'>click to show reviews</p>";
          details_text += "<img id = 'review_id'  style='clear:both;display: block;margin: auto;' src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png'"  + " width='" + 30 + "'height =' " + 15 + "'><br>";
          details_text += "<div id = 'for_review_table'  style='margin:auto;'></div>"
          details_text += "<p id = 'photos'  style='text-align:center;'>click to show photos</p>";
          details_text += "<img id = 'photo_id'  style='clear:both;display:block;margin: auto;' src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png'"  + " width='" + 30 + "'height =' " + 15 + "'><br>";
          details_text += "<div id = 'for_photo_table'  style='margin:auto;'></div>";

          document.getElementById("result").style.display = "";
          document.getElementById("result").innerHTML = details_text;

        var state_review = 0;
        var count_review = 0;
         document.getElementById("review_id").addEventListener("click", function(){
             document.getElementById("review_id").src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_up.png';
             document.getElementById("reviews").innerHTML = "click to hide reviews";
            //0 means I want to see
            if (state_review == 0) {
              if (count_review == 0) {
                var review_tableEle = document.createElement("div");
                // review_tableEle.setAttribute("id","review_table");

                var review_arr = jsonObj_details.reviews;
                var reviewTB_text = "<table id = 'reviewTB' border='2' width = '500px' style='margin:auto;text-align:center;'>";
                // console.log(reviewTB_text);
                if (review_arr.length == 0) {
                    reviewTB_text += "<tr><td>No Reviews Found</td></tr>";
                } else {

                  for(i = 0; i < review_arr.length; i++) {
                      reviewTB_text += "<tr>";
                      if (review_arr[i]["profile_photo_url"] == ""){
                        reviewTB_text += "<td><img src = ' " + review_arr[i]["profile_photo_url"] + "' alt ='' width='" + 20 + "'height ='" + 20 + "'>";
                      }else{
                        reviewTB_text += "<td><img src = ' " + review_arr[i]["profile_photo_url"] + "' width='" + 20 + "'height ='" + 20 + "'>";
                      }
                      reviewTB_text +=  review_arr[i]["author_name"] + "</td></tr>";
                      reviewTB_text += "<tr><td><p>" + review_arr[i]["text"] +"</p></td></tr>";
                  }
                }
                // for(i = 0; i < review_arr.length; i++) {
                //     reviewTB_text += "<tr>";
                //     reviewTB_text += "<td><img src = ' " + review_arr[i]["profile_photo_url"] + "' width='" + 15 + "'height ='" + 15 + "'>";
                //     reviewTB_text +=  review_arr[i]["author_name"] + "</td></tr>";
                //     reviewTB_text += "<tr><td><p>" + review_arr[i]["text"] +"</p></td></tr>";
                // }

                 reviewTB_text += "</table>";
                 // console.log(reviewTB_text);
                 review_tableEle.innerHTML = reviewTB_text;
                 document.getElementById("for_review_table").appendChild(review_tableEle);
                 count_review++;
              } else {
                  document.getElementById("reviewTB").style.display = "";
              }
              if (document.getElementById("photoTB")!=null){
                document.getElementById("photoTB").style.display = "none";
              }
              document.getElementById("photo_id").src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png';
              document.getElementById("photos").innerHTML = "click to show photos";
              state_review = 1;
           } else if ( state_review == 1) {
                document.getElementById("review_id").src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png';
                document.getElementById("reviews").innerHTML = "click to show reviews";
                document.getElementById("reviewTB").style.display = "none";
               state_review = 0;
            }


       });

       var state_photo = 0;
       var count_photo = 0;
          document.getElementById("photo_id").addEventListener("click", function(){
            document.getElementById("photo_id").src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_up.png';
            document.getElementById("photos").innerHTML = "click to hide photos";
            if (state_photo == 0) {
              if (count_photo == 0) {
                var photo_tableEle = document.createElement("div");
                // review_tableEle.setAttribute("id","review_table");

                var photo_arr = jsonObj_details.photos;
                var photoTB_text = "<table id = 'photoTB' border='2' width = '500px' style='margin:auto;text-align:center;'>";
                // console.log(photoTB_text);
                if (photo_arr.length == 0) {
                    photoTB_text += "<tr><td>No Photos Found</td></tr>";
                } else {
                for(i = 0; i < photo_arr.length; i++) {

                    photoTB_text += "<tr><td><a href='"+photo_arr[i]+"' target='_blank'>" + "<img src = '" + photo_arr[i] +"'width ='"+500+"'></a></td></tr>";
                }
               }

                 photoTB_text += "</table>";
                  console.log("photo:"+photoTB_text);

                 photo_tableEle.innerHTML = photoTB_text;
                  // console.log("inner:"+photoTB_text.innerHTML);
                 document.getElementById("for_photo_table").appendChild(photo_tableEle);
                 count_photo++;
               } else {
                  document.getElementById("photoTB").style.display = "";
               }
               if (document.getElementById("reviewTB")!=null){
                 document.getElementById("reviewTB").style.display = "none";
               }
              document.getElementById("review_id").src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png';
              document.getElementById("reviews").innerHTML = "click to show reviews";
              state_photo = 1;
            } else if ( state_photo == 1) {
              document.getElementById("photo_id").src = 'http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png';
              document.getElementById("photos").innerHTML = "click to show photos";
              document.getElementById("photoTB").style.display = "none";
             state_photo = 0;

            }

          });

    }

 </script>

 <script>

 function getMap() {
   // getCursorLoc();
   // this.setAttribute("style","position:relative;");
   var strat_lng;
   var start_lat;
   var end_lng;
   var end_lat;
   element_id = this.id;
   // alert("this element id  is :"+ element_id);
   xmlhttp3 = new XMLHttpRequest();
   placeID = this.parentElement.id;
   var end_place = this.childNodes[0].nodeValue;
   console.log("end place is: "+end_place);

   if(document.getElementById("here_radio").checked){
     xmlhttp3.open("POST","/page.php",true);
     xmlhttp3.setRequestHeader("Content-type","application/x-www-form-urlencoded");
     here_latLng = "longitude="+longitude_value+"&latitude="+latitude_value+"&end_place="+end_place;
     // alert(sendString);
     xmlhttp3.send(here_latLng);

   }else {
     var location_value =  document.getElementById("id_location").value;
     xmlhttp3.open("POST","/page.php",true);
     // xmlhttp.send(myJsonString);
     xmlhttp3.setRequestHeader("Content-type","application/x-www-form-urlencoded");
     location_latLng = "location="+location_value+"&end_place="+end_place;
     // alert(sendString);
     xmlhttp3.send(location_latLng);
   }

   xmlhttp3.onreadystatechange = function() {
        if (xmlhttp3.readyState == 4 && xmlhttp3.status == 200) {
          // alert("location ready!!!!!");
          var response_text3 = xmlhttp3.responseText;
           // document.getElementById("test2").innerHTML = response_text3;
           jsonObj_map = JSON.parse(response_text3);
            start_lng = jsonObj_map['start_lng'];
            start_lat = jsonObj_map['start_lat'];
            end_lng = jsonObj_map['end_lng'];
            end_lat = jsonObj_map['end_lat'];
            console.log(start_lat+","+start_lng+","+end_lat+","+end_lng);
            if (document.getElementById("here_radio").checked) {
              initMap(myLat,myLon,end_lat,end_lng,element_id,curX,curY);
            } else {
              initMap(start_lat,start_lng,end_lat,end_lng,element_id,curX,curY);
           }
       }
    };

}

function changeCol(color){
 color.style.background ="#CBCCCF";
}
function changeCol_text(color){
 color.style.color ="#CBCCCF";
}
function restoreCol_text(color){
 color.style.color ="black";
}
function restoreCol(color){
 color.style.background ="#DDDCDC";
}

function initMap(start_lat,start_lng,end_lat,end_lng,element_id,curX,curY) {

  if (document.getElementById("map").style.display != "none") {

    document.getElementById("map").style.display = "none";
    document.getElementById("mode_panel").style.display = "none";
    // document.getElementById(element_id).removeChild(document.getElementById("map_result"));
    // document.getElementById("walk").style.display = "none";
    // document.getElementById("bike").style.display = "none";
    // document.getElementById("drive").style.display = "none";
  } else {
    console.log(element_id);
     var map_div = document.getElementById("map");
     var mode_div = document.getElementById("mode_panel");
      map_div.style.left = curX + "px";
      map_div.style.top = curY + "px";
      mode_div.style.left = curX + "px";
      mode_div.style.top = curY + "px";


    document.getElementById("map").style.display = "inline";
    document.getElementById("mode_panel").style.display = "inline";


    var markers = [];
    var directionsDisplay;
    var directionsService = new google.maps.DirectionsService();

    directionsDisplay = new google.maps.DirectionsRenderer();
    var uluru = {lat: start_lat, lng: start_lng};
    var map = new google.maps.Map(document.getElementById('map'), {
    zoom: 10,
    center: uluru
    });
    var marker = new google.maps.Marker({
    position: uluru,
    map: map
    });
    markers.push(marker);

   directionsDisplay.setMap(map);
   document.getElementById("walk").addEventListener('click', stopEvent, false);
   document.getElementById("bike").addEventListener('click', stopEvent, false);
   document.getElementById("drive").addEventListener('click', stopEvent, false);
   function stopEvent(e) {
      e.stopPropagation();
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

   document.getElementById("walk").addEventListener("click", function() {
     deleteMarkers();
   console.log("end_lat:"+end_lat+"end_lng:"+end_lng);
   var start = new google.maps.LatLng(start_lat, start_lng);
   var end = new google.maps.LatLng(end_lat, end_lng);
   var request = {
     origin: start,
     destination: end,
     travelMode: 'WALKING'
  };
   directionsService.route(request, function(result, status) {
     if (status == 'OK') {
       directionsDisplay.setDirections(result);
     }
   });
  });

  document.getElementById("bike").addEventListener("click", function() {
    deleteMarkers();
  var start = new google.maps.LatLng(start_lat, start_lng);
  var end = new google.maps.LatLng(end_lat, end_lng);
  var request = {
    origin: start,
    destination: end,
    travelMode: 'BICYCLING'
 };
  directionsService.route(request, function(result, status) {
    if (status == 'OK') {
      directionsDisplay.setDirections(result);
    }
  });
 });

 document.getElementById("drive").addEventListener("click", function() {
   deleteMarkers();
  var start = new google.maps.LatLng(start_lat, start_lng);
  var end = new google.maps.LatLng(end_lat, end_lng);
 var request = {
   origin: start,
   destination: end,
   travelMode: 'DRIVING'
};
 directionsService.route(request, function(result, status) {
   if (status == 'OK') {
     directionsDisplay.setDirections(result);
   }
 });
});




   }
}
// document.getElementById("walk").addEventListener('click', stopEvent, false);
// document.getElementById("bike").addEventListener('click', stopEvent, false);
// document.getElementById("drive").addEventListener('click', stopEvent, false);
function changeToHere() {
  document.getElementById("id_location").disabled = false;
}
function changeToNotHere() {
  document.getElementById("id_location").value = "";
  document.getElementById("id_location").disabled = true;

}
 </script>
 <script async defer
     src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAAzyWWYDYdWqPNTCLN8jsfe16MzVsfSNY">
</script>
</head>

<body>
    <div id = "container">
    <div id = "form_section" >
      <h1 align="center"><i>Travel and Entertainment Search</i></h1>
      <hr>
      <form id = "myform" method = "post" action="" onsubmit="return false">
        <div style="padding-left:20px;display:inline;"><b>Keyword</b></div><input type = "text" name = "keyword" id = "id_keyword" required><br>
        <div style="padding-left:20px;display:inline;"><b>Category</b></div><select name = "category" id = "id_category"><br>
         <option value = "default">defalut</option>
         <option value = "cafe">cafe</option>
         <option value = "bakery">bakery</option>
         <option value = "restaurant">restaurant</option>
         <option value = "beauty_salon">beauty salon</option>
         <option value = "casino">casino</option>
         <option value = "moive_theater">moive theater</option>
         <option value = "lodging">lodging</option>
         <option value = "airport">airport</option>
         <option value = "train_station">train station</option>
         <option value = "subway_station">subway station</option>
         <option value = "bus_station">bus station</option>
       </select><br>
        <div style="padding-left:20px;display:inline;"><b>Distance(miles)</b></div><input type = "text" name = "distance" id = "id_distance" placeholder="10" pattern="[0-9]+">
        <span style="padding-left:8px;"><b>from</b></span>  <input type = "radio"  name = "location"  id = "here_radio" onclick = "changeToNotHere()" checked> <b>Here</b><br>
        <span  style ="padding-left:316px;"></span><input type = "radio"  name = "location" onclick = "changeToHere()" id = "not_here" >
        <input type = "text"  name = "input_location"  id = "id_location" placeholder="location"  disabled><br>
        <span style="padding-left:85px;"></span><button  id = "searchBtn" type = "sbumit" onclick = "pre_search()">Search</button>
        <button type = "button" onclick = "removeAll()">Clear</button>
      </form>
    </div>

    <!-- <p id = "walk" style ="display:none;"></p>
     -->
    <div id = "result" style = "margin:auto;"></div>
    <!-- <div id = "test"></div> -->
    <div id = "test2"></div>
    <div id = "mode_panel" style ="display:none;position:absolute;z-index:1;">
      <!-- <div id = "map" style ="display:none;position:absolute;left:4px;top:20px;z-index:1;"></div> -->
      <div id = "walk" onmouseover="changeCol(this)"  onmouseout="restoreCol(this)" style ="position:abosulte;width:80px;height:20px;background:#DDDCDC;z-index:2;">Walk there</div>
      <div id = "bike" onmouseover="changeCol(this)" onmouseout="restoreCol(this)" style ="position:abosulte;width:80px;height:20px;background:#DDDCDC;z-index:2;">Bike there</div>
      <div id = "drive" onmouseover="changeCol(this)" onmouseout="restoreCol(this)" style ="position:abosulte;width:80px;height:20px;background:#DDDCDC;z-index:2;">Drive there</div>
  </div>
  <div id = "map" style ="position:absolute;"></div>
  </div>
  </body>
</html>
