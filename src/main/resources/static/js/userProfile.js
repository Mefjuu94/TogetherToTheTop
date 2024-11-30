document.addEventListener("DOMContentLoaded", function () {
    // const modalHeader = document.getElementById("modal-header");
    // const modal = document.getElementById("edit-modal");
    // const fieldValueInput = document.getElementById("field-value");
    // const fieldNameInput = document.getElementById("fieldName");
    // const closeModal = document.querySelector(".close");
    //
    // // Nasłuchuj kliknięć w edytowalne kafelki
    // document.querySelectorAll('.tile.editable').forEach(tile => {
    //     tile.addEventListener('click', function () {
    //         const field = this.getAttribute("data-field"); // Nazwa pola np. email, username
    //         const label = this.getAttribute("data-label"); // Etykieta np. Email, Username
    //         const currentValue = this.querySelector("p").textContent; // Obecna wartość pola
    //
    //         // Ustaw odpowiedni nagłówek modala
    //         modalHeader.textContent = `Edit ${label}`;
    //
    //         // Wypełnij pole formularza aktualną wartością
    //         fieldValueInput.value = currentValue;
    //
    //         // Ustaw nazwę pola, które będzie edytowane
    //         fieldNameInput.value = field;
    //
    //         // Otwórz modal
    //         modal.style.display = "block";
    //     });
    // });
    //
    // // Zamykanie modala
    // closeModal.addEventListener('click', function () {
    //     modal.style.display = "none";
    // });
    //
    // // Zamykanie modala po kliknięciu poza jego obszar
    // window.addEventListener('click', function (event) {
    //     if (event.target == modal) {
    //         modal.style.display = "none";
    //     }
    // });

    // Pobieranie elementów modala i jego zamknięcia
    const modal = document.getElementById("edit-modal");
    const closeBtn = document.querySelector(".close");
    const tilesEditable = document.querySelectorAll(".tile-editable");

// Funkcja do otwierania modala
    function openModal(fieldLabel, fieldName, currentValue) {
        document.getElementById("modal-header").innerText = `Edit ${fieldLabel}`;
        document.getElementById("fieldName").value = fieldName;
        document.getElementById("field-value").value = currentValue;
        modal.style.display = "block";
    }

// Funkcja do zamykania modala
    function closeModal() {
        modal.style.display = "none";
    }

// Dodawanie event listenera do zamykania po kliknięciu "X"
    closeBtn.addEventListener("click", closeModal);

// Dodawanie event listenera do każdego edytowalnego kafelka, aby otworzyć modal
    tilesEditable.forEach(tile => {
        tile.addEventListener("click", function () {
            const fieldLabel = this.getAttribute("data-label");
            const fieldName = this.getAttribute("data-field");
            const currentValue = this.querySelector("p").innerText;
            openModal(fieldLabel, fieldName, currentValue);
        });
    });

// Zamknięcie modala po kliknięciu poza jego obszarem
    window.addEventListener("click", function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

});
