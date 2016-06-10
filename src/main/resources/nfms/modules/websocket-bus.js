define([ "message-bus" ], function(bus) {

   var listeners = {};

   var loc = window.location;
   var uri;
   if (loc.protocol === "https:") {
      uri = "wss:";
   } else {
      uri = "ws:";
   }
   uri += "//" + loc.host;
   uri += window.location.pathname + "/websocket-bus-endpoint";

   var socket = null;

   function connect() {
      socket = new WebSocket(uri);
      socket.onmessage = function(event) {
         var message = JSON.parse(event.data);
         bus.send(message.type, [ message.payload ]);
      }

      socket.onclose = function(event) {
         bus.send("websocket-reconnect");
         
         setInterval(connect, 5000);
      }
      socket.onopen= function(event) {
         bus.send("websocket-connected");
      }
   }
   connect();

   function send(name, msg) {
      if (socket.readyState == 0) {
         setTimeout(function() {
            send(name, msg);
         }, 100);
      } else {
         socket.send(JSON.stringify({
            type : name,
            payload : msg
         }));
      }
   }

   function listen(name, callback) {
      var eventListeners = listeners[name];
      eventListeners = eventListeners ? eventListeners : [];
      eventListeners.push(callback);
      listeners[name] = eventListeners;
   }

   return {
      send : send,
      listen : listen,

   }

});