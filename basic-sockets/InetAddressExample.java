import java.util.Enumeration;
import java.net.*;

public class InetAddressExample {
	public static void main(String[] args) throws UnknownHostException {
		String family = "(?)";

		System.out.println("Localhost: " + InetAddress.getLocalHost());
		System.out.println("Loopback: " + InetAddress.getLoopbackAddress());

		try {
			Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
			if (interfaceList == null) {
				System.out.println("--No interfaces found--");
			} else {
				while (interfaceList.hasMoreElements()) {
					NetworkInterface iface = interfaceList.nextElement();
					System.out.println("Interface " + iface.getName() + ":");
					Enumeration<InetAddress> addrList = iface.getInetAddresses();
					if (!addrList.hasMoreElements()) {
						System.out.println("\t(No addresses for this interface)");
					}
					while (addrList.hasMoreElements()) {
						InetAddress address = addrList.nextElement();
						if (address instanceof Inet4Address) {
							family = "(v4)";
						}
						if (address instanceof Inet6Address) {
							family = "(v6)";
						}
						System.out.print("\tAddress " + family);
						System.out.println(": " + address.getHostAddress());
					}
				}
			}

		} catch (SocketException e) {
			System.out.println("Error getting network interfaces: " + e.getMessage());
		}

		for (String host: args) {
			try {
				System.out.println(host + ":");
				InetAddress[] addressList = InetAddress.getAllByName(host);
				for (InetAddress address : addressList) {
					System.out.println("\t" + address.getHostName() + "/" + address.getHostAddress());
				}
			} catch (UnknownHostException e) {
				System.out.println("\tUnable to find address for " + host);
			}
		}
	}
}