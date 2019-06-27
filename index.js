const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

 exports.sendNotification = functions.firestore
      .document('shoutouts/{doc_id}')
      .onCreate((snap, context) => {
        const newValue = snap.data();

        const name = newValue.name;
        const description = newValue.description;
        
        const payload = {
                notification: {
                    title: name + " needs your help!",
                    body: description,
                    icon: "default",
                    color: '#f45342'
                }
            };
  
            return admin.messaging().sendToTopic("shoutouts",payload).then(result => {
                console.log("Notification sent!");
            });

});