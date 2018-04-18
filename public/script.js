//function submitfunc(){
//    document.getElementById("finish").innerHTML = "Submitted!";
//}


firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    document.getElementById("logout").style.display = "block";
    document.getElementById("login-link").style.display = "none";
    document.getElementById("signup-link").style.display = "none";
    document.getElementById("weak-pwd").style.display = "none";
    document.getElementById("invalid").style.display = "none";
    document.getElementById("invalid").style.display = "none";
    var incorrect = document.getElementsByClassName("incorrect_password");
    incorrect[0].style.display = "none";
      
  } else {
    // No user is signed in.
      document.getElementById("logout").style.display = "none";
      document.getElementById("login-link").style.display = "block";
      document.getElementById("signup-link").style.display = "block";
  }
});


function logout(){
    firebase.auth().signOut();
}

                                   

function login(){
    var username = document.getElementById("username").value;
    var userpwd = document.getElementById("password").value;

    
    firebase.auth().signInWithEmailAndPassword(username, userpwd).catch(function(error) {
        // Handle Errors here.
        var incorrect = document.getElementsByClassName("incorrect_password")
        incorrect[0].style.display = "block";
    });
    
}


function signup(){
    var username = document.getElementById("txtEmail").value;
    var userpwd = document.getElementById("txtPassword").value;
    var userpwd2 = document.getElementById("txtPassword2").value;
    var incorrect = document.getElementsByClassName("incorrect_password");
    var weak = document.getElementById("weak-pwd");
    var invalid = document.getElementById("invalid");
    var exists = document.getElementById("exists");
    
    
    if(userpwd != userpwd2){
        incorrect[0].style.display = "block";
        weak.style.display = "block";
        invalid.style.display = "none";
        exists.style.display = "none";
    }else{
        firebase.auth().createUserWithEmailAndPassword(username, userpwd).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;

            incorrect[0].style.display = "none";
            
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