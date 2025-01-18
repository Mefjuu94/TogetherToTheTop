document.addEventListener("DOMContentLoaded", function () {

    const deleteButton = document.getElementById("delete_comment");

    // Sprawdzenie, czy przycisk istnieje (ponieważ jest warunkowy)
    if (deleteButton) {
        // Dodanie event listenera na kliknięcie
        deleteButton.addEventListener("click", function () {
            console.log("delete button");
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        const toggleButton = document.getElementById("toggleRenewTripButton");
        const renewTripDiv = document.getElementById("renew_trip");

        toggleButton.addEventListener("click", function () {
            if (renewTripDiv.style.maxHeight && renewTripDiv.style.maxHeight !== "0px") {
                // Zwiń formularz
                renewTripDiv.style.maxHeight = "0";
            } else {
                // Rozwiń formularz
                renewTripDiv.style.maxHeight = renewTripDiv.scrollHeight + "px";
            }
        });
    });

});

var tripId = document.getElementById("IDofTrip").textContent;
const Thunderforest_API_KEY = 'b37f161fb8974439aa3fce208cdd45e0';

const map = L.map('map');

new L.tileLayer(
    'https://tile.thunderforest.com/outdoors/{z}/{x}/{y}.png?apikey=' + Thunderforest_API_KEY,
    {attribution: 'Maps © <a href="https://www.thunderforest.com">Thunderforest</a>, Data © <a href="https://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>'}
).addTo(map);

new L.GPX(
    '/downloadGpx?trip_Identity=' + tripId,
    {
        async: true,
        marker_options: {
            startIconUrl: 'https://cdn.jsdelivr.net/npm/leaflet-gpx@1.7.0/pin-icon-start.png',
            endIconUrl: 'https://cdn.jsdelivr.net/npm/leaflet-gpx@1.7.0/pin-icon-end.png',
            shadowUrl: 'https://cdn.jsdelivr.net/npm/leaflet-gpx@1.7.0/pin-shadow.png'
        }
    }
).on('loaded', function (e) {
    map.fitBounds(e.target.getBounds());
}).addTo(map);


function renewTrip() {
    // Pobranie wartości z pól
    const dateTime = document.getElementById("dateTime").value;
    const description = document.getElementById("descriptionTrip").value;

    // Checkboxy
    const driverChecked = document.getElementById("driver").checked;
    const animalsChecked = document.getElementById("animals").checked;
    const closedGroupChecked = document.getElementById("closedGroupCheckbox").checked;

    // Liczba pasażerów
    const driverPeople = driverChecked ? document.getElementById("DriverPeopleInput").value : "";
    const closedGroupPeople = closedGroupChecked ? document.getElementById("peopleInput").value : "";

    // Aktualizacja ukrytych pól w formularzu
    document.getElementById("hidden_date").value = dateTime;
    document.getElementById("hidden_description").value = description;
    document.getElementById("hidden_driver").value = driverChecked ? "yes" : "no";
    document.getElementById("hidden_driver_people").value = driverPeople;
    document.getElementById("hidden_animals").value = animalsChecked ? "yes" : "no";
    document.getElementById("hidden_closed_group").value = closedGroupChecked ? "yes" : "no";
    document.getElementById("hidden_closed_group_people").value = closedGroupPeople;

    // Wysłanie formularza
    document.getElementById("renew_trip").submit();
}

// Pokazywanie dodatkowych pól po zaznaczeniu checkboxów
document.getElementById("driver").addEventListener("change", function () {
    document.getElementById("DriverPeopleInputLabel").style.display = this.checked ? "block" : "none";
});

document.getElementById("closedGroupCheckbox").addEventListener("change", function () {
    document.getElementById("peopleInputLabel").style.display = this.checked ? "block" : "none";
});

function checkFormCompletion() {
    const dateTime = document.getElementById("dateTime").value;
    const description = document.getElementById("descriptionTrip").value;
    const button = document.getElementById("renew_trip_button");

    // Sprawdzanie, czy oba pola są wypełnione
    if (dateTime.trim() !== "" && description.trim() !== "") {
        document.getElementById("time").classList.add("completed");
        document.getElementById("descriptionTrip").classList.add("completed");
        button.classList.add("completed");
    } else {
        document.getElementById("dateTime").classList.remove("completed");
        document.getElementById("descriptionTrip").classList.remove("completed");
        button.classList.remove("completed");
    }

    if (description.trim() !== "") {
        document.getElementById("descriptionTrip").classList.add("completed");
        button.classList.add("completed");
    } else {
        document.getElementById("descriptionTrip").classList.remove("completed");
    }

    if (dateTime.trim() !== "" ) {
        document.getElementById("time").classList.add("completed");
        button.classList.add("completed");
    } else {
        document.getElementById("dateTime").classList.remove("completed");;
    }
}

function toggleDiv() {
    let div = document.getElementById("toggleDiv");
    div.classList.toggle("show");
    if (div.classList.contains("show")) {
        setTimeout(() => {
            div.scrollIntoView({ behavior: "smooth", block: "start" });
        }, 500); // Czekamy na zakończenie animacji
    }
}

// Nasłuchiwanie zmian w polach
document.getElementById("dateTime").addEventListener("input", checkFormCompletion);
document.getElementById("descriptionTrip").addEventListener("input", checkFormCompletion);

// Pokazywanie dodatkowych pól po zaznaczeniu checkboxów
document.getElementById("driver").addEventListener("change", function () {
    document.getElementById("DriverPeopleInputLabel").style.display = this.checked ? "block" : "none";
});

document.getElementById("closedGroupCheckbox").addEventListener("change", function () {
    document.getElementById("peopleInputLabel").style.display = this.checked ? "block" : "none";
});