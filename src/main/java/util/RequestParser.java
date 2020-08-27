package util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestParser {
    private String path;
    private String method;

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    //            parse method to handle client request
    public void parse(InputStream inputStream) {
        try {BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuilder sbRequest = new StringBuilder();

//            "\r\n" request ends with one empty line
            while(!(line = reader.readLine()).isBlank()) {
                sbRequest.append(line).append("\r\n");
            }


//            parsing the request
            String request = sbRequest.toString();
            String[] requestsLines = request.split("\r\n");
            String[] requestLine = requestsLines[0].split(" ");

            this.method = requestLine[0];
            this.path = requestLine[1];
            String version = requestLine[2];
            String host = requestsLines[1].split(" ")[1];

//          starting loop from 2 Because first line (index 0) is GET / HTTP/1.1
//          second line is host. The headers start with the third line of the request
            List<String> headers = new ArrayList<>(Arrays.asList(requestsLines).subList(2, requestsLines.length));

            String accessLog = String.format("Client: %s, method: %s, Path: %s, version %s, host %s",
                    inputStream.toString(), method, path, version, host);
            System.out.println(accessLog);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



