# BloodBank
BloodBank is an Android project that helps people and it could save their lives by sharing and asking for someone to help them with donating blood.

BloodBank was developed with MVVM architectural design pattern and multiple technologies such as Android Architecture Components - DataBinding - Dagger2 - Firebase Authentication - Firebase Firestore - Firebase Cloud Functions & Crashlytics

The project works in limited Governorate 'North Sinai' and it can be easily adjusted for any other place, all you have to do is to change the needed values in `strings.xml`

| App Notifications  |HomeActivity "Shoutouts"| SearchActivity | DetailsActivity |
| ------------- |--------------| ------------- |------------- |
| ![App Notifications](https://github.com/AbduallahAtta/BloodBank/blob/master/screenshots/app%20notifications.jpg)| ![HomeActivity "Shoutouts"](https://github.com/AbduallahAtta/BloodBank/blob/master/screenshots/home.jpg)| ![SearchActivity](https://github.com/AbduallahAtta/BloodBank/blob/master/screenshots/search.jpg)| ![SettingsActivity](https://github.com/AbduallahAtta/BloodBank/blob/master/screenshots/settings.jpg)

**Features** 
* Authenticating Users
* Apply For Donating
* Search For Donors within a specific city and blood type
* View Donor's contact information 
* Shoutout for donors in critical situations 'Push Notifications' 📯
* Share a Shoutout to gather help instantly
* Settings screen for disabling Notifications and changing language 'Arabic by default'

**Firebase technologies 🔥**
* [Firebase Authentication](https://firebase.google.com/products/auth/)
* [Firebase Firestore](https://firebase.google.com/products/firestore/)
* [Firebase Cloud Functions](https://firebase.google.com/products/functions/)
* [Firebase Messaging](https://firebase.google.com/docs/cloud-messaging)
* [Crashlytics](https://firebase.google.com/docs/crashlytics)

**Android Jetpack**
* [DataBinding](https://developer.android.com/topic/libraries/data-binding)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)

**Third-party libraries**
* [EventBus](http://greenrobot.org/eventbus/)
* [Dagger2](https://github.com/google/dagger)
* [Glide](https://github.com/bumptech/glide)
* [CircleImageView](https://github.com/hdodenhof/CircleImageView)
* [Ahoy onbroading](https://github.com/codemybrainsout/ahoy-onboarding)
* [Android SpinKit](https://github.com/ybq/Android-SpinKit)
* [SweetAlert](https://mvnrepository.com/artifact/com.github.f0ris.sweetalert/library/1.5.1)
* [BoomMenu](https://github.com/Nightonke/BoomMenu)

-----

All ready to go, just clone the repository, setup your firebase project and deploy the function in [`index.js`](https://github.com/AbduallahAtta/BloodBank/blob/master/index.js) file by following these steps [Get started with Cloud Functions](https://firebase.google.com/docs/functions/get-started)
