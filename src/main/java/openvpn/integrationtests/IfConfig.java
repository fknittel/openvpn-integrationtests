package openvpn.integrationtests;

public class IfConfig {
	private IpAddress localIpAddress;
	private IpAddress remoteIpAddress;

	public IfConfig(IpAddress localIpAddress, IpAddress remoteIpAddress) {
		this.localIpAddress = localIpAddress;
		this.remoteIpAddress = remoteIpAddress;
	}

	public IfConfig reversed() {
		return new IfConfig(remoteIpAddress, localIpAddress);
	}

	public IpAddress getLocalIpAddress() {
		return localIpAddress;
	}

	public IpAddress getRemoteIpAddress() {
		return remoteIpAddress;
	}
}