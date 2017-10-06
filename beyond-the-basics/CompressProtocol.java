import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CompressProtocol implements Runnable {
  public static final int BUFSIZE = 1024;
  private Socket clntSock;
  private Logger logger;

  public CompressProtocol(Socket clntSock, Logger logger) {
    this.clntSock = clntSock;
    this.logger = logger;
  }

  public static void handleCompressClient(Socket clntSock, Logger logger) {
    try {
      InputStream in = clntSock.getInputStream();
      GZIPOutputStream out = new GZIPOutputStream(clntSock.getOutputStream());

      byte[] buffer = new byte[BUFSIZE];
      int bytesRead;

      while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }
      out.finish();

      logger.info("Client " + clntSock.getRemoteSocketAddress() + "finished");
    } catch (IOException ex) {
      logger.log(Level.WARNING, "Exception in echo protocol", ex);
    }

    try {
      clntSock.close();
    } catch (IOException e) {
      logger.info("Exception = " + e.getMessage());
    }
  }

  public void run() {
    handleCompressClient(this.clntSock, this.logger);
  }
}