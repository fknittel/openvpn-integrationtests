package openvpn.integrationtests;

import openvpn.integrationtests.process.SimpleProcessBuilder;

public interface NetworkNamespace extends AutoCloseable {
	@Override
	void close();

	SimpleProcessBuilder buildProcess();
}
