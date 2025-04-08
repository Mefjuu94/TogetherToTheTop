document.addEventListener('DOMContentLoaded', function() {
    const newPasswordInput = document.getElementById('newPassword');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const resetButton = document.getElementById('resetButton');
    const codeInput = document.getElementById('codeText');
    document.getElementById('resetButton').style.backgroundColor = "grey";

    function validatePassword() {
        const password = newPasswordInput.value;

        const isValidLength = password.length >= 8;
        const hasUppercase = /[A-Z]/.test(password);
        const hasDigit = /\d/.test(password);

        return isValidLength && hasUppercase && hasDigit;
    }

    function validateCode() {
        const code = codeInput.value;
        const userCode = document.getElementById('userCode').value;

        const isTheSame = (userCode === code);

        return isTheSame;
    }


    function checkPasswordsMatch() {
        if (newPasswordInput.value === confirmPasswordInput.value && validatePassword() && validateCode()) {
            document.getElementById('resetButton').style.backgroundColor = "#28a745";
            resetButton.disabled = false; // unblock button
        } else {
            document.getElementById('resetButton').style.backgroundColor = "grey";
            resetButton.disabled = true; // block button
        }
    }

    newPasswordInput.addEventListener('input', checkPasswordsMatch);
    confirmPasswordInput.addEventListener('input', checkPasswordsMatch);
    codeInput.addEventListener('input', validateCode);
});