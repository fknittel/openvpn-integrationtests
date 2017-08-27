package openvpn.integrationtests;

import java.nio.file.Path;

import openvpn.integrationtests.process.SimpleProcessBuilder;

public class OpenvpnKeyBuilder {

	private SimpleProcessBuilder processBuilder;
	private String openvpnExecutablePath;
	private TemporaryDirectoryFactory temporaryDirectoryFactory;

	public OpenvpnKeyBuilder(TemporaryDirectoryFactory temporaryDirectoryFactory,
			SimpleProcessBuilder processBuilder, String openvpnExecutablePath) {
		this.temporaryDirectoryFactory = temporaryDirectoryFactory;
		this.processBuilder = processBuilder;
		this.openvpnExecutablePath = openvpnExecutablePath;
	}

	public OpenvpnKey build() {
		TemporaryDirectory temporaryDirectory = temporaryDirectoryFactory.create();
		Path keyPath = temporaryDirectory.resolve("static.key");
		processBuilder.extendCommand(openvpnExecutablePath, "--genkey", "--secret", keyPath.toString())
				.start()
				.waitForSuccess();
		return new OpenvpnKey(temporaryDirectory, keyPath);
	}
}
