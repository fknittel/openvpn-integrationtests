package openvpn.integrationtests;

public interface Enviroment {

	NetworkNamespaceFactory createNetworkNamespaceFactory();

	VirtualEthernetDevicePairFactory createVirtualEthernetDevicePairFactory();

	PingFactory createPingFactory();

	TemporaryDirectoryFactory createTemporaryDirectoryFactory();

	OpenvpnFactory createOpenvpnFactory(String executablePath);
}
