document.addEventListener("DOMContentLoaded", function () {

    // --- Pobranie Modali ---
    const contactModal = document.getElementById("contactModal");
    const aboutModal = document.getElementById("aboutModal");
    const loginModal = document.getElementById("loginModal");
    const registerModal = document.getElementById("registerModal");
    const findFriendModal = document.getElementById("findFriendModal");
    const findTripModal = document.getElementById("findTripModal");
    const privacyPolicyModal = document.getElementById("privacyPolicyModal");

    // --- Pobranie Przycisków/Kafelków (Poprawione selektory pod nową strukturę HTML) ---
    const loginButton = document.querySelector(".main-grid .tile a[href='/login']");
    const registerButton = document.querySelector(".main-grid .tile a[href='/register']");
    
    // Zabezpieczenie selektora profilu, szukamy kafelka z profile_not_active w obrazku
    const profileButton = document.querySelector(".main-grid .tile a[href='/login'] img[src*='user-profile_not_active']");
    
    const mapButton = document.getElementById("mapButton");
    const announcementButton = document.getElementById("announcementButton");
    const find_TripButton = document.getElementById("find_TripButton");
    const aboutButton = document.getElementById("aboutButton");
    const contactButton = document.getElementById("contactButton");
    
    // Nowe kafelki-przyciski dla zalogowanych użytkowników
    const findFriendTileBtn = document.getElementById("findFriendTileBtn");
    const findTripTileBtn = document.getElementById("findTripTileBtn");

    // --- Otwieranie Modali ---
    if (loginButton) {
        loginButton.onclick = function (event) {
            event.preventDefault();
            loginModal.style.display = "flex"; 
        }
    }

    if (registerButton) {
        registerButton.onclick = function (event) {
            event.preventDefault();
            registerModal.style.display = "flex";
        }
    }

    // Przekierowania na login dla niezalogowanych
    const loginRedirects = [mapButton, profileButton, announcementButton, find_TripButton];
    loginRedirects.forEach(btn => {
        if (btn) {
            btn.onclick = function (event) {
                event.preventDefault();
                loginModal.style.display = "flex";
            }
        }
    });

    if (aboutButton) {
        aboutButton.onclick = function (event) {
            event.preventDefault();
            aboutModal.style.display = "flex";
        }
    }

    if (contactButton) {
        contactButton.onclick = function (event) {
            event.preventDefault();
            contactModal.style.display = "flex";
        }
    }

    // Otwieranie nowych wyszukiwarek (dla zalogowanych)
    if (findFriendTileBtn) {
        findFriendTileBtn.onclick = function (event) {
            event.preventDefault();
            findFriendModal.style.display = "flex";
        }
    }

    if (findTripTileBtn) {
        findTripTileBtn.onclick = function (event) {
            event.preventDefault();
            findTripModal.style.display = "flex";
        }
    }

    // --- Zamykanie Modali przez kliknięcie w ikonę "X" ---
    const closeButtons = document.querySelectorAll(".modal .close");
    closeButtons.forEach(btn => {
        btn.onclick = function () {
            this.closest(".modal").style.display = "none";
        };
    });

    if (document.getElementById('closePrivacyPolicy')) {
        document.getElementById('closePrivacyPolicy').onclick = function () {
            privacyPolicyModal.style.display = 'none';
        };
    }

    // --- Zamykanie Modali przez kliknięcie w tło (Zunifikowane) ---
    window.onclick = function (event) {
        if (event.target.classList.contains("modal")) {
            event.target.style.display = "none";
        }
    };

    // --- Polityka Prywatności ---
    if (!localStorage.getItem('privacyPolicyAccepted') && privacyPolicyModal) {
        privacyPolicyModal.style.display = 'flex';
    }

    if (document.getElementById('acceptPrivacyPolicy')) {
        document.getElementById('acceptPrivacyPolicy').onclick = function () {
            localStorage.setItem('privacyPolicyAccepted', 'true');
            privacyPolicyModal.style.display = 'none';
        };
    }

    // --- Logika Walidacji Rejestracji ---
    const emailInput = document.getElementById("emailInput");
    const passwordInput = document.getElementById("passwordInput");
    const tooltipContent = document.getElementById("tooltipContent");

    let isPasswordCorrect = false;
    let isEmailCorrect = emailInput ? validateEmail(emailInput.value) : false;

    const lengthHint = document.createElement('p');
    const uppercaseHint = document.createElement('p');
    const numberHint = document.createElement('p');

    lengthHint.textContent = "- At least 8 signs";
    uppercaseHint.textContent = "- At least 1 uppercase";
    numberHint.textContent = "- At least 1 digit";

    lengthHint.classList.add("invalid");
    uppercaseHint.classList.add("invalid");
    numberHint.classList.add("invalid");

    function updatePasswordTooltip(password) {
        const hasUpperCase = /[A-Z]/.test(password);
        const hasNumber = /[0-9]/.test(password);
        const hasMinLength = password.length >= 8;

        lengthHint.className = hasMinLength ? "valid" : "invalid";
        uppercaseHint.className = hasUpperCase ? "valid" : "invalid";
        numberHint.className = hasNumber ? "valid" : "invalid";

        isPasswordCorrect = !!(hasMinLength && hasNumber && hasUpperCase);
    }

    function validateEmail(email) {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return emailRegex.test(email);
    }

    function updateTooltipContent(focusField) {
        if (!tooltipContent) return;
        
        if (focusField === "email") {
            const emailValue = emailInput.value;
            if (!emailValue) {
                tooltipContent.innerHTML = "<p>Please enter email address.</p>";
            } else if (validateEmail(emailValue)) {
                tooltipContent.innerHTML = "<p style='color: #2baf31; font-weight: bold;'>e-mail is valid! " +
                    " * Your name will be set to text before '@' - you can change it later.</p>";
                isEmailCorrect = true;
            } else {
                tooltipContent.innerHTML = "<p style='color: red;'>Please enter a valid email address.</p>";
                isEmailCorrect = false;
            }
        } else if (focusField === "password") {
            tooltipContent.innerHTML = "";
            tooltipContent.appendChild(lengthHint);
            tooltipContent.appendChild(uppercaseHint);
            tooltipContent.appendChild(numberHint);
        }
    }

    if (emailInput && passwordInput) {
        emailInput.addEventListener("focus", function () {
            updateTooltipContent("email");
        });

        passwordInput.addEventListener("focus", function () {
            updateTooltipContent("password");
        });

        function toggleSubmitButtonState() {
            const submitBtn = document.getElementById('submitRegister');
            if (submitBtn) {
                submitBtn.style.backgroundColor = (isEmailCorrect && isPasswordCorrect) ? "#2baf31" : "grey";
            }
        }

        passwordInput.addEventListener("input", function () {
            updatePasswordTooltip(passwordInput.value);
            toggleSubmitButtonState();
        });

        emailInput.addEventListener("input", function () {
            updateTooltipContent("email");
            toggleSubmitButtonState();
        });

        const submitRegister = document.getElementById('submitRegister');
        if (submitRegister) {
            submitRegister.addEventListener('click', function (event) {
                if (!isEmailCorrect || !isPasswordCorrect) {
                    event.preventDefault();
                }
            });
        }
    }
});