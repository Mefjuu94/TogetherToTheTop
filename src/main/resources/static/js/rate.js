document.addEventListener('DOMContentLoaded', function () {
    const coverflowInner = document.querySelector('.coverflow-inner');
    const coverItems = document.querySelectorAll('.cover-item');
    let currentIndex = 0;

    function setupStarsForActiveItem() {
        const activeItem = document.querySelector('.cover-item.active');
        if (!activeItem) return;

        const stars = activeItem.querySelectorAll('.star');
        const hiddenRate = activeItem.querySelector('.hiddenRate');
        const ratingValue = activeItem.querySelector('.rating-value');

        stars.forEach(star => {
            star.addEventListener('click', function () {
                const value = this.getAttribute('data-value');
                hiddenRate.value = value;
                ratingValue.textContent = value;

                stars.forEach(s => s.classList.remove('selected'));

                this.classList.add('selected');
                let nextSibling = this.nextElementSibling;
                while (nextSibling) {
                    nextSibling.classList.add('selected');
                    nextSibling = nextSibling.nextElementSibling;
                }
            });
        });
    }


    function updateCoverFlow() {
        const itemWidth = coverItems[0].offsetWidth;
        const containerWidth = coverflowInner.offsetWidth;
        const gap = 20;

        const offset = (containerWidth / 2) - (itemWidth / 2) - (currentIndex * (itemWidth + gap));
        coverflowInner.style.transform = `translateX(${offset}px)`;

        coverItems.forEach((item, index) => {
            item.classList.remove('active', 'prev', 'next');
            item.classList.add('inactive');
            item.style.opacity = '0.5';
            item.style.transform = 'scale(0.9)';

            if (index === currentIndex) {
                item.classList.add('active');
                item.classList.remove('inactive');
                item.style.opacity = '1';
                item.style.transform = 'scale(1.1)';
            } else if (index === (currentIndex - 1 + coverItems.length) % coverItems.length) {
                item.classList.add('prev');
                item.style.opacity = '0.7';
            } else if (index === (currentIndex + 1) % coverItems.length) {
                item.classList.add('next');
                item.style.opacity = '0.7';
            } else {
                item.style.opacity = '0';
                item.style.transform = 'scale(0)';
            }
        });

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

    updateCoverFlow();
});
