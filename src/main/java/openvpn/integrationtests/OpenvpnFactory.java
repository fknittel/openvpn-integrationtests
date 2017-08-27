package openvpn.integrationtests;

import openvpn.integrationtests.process.SimpleProcessBuilder;

public class OpenvpnFactory {
	private final TemporaryDirectoryFactory temporaryDirectoryFactory;
	private final SimpleProcessBuilder processBuilder;
	private final String openvpnExecutablePath;

	public OpenvpnFactory(TemporaryDirectoryFactory temporaryDirectoryFactory, SimpleProcessBuilder processBuilder,
			String openvpnExecutablePath) {
		this.temporaryDirectoryFactory = temporaryDirectoryFactory;
		this.processBuilder = processBuilder;
		this.openvpnExecutablePath = openvpnExecutablePath;
	}

	public OpenvpnProcessBuilder createOpenvpnBuilder(NetworkNamespace namespace) {
		return new OpenvpnProcessBuilder(temporaryDirectoryFactory, namespace, openvpnExecutablePath);
	}

	public OpenvpnKeyBuilder createOpenvpnKeyBuilder() {
		return new OpenvpnKeyBuilder(temporaryDirectoryFactory, processBuilder,
				openvpnExecutablePath);
	}
}
