package co.geomati.websocketBus;

import com.google.gson.JsonElement;

public interface Callback {

	void messageReceived(JsonElement payload);

}
