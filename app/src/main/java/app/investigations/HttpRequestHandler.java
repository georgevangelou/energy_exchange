package app.investigations;

import java.net.Socket;

public class HttpRequestHandler implements Runnable {
    protected final static String CRLF = "\r\n";
    private Socket socket;

    public HttpRequestHandler(Socket socket) throws Exception {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /*
     * Gets a request from another node.
     * Sends the file to the node if available.
     */
    private void processRequest() throws Exception {
        /*DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();

        // Extract the filename from the request line.
        // In Get request, the second token is the fie name
        String[] tokens = requestLine.split(" ");
        String fileName = tokens[1];

        // Prepend a "." so that file request is within the current directory.
        fileName = "." + fileName;

        // Open the requested file.
        FileInputStream fis = null;
        boolean fileExists = true;

        try
        {
            fis = new FileInputStream(fileName);
        }
        catch (FileNotFoundException e)
        {
            fileExists = false;
        }

        // construct the response Message
        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (fileExists)
        {
            statusLine = "HTTP/1.1 200 OK" + CRLF;
            contentTypeLine = "Content-Type: " + contentType(fileName) + CRLF;
        }
        else
        {
            statusLine = "HTTP/1.1 404 Not Found" + CRLF;
            contentTypeLine = "Content-Type: text/html" + CRLF;
            entityBody = "<HTML><HEAD><TITLE>404 Not Found</TITLE></HEAD><BODY>Error 404: Page Not Found</BODY></HTML>";
        }

        // Send the status line.
        os.writeBytes(statusLine);

        // Send the content type line.
        os.writeBytes(contentTypeLine);

        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);

        // Send the entity body.
        if (fileExists) {
            sendBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(entityBody);
        }
        // Close streams and socket.
        os.close();
        br.close();
        socket.close();

    }

    private static void sendBytes(FileInputStream fis, OutputStream os)
            throws Exception
            {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }*/
    }

    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        if (fileName.endsWith(".ram") || fileName.endsWith(".ra")) {
            return "audio/x-pn-realaudio";
        }
        return "application/octet-stream";
    }

    public Socket getSocket() {
        return socket;
    }
}
