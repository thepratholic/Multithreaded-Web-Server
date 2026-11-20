import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Model 1: SINGLE THREADED
// The server handles ONE client at a time. Everyone else waits in line.
public class Server {
    static final int PORT = 8010;

    // Reads the HTTP request, "works" for 100 ms (like a database call),
    // then sends a response back.
    static void handleClient(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // read request line + headers until the empty line
            }

            Thread.sleep(100); // pretend we are doing some slow work

            String body = "Hello from server! Handled by: " + Thread.currentThread().getName() + "\n";
            String response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "Content-Length: " + body.length() + "\r\n"
                    + "Connection: close\r\n"
                    + "\r\n"
                    + body;
            out.write(response.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT, 100);
        System.out.println("Single-threaded server listening on port " + PORT);

        while (true) {
            Socket client = serverSocket.accept();
            handleClient(client); // we are busy here until this client is done
        }
    }
}
