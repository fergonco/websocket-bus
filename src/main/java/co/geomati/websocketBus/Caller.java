package co.geomati.websocketBus;

import java.io.IOException;

import javax.websocket.Session;

import com.google.gson.JsonElement;

public interface Caller {

	void send(String eventName, JsonElement payload) throws IOException;

	Session getWebsocketSession();

	public abstract void sendError(String message);

}
