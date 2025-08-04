document.addEventListener('DOMContentLoaded', () => {
    const authStatus = document.getElementById('authStatus');
    const loginLink = document.getElementById('loginLink');

    function updateAuthStatus() {
        const token = localStorage.getItem('jwtToken');
        const isLoginPage = window.location.pathname.split('/').pop() === 'login.html';

        if (token) {
            authStatus.innerHTML = `
                <span>Welcome!</span>
                <a href="#" id="logoutLink">Logout</a>
            `;
            document.getElementById('logoutLink').addEventListener('click', logout);
        } else {
            if (isLoginPage) {
                authStatus.innerHTML = '<a href="index.html" id="loginLink">Home</a>';
            } else {
                authStatus.innerHTML = '<a href="login.html" id="loginLink">Login</a>';
            }
        }
    }

    function logout() {
        localStorage.removeItem('jwtToken');
        updateAuthStatus();
        window.location.href = 'index.html';
    }

    updateAuthStatus();
});