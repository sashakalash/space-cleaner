import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    private static final int CONNECTION_PORT = 23334;

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        try {
            final ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress("localhost", CONNECTION_PORT));
            while (true) {
                try (SocketChannel socketChannel = server.accept()) {
                    System.out.printf("Server started at %d port\n", CONNECTION_PORT);
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        if (bytesCount == -1) break;
                        final String msg = new String(inputBuffer.array(), 0, bytesCount,
                                StandardCharsets.UTF_8);
                        inputBuffer.clear();
                        String result = removeSpaces(msg);
                        socketChannel.write(ByteBuffer.wrap(("Ваш результат: " +
                                result).getBytes(StandardCharsets.UTF_8)));
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String removeSpaces(String msg) {
        return msg.replaceAll("\\s", "");
    }

}