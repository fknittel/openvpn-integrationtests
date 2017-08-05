package openvpn.integrationtests;

import java.nio.file.Path;

import openvpn.integrationtests.linux.TemporaryDirectory;
import openvpn.integrationtests.linux.TemporaryDirectoryFactory;

public class OpenvpnProcessBuilder {
	private String pathToExecutable;
	private OpenvpnConfiguration configuration;
	private NetworkNamespace namespace;
	private TemporaryDirectoryFactory temporaryDirectoryFactory;

	public OpenvpnProcessBuilder(TemporaryDirectoryFactory temporaryDirectoryFactory, NetworkNamespace namespace,
			String pathToExecutable) {
		this.temporaryDirectoryFactory = temporaryDirectoryFactory;
		this.namespace = namespace;
		this.pathToExecutable = pathToExecutable;
	}

	public OpenvpnProcessBuilder withConfig(OpenvpnConfiguration configuration) {
		this.configuration = configuration;
		return this;
	}

	public OpenvpnProcess build() {
		TemporaryDirectory temporaryDirectory = temporaryDirectoryFactory.create();

		Path configPath = temporaryDirectory.resolve("openvpn.conf");
		temporaryDirectory.write(configPath, configuration.toString());

		return new OpenvpnProcess(temporaryDirectory, namespace, pathToExecutable, configPath);
	}
}
