# Qwitter
A twitter clone for BYU's CS340. Utilizes AWS services such as DynamoDB, S3, Lambda and SQS for scalable user storage. 
User Authentication is handled by AWS cognito. Compatible with Android Oreo 8.0. Developed with Android Studio. UI layer follows the Model View Presenter design pattern. Facade and Proxy pattern's are used for accessing backend services. 

## Notes from the author 
This application was developed using an android emulator, therefore some of the existing functionality was for compatibility 
with the emulator. For example, every list view (except for the search results view) supports pagination, and each page is 
loaded with a load button on the bottm of the screen. This was because listening for scroll events on the emulator was quite 
buggy, and would fire multiple scroll events from one percieved scroll.  

Clicking on the text view of a status will open a single status view. 

To search for a user, an @ symbol must precede the user's alias. 

To search for a status with a particular hashtag, a # symbol must precede the tag to be searched. 

When entering login or sign up credentials, do not include an @ symbol.

Emulator used for development was a Nexus 5, so the front end will be most ideal on that model. While I understand that this 
is not ideal for "the real world",but at the end of the day this was for a class. Trust me, I know the front end could be much mroe flexible.

Thank you for checking out my app! It was a lot of work to get this thing done in a semester, but it proved to be a rewarding 
challenge. I was asked to keep my backend code private, so if you would like to see how I set up my AWS api, just ask!
I appreciate all feedback, please feel free to contact me at mjbriggs@icloud.com with any comments, questions or concerns. 



