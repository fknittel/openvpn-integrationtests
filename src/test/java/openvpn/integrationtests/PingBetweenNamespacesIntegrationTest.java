package openvpn.integrationtests;

import static openvpn.integrationtests.PingResult.SUCCESS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import openvpn.integrationtests.linux.LinuxRemoteEnviroment;

public class PingBetweenNamespacesIntegrationTest {

	@Test
	public void testPingBetweenNamespaces() {
		NameGenerator nameGenerator = new RandomNameGenerator();
		Enviroment environment = new LinuxRemoteEnviroment(nameGenerator, "root@docker-vm");
		NetworkNamespaceFactory networkNamespaceFactory = environment.createNetworkNamespaceFactory();
		VirtualEthernetDevicePairFactory virtualEthernetDevicePairFactory = environment
				.createVirtualEthernetDevicePairFactory();
		PingFactory pingFactory = environment.createPingFactory();

		try (NetworkNamespace namespace1 = networkNamespaceFactory.create();
				NetworkNamespace namespace2 = networkNamespaceFactory.create()) {
			VirtualEthernetDevicePair virtualEthernetDevicePair = virtualEthernetDevicePairFactory.create();

			virtualEthernetDevicePair.getLeftDevice() //
					.moveTo(namespace1) //
					.up() //
					.addAddress(new IpNetwork("10.0.0.1/24"));

			virtualEthernetDevicePair.getRightDevice() //
					.moveTo(namespace2) //
					.up() //
					.addAddress(new IpNetwork("10.0.0.2/24"));

			assertThat(pingFactory.createIn(namespace1).withDestination(new IpAddress("10.0.0.2")).build().run(),
					is(SUCCESS));

			assertThat(pingFactory.createIn(namespace2).withDestination(new IpAddress("10.0.0.1")).build().run(),
					is(SUCCESS));
		}
	}

}
