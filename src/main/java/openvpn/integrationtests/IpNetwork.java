package openvpn.integrationtests;

public class IpNetwork {

	private String ipNetwork;

	public IpNetwork(String ipNetwork) {
		this.ipNetwork = ipNetwork;
	}

	@Override
	public String toString() {
		return ipNetwork;
	}
}
