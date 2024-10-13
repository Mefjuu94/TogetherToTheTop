document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("edit-modal");
    const modalHeader = document.getElementById("modal-header");
    const fieldValueInput = document.getElementById("field-value");
    const fieldNameInput = document.getElementById("fieldName");
    const closeModal = document.querySelector(".close");

    // Nasłuchuj kliknięć w edytowalne kafelki
    document.querySelectorAll('.tile.editable').forEach(tile => {
        tile.addEventListener('click', function () {
            const field = this.getAttribute("data-field"); // Nazwa pola np. email, username
            const label = this.getAttribute("data-label"); // Etykieta np. Email, Username
            const currentValue = this.querySelector("p").textContent; // Obecna wartość pola

            // Ustaw odpowiedni nagłówek modala
            modalHeader.textContent = `Edit ${label}`;

            // Wypełnij pole formularza aktualną wartością
            fieldValueInput.value = currentValue;

            // Ustaw nazwę pola, które będzie edytowane
            fieldNameInput.value = field;

            // Otwórz modal
            modal.style.display = "block";
        });
    });

    // Zamykanie modala
    closeModal.addEventListener('click', function () {
        modal.style.display = "none";
    });

    // Zamykanie modala po kliknięciu poza jego obszar
    window.addEventListener('click', function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    });
});
