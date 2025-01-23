document.addEventListener("DOMContentLoaded", function () {

    const contactModal = document.getElementById("contactModal");
    const aboutModal = document.getElementById("aboutModal");
    const loginModal = document.getElementById("loginModal");
    const registerModal = document.getElementById("registerModal");

    const loginButton = document.querySelector(".gridLogin .tile a[href='/login']");
    const registerButton = document.querySelector(".gridLogin .tile a[href='/register']");

    //to view login modal

    const profileButton = document.querySelector(".grid .tile a[href='/login']");
    const mapButton = document.getElementById("mapButton");
    const announcementButton = document.getElementById("announcementButton");
    const find_TripButton = document.getElementById("find_TripButton");

    const closeLogin = loginModal.querySelector(".close");
    const closeRegister = registerModal.querySelector(".close");


    if (loginButton) {
        loginButton.onclick = function (event) {
            event.preventDefault();

            loginModal.style.display = "block";
        }
    }

    if (registerButton) {
        registerButton.onclick = function (event) {
            event.preventDefault();
            registerModal.style.display = "block";
        }
    }

    if (mapButton) {
        mapButton.onclick = function (event) {
            event.preventDefault();
            loginModal.style.display = "block";
        }
    }

    if (profileButton) {
        profileButton.onclick = function (event) {
            event.preventDefault();
            loginModal.style.display = "block";
        }
    }

    if (announcementButton) {
        announcementButton.onclick = function (event) {
            event.preventDefault();
            loginModal.style.display = "block";
        }
    }

    if (find_TripButton) {
        find_TripButton.onclick = function (event) {
            event.preventDefault();
            loginModal.style.display = "block";
        }
    }

/////////////////////////////////
    closeLogin.onclick = function () {
        loginModal.style.display = "none";
    }

    closeRegister.onclick = function () {
        registerModal.style.display = "none";
    }


//////////////////////////////////////////////
    window.onclick = function (event) {
        if (event.target === loginModal) {
            loginModal.style.display = "none";
        }
        if (event.target === registerModal) {
            registerModal.style.display = "none";
        }
        if (event.target === aboutModal) {
            aboutModal.style.display = "none";
        }
        if (event.target === contactModal) {
            contactModal.style.display = "none";
        }
    }


    const aboutButton = document.getElementById("aboutButton");
    const contactButton = document.getElementById("contactButton");

    aboutButton.onclick = function (event) {
        event.preventDefault();
        aboutModal.style.display = "block";
    }

    contactButton.onclick = function (event) {
        event.preventDefault();
        contactModal.style.display = "block";
    }

    const closeAbout = aboutModal.querySelector(".close");
    const closeContact = contactModal.querySelector(".close");

    closeAbout.onclick = function () {
        aboutModal.style.display = "none";
    }

    closeContact.onclick = function () {
        contactModal.style.display = "none";
    }

    // Zamknięcie modalów po kliknięciu w tło
    window.onclick = function (event) {
        if (event.target === aboutModal) {
            aboutModal.style.display = "none";
        }
        if (event.target === contactModal) {
            contactModal.style.display = "none";
        }
        if (event.target === loginModal) {
            loginModal.style.display = "none";
        }
        if (event.target === registerModal) {
            registerModal.style.display = "none";
        }
    }

    const emailInput = document.getElementById("emailInput");
    const passwordInput = document.getElementById("passwordInput");

    const tooltipContent = document.getElementById("tooltipContent");

    // Pobranie referencji do elementów podpowiedzi (dla hasła)
    const lengthHint = document.createElement('p');
    const uppercaseHint = document.createElement('p');
    const numberHint = document.createElement('p');

    lengthHint.textContent = "- At least 8 signs";
    uppercaseHint.textContent = "- At least 1 uppercase";
    numberHint.textContent = "- At least 1 digit";

    lengthHint.classList.add("invalid");
    uppercaseHint.classList.add("invalid");
    numberHint.classList.add("invalid");

    let isPasswordCorrect = false;
    let isEmailCorrect = validateEmail(emailInput);

    // Funkcja do aktualizacji tooltipa dla hasła
    function updatePasswordTooltip(password) {
        const hasUpperCase = /[A-Z]/.test(password);
        const hasNumber = /[0-9]/.test(password);
        const hasMinLength = password.length >= 8;

        lengthHint.className = hasMinLength ? "valid" : "invalid";
        uppercaseHint.className = hasUpperCase ? "valid" : "invalid";
        numberHint.className = hasNumber ? "valid" : "invalid";

        if (hasMinLength && hasNumber && hasUpperCase){
            isPasswordCorrect = true;
        }else {
            isPasswordCorrect = false;
            console.log('incorrect password')
        }
    }

    // Funkcja walidacji e-maila
    function validateEmail(email) {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return emailRegex.test(email);
    }

    // Funkcja zmiany zawartości tooltipa
    function updateTooltipContent(focusField) {
        if (focusField === "email") {
            const emailValue = emailInput.value;
            if (!emailValue) {
                tooltipContent.innerHTML = "<p>Please enter email address.</p>";
            } else if (validateEmail(emailValue)) {
                tooltipContent.innerHTML = "<p style='color: #2baf31; font-weight: bold;'>e-mail is valid!</p>";
                isEmailCorrect = true;
            } else {
                tooltipContent.innerHTML = "<p style='color: red;'>Please enter a valid email address.</p>";
                isEmailCorrect = false;
            }
        } else if (focusField === "password") {
            tooltipContent.innerHTML = ""; // Czyszczenie zawartości
            tooltipContent.appendChild(lengthHint);
            tooltipContent.appendChild(uppercaseHint);
            tooltipContent.appendChild(numberHint);
        }
    }

    // Obsługa zdarzeń dla e-maila
    emailInput.addEventListener("focus", function () {
        updateTooltipContent("email");
    });

    emailInput.addEventListener("input", function () {
        updateTooltipContent("email");
        if (isEmailCorrect && isPasswordCorrect){
            document.getElementById('submitRegister').style.backgroundColor = "#2baf31";
        }else {
            document.getElementById('submitRegister').style.backgroundColor = "grey";
        }
    });

    passwordInput.addEventListener("focus", function () {
        updateTooltipContent("password");
    });



    if (!localStorage.getItem('privacyPolicyAccepted')) {
        // Pokaż modal, jeśli użytkownik jeszcze nie zaakceptował polityki
        document.getElementById('privacyPolicyModal').style.display = 'block';
    }

    // Obsługa zamykania modala
    document.getElementById('closePrivacyPolicy').addEventListener('click', function () {
        document.getElementById('privacyPolicyModal').style.display = 'none';
    });

    // Obsługa akceptacji polityki prywatności
    document.getElementById('acceptPrivacyPolicy').addEventListener('click', function () {
        // Zapisywanie w localStorage, że polityka została zaakceptowana
        localStorage.setItem('privacyPolicyAccepted', 'true');
        document.getElementById('privacyPolicyModal').style.display = 'none';
    });

});

