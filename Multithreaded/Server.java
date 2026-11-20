import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Model 2: THREAD PER CONNECTION
// Every new client gets its own brand-new thread, so many clients are served at the same time.
public class Server {
    static final int PORT = 8010;

    // Same work as the single-threaded server.
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
        System.out.println("Multithreaded server listening on port " + PORT);

        while (true) {
            Socket client = serverSocket.accept();

            // Create and start a new thread for each client
            Thread thread = new Thread(() -> handleClient(client));
            thread.start();
        }
    }
}
