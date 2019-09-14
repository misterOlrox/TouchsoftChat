<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Chat</title>
  <style>
    <%@include file="style.css" %>
  </style>
</head>
<body>
<div id="chatControls">
  <input id="message" placeholder="Type your message">
  <button id="send">Send</button>
</div>
<ul id="userlist"> <!-- Built by JS --> </ul>
<div id="chat">    <!-- Built by JS --> </div>
<script><%@include file="websocketDemo.js" %></script>
</body>
</html>
