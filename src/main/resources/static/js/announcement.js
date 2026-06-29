document.addEventListener("DOMContentLoaded", function () {
    const tripModal = document.getElementById("trip_info");
    if (!tripModal) return; 


    const closeBtn = tripModal.querySelector(".close");

    window.openTripDetails = function (element) {
        if (!element) return;

        const tripData = {
            description: element.getAttribute('tripDescription') || 'No description available',
            destination: element.getAttribute('destination') || '',
            duration: element.getAttribute('tripDuration') || '',
            closedGroup: element.getAttribute('closedGroup') === 'true',
            amountOfClosedGroup: element.getAttribute('amountOfClosedGroup') || '0',
            driverPeople: element.getAttribute('DriverPeople') || '',
            amountOfDriverPeople: element.getAttribute('amountOfDriverPeople') || '0'
        };

        console.log("Loaded Trip Data:", tripData);

        const modalDest = document.getElementById("modalDestination");
        const modalDesc = document.getElementById("modalDescription");
        const modalDuration = document.getElementById("modalDuration");

        if (modalDest) modalDest.textContent = tripData.destination;
        if (modalDesc) modalDesc.textContent = tripData.description;
        if (modalDuration) modalDuration.textContent = tripData.duration;

        tripModal.style.display = "flex";
    };

  
    if (closeBtn) {
        closeBtn.onclick = function () {
            tripModal.style.display = "none";
        };
    }

    // --- close modal when click background ---
    window.addEventListener("click", function (event) {
        if (event.target === tripModal) {
            tripModal.style.display = "none";
        }
    });
});