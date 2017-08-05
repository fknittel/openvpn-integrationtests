package openvpn.integrationtests;

import java.nio.file.Path;

public class OpenvpnConfiguration {
	private ConfigWriter<OpenvpnDevice> deviceWriter = emptyWriter();
	private ConfigWriter<IfConfig> ifconfigWriter = emptyWriter();
	private ConfigWriter<Remote> remoteWriter = emptyWriter();
	private ConfigWriter<Path> staticKeyWriter = emptyWriter();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(deviceWriter.write());
		builder.append(ifconfigWriter.write());
		builder.append(staticKeyWriter.write());
		builder.append(remoteWriter.write());

		return builder.toString();
	}

	private interface ConfigWriter<T> {
		String write();
	}

	private static <T> ConfigWriter<T> emptyWriter() {
		return () -> "";
	}

	public static class Builder {
		private OpenvpnConfiguration configuration = new OpenvpnConfiguration();

		public OpenvpnConfiguration build() {
			return configuration;
		}

		public Builder withDev(OpenvpnDevice device) {
			configuration.deviceWriter = () -> "dev " + device + "\n";
			return this;
		}

		public Builder withIfconfig(IfConfig ifconfig) {
			configuration.ifconfigWriter = () -> "ifconfig " + ifconfig.getLocalIpAddress() + " "
					+ ifconfig.getRemoteIpAddress() + "\n";
			return this;
		}

		public Builder withRemote(Remote remote) {
			configuration.remoteWriter = () -> "remote " + remote.getIpAddress() + "\n";
			return this;
		}

		public Builder withSecret(Path secretKey) {
			configuration.staticKeyWriter = () -> "secret " + secretKey + "\n";
			return this;
		}
	}
}
