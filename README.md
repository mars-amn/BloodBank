# BloodBank
BloodBank is an Android project that helps people and it could save their lives by sharing and asking for someone to help them with donating blood.

BloodBank was developed with MVVM architectural design pattern and multiple technologies such as Android Architecture Components - DataBinding - Dagger2 - Firebase Authentication - Firebase Firestore - Firebase Cloud Functions & Crashlytics

The project works in limited Governorate 'North Sinai' and it can be easily adjusted for any other place, all you have to do is to change the needed values in `strings.xml`

| App Notifications  |HomeActivity "Shoutouts"| SearchActivity | SettingsActivity |
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

# License 
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

MIT License

Copyright (c) 2019 Abdullah El Amien

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

