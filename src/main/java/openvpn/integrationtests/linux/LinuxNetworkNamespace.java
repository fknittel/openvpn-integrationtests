package openvpn.integrationtests.linux;

import openvpn.integrationtests.NetworkNamespace;
import openvpn.integrationtests.process.SimpleProcessBuilder;

class LinuxNetworkNamespace implements NetworkNamespace {

	private final SimpleProcessBuilder netnsProcessBuilder;
	private final String namespaceName;

	public LinuxNetworkNamespace(SimpleProcessBuilder sudoProcessBuilder, String namespaceName) {
		this.namespaceName = namespaceName;
		this.netnsProcessBuilder = sudoProcessBuilder.extendCommand("ip", "netns");
	}

	public void open() {
		netnsProcessBuilder.extendCommand("add", namespaceName) //
				.start() //
				.waitForSuccess();
	}

	@Override
	public void close() {
		netnsProcessBuilder.extendCommand("del", namespaceName) //
				.start() //
				.waitForSuccess();
	}

	@Override
	public SimpleProcessBuilder buildProcess() {
		return netnsProcessBuilder.extendCommand("exec", namespaceName);
	}

	String getNamespaceName() {
		return namespaceName;
	}
}
