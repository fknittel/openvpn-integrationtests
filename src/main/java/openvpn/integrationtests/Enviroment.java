package openvpn.integrationtests;

public interface Enviroment {

	NetworkNamespaceFactory createNetworkNamespaceFactory();

	VirtualEthernetDevicePairFactory createVirtualEthernetDevicePairFactory();

	PingFactory createPingFactory();
}
