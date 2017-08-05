package openvpn.integrationtests;

import static openvpn.integrationtests.PingResult.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import openvpn.integrationtests.linux.LinuxRemoteEnviroment;

public class PingBetweenOpenvpnTunEndpointsIntegrationTest {
	private NameGenerator nameGenerator;
	private Enviroment environment;
	private NetworkNamespaceFactory networkNamespaceFactory;
	private VirtualEthernetDevicePairFactory virtualEthernetDevicePairFactory;
	private PingFactory pingFactory;

	@Before
	public void setupEnvironment() {
		nameGenerator = new HumanReadableNameGenerator();
		environment = new LinuxRemoteEnviroment(nameGenerator, "root@docker-vm", "/usr/sbin/openvpn");
		networkNamespaceFactory = environment.createNetworkNamespaceFactory();
		virtualEthernetDevicePairFactory = environment
				.createVirtualEthernetDevicePairFactory();
		pingFactory = environment.createPingFactory();
	}

	@Test
	public void testPingBetweenNamespaces() {
		try (OpenvpnKey staticKey = environment.createOpenvpnKeyBuilder().build();
				NetworkNamespace namespaceServer = networkNamespaceFactory.create();
				NetworkNamespace namespaceClient = networkNamespaceFactory.create()) {
			VirtualEthernetDevicePair virtualEthernetDevicePair = virtualEthernetDevicePairFactory.create();

			virtualEthernetDevicePair.getLeftDevice()
					.moveTo(namespaceServer)
					.up()
					.addAddress(new IpNetwork("10.0.0.1/24"));

			virtualEthernetDevicePair.getRightDevice()
					.moveTo(namespaceClient)
					.up()
					.addAddress(new IpNetwork("10.0.0.2/24"));

			IpAddress ipAddressServer = new IpAddress("10.0.0.1");
			IpAddress tunnelIpAddressServer = new IpAddress("10.8.0.1");
			IpAddress tunnelIpAddressClient = new IpAddress("10.8.0.2");

			IfConfig serverIfconfig = new IfConfig(tunnelIpAddressServer, tunnelIpAddressClient);
			try (OpenvpnProcess openvpnServer = environment.createOpenvpnBuilder(namespaceServer)
					.withConfig(new OpenvpnConfiguration.Builder()
							.withDev(OpenvpnDevice.TUN)
							.withIfconfig(serverIfconfig)
							.withSecret(staticKey.getPath())
							.build())
					.build()) {
				openvpnServer.run();

				try (OpenvpnProcess openvpnClient = environment.createOpenvpnBuilder(namespaceClient)
						.withConfig(new OpenvpnConfiguration.Builder()
								.withRemote(new Remote(ipAddressServer))
								.withDev(OpenvpnDevice.TUN)
								.withIfconfig(serverIfconfig.reversed())
								.withSecret(staticKey.getPath())
								.build())
						.build()) {
					openvpnClient.run();

					assertThat(
							pingFactory.createIn(namespaceServer).withDestination(tunnelIpAddressClient).build().run(),
							is(SUCCESS));

					assertThat(
							pingFactory.createIn(namespaceClient).withDestination(tunnelIpAddressServer).build().run(),
							is(SUCCESS));
				}
			}
		}
	}
}
