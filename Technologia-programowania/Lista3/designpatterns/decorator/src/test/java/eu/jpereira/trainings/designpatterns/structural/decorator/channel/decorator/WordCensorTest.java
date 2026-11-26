package eu.jpereira.trainings.designpatterns.structural.decorator.channel.decorator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannel;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannelBuilder;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannelProperties;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannelPropertyKey;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.spy.TestSpySocialChannel;

public class WordCensorTest extends AbstractSocialChanneldDecoratorTest {

	private String deliverThroughCensor(String message, String... censoredWords) {
		SocialChannelBuilder builder = createTestSpySocialChannelBuilder();
		SocialChannelProperties props = new SocialChannelProperties().putProperty(SocialChannelPropertyKey.NAME, TestSpySocialChannel.NAME);
		SocialChannel channel = builder.with(new WordCensor(Arrays.asList(censoredWords))).getDecoratedChannel(props);

		channel.deliverMessage(message);

		TestSpySocialChannel spyChannel = (TestSpySocialChannel) builder.buildChannel(props);
		return spyChannel.lastMessagePublished();
	}

	@Test
	public void testCensorWord() {
		assertEquals("### Windows is great!!", deliverThroughCensor("Microsoft Windows is great!!", "Microsoft"));
	}

	@Test
	public void testCensorEveryOccurrence() {
		assertEquals("### and ### again", deliverThroughCensor("Microsoft and Microsoft again", "Microsoft"));
	}

	@Test
	public void testCensorManyWords() {
		assertEquals("### Windows and ### Java", deliverThroughCensor("Microsoft Windows and Oracle Java", "Microsoft", "Oracle"));
	}

	@Test
	public void testMessageWithoutCensoredWordsIsUnchanged() {
		assertEquals("Linux is great", deliverThroughCensor("Linux is great", "Microsoft"));
	}

	@Test
	public void testDoesNotCensorPartOfAnotherWord() {
		assertEquals("Microsofty", deliverThroughCensor("Microsofty", "Microsoft"));
	}

	@Test
	public void testCensorWithoutBuilder() {
		TestSpySocialChannel spy = new TestSpySocialChannel();
		SocialChannel channel = new WordCensor(Arrays.asList("Microsoft"), spy);

		channel.deliverMessage("Microsoft Windows");

		assertEquals("### Windows", spy.lastMessagePublished());
	}
}
