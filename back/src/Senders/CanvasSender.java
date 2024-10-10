package Senders;

import Data.CanvasData;
import Data.HtmlDocument;
import Data.RequestData;
import Interfaces.Sendable;

import java.nio.charset.StandardCharsets;

public class CanvasSender implements Sendable {
    @Override
    public void send(RequestData requestData) {
        CanvasData container = (CanvasData) requestData;

        String content = container.getCanvas();

        var end = System.currentTimeMillis();
        var httpResponse = """
                        HTTP/1.1 200 OK
                        Content-Type: application/javascript
                        Content-Length: %d
                                                
                        %s
                        """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);

        // logger.warning("status: %s".formatted(true));
        System.out.println(httpResponse);
    }
}
