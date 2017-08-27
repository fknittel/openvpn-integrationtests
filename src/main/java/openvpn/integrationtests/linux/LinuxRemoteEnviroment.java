package openvpn.integrationtests.linux;

import openvpn.integrationtests.Enviroment;
import openvpn.integrationtests.NameGenerator;
import openvpn.integrationtests.NetworkNamespaceFactory;
import openvpn.integrationtests.OpenvpnFactory;
import openvpn.integrationtests.PingFactory;
import openvpn.integrationtests.TemporaryDirectoryFactory;
import openvpn.integrationtests.VirtualEthernetDevicePairFactory;
import openvpn.integrationtests.process.SimpleProcessBuilder;
import openvpn.integrationtests.process.SimpleProcessBuilderFactory;

public class LinuxRemoteEnviroment implements Enviroment {
	private final NameGenerator nameGenerator;
	private SimpleProcessBuilderFactory simpleProcessBuilderFactory;
	private SimpleProcessBuilder remoteProcessBuilder;

	public LinuxRemoteEnviroment(NameGenerator nameGenerator, String sshDestination) {
		this.nameGenerator = nameGenerator;
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
	public OpenvpnFactory createOpenvpnFactory(String executablePath) {
		return new OpenvpnFactory(createTemporaryDirectoryFactory(), remoteProcessBuilder, executablePath);
	}
}
