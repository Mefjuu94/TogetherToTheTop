document.addEventListener("DOMContentLoaded", function () {

    const addButton = document.getElementById("add_button");

    var tripId = document.getElementById("IDofTrip").textContent;
    var userId = document.getElementById("USER_id").textContent;


    addButton.onclick = function (event) {
        console.log("trip id " + tripId);
        console.log("my id: " + userId);
        document.getElementById("tripId").value = tripId;
        document.getElementById("userId").value = userId;
        document.getElementById("saveForm").submit(); // Wysyła formularz
    }


});