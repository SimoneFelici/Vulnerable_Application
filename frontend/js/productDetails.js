document.addEventListener('DOMContentLoaded', () => {
    function getAuthHeader() {
        const token = localStorage.getItem('jwtToken');
        return token ? { 'Authorization': `Bearer ${token}` } : {};
    }
    const productDetailsContent = document.getElementById('product-details-content');
    let currentProduct = null;

    const urlParams = new URLSearchParams(window.location.search);
    const productName = urlParams.get('name');

    if (productName) {
        fetchProductDetails(productName);
    } else {
        productDetailsContent.innerHTML = '<p>Product not found.</p>';
    }

    async function fetchProductDetails(productName) {
        try {
            const response = await fetch(
                `http://gitlab:8081/api/products/${encodeURIComponent(productName)}`,
                { headers: getAuthHeader() }
            );

            if (response.status === 401 || response.status === 403) {
                productDetailsContent.innerHTML = `
                    <div class="auth-error-message">
                        <h2>Accesso Negato</h2>
                        <p>Questa pagina è accessibile solo dagli amministratori.</p>
                        ${response.status === 401 ? '<a href="login.html" class="login-link">Accedi come amministratore</a>' : ''}
                        <a href="index.html" class="back-link">Torna alla lista prodotti</a>
                    </div>
                `;
                return;
            }
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            currentProduct = await response.json();

            productDetailsContent.innerHTML = `
                <div class="product-details">
                    <img src="http://gitlab:8081/images/${currentProduct.name.toLowerCase()}.png"
                         alt="${currentProduct.name}"
                         class="product-image-large"
                         onerror="this.src='http://gitlab:8081/images/apple.png'">
                    <h2>${currentProduct.name}</h2>
                    <div id="product-description">${currentProduct.description}</div>
                    <a href="index.html" class="back-to-list">Back to Products</a>
                    <button id="addToCartBtn">Add to Cart</button>
                </div>

                <div class="edit-section">
                    <h3>Edit Description</h3>
                    <textarea id="description-input" rows="4">${currentProduct.description}</textarea>
                    <button id="save-button">Save Changes</button>
                </div>
            `;

            document.getElementById('save-button').addEventListener('click', updateDescription);
            document.getElementById('addToCartBtn').addEventListener('click', addToCart);

        } catch (error) {
            console.error('Error:', error);
            productDetailsContent.innerHTML = `
                <div class="error-message">
                    <h2>Errore</h2>
                    <p>Si è verificato un errore durante il caricamento dei dettagli.</p>
                    <a href="index.html" class="back-link">Torna alla lista prodotti</a>
                </div>
            `;
        }
    }

    async function updateDescription() {
        const newDescription = document.getElementById('description-input').value;

        try {
            await fetch(`http://gitlab:8081/api/products/${currentProduct.name}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    ...getAuthHeader()
                },
                body: JSON.stringify({ description: newDescription })
            });

            document.getElementById('product-description').innerHTML = newDescription;
        } catch (error) {
            console.error('Update failed:', error);
            alert('Error updating description');
        }
    }

    async function addToCart() {
        const xmlPayload = `<?xml version="1.0" encoding="UTF-8"?>
<cart>
  <item>${currentProduct.name}</item>
</cart>`;

        try {
            const response = await fetch('http://gitlab:8081/api/cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/xml'
                },
                body: xmlPayload
            });
            const text = await response.text();
            alert("Aggiunto al carrello: " + text);
        } catch (error) {
            console.error('Error adding to cart:', error);
            alert('Errore durante l\'aggiunta al carrello: ' + error.message);
        }
    }
});
