Twitter top-word counter
==============

A software for fetching, storing and analyzing tweets. Specifically you can choose a database to store the tweets at and then you can count the distinct words from each tweet and show the top ten distinct words and hastags.

Installation:
=========================

  1. Download Java 8 and up from here: http://www.oracle.com/technetwork/java/javase/downloads
  2. Download the `release` folder to an easily reachabe location, for example `C:\release`
  3. Rename `release/twitter.example.properties` to `release/twitter.properties`
  4. Open the `release/twitter.properties` file and fill in the 4 keys that you have from Twitter API (oauth.consumerKey, oauth.consumerSecret, oauth.accessToken, oauth.accessTokenSecret)
  
Execution
=======================

  1. Open the `release` folder
  2. Double click the executable
  

Libraries used
=======================

* JCalendar 1.4 (http://toedter.com/jcalendar)
* MySQL Connector 5.1.4 (http://www.mysql.com/products/connector)
* Twitter4J 4.0.1 (http://twitter4j.org)
