package openvpn.integrationtests.linux;

import openvpn.integrationtests.NameGenerator;
import openvpn.integrationtests.NetworkNamespace;
import openvpn.integrationtests.NetworkNamespaceFactory;
import openvpn.integrationtests.process.SimpleProcessBuilder;

class LinuxNamespaceFactory implements NetworkNamespaceFactory {

	private SimpleProcessBuilder sudoProcessBuilder;
	private NameGenerator nameGenerator;

	public LinuxNamespaceFactory(NameGenerator nameGenerator, SimpleProcessBuilder sudoProcessBuilder) {
		this.nameGenerator = nameGenerator;
		this.sudoProcessBuilder = sudoProcessBuilder;
	}

	@Override
	public NetworkNamespace create() {
		LinuxNetworkNamespace namespace = new LinuxNetworkNamespace(sudoProcessBuilder,
				nameGenerator.generateWithPrefix("netns"));
		namespace.open();
		return namespace;
	}
}
