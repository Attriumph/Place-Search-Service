<p align=""left>
<img src="https://img.shields.io/badge/License-MIT-orange.svg">
<img src="https://img.shields.io/badge/release--date-07%2F2018-green.svg">	
</p>
# Place Search Service

## 1.High-level Discription
Developed a webpage that allows users to search for places using the Google Places API and display the results on the same page
below the form. Once the user clicks on a button to search for place details, the webpage will display several tabs which contain
an info table, photos of the place, map and route search form and reviews respectively. The webpage also supports adding places to
and removing places from favorites list and posting place info to Twitter.

## 2.How is it written?
It is written in three version. To be more specific:

* The Nodejs version implements a comprehensive web application[(See Video Demo Here)](https://www.youtube.com/watch?v=5OTM7qBMxfM)
      [Try it by yourself](http://placesearch-env.us-west-1.elasticbeanstalk.com/)

      1. Allow user to search places according to inputs provided by user based on Google Places API
      2. Matched places are displayed in a table showing type, name and address of places
      3. Enable user see place details in info tab, photo tab, map tab and reviews tab
      4. Map allows users to choose travel mode and input start location and end location
      5. Review tab allows user to sort the reviews(Goolge review and Yelp review)
      6. The place can be added as favorite places of users
      7. Responsive design, the web application works both on desktop and mobile
      7. Skills: AJAX, JSON, HTML5, Bootstrap, CSS, JavaScript,Node.js, AngularJS, Express, AWS

* The Android version implements a comprehensive android application[(See Video Demo Here)](https://www.youtube.com/watch?v=jhmInPC0m4M&feature=youtu.be).

      1. Developed based upon the server of nodejs version on AWS, so the function is same
      2. Matched places are displayed in a table showing type, name and address of places
      3. Enable user see place details in info tab, photo tab, map tab and reviews tab
      4. Map allows users to choose travel mode and input starting location and ending location
      5. Review tab allows user to sort the reviews(Google review and Yelp review)
      6. The places can be added as favorite places of users
      7. Skills: Android Studio, Java, AWS, Volley, Gson, Picasso, Google Play Service, Google Anrdoid API


* The PHP version implements basic function of the web app[(See Video Demo Here)](https://www.youtube.com/watch?v=1uffTsR2jLk&feature=youtu.be)
[Try it by yourself](http://cs-server.usc.edu:12293/page.php)

      1. Allow user to search places according to information provided by user using Google Places API
      2. Matched places are displayed in a table showing type, name and address of places
      3. Skills: AJAX, JSON, HTML, CSS, PHP, DOM
      4. Web Server: Apache, NGINX

 ## 3.Sreenshots
### 3.1 Web Application
  <div align="center">    
 <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/homepage.png" alt="homepage" width="800" style="display:inline"/>
 <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/detail.png" alt="info" width="800" style="display:inline" />
 </div>

  <div align="center">
   <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/result.png" alt="info" width="800" style="display:inline" />
   <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/map.png" alt="info" width="800" style="display:inline" />
  </div>
  <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/review.png" alt="info" width="800" style="display:inline" />

  ### 3.2 Android Application  
  <div align="center">
    <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/app_home.png" alt="info" width="30%" style="display:inline" />
    <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/app_result.png" alt="info" width="30%" style="display:inline" />  
    <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/app_detail.png" alt="info" width="30%" style="display:inline" />
  </div>  

 <div align="center">
    <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/app_nav.png" alt="info" width="30%" style="display:inline" />
  <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/app_review.png" alt="info" width="30%" style="display:inline" />
    <img src="https://github.com/Attriumph/Place-Search-Service/blob/master/images/app_photo.png" alt="info" width="30%" style="display:inline" />
  </div>
