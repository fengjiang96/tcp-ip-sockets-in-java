/*
 * Usage
 * ```
 * java UDPEchoClientTimeout 127.0.0.1 "hello world" 8080
 * ```
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InterruptedIOException;

public class UDPEchoClientTimeout {
	private static final int TIMEOUT = 3000;
	private static final int MAXTRIES = 5;

	public static void main(String[] args) throws IOException {
		if ((args.length < 2) || (args.length > 3)) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");
		}
		InetAddress serverAddress = InetAddress.getByName(args[0]);
		byte[] bytesToSend = args[1].getBytes();

		int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

		DatagramSocket socket = new DatagramSocket();

		socket.setSoTimeout(TIMEOUT);

		DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);

		DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

		int tries = 0;

		boolean receivedResponse = false;

		do {
			socket.send(sendPacket);
			try {
				socket.receive(receivePacket);

				if (!receivePacket.getAddress().equals(serverAddress)) {
					throw new IOException("Received packet from an unknown source");
				}
				receivedResponse = true;
			} catch (InterruptedIOException e) {
				tries += 1;
				System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
			}
		} while ((!receivedResponse) && (tries < MAXTRIES));

		if (receivedResponse) {
			System.out.println("Received: " + new String(receivePacket.getData()));
		} else {
			System.out.println("No response -- giving up.");
		}
		socket.close();
	}
}