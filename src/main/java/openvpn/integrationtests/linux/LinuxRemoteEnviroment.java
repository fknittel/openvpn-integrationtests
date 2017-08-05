package openvpn.integrationtests.linux;

import openvpn.integrationtests.Enviroment;
import openvpn.integrationtests.NameGenerator;
import openvpn.integrationtests.NetworkNamespace;
import openvpn.integrationtests.NetworkNamespaceFactory;
import openvpn.integrationtests.OpenvpnKeyBuilder;
import openvpn.integrationtests.OpenvpnProcessBuilder;
import openvpn.integrationtests.PingFactory;
import openvpn.integrationtests.VirtualEthernetDevicePairFactory;
import openvpn.integrationtests.process.SimpleProcessBuilder;
import openvpn.integrationtests.process.SimpleProcessBuilderFactory;

public class LinuxRemoteEnviroment implements Enviroment {
	private final NameGenerator nameGenerator;
	private final String openvpnExecutablePath;
	private SimpleProcessBuilderFactory simpleProcessBuilderFactory;
	private SimpleProcessBuilder remoteProcessBuilder;

	public LinuxRemoteEnviroment(NameGenerator nameGenerator, String sshDestination, String openvpnExecutablePath) {
		this.nameGenerator = nameGenerator;
		this.openvpnExecutablePath = openvpnExecutablePath;
		this.simpleProcessBuilderFactory = new SimpleProcessBuilderFactory();
		this.remoteProcessBuilder = simpleProcessBuilderFactory.create() //
				.extendCommand("ssh", sshDestination);
	}

	@Override
	public NetworkNamespaceFactory createNetworkNamespaceFactory() {
		return new LinuxNamespaceFactory(nameGenerator, remoteProcessBuilder);
	}

	@Override
	public VirtualEthernetDevicePairFactory createVirtualEthernetDevicePairFactory() {
		return new LinuxVirtualEthernetDevicePairFactory(nameGenerator, remoteProcessBuilder);
	}

	@Override
	public PingFactory createPingFactory() {
		return new LinuxPingFactory();
	}

	@Override
	public TemporaryDirectoryFactory createTemporaryDirectoryFactory() {
		return new LinuxRemoteTemporaryDirectoryFactory(remoteProcessBuilder);
	}

	@Override
	public OpenvpnProcessBuilder createOpenvpnBuilder(NetworkNamespace namespace) {
		return new OpenvpnProcessBuilder(createTemporaryDirectoryFactory(), namespace, openvpnExecutablePath);
	}

	@Override
	public OpenvpnKeyBuilder createOpenvpnKeyBuilder() {
		return new LinuxOpenvpnKeyBuilder(createTemporaryDirectoryFactory(), remoteProcessBuilder,
				openvpnExecutablePath);
	}
}
