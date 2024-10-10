package managers;

import data.CanvasData;
import data.Dot;
import data.HtmlDocument;
import data.ScriptData;
import interfaces.Sendable;
import senders.CanvasSender;
import senders.HtmlSender;
import senders.JsonSender;
import senders.ScriptSender;
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

            sender.send(request);
        }
    }

}