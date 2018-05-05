var firestore = firebase.firestore();

//
//Authorization Function
//
firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    document.getElementById("logout").style.display = "block";
    document.getElementById("login-link").style.display = "none";
    document.getElementById("signup-link").style.display = "none";
    document.getElementById("weak-pwd").style.display = "none";
    document.getElementById("invalid").style.display = "none";
    document.getElementById("incorrect_password") = "none";
    document.getElementById("weak-pwd") = "none";
    document.getElementById("invalid") = "none";
    document.getElementById("exists") = "none";
    document.getElementById("wrong") = "none";
      
  } else {
    // No user is signed in.
      document.getElementById("logout").style.display = "none";
      document.getElementById("login-link").style.display = "block";
      document.getElementById("signup-link").style.display = "block";
  }
});


//
//Logout function
//
function logout(){
    firebase.auth().signOut();
}

//                              
//Login Function
//
function login(){
    var username = document.getElementById("username").value;
    var userpwd = document.getElementById("password").value;

    
    firebase.auth().signInWithEmailAndPassword(username, userpwd).catch(function(error) {
        // Handle Errors here.
        var incorrect = document.getElementById("wrong")
        incorrect.style.display = "block";
    });
    
}

//
//signup Function
//
function signup(){
    var username = document.getElementById("txtEmail").value;
    var userpwd = document.getElementById("txtPassword").value;
    var userpwd2 = document.getElementById("txtPassword2").value;
    var incorrect = document.getElementById("incorrect_password");
    var weak = document.getElementById("weak-pwd");
    var invalid = document.getElementById("invalid");
    var exists = document.getElementById("exists");
    
    
    if(userpwd != userpwd2){
        incorrect.style.display = "block";
        weak.style.display = "none";
        invalid.style.display = "none";
        exists.style.display = "none";
    }else{
        firebase.auth().createUserWithEmailAndPassword(username, userpwd).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;

            incorrect.style.display = "none";
            
            if (errorCode == 'auth/weak-password') {
                weak.style.display = "block";
                invalid.style.display = "none";
                exists.style.display = "none";
            }
            if (errorCode == 'auth/invalid-email') {
                weak.style.display = "none";
                invalid.style.display = "block";
                exists.style.display = "none";
            }
            if (errorCode == 'auth/email-already-in-use') {
                weak.style.display = "none";
                invalid.style.display = "none";
                exists.style.display = "block";
            }

        });

    }
}

//
//Database Functions
//
 
const docRef = firestore.collection("input");
const sound1 = document.querySelector("#sound1").value;
const freq1 = document.querySelector("#freq1").value;
const amp1 = document.querySelector("#amp1").value;

const sound2 = document.querySelector("#sound2").value;
const freq2 = document.querySelector("#freq2").value;
const amp2 = document.querySelector("#amp2").value;

const rating = document.querySelector("#rating").value;
const date = document.querySelector("#date").value;
const exp = document.querySelector("#exp").value;

const saveButton = document.getElementById("submit");

saveButton.addEventListener("click", function(){
    console.log("I am trying to save input to Firestore");
    docRef.add({
        sound1: sound1,
        freq1: freq1,
        amp1: amp1,
        sound2: sound2,
        freq2: freq2,
        amp2: amp2,
        rating: rating,
        date: date,
        exp: exp
    }).then(function(){
        console.log("status saved!");
    }).catch(function(error){
        console.log("got an error", error);
    });
})
