package openvpn.integrationtests;

public enum OpenvpnDevice {
	TUN, TAP;

	@Override
	public String toString() {

		return name().toLowerCase();
	}

}
