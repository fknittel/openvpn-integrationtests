package openvpn.integrationtests.linux;

import openvpn.integrationtests.IpAddress;
import openvpn.integrationtests.NetworkNamespace;
import openvpn.integrationtests.Ping;
import openvpn.integrationtests.PingBuilder;
import openvpn.integrationtests.PingFactory;
import openvpn.integrationtests.PingResult;

class LinuxPingFactory implements PingFactory {

	private static class LinuxPing implements Ping {
		private NetworkNamespace namespace;
		private IpAddress destination;

		@Override
		public PingResult run() {
			return namespace.buildProcess().extendCommand("ping", "-4", "-c1", destination.toString()).start()
					.waitForResult() == 0 ? PingResult.SUCCESS : PingResult.FAILURE;
		}

		private static class Builder implements PingBuilder {
			private LinuxPing linuxPing = new LinuxPing();

			private Builder(NetworkNamespace namespace) {
				linuxPing.namespace = namespace;
			}

			@Override
			public PingBuilder withDestination(IpAddress destination) {
				linuxPing.destination = destination;
				return this;
			}

			@Override
			public Ping build() {
				return linuxPing;
			}
		}
	}

	@Override
	public PingBuilder createIn(NetworkNamespace namespace) {
		return new LinuxPing.Builder(namespace);
	}
}
