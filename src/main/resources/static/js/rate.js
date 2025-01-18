// function prepareFormData(event, button, index) {
//     event.preventDefault(); // Zatrzymuje domyślne wysyłanie formularza
//
//     const form = button.closest('form'); // Znajdź formularz
//     console.log("Form found:", form);
//
//     // Pobranie danych na podstawie indeksu
//     const behaviorInput = document.getElementById(`behavior-${index}`);
//     const rateInput = document.getElementById(`rate-${index}`);
//     const userIdInput = document.getElementById(`userId-${index}`);
//     const myIdInput = form.querySelector('input[name="me"]');
//
//     // Sprawdź, czy elementy istnieją
//     if (!behaviorInput || !rateInput || !userIdInput || !myIdInput) {
//         console.error("One or more inputs are missing!");
//         return;
//     }
//
//     const behavior = behaviorInput.value;
//     const rate = rateInput.value;
//     const userId = userIdInput.value;
//     const myId = myIdInput.value;
//
//     // Przypisanie wartości do ukrytych pól
//     const hiddenBehavior = form.querySelector('.hiddenBehavior');
//     const hiddenRate = form.querySelector('.hiddenRate');
//
//     if (hiddenBehavior && hiddenRate) {
//         hiddenBehavior.value = behavior;
//         hiddenRate.value = rate;
//     } else {
//         console.error("Hidden inputs for behavior or rate are missing!");
//         return;
//     }
//
//     console.log('Behavior:', behavior);
//     console.log('Rate:', rate);
//     console.log('User ID:', userId);
//     console.log('My ID:', myId);
//
//     // Wysłanie formularza
//     form.submit();
// }
