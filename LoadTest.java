import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

// Fires many clients at the server at the same time and measures how long it takes.
//   javac LoadTest.java
//   java LoadTest 50        (50 clients; default is 50)
public class LoadTest {
    static final int PORT = 8010;
    static final AtomicInteger failed = new AtomicInteger();

    static void sendOneRequest() {
        try (Socket socket = new Socket("localhost", PORT)) {
            OutputStream out = socket.getOutputStream();
            out.write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
            out.flush();

            InputStream in = socket.getInputStream();
            in.readAllBytes(); // wait for the full response
        } catch (Exception ex) {
            failed.incrementAndGet();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int clients = args.length > 0 ? Integer.parseInt(args[0]) : 50;
        Thread[] threads = new Thread[clients];

        long start = System.currentTimeMillis();
        for (int i = 0; i < clients; i++) {
            threads[i] = new Thread(LoadTest::sendOneRequest);
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long ms = System.currentTimeMillis() - start;

        System.out.println(clients + " clients finished in " + ms + " ms"
                + "  (" + (clients * 1000L / Math.max(ms, 1)) + " requests/second)"
                + "  failed: " + failed.get());
    }
}
