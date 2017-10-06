import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class TCPEchoServerExcecutor {
  
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new IllegalArgumentException("Parameter(s): <Port>");
    }

    int echoServPort = Integer.parseInt(args[0]);

    ServerSocket servSock = new ServerSocket(echoServPort, 0, InetAddress.getLoopbackAddress());

    Logger logger = Logger.getLogger("practical");
    logger.info("Running on: " + servSock.getInetAddress());

    Executor service = Executors.newCachedThreadPool();

    while (true) {
      Socket clntSock = servSock.accept();
      service.execute(new EchoProtocol(clntSock, logger));
    }
  }
}