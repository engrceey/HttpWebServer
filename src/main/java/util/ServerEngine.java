package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

public class ServerEngine extends Thread {

    private final Socket socket;

    public ServerEngine(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String filePath = "src/main/resources/";

//            InputStream, OutputStream for Client/Server communication session
        try (InputStream clientInput = socket.getInputStream();
             OutputStream clientOutput = socket.getOutputStream()) {

            RequestParser requestParser = new RequestParser();
            requestParser.parse(clientInput);

            String method = requestParser.getMethod();
            String path = requestParser.getPath();

            if(!"GET".equals(method)) {
                return;
            }

//            Switching path between the html file and json file
            filePath += "/".equals(path) ? "index.html" :
                    "/json".equals(path) ? "program.json"  : path;

            File file = new File(filePath);

//          \r\n means new line
//          variables for sending response to output stream
            String CRLF = "\r\n";
            String statusLine = (!file.exists() || file.isDirectory() ? "HTTP/1.1 404 Not Found" : "HTTP/1.1 200 OK") + CRLF;
            String serverDetails = "Server: Java HTTPServer" + CRLF;
            String contentType = "content-type: " + this.contentType.apply(file) + CRLF;
            byte[] content = contentGenerator.apply(file);
            String contentLength = "Content-Length: " + content.length + CRLF + CRLF;
            String footer = CRLF + CRLF;

//            Sending response to the client output stream
            clientOutput.write(statusLine.getBytes());
            clientOutput.write(serverDetails.getBytes());
            clientOutput.write(contentType.getBytes());
            clientOutput.write(contentLength.getBytes());
            clientOutput.write(content);
            clientOutput.write(footer.getBytes());
            clientOutput.flush();

        }catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    //    ContentType -to tell the browser what kind of content we are sending
    protected Function<File, String> contentType = (path) -> {
        String contentType;
        if (!path.exists() || path.isDirectory()) {
            contentType = "text/html";
        } else {
            try {
                contentType = Files.probeContentType(Paths.get(path.getPath()));
            } catch (IOException e) {
                contentType = "text/html";
                e.printStackTrace();
            }
        }
        return contentType;
    };


    public Function<File, byte[]> contentGenerator =  (path) -> {
        byte[] contents = new byte[] {};
        if (!path.exists() || path.isDirectory()) {
            contents = "<b> Not found......".getBytes();
        } else {
            try {
                contents = Files.readAllBytes(Paths.get(path.getPath()));
            }catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
        return contents;
    } ;
}

