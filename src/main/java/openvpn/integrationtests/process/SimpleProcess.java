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
			System.out.println("Timeout of process");
			process.destroyForcibly();
			System.out.println("Process ordered to die");
			process.waitFor();
			System.out.println("Process dead");
			String output = outputReader.getOutput();
			System.out.println("Output: " + output);
			throw new FailedProcessException("process timed out:\n" + output, builder);
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
				System.out.println("Waiting for output thread to end...");
				join();
				System.out.println("... ended");
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return output.get();
		}

		@Override
		public void run() {
			try {
				System.out.println("Starting output thread");
				output.set(IOUtils.toString(inputStream, Charset.defaultCharset()));
				System.out.println("Ending output thread");
			} catch (IOException e) {
				output.set("<output generation failed due to exception: " + e.toString() + ">");
			}
		}
	}
}
