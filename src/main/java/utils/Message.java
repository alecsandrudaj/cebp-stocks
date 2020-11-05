package utils;

import java.util.UUID;

public class Message {
    private int sharesNumber, pricePerAction;
    private UUID clientId;
    private MessageType type;

    public Message(int sharesNumber, int pricePerAction, UUID clientId, MessageType type) {
        this.clientId = clientId;
        this.pricePerAction = pricePerAction;
        this.sharesNumber = sharesNumber;
        this.type = type;
    }

    public Message(String recievedData) {

    }

    public enum MessageType {
        ORDER, HISTORY;
    }
}
