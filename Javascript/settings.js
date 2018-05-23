var firestore = firebase.firestore();
var settings;
var subscribeSection = document.getElementById("subscrbe");
var unSubscribeSection = document.getElementById("un-subscrbe");
var subscribeBtn = document.getElementById("subscribeBtn");
var unSubscribeBtn = document.getElementById("unSubscribeBtn");
var nullSector = firestore.collection('USERS').doc('null').collection('records');


firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
      var currentUser = firebase.auth().currentUser.email;
      var docSettings = firestore.collection('USERS').doc(currentUser).collection('settings').doc('settings');
    docSettings.get().then(function(doc) {
      if (doc.exists) {
         settings = doc;
          console.log(settings.data().forum);
        if(settings.data().forum == "no"){
            subscribeSection.style.display = "block";
            unSubscribeSection.style.display = "none";
        }else{
            subscribeSection.style.display = "none";
            unSubscribeSection.style.display = "block";
        }
        subscribeBtn.addEventListener("click", function(){
            docSettings.set({
                forum: 'yes'
            }).catch(function(error){
                console.log("got an error", error);
            });
            firestore.collection('USERS').doc(currentUser).collection('records').get().then(function(querySnapshot) {
              querySnapshot.forEach(function(doc) {
                  var sound1 = doc.data().sound1;
                  var tags = doc.data().tags;
                  var rating = doc.data().rating;
                  var freq1 = doc.data().freq1;
                  var amp1 = doc.data().amp1;
                  var sound2 = doc.data().sound2;
                  var freq2 = doc.data().freq2;
                  var amp2 = doc.data().amp2;
                  var date = doc.data().date;
                  var exp = doc.data().exp;
                  console.log(date);
                  nullSector.doc(date).set({
                      sound1: sound1,
                      tags: tags,
                      rating: rating,
                      freq1: freq1,
                      amp1: amp1,
                      sound2: sound2,
                      freq2: freq2,
                      amp2: amp2,
                      date: date,
                      exp: exp,
                      user: currentUser
                  });
                });
                });
            subscribeSection.style.display = "none";
            unSubscribeSection.style.display = "block";
        });
        unSubscribeBtn.addEventListener("click", function(){
            docSettings.set({
                forum: 'no'
            }).catch(function(error){
                console.log("got an error", error);
            });
            nullSector.where("user", "==", currentUser).get().then(function(querySnapshot) {
                querySnapshot.forEach(function(doc) {
                    var date = doc.data().date;
                    nullSector.doc(date).delete().then(function() {
                        console.log("Document successfully deleted!");
                    }).catch(function(error) {
                        console.error("Error removing document: ", error);
                    });
                });
            });
            subscribeSection.style.display = "block";
            unSubscribeSection.style.display = "none";
        }); 
    } else {
        // doc.data() will be undefined in this case
        docSettings.set({
            forum: 'no'
    }).catch(function(error){
        console.log("got an error", error);
    });
        

        subscribeSection.style.display = "block";
        unSubscribeSection.style.display = "none";

        subscribeBtn.addEventListener("click", function(){
            docSettings.set({
                forum: 'yes'
            }).catch(function(error){
                console.log("got an error", error);
            });
            firestore.collection('USERS').doc(currentUser).collection('records').get().then(function(querySnapshot) {
              querySnapshot.forEach(function(doc) {
                  var sound1 = doc.data().sound1;
                  var tags = doc.data().tags;
                  var rating = doc.data().rating;
                  var freq1 = doc.data().freq1;
                  var amp1 = doc.data().amp1;
                  var sound2 = doc.data().sound2;
                  var freq2 = doc.data().freq2;
                  var amp2 = doc.data().amp2;
                  var date = doc.data().date;
                  var exp = doc.data().exp;
                  console.log(date);
                  nullSector.doc(date).set({
                      sound1: sound1,
                      tags: tags,
                      rating: rating,
                      freq1: freq1,
                      amp1: amp1,
                      sound2: sound2,
                      freq2: freq2,
                      amp2: amp2,
                      date: date,
                      exp: exp,
                      user: currentUser
                  });
                });
                });
            subscribeSection.style.display = "none";
            unSubscribeSection.style.display = "block";
        });
        unSubscribeBtn.addEventListener("click", function(){
            docSettings.set({
                forum: 'no'
            }).catch(function(error){
                console.log("got an error", error);
            });
            nullSector.where("user", "==", currentUser).get().then(function(querySnapshot) {
                querySnapshot.forEach(function(doc) {
                    var date = doc.data().date;
                    nullSector.doc(date).delete().then(function() {
                        console.log("Document successfully deleted!");
                    }).catch(function(error) {
                        console.error("Error removing document: ", error);
                    });
                });
            });
            subscribeSection.style.display = "block";
            unSubscribeSection.style.display = "none";
        }); 
        
        
        
        
        
        
        
        }
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });

    }else {
      location.replace("./index.html");
      document.getElementById("login").style.display = "block";
      document.getElementById("recordings").style.display = "none";
  }
});

