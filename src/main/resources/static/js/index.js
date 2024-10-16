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

