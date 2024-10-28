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


    const deleteButton = document.getElementById("delete_comment");

    // Sprawdzenie, czy przycisk istnieje (ponieważ jest warunkowy)
    if (deleteButton) {
        // Dodanie event listenera na kliknięcie
        deleteButton.addEventListener("click", function() {
            console.log("delete button");
        });
    }



});

var tripId = document.getElementById("IDofTrip").textContent;
const Thunderforest_API_KEY = 'b37f161fb8974439aa3fce208cdd45e0';

const map = L.map('map');

new L.tileLayer(
    'https://tile.thunderforest.com/outdoors/{z}/{x}/{y}.png?apikey=' + Thunderforest_API_KEY,
    { attribution: 'Maps © <a href="https://www.thunderforest.com">Thunderforest</a>, Data © <a href="https://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>' }
).addTo(map);

new L.GPX(
    '/downloadGpx?trip_Identity='+ tripId,
    {
        async: true,
        marker_options: {
            startIconUrl: 'https://cdn.jsdelivr.net/npm/leaflet-gpx@1.7.0/pin-icon-start.png',
            endIconUrl: 'https://cdn.jsdelivr.net/npm/leaflet-gpx@1.7.0/pin-icon-end.png',
            shadowUrl: 'https://cdn.jsdelivr.net/npm/leaflet-gpx@1.7.0/pin-shadow.png'
        }
    }
).on('loaded', function(e) {
    map.fitBounds(e.target.getBounds());
}).addTo(map);