package openvpn.integrationtests.process;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.IOUtils;

public class SimpleProcess {
	private final Process process;
	private final SimpleProcessBuilder builder;
	private ProcessOutputReader outputReader;

	SimpleProcess(SimpleProcessBuilder builder, Process process) {
		this.builder = builder;
		this.process = process;
		this.outputReader = new ProcessOutputReader(process.getInputStream());
		this.outputReader.start();
	}

	public void waitForSuccess() {
		try {
			waitForCompletionWithinTimeout();
			if (process.exitValue() != 0) {
				throw new FailedProcessException(
						"process failed with exit value " + process.exitValue() + ":\n" + outputReader.getOutput(),
						builder);
			}
		} catch (InterruptedException e) {
			throw new FailedProcessException(outputReader.getOutput(), builder, e);
		}
	}

	private void waitForCompletionWithinTimeout() throws InterruptedException {
		if (!process.waitFor(10L, TimeUnit.SECONDS)) {
			process.destroyForcibly();
			process.waitFor();
			throw new FailedProcessException("process timed out:\n" + outputReader.getOutput(), builder);
		}
	}

	public int waitForResult() {
		try {
			waitForCompletionWithinTimeout();
			return process.exitValue();
		} catch (InterruptedException e) {
			throw new FailedProcessException(outputReader.getOutput(), builder, e);
		}
	}

	private static class ProcessOutputReader extends Thread {
		private final InputStream inputStream;
		private AtomicReference<String> output = new AtomicReference<String>("<no output data available yet>");

		public ProcessOutputReader(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public String getOutput() {
			try {
				join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return output.get();
		}

		@Override
		public void run() {
			try {
				output.set(IOUtils.toString(inputStream, Charset.defaultCharset()));
			} catch (IOException e) {
				output.set("<output generation failed due to exception: " + e.toString() + ">");
			}
		}
	}
}
