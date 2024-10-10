package managers;

import Data.CanvasData;
import Data.Dot;
import Data.HtmlDocument;
import Data.ScriptData;
import Interfaces.Sendable;
import Senders.CanvasSender;
import Senders.HtmlSender;
import Senders.JsonSender;
import Senders.ScriptSender;
import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.util.logging.Logger;

public class ResponseSender {

    private final FunctionCalc functionCalc;

    final Logger logger = LoggerConfig.getLogger(this.getClass().getName());

    private final RequestHandler requestHandler;

    Sendable sender;


    public ResponseSender(FunctionCalc functionCalc, RequestHandler requestHandler) {
        this.functionCalc = functionCalc;
        this.requestHandler = requestHandler;

    }


    public void sendResponse() throws IOException {


        var fcgiInterface = new FCGIInterface();

        logger.info("Waiting for requests...");
        while (fcgiInterface.FCGIaccept() >= 0) {
            var request = requestHandler.readRequest();
            sender = (Dot.class.isInstance(request)) ? new JsonSender() :
                    CanvasData.class.isInstance(request) ? new CanvasSender() :
                            HtmlDocument.class.isInstance(request) ? new HtmlSender() :
                                    ScriptData.class.isInstance(request) ? new ScriptSender() :
                                            new CanvasSender();
            logger.info(sender.getClass().getName());
            sender.send(request);
//            try {
//                //var status = functionCalc.isInTheSpot(dot);
//                var content = """
//                        {
//
//                        "status": %s,
//                        "time": %s,
//                        "x": %d,
//                        "y": %f,
//                        "r": %d
//
//                        }
//                        """;
//                var end = System.currentTimeMillis();
//                content = content.formatted(true, String.format("%.4f", (double) (end - start) / 1000), 1, 2, 3);
//                var httpResponse = """
//                        HTTP/1.1 200 OK
//                        Content-Type: application/json
//                        Content-Length: %d
//
//                        %s
//                        """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
//
//                logger.warning("status: %s".formatted(true));
//                System.out.println(httpResponse);
//
//
//            } catch (Exception e) {
//                var content = """
//                        {
//
//                        error: %s
//
//                        }
//                        """;
//                content = content.formatted(e.getMessage());
//                var httpResponse = """
//                        HTTP/1.1 400 Bad Request
//                        Content-Type: application/json
//                        Content-Length: %d
//
//                        %s
//                        """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
//
//                System.out.println(httpResponse);
//
//
//            }
//
//
//        }
        }
    }

}