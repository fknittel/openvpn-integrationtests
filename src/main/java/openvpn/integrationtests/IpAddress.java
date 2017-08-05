package openvpn.integrationtests;

public class IpAddress {

	private String ipAddress;

	public IpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString() {
		return ipAddress;
	}
}
