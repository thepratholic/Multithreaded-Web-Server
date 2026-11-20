import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Model 3: THREAD POOL
// A fixed number of worker threads are created once and reused for all clients.
// If all workers are busy, new clients wait in a queue.
public class Server {
    static final int PORT = 8010;
    static final int POOL_SIZE = 10; // change this and see what happens to the speed

    // Same work as the other two servers.
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
        ExecutorService threadPool = Executors.newFixedThreadPool(POOL_SIZE);
        ServerSocket serverSocket = new ServerSocket(PORT, 100);
        System.out.println("Thread pool server (" + POOL_SIZE + " threads) listening on port " + PORT);

        try {
            while (true) {
                Socket client = serverSocket.accept();

                // Give the client to a free worker thread
                threadPool.execute(() -> handleClient(client));
            }
        } finally {
            threadPool.shutdown();
        }
    }
}
