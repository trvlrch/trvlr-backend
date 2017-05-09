$(function () {
    var room = "0";
    var stompClient = null;

    // Initialize Firebase
    var config = {
        apiKey: "AIzaSyDsvs8gSMuNzwpLDG-CNn6FD8aZ_0c4Jds",
        authDomain: "trvlr-312df.firebaseapp.com",
        databaseURL: "https://trvlr-312df.firebaseio.com",
        projectId: "trvlr-312df",
        storageBucket: "trvlr-312df.appspot.com",
        messagingSenderId: "12962208485"
    };

    firebase.initializeApp(config);

    var provider = new firebase.auth.GoogleAuthProvider();

    var connect = function(room) {
        firebase.auth().signInWithPopup(provider).then(function(result) {
            // var token = result.credential.accessToken;
            var user = result.user;
            console.log(user);
            firebase.auth().currentUser.getToken(/* forceRefresh */ true).then(function(idToken) {
                // Send token to your backend via HTTPS
                // ...
                console.log(idToken);
                createSocketConnecton(idToken, room);

            }).catch(function(error) {
                // Handle error
            });


        }).catch(function(error) {
            console.error(error);
        });
    };

    var createSocketConnecton = function(token, room) {
        var socket = new SockJS('/socket');
        stompClient = Stomp.over(socket);
        stompClient.connect({'token': token}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/chat/' + room, function (message) {
                console.log(message);
                obj = JSON.parse(message.body);
                showMessage(obj.author + ": " + obj.text);
            });
        }, function(e) {
            console.log(e);
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

        var text = $("#message").val();
        stompClient.send("/app/chat/" + room, {}, JSON.stringify({'text': text}));
    });
});