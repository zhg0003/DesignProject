var firestore = firebase.firestore();
//
//Database Functions
//
//This checks if you are logged in
firebase.auth().onAuthStateChanged(function(user) {
    //If your logged in
  if (user) {
      //get global variables
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
    if(sound1.trim() == ""){
        sound1 = '-1';
    }
    var freq1 = document.querySelector("#freq1").value;
    if(isNaN(freq1) || freq1.trim() == ""){
        freq1 = '-1';
    }
    var amp1 = document.querySelector("#amp1").value;
    if(amp1.trim() == ""){
        amp1 = '-1';
    }
    var sound2 = document.querySelector("#sound2").value;
    if(sound2.trim() == ""){
        sound2 = '-1';
    }
    var freq2 = document.querySelector("#freq2").value;
    if(isNaN(freq2) || freq2.trim() == ""){
        freq2 = '-1';
    }
    var amp2 = document.querySelector("#amp2").value;
    if(amp2.trim() == ""){
        amp2 = '-1';
    }
    var tags = document.querySelector("#tags").value;
    if(tags.trim() == ""){
        tags = '-1';
    }
    var dateTime = new Date();
    var date = "Day: " + dateTime.getFullYear() + ":" + dateTime.getMonth() + ":" + dateTime.getDate() + " Time: " + dateTime.getHours() + ":" + dateTime.getMinutes() + ":" + dateTime.getSeconds();
    var exp = document.querySelector("#exp").value;
    if(exp.trim() == ""){
        exp = "-1";
    }
    var rating = document.getElementById("rating").value;
    if(rating.trim() == ""){
        rating = '-1';
    }
    
    if(sound1 != -1 || sound2 != -1 || freq1 != -1 || freq2 != -1 || amp1 != -1 || amp2 != -1 || tags != -1 || exp != -1){
        
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
    }
    })
      //IF your not logged in, it brings you to the homepage!
  } else {
      location.replace("./index.html");
      document.getElementById("inputs").style.display = "none";
      document.getElementById("login").style.display = "block";
  }
});