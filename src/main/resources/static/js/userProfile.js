document.addEventListener("DOMContentLoaded", function () {

    // download modal and it close
    const modal = document.getElementById("edit-modal");
    const closeBtn = document.querySelector(".close");
    const tilesEditable = document.querySelectorAll(".tile-editable");

// for open modal
    function openModal(fieldLabel, fieldName, currentValue) {
        document.getElementById("modal-header").innerText = `Edit ${fieldLabel}`;
        document.getElementById("fieldName").value = fieldName;
        document.getElementById("field-value").value = currentValue;
        modal.style.display = "block";
    }

// to close modal
    function closeModal() {
        modal.style.display = "none";
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
