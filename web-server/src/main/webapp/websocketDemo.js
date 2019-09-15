//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://localhost:8080/web_server_war/chat");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";

    }

    var author = '<b> ' + 'You' + '</b>';

    var currentTime = new Date();
    var time = '<span class="timestamp">'
        + currentTime.getHours() + ':'
        + currentTime.getMinutes()
        + '</span>';

    var text = '<p>' + message + '</p>';

    var article = '<article>' + author + time + text + '</article>';

    id('chat').innerHTML += article;
    id('message').scrollIntoView();
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    var data = JSON.parse(msg.data);
    insertMessage("chat", data);
}

//Helper function for inserting HTML as the first child of an element
function insertMessage(targetId, data) {

    var author = '<b>' + data.author + '</b>';
    var time = '<span class="timestamp">' + data.time + '</span>';
    var text = '<p>' + data.text + '</p>';

    var article = '<article>' + author + time + text + '</article>';

    id('chat').innerHTML += article;
    id('message').scrollIntoView();
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
