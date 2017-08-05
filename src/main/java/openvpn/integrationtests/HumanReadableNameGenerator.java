package openvpn.integrationtests;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.text.RandomStringGenerator;

public class HumanReadableNameGenerator implements NameGenerator {
	private RandomStringGenerator randomStringGenerator;

	public HumanReadableNameGenerator() {
		this(new SecureRandom());
	}

	public HumanReadableNameGenerator(Random random) {
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
