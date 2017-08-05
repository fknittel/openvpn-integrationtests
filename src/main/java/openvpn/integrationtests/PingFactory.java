package openvpn.integrationtests;

public interface PingFactory {
	PingBuilder createIn(NetworkNamespace namespace);
}
