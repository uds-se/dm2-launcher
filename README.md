# DM-2 Launcher

Usage instructions:

### Build 

```
./gradlew build
./gradlew assembleReleaseAndroidTest
```

### Install 

```
adb install ./app/build/outputs/apk/release/app-release.apk
adb install ./app/build/outputs/apk/androidTest/release/app-release-androidTest.apk
```

### Run

```
adb shell am instrument -w -r -e class 'com.example.dm2launcher.StartDMTest' com.example.dm2launcher.test/androidx.test.runner.AndroidJUnitRunner    
```