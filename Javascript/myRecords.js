var firestore = firebase.firestore();
var tileNum = 0;
var nullSector = firestore.collection('USERS').doc('null').collection('records');
document.getElementById("empty").style.display = "flex";

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
      document.getElementById("login").style.display = "none";
      var records = document.getElementById("recordings");
      records.style.display = "flex";
      var currentUser = firebase.auth().currentUser.email;
      var sortButton = document.getElementById("sorting");
      var sort = document.getElementById("sort").value;
      var order = document.getElementById("order").value;
      
      
      
      
      for(var i = 0; i < tileNum; i++){
              var tileID = "tile:" + i;
              var oldTiles = document.getElementById(tileID);
              if(oldTiles != null){
              records.removeChild(oldTiles);
              }
          }
          tileNum = 0;
          var sort = document.getElementById("sort").value;
          var order = document.getElementById("order").value;
          firestore.collection('USERS').doc(currentUser).collection('records').orderBy(sort, order).limit(100).get().then(function(querySnapshot) {
              querySnapshot.forEach(function(doc) {
                  var tile = document.createElement('div');
                  tile.className = "tile";
                  tile.id = "tile:" + tileNum;
                  tileNum++;
                  if(doc.data().sound1 != undefined && doc.data().sound1.trim() != "-1"){
                    var sound1 = document.createElement("p");
                    sound1.textContent = "Sound 1: " + doc.data().sound1;
                    tile.appendChild(sound1);
                  }
                  if(doc.data().sound2 != undefined && doc.data().sound2.trim() != "-1"){
                    var sound2 = document.createElement("p");
                    sound2.textContent = "Sound 2: " + doc.data().sound2;
                    tile.appendChild(sound2);
                  }
                  if(doc.data().freq1 != undefined && doc.data().freq1.trim() != "-1"){
                    var freq1 = document.createElement("p");
                    freq1.textContent = "freq 1: " + doc.data().freq1;
                    tile.appendChild(freq1); 
                  }
                  if(doc.data().freq2 != undefined && doc.data().freq2.trim() != "-1"){
                    var freq2 = document.createElement("p");
                    freq2.textContent = "freq 2: " + doc.data().freq2;
                    tile.appendChild(freq2);  
                  }
                  if(doc.data().amp1 != undefined && doc.data().amp1.trim() != "-1"){
                    var amp1 = document.createElement("p");
                    amp1.textContent = "amplitude 1: " + doc.data().amp1;
                    tile.appendChild(amp1);
                  }
                  if(doc.data().amp2 != undefined && doc.data().amp2.trim() != "-1"){
                    var amp2 = document.createElement("p");
                    amp2.textContent = "amplitude 2: " + doc.data().amp2;
                    tile.appendChild(amp2);
                  }
                  if(doc.data().rating != undefined && doc.data().rating.trim() != "-1"){
                    var rating = document.createElement("p");
                    rating.textContent = "rating: " + doc.data().rating;
                    tile.appendChild(rating);
                  }
                  if(doc.data().exp != undefined && doc.data().exp.trim() != "-1" ){
                    var exp = document.createElement("p");
                    exp.textContent = "Experience: " + doc.data().exp;
                    tile.appendChild(exp);
                  }
                  if(doc.data().tags != undefined && doc.data().tags.trim() != "-1"){
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
                  addOnclickDelete(button, deleteLog, tile.id, doc.data().date, currentUser);
    //              addOnclick2(button, )
                  tile.appendChild(button);
                  records.appendChild(tile);
                  document.getElementById("empty").style.display = "none";
              });
          });



      sortButton.addEventListener("click", function(){
          for(var i = 0; i < tileNum; i++){
              var tileID = "tile:" + i;
              var oldTiles = document.getElementById(tileID);
              if(oldTiles != null){
              records.removeChild(oldTiles);
              }
          }
          tileNum = 0;
          var sort = document.getElementById("sort").value;
          var order = document.getElementById("order").value;
          firestore.collection('USERS').doc(currentUser).collection('records').orderBy(sort, order).limit(100).get().then(function(querySnapshot) {
              querySnapshot.forEach(function(doc) {
                  var tile = document.createElement('div');
                  tile.className = "tile";
                  tile.id = "tile:" + tileNum;
                  tileNum++;
                  if(doc.data().sound1 != undefined && doc.data().sound1.trim() != "-1"){
                    var sound1 = document.createElement("p");
                    sound1.textContent = "Sound 1: " + doc.data().sound1;
                    tile.appendChild(sound1);
                  }
                  if(doc.data().sound2 != undefined && doc.data().sound2.trim() != "-1"){
                    var sound2 = document.createElement("p");
                    sound2.textContent = "Sound 2: " + doc.data().sound2;
                    tile.appendChild(sound2);
                  }
                  if(doc.data().freq1 != undefined && doc.data().freq1.trim() != "-1"){
                    var freq1 = document.createElement("p");
                    freq1.textContent = "freq 1: " + doc.data().freq1;
                    tile.appendChild(freq1); 
                  }
                  if(doc.data().freq2 != undefined && doc.data().freq2.trim() != "-1"){
                    var freq2 = document.createElement("p");
                    freq2.textContent = "freq 2: " + doc.data().freq2;
                    tile.appendChild(freq2);  
                  }
                  if(doc.data().amp1 != undefined && doc.data().amp1.trim() != "-1"){
                    var amp1 = document.createElement("p");
                    amp1.textContent = "amplitude 1: " + doc.data().amp1;
                    tile.appendChild(amp1);
                  }
                  if(doc.data().amp2 != undefined && doc.data().amp2.trim() != "-1"){
                    var amp2 = document.createElement("p");
                    amp2.textContent = "amplitude 2: " + doc.data().amp2;
                    tile.appendChild(amp2);
                  }
                  if(doc.data().rating != undefined && doc.data().rating.trim() != "-1"){
                    var rating = document.createElement("p");
                    rating.textContent = "rating: " + doc.data().rating;
                    tile.appendChild(rating);
                  }
                  if(doc.data().exp != undefined && doc.data().exp.trim() != "-1"){
                    var exp = document.createElement("p");
                    exp.textContent = "Experience: " + doc.data().exp;
                    tile.appendChild(exp);
                  }
                  if(doc.data().tags != undefined && doc.data().tags.trim() != "-1"){
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
                  addOnclickDelete(button, deleteLog, tile.id, doc.data().date, currentUser);
    //              addOnclick2(button, )
                  tile.appendChild(button);

                  records.appendChild(tile);

              });
          });
      })} else {
      location.replace("./index.html");
      document.getElementById("login").style.display = "block";
      document.getElementById("recordings").style.display = "none";
  }
});




function addOnclickDelete(button, func, tile, date, user) {

	// the closure of noarg includes local variables
	// "func" and "param"
	function noarg() {
		func(tile, date, user);
		}

	button.onclick = noarg;  // it will remember its closure
}

function deleteLog(tile, date, currentUser){
    

    nullSector.doc(date).delete().then(function() {
        console.log("Document successfully deleted!");
    }).catch(function(error) {
        console.error("Error removing document: ", error);
    });
    
    firestore.collection('USERS').doc(currentUser).collection('records').doc(date).delete().then(function() {
        console.log("Document successfully deleted!");
    }).catch(function(error) {
        console.error("Error removing document: ", error);
    });
    var div = document.getElementById(tile);
    div.style.display = "none";
}
