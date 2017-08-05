package openvpn.integrationtests.linux;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import openvpn.integrationtests.process.SimpleProcess;
import openvpn.integrationtests.process.SimpleProcessBuilder;

public class LinuxRemoteTemporaryDirectoryFactory implements TemporaryDirectoryFactory {

	private SimpleProcessBuilder remoteProcessBuilder;

	public LinuxRemoteTemporaryDirectoryFactory(SimpleProcessBuilder remoteProcessBuilder) {
		this.remoteProcessBuilder = remoteProcessBuilder;
	}

	@Override
	public TemporaryDirectory create() {
		return new LinuxRemoteTemporaryDirectory(remoteProcessBuilder,
				remoteProcessBuilder.extendCommand("mktemp", "-d")
						.start()
						.waitForSuccess()
						.getOutput()
						.trim());
	}

	private static class LinuxRemoteTemporaryDirectory implements TemporaryDirectory {

		private SimpleProcessBuilder remoteProcessBuilder;
		private String directoryPath;

		public LinuxRemoteTemporaryDirectory(SimpleProcessBuilder remoteProcessBuilder, String directoryPath) {
			this.remoteProcessBuilder = remoteProcessBuilder;
			this.directoryPath = directoryPath;
		}

		@Override
		public void close() {
			remoteProcessBuilder.extendCommand("rm", "-rf", directoryPath)
					.start()
					.waitForSuccess();
		}

		@Override
		public Path resolve(String subPath) {
			return Paths.get(directoryPath, subPath);
		}

		@Override
		public void write(Path targetPath, String content) {
			SimpleProcess process = remoteProcessBuilder.extendCommand("cat > " + targetPath)
					.start();
			try (OutputStream standardInput = process.standardInput()) {
				standardInput.write(content.getBytes("UTF-8"));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			process.waitForSuccess();
		}
	}
}
