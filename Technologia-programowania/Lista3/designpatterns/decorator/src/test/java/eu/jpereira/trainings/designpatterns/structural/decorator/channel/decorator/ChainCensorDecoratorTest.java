package eu.jpereira.trainings.designpatterns.structural.decorator.channel.decorator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannel;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannelBuilder;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannelProperties;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.SocialChannelPropertyKey;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.spy.TestSpySocialChannel;

public class ChainCensorDecoratorTest extends AbstractSocialChanneldDecoratorTest {

	@Test
	public void testChainCensorWithTruncatorAndURLAppender() {
		SocialChannelBuilder builder = createTestSpySocialChannelBuilder();
		SocialChannelProperties props = new SocialChannelProperties().putProperty(SocialChannelPropertyKey.NAME, TestSpySocialChannel.NAME);

		// censor first, so the truncator counts the already censored text
		SocialChannel channel = builder.
				with(new WordCensor(Arrays.asList("Microsoft"))).
				with(new MessageTruncator(15)).
				with(new URLAppender("http://jpereira.eu")).
				getDecoratedChannel(props);

		channel.deliverMessage("Microsoft Windows is great!!");

		TestSpySocialChannel spyChannel = (TestSpySocialChannel) builder.buildChannel(props);
		assertEquals("### Windows ... http://jpereira.eu", spyChannel.lastMessagePublished());
	}

	@Test
	public void testChainWithoutBuilder() {
		TestSpySocialChannel spy = new TestSpySocialChannel();
		SocialChannel urlAppender = new URLAppender("http://jpereira.eu", spy);
		SocialChannel truncator = new MessageTruncator(15, urlAppender);
		SocialChannel censor = new WordCensor(Arrays.asList("Microsoft"), truncator);

		censor.deliverMessage("Microsoft Windows is great!!");

		assertEquals("### Windows ... http://jpereira.eu", spy.lastMessagePublished());
	}
}
