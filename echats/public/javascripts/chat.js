const host = 'http://localhost:8080';

const wsUrl = 'ws://127.0.0.1:8080/wschat';

let webSocket;

function fetchHistory() {
    let url = `${host}/messages`;
    fetch(url, {
        method: 'GET',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt') }
    }).then(response => response.json())
        .then(messages => {
            messages.forEach(prependMessage);
        });
}

function connect() {
    document.cookie = `X-Authorization=${localStorage.getItem('jwt')};path=/`;
    webSocket = new WebSocket(wsUrl);

    webSocket.onmessage = function receiveMessage(response) {
        console.log(response);
        debugger;
        let data = response['data'];
        let json = JSON.parse(data);
        prependMessage(json);
    };

    webSocket.onerror = function errorShow() {
        alert('Ошибка авторизации')
    }
}

function prependMessage(message) {
    const messages = document.getElementById('messages');
    let messageView = document.createElement('div');
    messageView.classList.add('card')
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

    let message = {
        "from": localStorage.getItem('jwt'),
        "text": text
    };

    webSocket.send(JSON.stringify(message));
}

function waitForMessage() {
    let url = `${host}/messages/notification`;
    fetch(url, {
        method: 'GET',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt') }
    }).then(response => response.json())
        .then(message => {
            prependMessage(message);
            waitForMessage();
        });
}
