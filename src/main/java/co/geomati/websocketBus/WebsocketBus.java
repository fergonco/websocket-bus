package co.geomati.websocketBus;

import com.google.gson.JsonElement;

public interface WebsocketBus {

	public static WebsocketBus INSTANCE = new WebsocketBusImpl();

	void addListener(String eventName, Callback callback);

	boolean removeListener(String eventName, Callback callback);

	void broadcast(String eventName, JsonElement payload);
}
