var firestore = firebase.firestore();


firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
      document.getElementById("login").style.display = "none";
      var records = document.getElementById("recordings");
      records.style.display = "flex";
      var currentUser = firebase.auth().currentUser.email;
      
      firestore.collection('USERS').doc(currentUser).collection('records').get().then(function(querySnapshot) {
          querySnapshot.forEach(function(doc) {
              var tile = document.createElement('div');
              tile.className = "tile";
              var sound1 = document.createElement("p");
              sound1.textContent = "Sound 1: " + doc.data().sound1;
              var sound2 = document.createElement("p");
              sound2.textContent = "Sound 2: " + doc.data().sound2;
              var freq1 = document.createElement("p");
              freq1.textContent = "freq 1: " + doc.data().freq1;
              var freq2 = document.createElement("p");
              freq2.textContent = "freq 2: " + doc.data().freq2;
              var amp1 = document.createElement("p");
              amp1.textContent = "amplitude 1: " + doc.data().amp1;
              var amp2 = document.createElement("p");
              amp2.textContent = "amplitude 2: " + doc.data().amp2;
              var rating = document.createElement("p");
              rating.textContent = "rating: " + doc.data().rating;
              var date = document.createElement("p");
              date.textContent = "date: " + doc.data().date;
              var exp = document.createElement("p");
              exp.textContent = "Experience: " + doc.data().exp;
              
              tile.appendChild(sound1);
              tile.appendChild(sound2);
              tile.appendChild(freq1);
              tile.appendChild(freq2);
              tile.appendChild(amp1);
              tile.appendChild(amp2);
              tile.appendChild(rating);
              tile.appendChild(date);
              tile.appendChild(exp);
              records.appendChild(tile);
              
          });
      });
  } else {
      document.getElementById("login").style.display = "block";
      document.getElementById("recordings").style.display = "none";
  }
});
