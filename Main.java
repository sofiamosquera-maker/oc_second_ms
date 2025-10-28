import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.createContext("/", new HelloHandler());
        server.setExecutor(null); // Usa el ejecutor por defecto
        server.start();
        System.out.println("Servidor iniciado en el puerto 3000");
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/health".equals(path) || "/readiness".equals(path) || "/startup".equals(path) ) {
                exchange.sendResponseHeaders(404, -1);
                exchange.close();
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "Hola mundo";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Método no permitido
            }
        }
    }
}
