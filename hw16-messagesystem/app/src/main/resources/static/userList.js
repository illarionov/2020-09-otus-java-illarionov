let stompClient = null;
let contextPath = $('#userList_js').attr('contextPath')

const setConnected = (connected) => {
    $("#refreshUserList").prop("disabled", !connected);
}

const connect = () => {
    stompClient = Stomp.over(new SockJS(contextPath + 'user-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/admin/users', (message) => setUserList(JSON.parse(message.body)));
        stompClient.subscribe('/user/queue/errors', (message) => showErrorMessage(message))
        stompClient.subscribe('/topic/admin/user/create', (message) => addUserRow(JSON.parse(message.body)))
        refreshUserList();
    });
}

const showErrorMessage = (message) => {
    $("div.error").text("error: " + message.body);
    console.error(message);
}

const setUserList = (userList) => {
    console.log("setUserList()");
    const usersHtml = userList.map(mapUserToTableRow).join('')
    $('#userList tbody').html(usersHtml)
}

const addUserRow = (user) => {
    console.log("addUserRow()");
    const usersHtml = mapUserToTableRow(user);
    $('#userList tbody').append(usersHtml);
}

const mapUserToTableRow = function(user) {
    return '<tr>' +
            "<td>" + user.id + "</a></td>\n" +
            "<td>" + user.login + "</td>\n" +
            "<td>" + user.password + "</td>\n" +
            "<td>" + user.role + "</td>\n" +
            "<td nowrap>" + user.name + "</td>\n" +
            "<td nowrap>" + (user.age ?? 'Не указан') + "</td>\n" +
            "<td nowrap>" + (user.address?.street || '') + "</td>\n" +
            "<td>" + $.map(user.phones, function (phone, i) { return phone.number}).join("<br/>") + "</td>\n" +
            "</tr>";
}

const refreshUserList = () => {
    stompClient.send("/app/admin/users"/*, (message) => setUserList(JSON.parse(message.body)) */);
}

$(function () {
    connect();
    $("#refreshUserList").click(refreshUserList);
});
