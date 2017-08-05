package openvpn.integrationtests;

public interface PingBuilder {

	Ping build();

	PingBuilder withDestination(IpAddress ipAddress);

}
