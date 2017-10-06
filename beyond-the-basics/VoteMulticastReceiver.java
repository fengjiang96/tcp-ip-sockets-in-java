import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

public class VoteMulticastReceiver {
  public static void main(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("Parameter(s): <Multicast Addr> <Port>");
    }

    InetAddress address = InetAddress.getByName(args[0]);

    if (!address.isMulticastAddress()) {
      throw new IllegalArgumentException("Not a multicast address");
    }

    int port = Integer.parseInt(args[1]);

    MulticastSocket sock = new MulticastSocket(port);
    sock.joinGroup(address);

    VoteMsgTextCoder coder = new VoteMsgTextCoder();

    DatagramPacket packet = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH], VoteMsgTextCoder.MAX_WIRE_LENGTH);
    sock.receive(packet);

    VoteMsg vote = coder.fromWire(Arrays.copyOfRange(packet.getData), 0, packet.getLength());

    System.out.println("Received Text-Encoded Request (" + packet.getLength() + " bytes): ");
    System.out.println(vote);
    
    sock.close();
  }
}