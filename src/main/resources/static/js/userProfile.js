document.addEventListener("DOMContentLoaded", function () {

    // download modal and it close
    const modal = document.getElementById("edit-modal");
    const closeBtn = document.querySelector(".close");
    const tilesEditable = document.querySelectorAll(".tile-editable");
    const fieldValueInput = document.getElementById("field-value");

// for open modal
    function openModal(fieldLabel, fieldName, currentValue) {
        document.getElementById("modal-header").innerText = `Edit ${fieldLabel}`;
        document.getElementById("fieldName").value = fieldName;
        document.getElementById("field-value").value = currentValue;
        fieldValueInput.value = currentValue;
        modal.style.display = "block";

        if (fieldName === "age") {
            fieldValueInput.addEventListener("input", validateNumericInput);
        } else {
            fieldValueInput.removeEventListener("input", validateNumericInput);
        }
        if (fieldName === "city" || fieldName === "username") {
            fieldValueInput.addEventListener("input", validateAlphabeticInput);
        } else {
            fieldValueInput.removeEventListener("input", validateAlphabeticInput);
        }
    }

// to close modal
        function closeModal() {
            modal.style.display = "none";
        }

        function validateNumericInput(event) {
            const inputValue = event.target.value;
            if (!/^\d*\.?\d*$/.test(inputValue)) {
                console.log("napis");
                event.target.value = inputValue.replace(/[^0-9.]/g, ""); // Usuwa wszystkie znaki poza cyframi i kropką
            }
        }

        function validateAlphabeticInput(event) {
            const inputValue = event.target.value;
            console.log("napis");
            event.target.value = inputValue.replace(/[^a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\s-]/g, ""); // Pozwala tylko litery i spacje
        }
// add event listener to button "X" in modal
        closeBtn.addEventListener("click", closeModal);

// event listener to open modal
        tilesEditable.forEach(tile => {
            tile.addEventListener("click", function () {
                const fieldLabel = this.getAttribute("data-label");
                const fieldName = this.getAttribute("data-field");
                const currentValue = this.querySelector("p").innerText;
                openModal(fieldLabel, fieldName, currentValue);
            });
        });
// close modal if click somewhere around modal
        window.addEventListener("click", function (event) {
            if (event.target === modal) {
                closeModal();
            }
        });
});