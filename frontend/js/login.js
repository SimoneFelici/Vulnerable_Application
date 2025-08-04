const loginForm = document.getElementById('loginForm');
const errorMessage = document.getElementById('loginError');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    errorMessage.classList.remove('active');

    const username = encodeURIComponent(document.getElementById('username').value);
    const password = encodeURIComponent(document.getElementById('password').value);

    try {
        const response = await fetch(
            `http://gitlab:8081/api/login?username=${username}&password=${password}`,
            {
                method: 'POST',
                headers: {
                    'Accept': 'application/json'
                }
            }
        );

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Login failed');
        }

        const responseData = await response.json();

        if (!responseData.token) {
            throw new Error('Invalid response format');
        }

        localStorage.setItem('jwtToken', responseData.token);
        window.location.href = 'index.html';

    } catch (error) {
        console.error('Login error:', error);
        errorMessage.textContent = error.message.startsWith('{')
            ? JSON.parse(error.message).error
            : error.message;
        errorMessage.classList.add('active');

        setTimeout(() => {
            errorMessage.classList.remove('active');
        }, 5000);
    }
});