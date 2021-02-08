let stompClient = null;
let contextPath = $('#userCreate_js').attr('contextPath')

const setConnected = (connected) => {
    $("#send").prop("disabled", !connected);
}

const connect = () => {
    stompClient = Stomp.over(new SockJS(contextPath + 'user-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        stompClient.subscribe('/user/queue/admin/user/create', (message) => showSuccessMessageMessage(message));
        stompClient.subscribe('/user/queue/errors', (message) => showErrorMessage(message))
    });
}

const showSuccessMessageMessage = (message) => {
    $("#errors").empty();
    let user = JSON.parse(message.body).user
    $("#messages").html("Создан пользователь " + JSON.stringify(user));
}

const showErrorMessage = (message) => {
    console.error(message);
    $("#messages").empty();

    const errorsHtml = $.map(JSON.parse(message.body).messages,
            (msg, index) => "<li>" + msg +  "</li>"
    ).join('');

    $("#errors").html(errorsHtml);
}

const submitForm = () => {
    let form = $('form').serializeArray().reduce(function(obj, item) {
        if (item.value.trim() !== '') {
            obj[item.name] = item.value;
        }
        return obj;
    }, {});

    stompClient.send("/app/admin/user/create", {}, JSON.stringify(form));
}

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#submit").click(submitForm);
    connect();
});