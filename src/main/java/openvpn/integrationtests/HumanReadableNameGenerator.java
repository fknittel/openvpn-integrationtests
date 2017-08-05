package openvpn.integrationtests;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.text.RandomStringGenerator;
import org.kohsuke.randname.RandomNameGenerator;

public class HumanReadableNameGenerator implements NameGenerator {
	private RandomNameGenerator randomNameGenerator;
	private RandomStringGenerator randomStringGenerator;

	public HumanReadableNameGenerator() {
		this(new RandomNameGenerator(), new SecureRandom());
	}

	public HumanReadableNameGenerator(RandomNameGenerator randomNameGenerator, Random random) {
		this.randomNameGenerator = randomNameGenerator;
		this.randomStringGenerator = new RandomStringGenerator.Builder() //
				.usingRandom(random::nextInt) //
				.withinRange('0', 'z') //
				.filteredBy(LETTERS, DIGITS) //
				.build();
	}

	@Override
	public String generateWithPrefix(String prefix) {
		return prefix + '-' + randomStringGenerator.generate(5);
	}
}
