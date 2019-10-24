const host = 'http://localhost:8080';

function login() {
    let username = document.getElementById('username-input').value;
    let password = document.getElementById('password-input').value;

    let credentials = {
        'username': username,
        'password': password
    };

    let url = `${host}/login`;
    fetch(url, {
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    })
        .then(response => response.json())
        .then(body => {
            localStorage.setItem('jwt', body['jwt']);
            window.location.replace('/pages/chat.html');
        });
}
