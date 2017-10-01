import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class VoteClientUDP {
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Parameter(s): <Destination> <Port> <Candidate#>");
		}

		InetAddress destAddr = InetAddress.getByName(args[0]);
		int destPort = Integer.parseInt(args[1]);
		int candidate = Integer.parseInt(args[2]);

		DatagramSocket sock = new DatagramSocket();
		sock.connect(destAddr, destPort);

		// create a voting message
		VoteMsg vote = new VoteMsg(false, false, candidate, 0);

		// encoding strategy
		VoteMsgCoder coder = new VoteMsgTextCoder();

		// send request
		byte[] encodedVote = coder.toWire(vote);
		System.out.println("Sending Text-Encoded Request (" + encodedVote.length + " bytes):");
		System.out.println(vote);
		DatagramPacket message = new DatagramPacket(encodedVote, encodedVote.length);
		sock.send(message);

		// Receive response
		message = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH], VoteMsgTextCoder.MAX_WIRE_LENGTH);
		sock.receive(message);
		encodedVote = Arrays.copyOfRange(message.getData(), 0, message.getLength());

		System.out.println("Received Text-Encoded Response (" + encodedVote.length + " bytes):");
		vote = coder.fromWire(encodedVote);
		System.out.println(vote);
	}
}