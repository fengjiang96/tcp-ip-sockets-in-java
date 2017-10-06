import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.logging.Logger;

public class TCPEchoServerThread {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new IllegalArgumentException("Parameter(s): <Port>");
    }

    int echoServPort = Integer.parseInt(args[0]);

    ServerSocket servSock = new ServerSocket(echoServPort, 0, InetAddress.getLoopbackAddress());
    
    Logger logger = Logger.getLogger("practical");
    logger.info("Running on: " + servSock.getInetAddress());

    while (true) {
      Socket clntSock = servSock.accept();

      Thread thread = new Thread(new EchoProtocol(clntSock, logger));
      thread.start();
      logger.info("Created and started Thread " + thread.getName());
    }
  }
}