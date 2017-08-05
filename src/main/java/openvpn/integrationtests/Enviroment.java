package openvpn.integrationtests;

import openvpn.integrationtests.linux.TemporaryDirectoryFactory;

public interface Enviroment {

	NetworkNamespaceFactory createNetworkNamespaceFactory();

	VirtualEthernetDevicePairFactory createVirtualEthernetDevicePairFactory();

	PingFactory createPingFactory();

	OpenvpnProcessBuilder createOpenvpnBuilder(NetworkNamespace namespaceServer);

	OpenvpnKeyBuilder createOpenvpnKeyBuilder();

	TemporaryDirectoryFactory createTemporaryDirectoryFactory();
}
