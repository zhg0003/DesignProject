var firestore = firebase.firestore();
var tileNum = 0;


firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
      document.getElementById("login").style.display = "none";
      var records = document.getElementById("recordings");
      records.style.display = "flex";
      var currentUser = firebase.auth().currentUser.email;
      
      firestore.collection('USERS').doc(currentUser).collection('records').orderBy("date", "desc").get().then(function(querySnapshot) {
          querySnapshot.forEach(function(doc) {
              console.log(doc.id);
              var tile = document.createElement('div');
              tile.className = "tile";
              tile.id = "tile:" + tileNum;
              tileNum++;
              if(doc.data().sound1 != ""){
                var sound1 = document.createElement("p");
                sound1.textContent = "Sound 1: " + doc.data().sound1;
                tile.appendChild(sound1);
              }
              if(doc.data().sound2 != ""){
                var sound2 = document.createElement("p");
                sound2.textContent = "Sound 2: " + doc.data().sound2;
                tile.appendChild(sound2);
              }
              if(doc.data().freq1 != ""){
                var freq1 = document.createElement("p");
                freq1.textContent = "freq 1: " + doc.data().freq1;
                tile.appendChild(freq1); 
              }
              if(doc.data().freq2 != ""){
                var freq2 = document.createElement("p");
                freq2.textContent = "freq 2: " + doc.data().freq2;
                tile.appendChild(freq2);  
              }
              if(doc.data().amp1 != ""){
                var amp1 = document.createElement("p");
                amp1.textContent = "amplitude 1: " + doc.data().amp1;
                tile.appendChild(amp1);
              }
              if(doc.data().amp2 != ""){
                var amp2 = document.createElement("p");
                amp2.textContent = "amplitude 2: " + doc.data().amp2;
                tile.appendChild(amp2);
              }
              if(doc.data().rating != ""){
                var rating = document.createElement("p");
                rating.textContent = "rating: " + doc.data().rating;
                tile.appendChild(rating);
              }
              if(doc.data().exp != ""){
                var exp = document.createElement("p");
                exp.textContent = "Experience: " + doc.data().exp;
                tile.appendChild(exp);
              }
              if(doc.data().tags != ""){
                var tags = document.createElement("p");
                tags.textContent = "Tags: " + doc.data().tags;
                tile.appendChild(tags);
              }

              
              var date = document.createElement("p");
              date.textContent = "Submitted: " + doc.data().date + "\n";
              
              tile.appendChild(date);
              
              var button = document.createElement("button");
              button.textContent = "Delete Log!";
              button.className = "deleteMe";
//              addOnclick2(button, )
              tile.appendChild(button);
              
              records.appendChild(tile);
              
          });
      });
  } else {
      document.getElementById("login").style.display = "block";
      document.getElementById("recordings").style.display = "none";
  }
});


function addOnclick2(element, func, param, param2) {

	// the closure of noarg includes local variables
	// "func" and "param"
	function noarg() {
		func(param, param2);
		}

	element.onclick = noarg;  // it will remember its closure
}

function disappear(param){
    var div = document.getElementById(param);
    div.style.display = "none";
}
