package openvpn.integrationtests;

public interface VirtualEthernetDevicePair {
	VirtualEthernetDevice getLeftDevice();

	VirtualEthernetDevice getRightDevice();
}
