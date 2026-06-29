document.addEventListener('DOMContentLoaded', function () {
    const coverflowInner = document.querySelector('.coverflow-inner');
    const coverItems = document.querySelectorAll('.cover-item');
    let currentIndex = 0;

    function setupActiveItemLogic() {
        const activeItem = document.querySelector('.cover-item.active');
        if (!activeItem) return;

        const stars = activeItem.querySelectorAll('.star');
        const hiddenRate = activeItem.querySelector('.hiddenRate');
        const ratingValue = activeItem.querySelector('.rating-value');
        const approveButton = activeItem.querySelector('#approveButton');
        
        // cleaning old listeners
        stars.forEach(star => {
            const newStar = star.cloneNode(true);
            star.parentNode.replaceChild(newStar, star);
        });

        const freshStars = activeItem.querySelectorAll('.star');

        freshStars.forEach(star => {
            star.addEventListener('click', function () {
                const value = this.getAttribute('data-value');
                hiddenRate.value = value;
                ratingValue.textContent = value;

                freshStars.forEach(s => s.classList.remove('selected'));
                this.classList.add('selected');
                
                let nextSibling = this.nextElementSibling;
                while (nextSibling) {
                    nextSibling.classList.add('selected');
                    nextSibling = nextSibling.nextElementSibling;
                }

                approveButton.disabled = !hiddenRate.value || hiddenRate.value <= 0;
            });
        });

        approveButton.disabled = !hiddenRate.value || hiddenRate.value <= 0;
    }

    // rewrite comment bfore sendd to spring
window.prepareFormData = function() {
    // 1. take only middle tile
    const activeItem = document.querySelector('.cover-item.active');
    if (!activeItem) return;

    // 2. search input only in this active tile
    const visibleInput = activeItem.querySelector('input[name="behavior-visible"]');
    // 3. lf hidden input where active tile
    const hiddenInput = activeItem.querySelector('.hiddenBehavior');

    // 4. if 2 fields exis then rewrite safty
    if (visibleInput && hiddenInput) {
        hiddenInput.value = visibleInput.value;
    }
};

    document.querySelectorAll('.cover-item form').forEach(form => {
        form.addEventListener('submit', function() {
            window.prepareFormData();
        });
    });

    // math for make middle point
    function updateCoverFlow() {
    if (coverItems.length === 0) return;
    
    const itemWidth = coverItems[0].offsetWidth;
    const gap = 40; // css gap

    // center tile
    const offset = - (currentIndex * (itemWidth + gap));
    coverflowInner.style.transform = `translateX(${offset}px)`;

    coverItems.forEach((item, index) => {
        item.classList.remove('active', 'prev', 'next');
        
        if (index === currentIndex) {
            item.classList.add('active');
        } else if (index === (currentIndex - 1 + coverItems.length) % coverItems.length) {
            item.classList.add('prev');
        } else if (index === (currentIndex + 1) % coverItems.length) {
            item.classList.add('next');
        }
    });

    setupActiveItemLogic();
}

    document.getElementById('prevBtn').addEventListener('click', function () {
        currentIndex = (currentIndex - 1 + coverItems.length) % coverItems.length;
        updateCoverFlow();
    });

    document.getElementById('nextBtn').addEventListener('click', function () {
        currentIndex = (currentIndex + 1) % coverItems.length;
        updateCoverFlow();
    });

    window.addEventListener('resize', updateCoverFlow);
    updateCoverFlow();
});