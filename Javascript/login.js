var firestore = firebase.firestore();

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