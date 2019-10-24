const host = 'http://localhost:8080';

function join() {
    let username = document.getElementById('username-input').value;
    let password = document.getElementById('password-input').value;

    let credentials = {
        'username': username,
        'password': password
    };

    let url = `${host}/join`;
    fetch(url, {
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    }).then(response => window.location.replace('/pages/login.html'));
}
