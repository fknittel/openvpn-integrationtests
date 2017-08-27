package openvpn.integrationtests;

import java.nio.file.Path;

public class OpenvpnKey implements AutoCloseable {

	private TemporaryDirectory temporaryDirectory;
	private Path keyPath;

	public OpenvpnKey(TemporaryDirectory temporaryDirectory, Path keyPath) {
		this.temporaryDirectory = temporaryDirectory;
		this.keyPath = keyPath;
	}

	public Path getPath() {
		return keyPath;
	}

	@Override
	public void close() {
		temporaryDirectory.close();
	}
}
