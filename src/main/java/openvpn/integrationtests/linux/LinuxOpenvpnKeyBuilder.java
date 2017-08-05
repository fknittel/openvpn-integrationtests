package openvpn.integrationtests.linux;

import java.nio.file.Path;

import openvpn.integrationtests.OpenvpnKey;
import openvpn.integrationtests.OpenvpnKeyBuilder;
import openvpn.integrationtests.process.SimpleProcessBuilder;

public class LinuxOpenvpnKeyBuilder implements OpenvpnKeyBuilder {

	private SimpleProcessBuilder processBuilder;
	private String openvpnExecutablePath;
	private TemporaryDirectoryFactory temporaryDirectoryFactory;

	public LinuxOpenvpnKeyBuilder(TemporaryDirectoryFactory temporaryDirectoryFactory,
			SimpleProcessBuilder processBuilder, String openvpnExecutablePath) {
		this.temporaryDirectoryFactory = temporaryDirectoryFactory;
		this.processBuilder = processBuilder;
		this.openvpnExecutablePath = openvpnExecutablePath;
	}

	@Override
	public OpenvpnKey build() {
		TemporaryDirectory temporaryDirectory = temporaryDirectoryFactory.create();
		Path keyPath = temporaryDirectory.resolve("static.key");
		processBuilder.extendCommand(openvpnExecutablePath, "--genkey", "--secret", keyPath.toString())
				.start()
				.waitForSuccess();
		return new LinuxOpenvpnKey(temporaryDirectory, keyPath);
	}

	public static class LinuxOpenvpnKey implements OpenvpnKey {

		private TemporaryDirectory temporaryDirectory;
		private Path keyPath;

		public LinuxOpenvpnKey(TemporaryDirectory temporaryDirectory, Path keyPath) {
			this.temporaryDirectory = temporaryDirectory;
			this.keyPath = keyPath;
		}

		@Override
		public Path getPath() {
			return keyPath;
		}

		@Override
		public void close() {
			temporaryDirectory.close();
		}
	}
}
