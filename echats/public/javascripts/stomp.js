let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function connect() {
    // создаем объект SockJs
    let socket = new SockJS('http://localhost:8080/messages');
    // создаем stomp-клиент поверх sockjs
    stompClient = Stomp.over(socket);
    // при подключении
    stompClient.connect({jwt: localStorage.jwt}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // подписываемся на url
        stompClient.subscribe('/topic/chat', (stompResponse) => {
            showMessage(JSON.parse(stompResponse.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function send() {
    // отправляем на определенный url
    stompClient.send("/app/hello", {jwt: localStorage.jwt}, $("#name").val());
}

function showMessage(message) {
    let date = message.dateTime.date;
    let time = message.dateTime.time;
    let dateString = new Date(
        date.year, date.month, date.day,
        time.hour, time.minute, time.second
    ).toDateString();
    $("#messages").append(
        `<tr>
            <td>${message.author.username}</td>
            <td>${message.text}</td>
            <td>${dateString}</td>
        </tr>`
    );
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        send();
    });
});
