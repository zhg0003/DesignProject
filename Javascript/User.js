var firestore = firebase.firestore();

//
//Authorization Function
//
firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    var currentUser = firebase.auth().currentUser.email;
    document.getElementById("logout").style.display = "block";
    document.getElementById("logDreams").style.display = "block";
    document.getElementById("settings").style.display = "block";
    document.getElementById("records").style.display = "block";
    var logged_in_as = document.getElementById("logged_in");
    logged_in_as.append(currentUser);
    logged_in_as.style.display = "block";
      
    if(document.getElementById("loggedin") != null){
        location.replace("./index.html");
        document.getElementById("loggedin").style.display = "block";
    }
    if(document.getElementById("signupLink") != null){
        document.getElementById("signupLink").style.display = "none";
    }
      

    if(document.getElementById("weak-pwd") != null){
        document.getElementById("weak-pwd").style.display = "none";
    } 
    if( document.getElementById("invalid") != null){
         document.getElementById("invalid").style.display = "none";
    } 
    if(document.getElementById("incorrect_password") != null){
        document.getElementById("incorrect_password").style.display = "none";
    }
    if(document.getElementById("exists") != null){
        document.getElementById("exists").style.display = "none";
    } 
    if(document.getElementById("wrong") != null){
        document.getElementById("wrong").style.display = "none";
    } 
    document.getElementById("not_logged_in").style.display = "none";   
    document.getElementById("login-link").style.display = "none";
    document.getElementById("signup-link").style.display = "none";
      
  } else {
    // No user is signed in.
      document.getElementById("logout").style.display = "none";
      document.getElementById("logDreams").style.display = "none";
      document.getElementById("settings").style.display = "none";
      document.getElementById("records").style.display = "none";
      document.getElementById("logged_in").style.display = "none";
      
      if(document.getElementById("loggedin") != null){
            document.getElementById("loggedin").style.display = "none";
        }
      if(document.getElementById("signupLink") != null){
        document.getElementById("signupLink").style.display = "block";
      }
      
      
      document.getElementById("not_logged_in").style.display = "block";
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