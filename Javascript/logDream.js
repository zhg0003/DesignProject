var firestore = firebase.firestore();
//
//Database Functions
//
firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
      document.getElementById("inputs").style.display = "block";
      document.getElementById("login").style.display = "none";
      var currentUser = firebase.auth().currentUser.email;
      var nullSector = firestore.collection('USERS').doc('null').collection('records');
      var docRef = firestore.collection('USERS').doc(currentUser).collection('records');
      var saveButton = document.getElementById("submit");
      var docSettings = firestore.collection('USERS').doc(currentUser).collection('settings').doc('settings');
      docSettings.get().then(function(doc) {
        if (doc.exists) {
            settings = doc.data().forum;
        } else {
        // doc.data() will be undefined in this case
            docSettings.set({
                forum: 'no'
            }).catch(function(error){
                console.log("got an error", error);
            });
        }
      });

    saveButton.addEventListener("click", function(){
    var sound1 = document.querySelector("#sound1").value;
    var freq1 = document.querySelector("#freq1").value;
    var amp1 = document.querySelector("#amp1").value;
    var sound2 = document.querySelector("#sound2").value;
    var freq2 = document.querySelector("#freq2").value;
    var amp2 = document.querySelector("#amp2").value;
    var tags = document.querySelector("#tags").value;
    var dateTime = new Date();
    var date = "Day: " + dateTime.getFullYear() + ":" + dateTime.getMonth() + ":" + dateTime.getDate() + " Time: " + dateTime.getHours() + ":" + dateTime.getMinutes() + ":" + dateTime.getSeconds();
    var exp = document.querySelector("#exp").value;
    var rating = document.getElementById("rating").value;
    
    
    console.log("I am trying to save input to Firestore");
    docRef.doc(date).set({
        sound1: sound1,
        tags: tags,
        rating: rating,
        freq1: freq1,
        amp1: amp1,
        sound2: sound2,
        freq2: freq2,
        amp2: amp2,
        date: date,
        exp: exp
    }).then(function(){
        document.querySelector("#sound1").value = "";
        document.querySelector("#freq1").value = "";
        document.querySelector("#amp1").value = "";
        document.querySelector("#sound2").value = "";
        document.querySelector("#freq2").value = "";
        document.querySelector("#amp2").value = "";
        document.querySelector("#exp").value = "";
        document.getElementById("rating").value = "";
        document.getElementById("tags").value = "";
        document.getElementById("loggedDream").style.display = "block";
        console.log("status saved!");
    }).catch(function(error){
        console.log("got an error", error);
    });
        if(settings == "yes"){
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
        }
    })
  } else {
      location.replace("./index.html");
      document.getElementById("inputs").style.display = "none";
      document.getElementById("login").style.display = "block";
  }
});