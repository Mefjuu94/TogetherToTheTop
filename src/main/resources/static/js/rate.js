function prepareFormData(event, button) {
    event.preventDefault(); // Zatrzymuje domyślne wysyłanie formularza

    const form = button.closest('form'); // Znajdź formularz
    console.log("Form found:", form);

    const behaviorInput = form.querySelector('input[name="behavior"]');
    const userIdInput = form.querySelector('input[name="userId"]');
    const hiddenRate = form.querySelector('.hiddenRate'); // Ukryte pole dla oceny

    // Sprawdź, czy elementy istnieją
    if (!behaviorInput || !userIdInput || !hiddenRate) {
        console.error("One or more inputs are missing!");
        return;
    }

    const behavior = behaviorInput.value;
    const userId = userIdInput.value;

    // Sprawdź, czy starValue jest ustawione
    if (!starValue || starValue === 0) {
        alert("Please select a rating before submitting.");
        return;
    }

    // Przypisanie wartości do ukrytych pól
    hiddenRate.value = starValue; // Użycie starValue
    console.log("Hidden rate set to:", hiddenRate.value); // Debugowanie

    // Logowanie wartości
    console.log('Behavior:', behavior);
    console.log('Rate:', starValue);
    console.log('User ID:', userId);

    // Wysłanie formularza
    form.submit(); // Odkomentuj, aby wysłać formularz
}



let starValue = 0; // Zmienna do przechowywania wartości gwiazdki

const stars = document.querySelectorAll('.star');
const ratingValue = document.querySelector('.rating-value');

document.addEventListener('DOMContentLoaded', function () {

    stars.forEach(star => {
        star.addEventListener('click', function () {
            let value = this.getAttribute('data-value');
            ratingValue.textContent = value; // Ustawienie wartości oceny
            starValue = value;

            // Ustawienie wartości w ukrytym polu dla aktualnego formularza
            const form = this.closest('form');
            const hiddenRate = form.querySelector('.hiddenRate');
            hiddenRate.value = starValue;

            stars.forEach(s => s.classList.remove('selected')); // Usunięcie klasy 'selected' z wszystkich gwiazdek

            // Dodanie klasy 'selected' do klikniętej gwiazdki i wszystkich po niej (w układzie row-reverse)
            this.classList.add('selected');
            let nextSibling = this.nextElementSibling;
            while (nextSibling) {
                nextSibling.classList.add('selected');
                nextSibling = nextSibling.nextElementSibling;
            }
        });
    });

    // Funkcja do ustawienia obsługi gwiazdek dla aktywnego elementu
    function setupStarsForActiveItem() {
        const activeItem = document.querySelector('.cover-item.active');
        if (!activeItem) return;

        const stars = activeItem.querySelectorAll('.star');
        const hiddenRate = activeItem.querySelector('.hiddenRate'); // Ukryte pole dla oceny

        stars.forEach(star => {
            star.addEventListener('click', function () {
                const value = this.getAttribute('data-value');
                hiddenRate.value = value; // Ustawienie wartości w ukrytym polu
                ratingValue.textContent = value; // Ustawienie wartości oceny
                starValue = value;

                // Usuń zaznaczenie z innych gwiazdek
                stars.forEach(s => s.classList.remove('selected'));

                // Zaznacz klikniętą gwiazdkę i wszystkie poprzednie
                this.classList.add('selected');
                let nextSibling = this.nextElementSibling;
                while (nextSibling) {
                    nextSibling.classList.add('selected');
                    nextSibling = nextSibling.nextElementSibling;
                }
            });
        });
    }




    const coverflowInner = document.querySelector('.coverflow-inner');
    const coverItems = document.querySelectorAll('.cover-item');
    let currentIndex = 0;

    function updateCoverFlow() {
        // Obliczanie przesunięcia do wyśrodkowania aktywnego elementu
        const itemWidth = coverItems[0].offsetWidth; // Szerokość jednego elementu
        const containerWidth = coverflowInner.offsetWidth; // Szerokość kontenera
        const gap = 20; // Odstęp między elementami

        // Obliczamy przesunięcie tak, aby aktywny element był na środku
        const offset = (containerWidth / 2) - (itemWidth / 2) - (currentIndex * (itemWidth + gap));
        coverflowInner.style.transform = `translateX(${offset}px)`; // Przesunięcie kontenera

        // Aktualizacja klas dla każdego elementu
        coverItems.forEach((item, index) => {
            item.classList.remove('active', 'prev', 'next');
            item.style.opacity = '0.5'; // Domyślna przezroczystość
            item.style.transform = 'scale(0.9)'; // Domyślne zmniejszenie

            if (index === currentIndex) {
                item.classList.add('active');
                item.style.opacity = '1'; // Aktywny element jest w pełni widoczny
                item.style.transform = 'scale(1.1)'; // Powiększenie aktywnego elementu
            } else if (index === (currentIndex - 1 + coverItems.length) % coverItems.length) {
                item.classList.add('prev');
                item.style.opacity = '0.7';
            } else if (index === (currentIndex + 1) % coverItems.length) {
                item.classList.add('next');
                item.style.opacity = '0.7';
            } else {
                item.style.opacity = '0'; // Ukrycie pozostałych elementów
                item.style.transform = 'scale(0)'; // Ukrycie pozostałych elementów
            }
        });
    }

    function initializeCoverFlow() {
        updateCoverFlow(); // Wywołanie aktualizacji pozycji elementów
        setupStarsForActiveItem();
    }

    document.getElementById('prevBtn').addEventListener('click', function () {
        currentIndex = (currentIndex - 1 + coverItems.length) % coverItems.length;
        updateCoverFlow();
    });

    document.getElementById('nextBtn').addEventListener('click', function () {
        currentIndex = (currentIndex + 1) % coverItems.length;
        updateCoverFlow();
    });

    initializeCoverFlow(); // Inicjalizacja układu karuzeli przy ładowaniu strony
});