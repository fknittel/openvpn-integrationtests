package openvpn.integrationtests.linux;

import openvpn.integrationtests.Enviroment;
import openvpn.integrationtests.NameGenerator;
import openvpn.integrationtests.NetworkNamespaceFactory;
import openvpn.integrationtests.PingFactory;
import openvpn.integrationtests.VirtualEthernetDevicePairFactory;
import openvpn.integrationtests.process.SimpleProcessBuilder;
import openvpn.integrationtests.process.SimpleProcessBuilderFactory;

public class LinuxSudoEnviroment implements Enviroment {
	private NameGenerator nameGenerator;
	private SimpleProcessBuilderFactory simpleProcessBuilderFactory;
	private SimpleProcessBuilder sudoProcessBuilder;

	public LinuxSudoEnviroment(NameGenerator nameGenerator, String sshDestination) {
		this.nameGenerator = nameGenerator;
		this.simpleProcessBuilderFactory = new SimpleProcessBuilderFactory();
		this.sudoProcessBuilder = simpleProcessBuilderFactory.create() //
				.extendCommand("ssh", sshDestination);
	}

	@Override
	public NetworkNamespaceFactory createNetworkNamespaceFactory() {
		return new LinuxNamespaceFactory(nameGenerator, sudoProcessBuilder);
	}

	@Override
	public VirtualEthernetDevicePairFactory createVirtualEthernetDevicePairFactory() {
		return new LinuxVirtualEthernetDevicePairFactory(nameGenerator, sudoProcessBuilder);
	}

	@Override
	public PingFactory createPingFactory() {
		return new LinuxPingFactory();
	}
}
