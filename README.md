# MyTank App - The Fish Tank Manager
![Header Image](assets/header.png)

## Why?
With MyTank you can manage all your aquariums. Add your snails, shrimp, fish and even plants and create various reminders.
You can create feeding reminders for animals, alarms for whole aquariums (e.g. water change) or even fertilizer alarms for plants!

## Features
- Register/Login Methods
    - Email/Password (with reset password functionality)
    - Google Auth
    - Anonymously
- Add/Update/Delete Fish Tanks
    - With image upload
- Add/Update/Delete Tank Items
    - With image upload
- Add/Update/Delete Alarms for Tanks and for Tank Items
    - Alarms are synchronized across devices
    - Reminders work even after a reboot without starting the app
    - They can be configured repeatedly for each day/hour, ...
- Night/Light mode
    - Can be set to "System" as of Android 10
    - Default is Night mode

## Technical Features
- Use of Firebase
    - Firebase Authentication
    - Cloud Firestore
    - Firebase Storage
- Kotlin Coroutines
    - For async/await Firebase data
- BroadcastReceiver
    - Hourly BroadcastReceiver
    - On Boot BroadcastReceiver
- Applied MVVM (Model View ViewModel) Pattern
- SplashScreen to load data

## Screenshots
### Alarm Notification
![Alarm Notification](readme-assets/alarm-notification.jpg)

### Splashscreen
![Splashscreen](readme-assets/splashscreen.jpg)

### Tank View
![Tank](readme-assets/screen-5.jpg)

### Tank Item: Shrimps
![Shrimps](readme-assets/screen-1.jpg)

### Tank Item: Betta
![Betta](readme-assets/screen-3.jpg)

### Tank Item: Turret snail
![Turret snail](readme-assets/screen-4.jpg)

### Item Alarms: Overview
![Item Alarms](readme-assets/screen-6.jpg)

### Item Alarms: Edit Alarm
![Edit Item Alarm](readme-assets/screen-2.jpg)

### Light Mode
![Light Mode](readme-assets/screen-7.jpg)

## References
- Firebase: https://firebase.google.com/docs/firestore/quickstart
- Google SignIn: https://firebase.google.com/docs/auth/android/google-signin
- Coroutines: https://developer.android.com/kotlin/coroutines