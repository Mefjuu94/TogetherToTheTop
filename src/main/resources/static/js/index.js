let passwordValidation = false;
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

});

document.addEventListener('DOMContentLoaded', function() {
    function toggleSubmitButton() {
        if (passwordValidation) {
            document.getElementById("submitRegister").disabled = false;  // Odblokowanie przycisku
        } else {
            document.getElementById("submitRegister").disabled = true;   // Zablokowanie przycisku
        }
    }


    const usersDataString = document.getElementById("usersData").value;

// Usunięcie nawiasów kwadratowych i cudzysłowów, a następnie podzielenie po przecinkach
    const usersData2 = usersDataString.replace(/[\[\]" ]/g, '');
    const usersData1 =  usersData2.trim();
    const usersData = usersData1.split(',')


    let emailInput = document.getElementById("emailInput");
    let passwordInput = document.getElementById("passwordInput");

    emailInput.addEventListener('input', function() {

        let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        // Sprawdzamy, czy e-mail pasuje do wyrażenia regularnego
        if (emailRegex.test(emailInput.value)) {
            for (let i = 0; i < usersData.length; i++) {
                if (emailInput.value === usersData[i]) {
                    document.getElementById("emailInput").style.borderColor = "red";
                } else {
                    document.getElementById("emailInput").style.borderColor = "default";
                }
            }
            document.getElementById("emailInput").style.borderColor = "green";
        }else{
            document.getElementById("emailInput").style.borderColor = "red";
        }

    });

    passwordInput.addEventListener('input', function() {

        let passwordValue = passwordInput.value;

        // Warunki do sprawdzenia
        let hasUpperCase = /[A-Z]/.test(passwordValue);      // Sprawdza obecność wielkich liter
        let hasNumber = /[0-9]/.test(passwordValue);          // Sprawdza obecność cyfr
        let hasMinLength = passwordValue.length >= 8;         // Sprawdza długość co najmniej 8 znaków

        // Sprawdzenie, czy wszystkie warunki są spełnione
        if (hasUpperCase && hasNumber && hasMinLength) {
            document.getElementById("passwordInput").style.borderColor = "green"
            passwordValidation = true;
        } else {
            document.getElementById("passwordInput").style.borderColor = "red"
            passwordValidation = false;
        }

        toggleSubmitButton();
    });
});