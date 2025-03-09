let trip = null;

document.getElementById('trip_info').onclick = function () {
    tripDescription = this.getAttribute('tripDescription');
    destination = this.getAttribute('destination');
    tripDuration = this.getAttribute('tripDuration');
    closedGroup = this.getAttribute('closedGroup');
    amountOfClosedGroup = this.getAttribute('amountOfClosedGroup');
    DriverPeople = this.getAttribute('DriverPeople');
    amountOfDriverPeople = this.getAttribute('amountOfDriverPeople');

    console.log(trip);
};


document.addEventListener("DOMContentLoaded", function () {

    const tripInfo = document.getElementById("trip_info");

    window.onclick = function (event) {
        if (event.target === tripInfo) {
            tripInfo.style.display = "none";
        }

        const aboutButton = document.getElementById("trip_info");


        aboutButton.onclick = function (event) {
            event.preventDefault();
            tripInfo.style.display = "block";
        }

        const closeAbout = tripInfo.querySelector(".close");

        closeAbout.onclick = function () {
            tripInfo.style.display = "none";
        }

        window.onclick = function (event) {
            if (event.target === tripInfo) {
                tripInfo.style.display = "none";
            }
        }
    }
});