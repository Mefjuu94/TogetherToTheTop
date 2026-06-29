import { Thunderforest_API_KEY } from '../passwords/apiKeys.js';

document.addEventListener("DOMContentLoaded", function () {

    const idElement = document.getElementById("IDofTrip");
    
    if (idElement) {
        const tripId = idElement.textContent.trim();
        const map = L.map('map');

        L.tileLayer(
            'https://tile.thunderforest.com/outdoors/{z}/{x}/{y}.png?apikey=' + Thunderforest_API_KEY,
            { attribution: 'Maps © <a href="https://www.thunderforest.com">Thunderforest</a>, Data © <a href="https://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>' }
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
    } else {
        console.error("Nie znaleziono elementu #IDofTrip na stronie. Mapa nie wystartowała.");
    }

    const deleteButton = document.getElementById("delete_comment");
    if (deleteButton) {
        deleteButton.addEventListener("click", function () {
            console.log("delete button");
        });
    }

    const toggleButton = document.getElementById('toggleButton');
    if (toggleButton) {
        toggleButton.addEventListener('click', toggleDiv);
    }

    function toggleDiv() {
        let div = document.getElementById("toggleDiv");
        if (div) {
            div.classList.toggle("show");
            if (div.classList.contains("show")) {
                setTimeout(() => {
                    div.scrollIntoView({ behavior: "smooth", block: "start" });
                }, 500);
            }
        }
    }


    const renewTripButton = document.getElementById('renew_trip_button');
    if (renewTripButton) {
        renewTripButton.addEventListener('click', renewTrip);
    }

    function renewTrip() {
        const dateTime = document.getElementById("dateTime")?.value || "";
        const description = document.getElementById("descriptionTrip")?.value || "";

        const driverChecked = document.getElementById("driver")?.checked || false;
        const animalsChecked = document.getElementById("animals")?.checked || false;
        const closedGroupChecked = document.getElementById("closedGroupCheckbox")?.checked || false;

        const driverPeople = driverChecked ? document.getElementById("DriverPeopleInput")?.value || "" : "";
        const closedGroupPeople = closedGroupChecked ? document.getElementById("peopleInput")?.value || "" : "";

        if (document.getElementById("hidden_date")) document.getElementById("hidden_date").value = dateTime;
        if (document.getElementById("hidden_description")) document.getElementById("hidden_description").value = description;
        if (document.getElementById("hidden_driver")) document.getElementById("hidden_driver").value = driverChecked ? "yes" : "no";
        if (document.getElementById("hidden_driver_people")) document.getElementById("hidden_driver_people").value = driverPeople;
        if (document.getElementById("hidden_animals")) document.getElementById("hidden_animals").value = animalsChecked ? "yes" : "no";
        if (document.getElementById("hidden_closed_group")) document.getElementById("hidden_closed_group").value = closedGroupChecked ? "yes" : "no";
        if (document.getElementById("hidden_closed_group_people")) document.getElementById("hidden_closed_group_people").value = closedGroupPeople;

        const renewForm = document.getElementById("renew_trip");
        if (renewForm) renewForm.submit();
    }


    const driverCheckbox = document.getElementById("driver");
    if (driverCheckbox) {
        driverCheckbox.addEventListener("change", function () {
            const label = document.getElementById("DriverPeopleInputLabel");
            if (label) label.style.display = this.checked ? "block" : "none";
        });
    }

    const closedCheckbox = document.getElementById("closedGroupCheckbox");
    if (closedCheckbox) {
        closedCheckbox.addEventListener("change", function () {
            const label = document.getElementById("peopleInputLabel");
            if (label) label.style.display = this.checked ? "block" : "none";
        });
    }

    const dateTimeInput = document.getElementById("dateTime");
    const descTripInput = document.getElementById("descriptionTrip");

    if (dateTimeInput) dateTimeInput.addEventListener("input", checkFormCompletion);
    if (descTripInput) descTripInput.addEventListener("input", checkFormCompletion);

    function checkFormCompletion() {
        const dateTime = document.getElementById("dateTime")?.value || "";
        const description = document.getElementById("descriptionTrip")?.value || "";
        const button = document.getElementById("renew_trip_button");
        const timeDiv = document.getElementById("time");
        const descInput = document.getElementById("descriptionTrip");

        if (dateTime.trim() !== "" && description.trim() !== "") {
            if (timeDiv) timeDiv.classList.add("completed");
            if (descInput) descInput.classList.add("completed");
            if (button) button.classList.add("completed");
        } else {
            if (dateTimeInput) dateTimeInput.classList.remove("completed");
            if (descInput) descInput.classList.remove("completed");
            if (button) button.classList.remove("completed");
        }

        if (description.trim() !== "") {
            if (descInput) descInput.classList.add("completed");
            if (button) button.classList.add("completed");
        } else {
            if (descInput) descInput.classList.remove("completed");
        }

        if (dateTime.trim() !== "") {
            if (timeDiv) timeDiv.classList.add("completed");
            if (button) button.classList.add("completed");
        } else {
            if (dateTimeInput) dateTimeInput.classList.remove("completed");
        }
    }
});