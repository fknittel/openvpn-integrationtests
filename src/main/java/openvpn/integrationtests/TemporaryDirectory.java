package openvpn.integrationtests;

import java.nio.file.Path;

public interface TemporaryDirectory extends AutoCloseable {
	@Override
	void close();

	Path resolve(String subPath);

	void write(Path configPath, String string);
}
