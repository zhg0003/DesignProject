var firestore = firebase.firestore();
const docRef = firestore.collection("input");
const recordings = document.getElementById("recordings");

//getRealtimeUpdates = function() {
//    docRef.onSnapshot(function(doc){
//        if(doc && doc.exists){
//            const myData = doc.data();
//            recordings.innerText = "Sound 1: " + myData.sound1 + "\tsound 2: " + myData.sound2 + "\nfreq 1: " + myData.freq1 + "\tfreq 2: " + myData.freq2 + "\namp 1: " + myData.amp1 + "\tamp 2: " + myData.amp2 + "\nrating: " + myData.rating + "\tDate: " + myData.date + "\nExperience: " + myData.exp;
//        }
//    });
//}

//db.collection("cities").get().then(function(querySnapshot) {
//    querySnapshot.forEach(function(doc) {
//        // doc.data() is never undefined for query doc snapshots
//        console.log(doc.id, " => ", doc.data());
//    });
//});

firestore.collection("input").get().then(function(querySnapshot) {
    querySnapshot.forEach(function(doc) {
        // doc.data() is never undefined for query doc snapshots
        console.log(doc.id, " => ", doc.data());
        recordings.innerText = "Sound 1: " + doc.data().sound1 + "\tsound 2: " + doc.data().sound2 + "\nfreq 1: " +  doc.data().freq1 + "\tfreq 2: " +  doc.data().freq2 + "\namp 1: " +  doc.data().amp1 + "\tamp 2: " +  doc.data().amp2 + "\nrating: " +  doc.data().rating + "\tDate: " +  doc.data().date + "\nExperience: " +  doc.data().exp;
    });
});

//getRealtimeUpdates();