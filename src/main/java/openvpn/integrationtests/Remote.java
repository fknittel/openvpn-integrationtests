package openvpn.integrationtests;

public class Remote {

	private IpAddress ipAddress;

	public Remote(IpAddress ipAddressServer) {
		this.ipAddress = ipAddressServer;
	}

	public IpAddress getIpAddress() {
		return ipAddress;
	}
}