package openvpn.integrationtests.linux;

import openvpn.integrationtests.IpNetwork;
import openvpn.integrationtests.NameGenerator;
import openvpn.integrationtests.NetworkNamespace;
import openvpn.integrationtests.VirtualEthernetDevice;
import openvpn.integrationtests.VirtualEthernetDevicePair;
import openvpn.integrationtests.VirtualEthernetDevicePairFactory;
import openvpn.integrationtests.process.SimpleProcessBuilder;

class LinuxVirtualEthernetDevicePairFactory implements VirtualEthernetDevicePairFactory {

	private static class LinuxVirtualEthernetDevice implements VirtualEthernetDevice {
		private SimpleProcessBuilder processBuilder;
		private String linkName;

		public LinuxVirtualEthernetDevice(SimpleProcessBuilder processBuilder, String linkName) {
			this.processBuilder = processBuilder;
			this.linkName = linkName;
		}

		@Override
		public VirtualEthernetDevice moveTo(NetworkNamespace namespace) {
			return moveTo((LinuxNetworkNamespace) namespace);
		}

		private VirtualEthernetDevice moveTo(LinuxNetworkNamespace linuxNamespace) {
			processBuilder.extendCommand("ip", "link", "set", linkName, "netns", linuxNamespace.getNamespaceName()) //
					.start() //
					.waitForSuccess();
			return new LinuxVirtualEthernetDevice(linuxNamespace.buildProcess(), linkName);
		}

		@Override
		public VirtualEthernetDevice up() {
			processBuilder.extendCommand("ip", "link", "set", linkName, "up") //
					.start() //
					.waitForSuccess();
			return this;
		}

		@Override
		public VirtualEthernetDevice addAddress(IpNetwork ipNetwork) {
			processBuilder.extendCommand("ip", "addr", "add", ipNetwork.toString(), "dev", linkName) //
					.start() //
					.waitForSuccess();
			return this;
		}

	}

	private SimpleProcessBuilder sudoProcessBuilder;
	private NameGenerator nameGenerator;

	public LinuxVirtualEthernetDevicePairFactory(NameGenerator nameGenerator, SimpleProcessBuilder sudoProcessBuilder) {
		this.nameGenerator = nameGenerator;
		this.sudoProcessBuilder = sudoProcessBuilder;
	}

	@Override
	public VirtualEthernetDevicePair create() {
		String leftLinkName = nameGenerator.generateWithPrefix("veth");
		String rightLinkName = nameGenerator.generateWithPrefix("veth");

		sudoProcessBuilder
				.extendCommand("ip", "link", "add", leftLinkName, "type", "veth", "peer", "name", rightLinkName) //
				.start() //
				.waitForSuccess();

		return new VirtualEthernetDevicePair() {
			@Override
			public VirtualEthernetDevice getRightDevice() {
				return new LinuxVirtualEthernetDevice(sudoProcessBuilder, leftLinkName);
			}

			@Override
			public VirtualEthernetDevice getLeftDevice() {
				return new LinuxVirtualEthernetDevice(sudoProcessBuilder, rightLinkName);
			}
		};
	}
}
