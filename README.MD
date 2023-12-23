# YouTube Clone Kotlin Mutliplatform
## Screen Shots
<img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/1.png" width="400px" height="800px" /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/2.png" width="400px" height="800px" />
<img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/3.png" width="400px" height="800px" /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/7.png" width="400px" height="800px" /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/8.png" width="400px" height="800px" /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/9.png" width="400px" height="800px" />  <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/10.png" width="400px" height="800px" />  <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/11.png" width="400px" height="800px" />  <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/12.png" width="400px" height="800px" />  <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/13.png" width="400px" height="800px" />  <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/14.png" width="400px" height="800px" />  <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/15.png" width="400px" height="800px" />
<img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/4.png" /> 
<img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/5.png" /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/6.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/16.png"  />  <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/17.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/18.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/19.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/20.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/21.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/21.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/23.png"  /> <img src="https://github.com/KhubaibKhan4/Youtube-Clone-KMP/blob/master/screenshots/24.png"  /> 
## Before running!
 - check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
 - install JDK 17 on your machine
 - add `local.properties` file to the project root and set a path to Android SDK there

### Android
To run the application on android device/emulator:  
 - open project in Android Studio and run imported android run configuration

To build the application bundle:
 - run `./gradlew :composeApp:assembleDebug`
 - find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

### Desktop
Run the desktop application: `./gradlew :composeApp:run`

### iOS
To run the application on iPhone device/simulator:
 - Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration
 - Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio

### Browser
Run the browser application: `./gradlew :composeApp:jsBrowserDevelopmentRun`

