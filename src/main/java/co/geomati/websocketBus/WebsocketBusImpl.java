package co.geomati.websocketBus;

import com.google.gson.JsonElement;

class WebsocketBusImpl implements WebsocketBus {

	public void addListener(String eventName, Callback callback) {
		WebsocketBusHandler.addListener(eventName, callback);
	}

	public boolean removeListener(String eventName, Callback callback) {
		return WebsocketBusHandler.removeListener(eventName, callback);
	}

	@Override
	public void broadcast(String eventName, JsonElement payload) {
		WebsocketBusHandler.broadcast(eventName, payload);
	}

}
