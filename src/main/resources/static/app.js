$(function () {
    var room = "0";
    var stompClient = null;

    var connect = function(room) {
        var socket = new SockJS('/socket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/chat/' + room, function (message) {
                console.log(message);
                obj = JSON.parse(message.body);
                showMessage(obj.name + ": " + obj.text);
            });
        });
    };

    var showMessage = function(message) {
        $("#messages").append("<tr><td>" + message + "</td></tr>");
    };

    $( "#room-form" ).submit(function(e) {
        e.preventDefault();

        room = $("#room").val();
        showMessage("--- connect to room: " + room);
        connect(room);
    });

    $( "#message-form" ).submit(function(e) {
        e.preventDefault();

        var name = $("#name").val();
        var text = $("#message").val();
        stompClient.send("/app/chat/" + room, {}, JSON.stringify({'name': name, 'text': text}));
    });
});