# BYTEC

------------------------------------------
THINGS YOU NEED 
------------------------------------------
1. Install JavaFX and remember path
  (https://gluonhq.com/products/javafx/)
2. Install IntelliJ IDEA community
  (https://www.jetbrains.com/idea/download/#section=windows)
3. Install Scenebuilder (ver 8.5) 
  (File given seperately)
4. Download Codes from this directory

------------------------------------------
RUNNING JAVAFX PROJECT WITH INTELLIJ IDEA 
------------------------------------------
1. Run IntelliJ IDEA
2. Open project from folder (File>Open)
3. Go to File>Project structure
4. Under Libraries, add JavaFX lib folder to library 
  eg. (D:\programs\javafx-sdk-15\lib)
5. Under Modules>Dependencies, add JavaFX lib directory and jfoenix.jar
6. Go to Run>Edit Configurations...
7. Modify Options> Add VM Options
8. Add (--module-path "D:\programs\javafx-sdk-15\lib" --add-modules javafx.controls,javafx.fxml) in VM Options. Change path to your own JavaFX directory. Apply and close.
9. Run Main

-----------------------------------------
USERS
--------------------
Warehouse Manager
username: 111
password: 111
-------------------
Supervisor
username:1234
password: 123
-------------------
Picker/Packer
username:9999
password:123
-------------------
Receiver
username:7890
password:222
