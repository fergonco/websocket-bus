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
   console.log(uri);

   var socket = new WebSocket(uri);
   socket.onmessage = function(event) {
      var message = JSON.parse(event.data);
      bus.send(message.type, [ message.payload ]);
   }

   return {
      send : function(name, msg) {
         socket.send(JSON.stringify({
            type : name,
            payload : msg
         }));
      },
      listen : function(name, callback) {
         var eventListeners = listeners[name];
         eventListeners = eventListeners ? eventListeners : [];
         eventListeners.push(callback);
         listeners[name] = eventListeners;
      },

   }

});