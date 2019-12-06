const host = 'http://localhost:8080';

const wsUrl = 'ws://127.0.0.1:8080/wschat';

let webSocket;

function fetchHistory() {
    let url = `${host}/messages`;
    fetch(url, {
        method: 'GET',
        mode: 'cors',
        headers: {'Content-Type': 'application/json', 'jwt': localStorage.jwt}
    }).then(response => response.json())
        .then(messages => {
            messages.forEach(prependMessage);
        });
}

function connect() {
    let jwtParam = encodeURI(`jwt=${localStorage.jwt}`);
    webSocket = new WebSocket(`${wsUrl}?${jwtParam}`);

    webSocket.onmessage = (response) => {
        let message = JSON.parse(response.data);
        prependMessage(message);
    };

    webSocket.onerror = () => alert('Ошибка авторизации');
}

function prependMessage(message) {
    const messages = document.getElementById('messages');
    let messageView = document.createElement('div');
    messageView.classList.add('card');
    messageView.classList.add('mt-4');
    messageView.innerHTML = `
    <div class="card-body">
        <h6 class="card-subtitle mb-2 text-muted">by ${message.author.username}</h6>
        <p class="card-text">${message.text}</p>
    </div>`;
    messages.prepend(messageView);
}

function sendMessage() {
    let input = document.getElementById('message-input');
    let text = input.value;

    let message = {'text': text};

    webSocket.send(JSON.stringify(message));
    input.value = '';
}
