package openvpn.integrationtests.process;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleProcessBuilder {
	private List<String> command;
	private Map<String, String> environment;

	SimpleProcessBuilder() {
		this.command = new ArrayList<>();
		this.environment = new HashMap<>();
	}

	SimpleProcessBuilder(SimpleProcessBuilder base) {
		this.command = new ArrayList<>(base.command);
		this.environment = new HashMap<>(base.environment);
	}

	public SimpleProcessBuilder extendCommand(String... command) {
		SimpleProcessBuilder newBuilder = new SimpleProcessBuilder(this);
		newBuilder.command.addAll(asList(command));
		return newBuilder;
	}

	public SimpleProcess start() {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.environment().putAll(environment);
		processBuilder.redirectErrorStream(true);
		try {
			return new SimpleProcess(this, processBuilder.start());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public SimpleProcessBuilder addEnvironment(String key, String value) {
		environment.put(key, value);
		return this;
	}

	@Override
	public String toString() {
		return command.stream().collect(joining(" "));
	}
}
