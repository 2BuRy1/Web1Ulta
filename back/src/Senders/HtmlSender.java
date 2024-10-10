package Senders;

import Data.HtmlDocument;
import Data.RequestData;
import Interfaces.Sendable;

import java.nio.charset.StandardCharsets;

public class HtmlSender implements Sendable {
    @Override
    public void send(RequestData requestData) {
        HtmlDocument container = (HtmlDocument) requestData;

        String content = container.getHtml();

        var end = System.currentTimeMillis();
        var httpResponse = """
                        HTTP/1.1 200 OK
                        Content-Type: text/html
                        Content-Length: %d
                                                
                        %s
                        """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);

       // logger.warning("status: %s".formatted(true));
        System.out.println(httpResponse);

    }
}
