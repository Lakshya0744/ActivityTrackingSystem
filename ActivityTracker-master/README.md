# ActivityTracker

## Setup Instructions
1. `git clone https://github.com/kha1989led/ActivityTracker`, if you have access to the repo. Otherwise, unzip the accompanied zip file.
2. Download latest version of Android Studio (tested on version 3.5.2).
3. Download Java JDK (tested on JDK 8 - 1.8.0_202).
4. Open SDK manager from within Android Studio (through toolbar, or Preferences > Android SDK).
5. From SDK Platforms tab: download Android SDK Platform for Android 9.0 Pie (API level 28) (tested on revision 6).
6. From SDK Tools tab: download Android SDK-build tools, Android Emulator (if needed, otherwise an android smartwatch would suffice),
   Android SDK Platform-Tools, Android SDK Tools, Google Play Services, and Intel emulator accelerator.
7. Click on File > Project Structure > 2nd option (SDK Location) > Set the Android SDK location path.
8. Click on File > Open... > Location the root folder of the `ActivityTracker` project.
9. Once the project is open, let gradle download the required dependencies and sync the project.
10. To create a new virtual device (if needed), open the AVD manager from the top toolbar.
11. Click on `Create Virtual Device...` on the bottom left. 
12. Choose `Wear OS` as the category from the left.
13. Choose Name: `Wear OS Round` Size: `1.84` Resolution: `320x320` Density: `hdpi`.
14. Choose the recommended software version `Android 9.0 Wear OS`.
15. Click next and finish.
16. Choose the emulator from the top toolbar, then hit the `Debug` button to start the application.

## Alternative Instructions
1. Follow steps 10 to 15 to create a new virtual device.
2. Launch the emualtor (the virutal device).
3. Drag and drop the apk file `ActivityTracker.apk` (in the root folder) on the emulator to install it.
4. Once it installs, you should be able to navigate the watch and open the `Activity Tracker` app.

## Notes
1. The emulator doesn't have any sensors (both the heart rate and steps have fake data).
2. The keyboard tends to jam in the emulator. If it did, click back and focus on the edit field again.
3. The curved scroll also tends to jam. If it did, just hit the back button once.
4. The emulator can be connected to a physial phone for debugging (see this: https://medium.com/@DaniAmjad/connect-android-device-with-android-wear-emulator-pair-android-device-with-android-wear-emulator-c54d1913b244).
5. The accompanied video was recorded using the emulator. Thus, the heart rate sensor is NULL and the steps count is fixed to 598 steps.
