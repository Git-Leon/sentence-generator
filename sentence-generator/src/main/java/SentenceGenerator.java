package main.java;

public abstract class SentenceGenerator {
	static String[] conjunctions = { "and", "or", "but", "because", "when", "if", "unless", "until", "when", "while",
			"than" };
	static String[] properNouns = { "Leon", "Tenny", "Justin", "Steve", "Nikunj", "Joe" };
	static String[] commonNouns = { "fish", "dish", "chip", "relevant", "skeleton", "reverant", "elephant", "God",
			"frog", "dog", "log" };
	static String[] determiners = { "a", "the", "some" };
	static String[] adjectives = { "big", "fat", "tiny", "sticky", "gooey", "pink", "nasty", "stinky", "smelly" };
	static String[] intransitiveVerbs = { "runs", "jumps", "talks", "sleeps" };
	static String[] transitiveVerbs = { "loved", "hated", "saw", "knew", "looked for", "found" };

	/**
	 * subroutine that defines how SimpleSentence is put together, nesting
	 * NounPhrase and VerbPhrase subroutines
	 */

	public static String getSimpleSentence() {
		String nounPhrase = getNounPhrase();
		String verbPhrase = getVerbPhrase();
		String suffixVerb = Utils.randomItem(intransitiveVerbs);
		return String.format("%s %s %s.", nounPhrase, verbPhrase, suffixVerb);
	}

	/**
	 * subroutine that defines the nested variable NounPhrase
	 */
	public static String getNounPhrase() {
		String result = "%s %s %s %s";
		String val1 = Utils.randomItem(properNouns);
		String val2 = Utils.randomItem(determiners);
		String val3 = Utils.randomItem(commonNouns);
		// String val4 = Utils.randomItem(adjectives);
		String val4 = Adjective.random();
		return String.format(result, val1, val2, val4, val3);
	}

	/**
	 * subroutine that defines the nested variable VerbPhrase
	 */
	public static String getVerbPhrase() {
		String result = "%s %s %s";
		String val1 = Utils.randomItem(intransitiveVerbs);
		String val2 = Utils.randomItem(conjunctions);
		return String.format(result, val1, val2, getNounPhrase());
	}

	public static void printSentenceComponents() {
		String nounPhrase = getNounPhrase();
		String verbPhrase = getVerbPhrase();
		String suffixVerb = Utils.randomItem(intransitiveVerbs);
		System.out.format("\nNounPhrase = %s", nounPhrase);
		System.out.format("\nVerbPhrase = %s", verbPhrase);
		System.out.format("\nSentence = %s %s %s.", nounPhrase, verbPhrase, suffixVerb);
	}

	public static void main(String[] args) {
		System.out.println("\f");
		for (Integer i = 0; i < 100; i++) {
			System.out.format("\nSentence:\n\t>> %s", getSimpleSentence());
		}
	}
}