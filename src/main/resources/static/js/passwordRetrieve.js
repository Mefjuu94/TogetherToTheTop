document.addEventListener('DOMContentLoaded', function() {
    const newPasswordInput = document.getElementById('newPassword');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const resetButton = document.getElementById('resetButton');
    document.getElementById('resetButton').style.backgroundColor = "grey";

    // Funkcja do walidacji hasła
    function validatePassword() {
        const password = newPasswordInput.value;

        // Sprawdzenie warunków
        const isValidLength = password.length >= 8;
        const hasUppercase = /[A-Z]/.test(password);
        const hasDigit = /\d/.test(password);

        // Sprawdzenie, czy hasło jest poprawne
        return isValidLength && hasUppercase && hasDigit;
    }

    // Funkcja do sprawdzania, czy oba hasła są takie same
    function checkPasswordsMatch() {
        if (newPasswordInput.value === confirmPasswordInput.value && validatePassword()) {
            document.getElementById('resetButton').style.backgroundColor = "#28a745";
            resetButton.disabled = false; // Odblokuj przycisk
        } else {
            document.getElementById('resetButton').style.backgroundColor = "grey";
            resetButton.disabled = true; // Zablokuj przycisk
        }
    }

    // Nasłuchuj zmiany w polach hasła
    newPasswordInput.addEventListener('input', checkPasswordsMatch);
    confirmPasswordInput.addEventListener('input', checkPasswordsMatch);
});