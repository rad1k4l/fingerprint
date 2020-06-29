package request;

import org.bson.Document;

public class BaseRequest {

    public static Document cmd(String command) {
        return new Document()
                .append("cmd", command);
    }
}