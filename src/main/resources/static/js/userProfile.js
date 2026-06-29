document.addEventListener("DOMContentLoaded", function () {

    const modal = document.getElementById("edit-modal");
    const closeBtn = document.querySelector(".close");
    const tilesEditable = document.querySelectorAll(".tile-editable");
    const fieldValueInput = document.getElementById("field-value");
    const avatarFileInput = document.getElementById("avatar-file-input");
    const fieldNameInput = document.getElementById("fieldName");

    function openModal(fieldLabel, fieldName, currentValue) {
        document.getElementById("modal-header").innerText = `Edit ${fieldLabel}`;
        fieldNameInput.value = fieldName;

        modal.style.display = "flex";

        if (fieldName === "avatar") {
            fieldValueInput.style.display = "none";
            fieldValueInput.required = false;
            avatarFileInput.style.display = "block";
            avatarFileInput.required = true;
            fieldValueInput.value = ""; 
        } else {
            fieldValueInput.style.display = "block";
            fieldValueInput.required = true;
            avatarFileInput.style.display = "none";
            avatarFileInput.required = false;

            fieldValueInput.value = currentValue;
        }

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

    // Convert image to Base64 upon file selection
    // Compression, cropping to a square, and conversion to a small Base64 string
    avatarFileInput.addEventListener("change", function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const img = new Image();
                img.onload = function () {
                    
                    const canvas = document.createElement("canvas");
                    const size = 150; // Initial avatar size: 150x150px
                    canvas.width = size;
                    canvas.height = size;
                    const ctx = canvas.getContext("2d");
                    
                    let sourceX = 0;
                    let sourceY = 0;
                    let sourceSize = Math.min(img.width, img.height);

                    // If the image is wide (panoramic), crop the center horizontally
                    if (img.width > img.height) {
                        sourceX = (img.width - img.height) / 2;
                    } 
                    // If the image is tall (portrait orientation), we crop the vertical center.
                    else if (img.height > img.width) {
                        sourceY = (img.height - img.width) / 2;
                    }

                    // Draw the cropped and scaled image onto the canvas
                    ctx.drawImage(img, sourceX, sourceY, sourceSize, sourceSize, 0, 0, size, size);

                    // Generate a lightweight Base64 string in JPEG format with 80% quality
                    const compressedBase64 = canvas.toDataURL("image/jpeg", 0.8);
                    
                    // Assign the ready-made, short text from base64 to the form input
                    fieldValueInput.value = compressedBase64;
                    console.log("Avatar skompresowany pomyślnie. Rozmiar ciągu: " + compressedBase64.length + " znaków.");
                };
                img.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });

    function closeModal() {
        modal.style.display = "none";
        avatarFileInput.value = ""; // reset pliku
    }

    function validateNumericInput(event) {
        const inputValue = event.target.value;
        if (!/^\d*\.?\d*$/.test(inputValue)) {
            event.target.value = inputValue.replace(/[^0-9.]/g, "");
        }
    }

    function validateAlphabeticInput(event) {
        const inputValue = event.target.value;
        event.target.value = inputValue.replace(/[^a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\s-]/g, "");
    }

    closeBtn.addEventListener("click", closeModal);

    tilesEditable.forEach(tile => {
        tile.addEventListener("click", function () {
            const fieldLabel = this.getAttribute("data-label");
            const fieldName = this.getAttribute("data-field");
            
            let currentValue = "";
            if (fieldName !== "avatar") {
                const pElement = this.querySelector("p");
                currentValue = pElement ? pElement.innerText : "";
            }
            
            openModal(fieldLabel, fieldName, currentValue);
        });
    });

    window.addEventListener("click", function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });
});