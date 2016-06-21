package co.geomati.websocketBus;

import com.google.gson.JsonElement;

public interface Callback {

	/**
	 * Handlers a message received in the bus
	 * 
	 * @param caller
	 *            Origin of the message
	 * @param bus
	 *            interface to interact with the bus
	 * @param payload
	 *            Content of the message
	 * @throws CallbackException
	 *             If there is an error dealing with this message. The message
	 *             of the exception will be sent on the bus as an error message
	 * @throws InternalLogException
	 *             If there is an error dealing with this message but it should
	 *             not be sent to the other endpoint, only log the problem
	 *             internally and do nothing else
	 */
	void messageReceived(Caller caller, WebsocketBus bus, JsonElement payload)
			throws CallbackException, InternalLogException;

}
