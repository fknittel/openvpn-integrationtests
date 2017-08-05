package openvpn.integrationtests;

import java.nio.file.Path;

public interface OpenvpnKey extends AutoCloseable {
	@Override
	void close();

	Path getPath();
}
