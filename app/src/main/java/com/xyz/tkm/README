This is the implementation of the Task Manager App to keep a track of useful activities required in our daily lives.

Following are the structure of my backend activities files:-
In "app/src/main/java/com/xyz/tkm"
1. FirebaseHelper-> Used to inculcate firebase remote newtwork syncing from firebasestore where three methods are created which do there respective tasks

2. HomeActivity-> The main UI frontend/backend layout for the app where recyclerview and various other methods are implemented to use the necessary features

3. ItemTaskActivity-> The task activity card showed after adding tasks in the search box bar with a bottomsheetdialog embedded

4. LoginActivity-> The login page(basically it acts as a guest mode user only where you can put any random text and it will still redirect you to the homepage)

5. MainActivity -> Where all the kt files get accumulated and do their respective methods execution and task status button like if its pending then right corner icon will be yellow, done then green and incomplete then red.

6. SignupActivity-> Same as login page

7. SplashActivity-> Gets a short animation of the app logo

8. Task-> data class for attributes

9. TaskAdapter-> Where setOnClicks methods bind and holder are implemented basically all the task plugins are there.

10. TaskDao-> SQL database queries for the task manager respectively

11. TaskDatabase-> Contains TaskDao attached to RoomDatabase basically my local database implementation

12. TaskRepository-> Contains methods synced by RoomDatabase+FirebaseFirestore

13. TaskViewModel-> Contains viewModelScope with MVVM style code composed by TaskRepository and RoomDatabase

Now in res folder all frontend files layout are present as follows:-
In "app/src/main/res/layout"
1. activity_home.xml for homepageUI
2. activity_login.xml for loginpageUI
3. activity_main.xml for searchbar and toolbar and other UI elements
4. activity_signup.xml for signuppageUI
5. activity_splash.xml for splash logo anime screen
6. dialogue_add_task.xml for bottomsheetdialog UI
7. item_task_template.xml for task UI card

All the logos and pics vector images are in drawables folder as follows:-
In "app/src/main/res/drawable"

A NOTE :-
You might see already populated tasks when launching the app because of my testing and local db+ firebase storage.
Few points to keep in mind:-
The app does its work smoothly however there are some flaws like or things which are not as expected
1. The searchbox does takes input but doesnt find what you are searching for that is you have to scroll manually and look for it,
   also "enter" button does not work instead you have to click on "+" button on right corner and a dialog sheet will appear asking to add title and description which then stores the task

2. The "edit" button right above "delete" button does ask for title and description in dialog sheet but not giving it a title does will not save new task however giving description first and then title will save the task as its title
   means the description of the task will become its title.

Rest is fine. :)
