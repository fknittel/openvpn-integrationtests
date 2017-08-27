package openvpn.integrationtests;

import java.nio.file.Path;

import openvpn.integrationtests.process.SimpleProcess;

public class OpenvpnProcess implements AutoCloseable {

	private TemporaryDirectory temporaryDirectory;
	private NetworkNamespace namespace;
	private String pathToExecutable;
	private Path configPath;
	private SimpleProcess process;

	public OpenvpnProcess(TemporaryDirectory temporaryDirectory, NetworkNamespace namespace, String pathToExecutable,
			Path configPath) {
		this.temporaryDirectory = temporaryDirectory;
		this.namespace = namespace;
		this.pathToExecutable = pathToExecutable;
		this.configPath = configPath;
	}

	public void run() {
		process = namespace.buildProcess()
				.extendCommand(pathToExecutable, "--config", configPath.toString())
				.start();
		try {
			// FIXME track openvpn log messages to know when it is ready / has failed
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		process.assertAlive();
	}

	@Override
	public void close() {
		if (process != null) {
			process.kill();
		}
		temporaryDirectory.close();
	}
}
