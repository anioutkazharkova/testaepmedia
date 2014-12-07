Test project for AEP media

Goal:
Simple application to download and show 50 random images of random sizes (different height, but equal width).

Project structure:

com.example.testaepmedia
/TestAepMedia/src/com/example/testaepmedia/InfoDialogFragment.java - info dialog
/TestAepMedia/src/com/example/testaepmedia/LoadDialogFragment.java - custom progress dialog
/TestAepMedia/src/com/example/testaepmedia/MainActivity.java - Main Activity of application

com.example.testaepmedia.database
/TestAepMedia/src/com/example/testaepmedia/database/DatabaseHelper.java - custom SqliteOpenHelper

com.example.testaepmedia.entities
/TestAepMedia/src/com/example/testaepmedia/entities/ImageEntity.java - class to hold image data (link and optional byte array)

com.example.testaepmedia.service
/TestAepMedia/src/com/example/testaepmedia/service/ImageCachedLoadTask.java - asynctask class to load cached images from cache 
/TestAepMedia/src/com/example/testaepmedia/service/ImageLoadTask.java - asynctask to load from network images
/TestAepMedia/src/com/example/testaepmedia/service/UtilityMethods.java - class with common utility methods

com.example.testaepmedia.views
/TestAepMedia/src/com/example/testaepmedia/views/OnScrollViewListener.java - interface to monitor scrollchanged event 
/TestAepMedia/src/com/example/testaepmedia/views/ScrollViewWithListener.java - custom scrollview allows to monitor scroll event
