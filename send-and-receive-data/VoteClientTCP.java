import java.io.OutputStream;
import java.net.Socket;

public class VoteClientTCP {
	public static final int CANDIDATEID = 888;

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
		}

		String destAddr = args[0];
		int destPort = Integer.parseInt(args[1]);

		Socket sock = new Socket(destAddr, destPort);
		OutputStream out = sock.getOutputStream();

		VoteMsgCoder coder = new VoteMsgBinCoder();
		Framer framer = new LengthFramer(sock.getInputStream());

		VoteMsg msg = new VoteMsg(false, true, CANDIDATEID, 0);
		byte[] encodeMsg = coder.toWire(msg);

		System.out.println("Sending Inquiry (" + encodeMsg.length + " bytes):");
		System.out.println(msg);

		framer.frameMsg(encodeMsg, out);

		// Now send a vote
		msg.setInquiry(false);
		encodeMsg = coder.toWire(msg);
		System.out.println("Sending Vote (" + encodeMsg.length + " bytes):");
		framer.frameMsg(encodeMsg, out);

		// Receive inquiry response
		encodeMsg = framer.nextMsg();
		msg = coder.fromWire(encodeMsg);
		System.out.println("Received Response (" + encodeMsg.length + " bytes):");
		System.out.println(msg);

		// Receive vote reponse
		msg = coder.fromWire(framer.nextMsg());
		System.out.println("Received Response (" + encodeMsg.length + " bytes):");
		System.out.println(msg);

		sock.close();
	}
}