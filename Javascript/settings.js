var firestore = firebase.firestore();



firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
      
    firestore.collection('USERS').doc('subscriptions').get().then(function(doc) {
      if (doc.exists) {
        console.log("Document data:", doc.data());
    } else {
        // doc.data() will be undefined in this case
        console.log("No such document!");
        }
    }).catch(function(error) {
        console.log("Error getting document:", error);
    });

      
      
      var subscribeBtn = document.getElementById("subscribeBtn");
      var unSubscribeBtn = document.getElementById("unSubscribeBtn");
      records.style.display = "flex";
      var currentUser = firebase.auth().currentUser.email;

      subscribeBtn.addEventListener("click", function(){
          
      });
      unSubscribeBtn.addEventListener("click", function(){
          
      });
      }else {
      document.getElementById("login").style.display = "block";
      document.getElementById("recordings").style.display = "none";
  }
});