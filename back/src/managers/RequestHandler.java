package managers;

import Data.*;
import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class RequestHandler {
    final Logger logger = LoggerConfig.getLogger(this.getClass().getName());


    public RequestData readRequest() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes =
                FCGIInterface.request.inStream.read(buffer.array(), 0,
                        contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();
        //DOCUMENT_URI: /api/
        var request = new String(requestBodyRaw, StandardCharsets.UTF_8);

        String uri = FCGIInterface.request.params.getProperty("DOCUMENT_URI");



        if (uri.equals("/index.html") || uri.equals("/")) {
            String html = Files.readString(Paths.get("/opt/user/myapp/frontend/index.html"));
            return new HtmlDocument(html);

        }
        else if(uri.equals("/dist/canvas.js") || uri.equals("/canvas.js")){
            String canvas = Files.readString(Paths.get("/opt/user/myapp/frontend/dist/canvas.js"));
            return new CanvasData(canvas);
        }

        else if (uri.equals("/dist/script.js") || uri.equals("/script.js")) {
            String script = Files.readString(Paths.get("/opt/user/myapp/frontend/dist/script.js"));
            logger.info(script);
            return new ScriptData(script);

        }


        else if (uri.equals("/api/")) {
            return JsonParser.parseJson(request);
        }
        String html = Files.readString(Paths.get("/opt/user/myapp/frontend/index.html"));
        return new HtmlDocument(html);
    }
}



