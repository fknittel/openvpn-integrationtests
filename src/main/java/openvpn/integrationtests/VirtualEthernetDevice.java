package openvpn.integrationtests;

public interface VirtualEthernetDevice {

	VirtualEthernetDevice moveTo(NetworkNamespace namespace1);

	VirtualEthernetDevice addAddress(IpNetwork ipNetwork);

	VirtualEthernetDevice up();
}
