package org.dcv;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class HttpWarServer {
    static Thread thread;

    static class ShutdownHandler implements HttpHandler {

        public void handle(final HttpExchange httpExchange) throws IOException {
            try (final InputStream is = httpExchange.getRequestBody()) {
                while (is.read() != -1) ;

                synchronized (thread) {
                    thread.notify();
                }
                final String response = format("HttpWarServer stopped%n");
                httpExchange.sendResponseHeaders(200, response.length());
                try (final OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    static class FileHandler implements HttpHandler {

        public void handle(final HttpExchange httpExchange) throws IOException {
            try (final InputStream is = httpExchange.getRequestBody()) {
                while (is.read() != -1) ;
                final File file = new File("target", "deployment-credential-vault-1.0.0-SNAPSHOT.war");
                httpExchange.sendResponseHeaders(200, file.length());
                final byte[] buffer = new byte[(int) file.length()];

                try (final InputStream inputStream = new FileInputStream(file)) {
                    inputStream.read(buffer);
                }

                try (final OutputStream os = httpExchange.getResponseBody()) {
                    os.write(buffer);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8161), 2);
        server.createContext("/stop", new ShutdownHandler());
        server.createContext("/dcv", new FileHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        thread = Thread.currentThread();

        synchronized (thread) {
            thread.wait();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        server.stop(0);
    }
}
