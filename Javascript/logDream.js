var firestore = firebase.firestore();
//
//Database Functions
//
firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
      document.getElementById("inputs").style.display = "block";
      document.getElementById("login").style.display = "none";
      var currentUser = firebase.auth().currentUser.email;
      var docRef = firestore.collection('USERS').doc(currentUser).collection('records');
      var saveButton = document.getElementById("submit");

      saveButton.addEventListener("click", function(){
    var sound1 = document.querySelector("#sound1").value;
    var freq1 = document.querySelector("#freq1").value;
    var amp1 = document.querySelector("#amp1").value;
    var sound2 = document.querySelector("#sound2").value;
    var freq2 = document.querySelector("#freq2").value;
    var amp2 = document.querySelector("#amp2").value;
    var date = document.querySelector("#date").value;
    var exp = document.querySelector("#exp").value;
    var rating = document.getElementById("rating").value;
    
    
    console.log("I am trying to save input to Firestore");
    docRef.add({
        sound1: sound1,
        rating: rating,
        freq1: freq1,
        amp1: amp1,
        sound2: sound2,
        freq2: freq2,
        amp2: amp2,
        date: date,
        exp: exp
    }).then(function(){
        console.log("status saved!");
    }).catch(function(error){
        console.log("got an error", error);
    });
    })
  } else {
      document.getElementById("inputs").style.display = "none";
      document.getElementById("login").style.display = "block";
  }
});