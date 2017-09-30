import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPEchoServer {
	private static final int ECHOMAX = 255;

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int servPort = Integer.parseInt(args[0]);

		DatagramSocket socket = new DatagramSocket(servPort, InetAddress.getLoopbackAddress());
		DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

		System.out.println("Running on: " + socket.getLocalAddress());

		while (true) {
			socket.receive(packet);
			System.out.println("Handling client at " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());
			socket.send(packet);
			packet.setLength(ECHOMAX);
		}
	}
}