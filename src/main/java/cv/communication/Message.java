package cv.communication;

import org.json.JSONException;
import org.json.JSONObject;

/** Represents a message to be sent to and from the
 *  Client. **/
public class Message {
    private int code;
    private JSONObject data;

    /**
     * Construct a new message to be sent to the Client, or wrap the message
     * sent from the Client
     * @param code The code representing the message type. Must larger than 0
     * @param data The data within the message ("the message itself"). Can be null
     */
    public Message(int code, JSONObject data) throws IllegalArgumentException {
        if( code < 1 )
            throw new IllegalArgumentException("Message code must be positive");
        this.code = code;

        // Just to ensure that the data property always exists
        if(data == null)
            this.data = new JSONObject();
        else
            this.data = data;
    }

    // For construction of object from JSON
    private Message(){}

    public int getCode() {
        return code;
    }

    public JSONObject getData() {
        return data;
    }


    /**
     * Constructs a Message object from the data of a JSON object.
     * @param jsonObject The JSON object to convert from which must conform to the format: {'code': [int], 'data' : { [data] }}
     * @return A new Message object containing the data within the JSON object
     * @throws JSONException If the message does not conform to the given format
     */
    public static Message fromJSON(JSONObject jsonObject) throws JSONException {
        // Check we don't too many properties
        for(Object name : jsonObject.names())
            if( !name.equals("code") && !name.equals("data") )
                throw new JSONException("Unknown property '" + name + "' in JSON object");

        // Check if code is valid
        int code = jsonObject.getInt("code");
        if( code < 1 )
            throw new JSONException("Property 'code' must be larger than 0");

        // Check that data exsists
        JSONObject data = jsonObject.getJSONObject("data");
        return new Message(code, data);
    }

    public JSONObject toJSON() {
        return new JSONObject(this);
    }

    @Override
    public String toString() {
        return "Message{ " +
                "code=" + code +
                ", data=" + data +
                " }";
    }

}
