document.addEventListener('DOMContentLoaded', () => {
    const cartItemsList = document.getElementById('cartItemsList');
    const cartMessage = document.getElementById('cartMessage');
    const checkoutBtn = document.getElementById('checkoutBtn');

    const importFile = document.getElementById('importFile');
    const importBtn = document.getElementById('importBtn');
    const importMessage = document.getElementById('importMessage');

    async function loadCart() {
        try {
            const response = await fetch('http://gitlab:8081/api/cart');
            if (!response.ok) {
                throw new Error('Errore nel recupero del carrello');
            }
            const items = await response.json();
            cartItemsList.innerHTML = '';
            if (items.length === 0) {
                cartItemsList.innerHTML = '<li>Il carrello è vuoto.</li>';
            } else {
                items.forEach((item) => {
                    const li = document.createElement('li');
                    li.textContent = item;
                    cartItemsList.appendChild(li);
                });
            }
        } catch (error) {
            cartMessage.textContent = 'Errore: ' + error.message;
        }
    }

    async function checkout() {
        try {
            const response = await fetch('http://gitlab:8081/api/cart/checkout', {
                method: 'POST'
            });
            const message = await response.text();
            cartMessage.textContent = message;
            loadCart();
        } catch (error) {
            cartMessage.textContent = 'Errore nel completare l\'acquisto: ' + error.message;
        }
    }

    loadCart();
    checkoutBtn.addEventListener('click', checkout);
});
