/*
 * Usage
 * ```
 * java TCPEchoClient 127.0.0.1 "echo this" 8080
 * ```
 */
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TCPEchoCLient {
	public static void main(String[] args) throws IOException {
		if ((args.length < 2) || (args.length > 3)) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Word [<Port>]>");
		}
		String server = args[0];
		byte[] data = args[1].getBytes();

		int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

		Socket socket = new Socket(server, servPort);
		System.out.println("Connected to server...sending echo string");

		InputStream in =socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		out.write(data);

		int totalBytesRcvd = 0;
		int bytesRcvd;
		while (totalBytesRcvd < data.length) {
			if ((bytesRcvd = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd)) == - 1) {
				throw new SocketException("Connection closed prematurely");
			}
			totalBytesRcvd += bytesRcvd;
		}

		System.out.println("Received: " + new String(data));
		socket.close();
	}
}