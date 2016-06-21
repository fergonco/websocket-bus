package co.geomati.websocketBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ServerEndpoint(value = "/websocket-bus-endpoint")
public class WebsocketBusHandler {
	private static Logger logger = Logger.getLogger("websocket handler");

	private static final Set<WebsocketBusHandler> handlers = new HashSet<WebsocketBusHandler>();
	private Session session;

	private static HashMap<String, ArrayList<Callback>> listeners = new HashMap<>();

	@OnOpen
	public synchronized void start(Session session) {
		this.session = session;
		handlers.add(this);
	}

	@OnClose
	public synchronized void end() {
		handlers.remove(this);
	}

	@OnMessage
	public void incoming(String message) {
		try {
			JsonObject jsonMessage = (JsonObject) new JsonParser()
					.parse(message);
			String type = jsonMessage.get("type").getAsString();
			logger.fine("Event received: " + type);
			ArrayList<Callback> eventListeners = listeners.get(type);
			if (eventListeners != null) {
				for (Callback callback : eventListeners) {
					try {
						callback.messageReceived(new CallerImpl(session),
								WebsocketBus.INSTANCE,
								jsonMessage.get("payload"));
					} catch (RuntimeException e) {
						logger.log(Level.SEVERE,
								"runtime exception in callback", e);
					}
				}
			}
		} catch (CallbackException e) {
			JsonObject jsonError = new JsonObject();
			jsonError.addProperty("error", true);
			jsonError.addProperty("payload", e.getMessage());
			try {
				session.getBasicRemote().sendText(new Gson().toJson(jsonError));
			} catch (IOException e1) {
				logger.log(Level.SEVERE, "Cannot send error message", e1);
			}
		}
	}

	public static void addListener(String eventName, Callback callback) {
		ArrayList<Callback> eventListeners = listeners.get(eventName);
		eventListeners = eventListeners != null ? eventListeners
				: new ArrayList<Callback>();
		eventListeners.add(callback);
		listeners.put(eventName, eventListeners);
	}

	public static boolean removeListener(String eventName, Callback callback) {
		ArrayList<Callback> eventListeners = listeners.get(eventName);
		return eventListeners.remove(callback);
	}

	public static void broadcast(String eventName, JsonElement payload) {
		logger.fine("Broadcasting");
		JsonObject message = buildMessage(eventName, payload);
		for (WebsocketBusHandler handler : handlers) {
			try {
				synchronized (handler) {
					handler.session.getBasicRemote().sendText(
							message.toString());
				}
			} catch (IOException e) {
				handlers.remove(handler);
				try {
					handler.session.close();
				} catch (IOException e1) {
					// Ignore
				}
			}
		}
	}

	private static JsonObject buildMessage(String eventName, JsonElement payload) {
		JsonObject message = new JsonObject();
		message.addProperty("type", eventName);
		message.add("payload", payload);
		return message;
	}

	private class CallerImpl implements Caller {

		private Session session;

		public CallerImpl(Session session) {
			this.session = session;
		}

		@Override
		public void send(String eventName, JsonElement payload)
				throws IOException {
			JsonObject message = buildMessage(eventName, payload);
			this.session.getBasicRemote().sendText(message.toString());
		}

		@Override
		public Session getWebsocketSession() {
			return session;
		}

	}

}
