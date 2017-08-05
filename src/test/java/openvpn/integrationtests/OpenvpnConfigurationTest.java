package openvpn.integrationtests;

import static openvpn.integrationtests.OpenvpnDevice.TUN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;

import org.junit.Test;

import openvpn.integrationtests.OpenvpnConfiguration.Builder;

public class OpenvpnConfigurationTest {
	@Test
	public void tunDevice() {
		OpenvpnConfiguration.Builder configBuilder = builder()
				.withDev(TUN);

		assertThat(asString(configBuilder), is("dev tun\n"));
	}

	private Builder builder() {
		return new OpenvpnConfiguration.Builder();
	}

	@Test
	public void ifconfig() {
		OpenvpnConfiguration.Builder configBuilder = builder()
				.withIfconfig(new IfConfig(new IpAddress("10.8.0.1"), new IpAddress("10.8.0.2")));

		assertThat(asString(configBuilder), is("ifconfig 10.8.0.1 10.8.0.2\n"));
	}

	@Test
	public void secret() {
		OpenvpnConfiguration.Builder configBuilder = builder()
				.withSecret(Paths.get("/secret/key.file"));

		assertThat(asString(configBuilder), is("secret /secret/key.file\n"));
	}

	@Test
	public void remote() {
		OpenvpnConfiguration.Builder configBuilder = builder()
				.withRemote(new Remote(new IpAddress("10.1.2.3")));

		assertThat(asString(configBuilder), is("remote 10.1.2.3\n"));
	}

	private String asString(OpenvpnConfiguration.Builder configBuilder) {
		String configString = configBuilder
				.build()
				.toString();
		return configString;
	}
}
