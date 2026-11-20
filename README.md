# Simple Multithreaded Web Server (Java)

Three small web servers that do the same job in three different ways, so you can *see* how threading changes speed. Plain Java, no libraries, no build tool.

| Folder | How it handles clients |
|---|---|
| `SingleThreaded/` | one client at a time, the rest wait in line |
| `Multithreaded/` | a new thread for every client |
| `ThreadPool/` | a fixed pool of 10 reusable worker threads |

Each server listens on port **8010**, reads the HTTP request, "works" for 100 ms (pretending to be a database call) and replies with a short text message. Because it speaks real HTTP, you can also open `http://localhost:8010` in a browser.

## Run

Open a terminal inside one of the folders and run (JDK 11 or newer):

```
javac Server.java
java Server
```

Stop it with `Ctrl+C` before starting a different one (they all use port 8010).

## Test the speed

With a server running, open a second terminal in the project root:

```
javac LoadTest.java
java LoadTest 50
```

`LoadTest` starts 50 clients at the same time and prints how long all of them took.

## Results

50 clients, each request takes 100 ms of "work" (measured on a 2-core machine; yours will differ a little):

| Server | Total time for 50 clients | Why |
|---|---:|---|
| Single-threaded | ~5,070 ms | 50 x 100 ms, one after another |
| Thread pool (10 threads) | ~550 ms | 50 requests / 10 workers x 100 ms |
| Thread per connection | ~155 ms | all 50 run at the same time |

## Things to try

- Change `POOL_SIZE` in `ThreadPool/Server.java` to 1, 5, 50 and run `LoadTest` again.
- Change `Thread.sleep(100)` to `0`. With no waiting, the difference between the three servers shrinks. Why?
- Run `LoadTest 5000` against the thread-per-connection server. What happens to memory? This is why thread pools exist.

## What I learned

- Why a single-threaded server cannot serve clients in parallel.
- Creating a thread for every request is simple, but threads are expensive and unlimited.
- A thread pool reuses a fixed number of threads and queues the extra work.
