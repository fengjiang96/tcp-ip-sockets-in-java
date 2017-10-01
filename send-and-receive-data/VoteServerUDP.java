import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class VoteServerUDP {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int port = Integer.parseInt(args[0]);

		DatagramSocket sock = new DatagramSocket(port);

		byte[] inBuffer = new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH];

		VoteMsgCoder coder = new VoteMsgTextCoder();
		VoteService service = new VoteService();

		while (true) {
			DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
			sock.receive(packet);
			byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
			System.out.println("Handling request from " + packet.getSocketAddress() + " (" + encodedMsg.length + " bytes)");

			try {
				VoteMsg msg = coder.fromWire(encodedMsg);
				msg = service.handleRequest(msg);
				packet.setData(coder.toWire(msg));
				System.out.println("Sending response (" + packet.getLength() + " byte):");
				System.out.println(msg);
				sock.send(packet);
			} catch (IOException e) {
				System.out.println("Parse error in message: " + e.getMessage());
			}
		}
	}
}