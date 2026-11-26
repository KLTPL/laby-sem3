package eu.jpereira.trainings.designpatterns.structural.decorator.channel.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannel;

/**
 * Decorator that replaces censored words with "###" before delegating the message.
 * Only whole words are censored ("Microsoft" is censored, "Microsofty" is not).
 */
public class WordCensor extends SocialChannelDecorator {

	public static final String REPLACEMENT = "###";

	private final List<Pattern> censoredWords = new ArrayList<Pattern>();

	public WordCensor(List<String> censoredWords) {
		for (String word : censoredWords) {
			this.censoredWords.add(Pattern.compile("\\b" + Pattern.quote(word) + "\\b"));
		}
	}

	public WordCensor(List<String> censoredWords, SocialChannel decoratedChannel) {
		this(censoredWords);
		this.delegate = decoratedChannel;
	}

	@Override
	public void deliverMessage(String message) {
		String censored = message;
		for (Pattern word : censoredWords) {
			censored = word.matcher(censored).replaceAll(REPLACEMENT);
		}
		delegate.deliverMessage(censored);
	}
}
