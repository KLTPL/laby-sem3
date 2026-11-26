package eu.jpereira.trainings.designpatterns.structural.decorator.channel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import eu.jpereira.trainings.designpatterns.structural.decorator.channel.decorator.MessageTruncator;
import eu.jpereira.trainings.designpatterns.structural.decorator.channel.decorator.WordCensor;

public class GooglePlusChannelTest {

	private final SocialChannelProperties googlePlus = new SocialChannelProperties().putProperty(SocialChannelPropertyKey.NAME, GooglePlusChannel.NAME);

	@Test
	public void testBuildGooglePlusChannel() {
		SocialChannelBuilder builder = new GooglePlusSocialChannelBuilder();
		assertTrue(builder.buildChannel(googlePlus) instanceof GooglePlusChannel);
	}

	@Test
	public void testDefaultChannelsStillAvailable() {
		SocialChannelBuilder builder = new GooglePlusSocialChannelBuilder();
		SocialChannelProperties twitter = new SocialChannelProperties().putProperty(SocialChannelPropertyKey.NAME, TwitterChannel.NAME);
		assertTrue(builder.buildChannel(twitter) instanceof TwitterChannel);
	}

	@Test
	public void testDecorateGooglePlusChannel() {
		SocialChannelBuilder builder = new GooglePlusSocialChannelBuilder();
		SocialChannel channel = builder.with(new WordCensor(Arrays.asList("Microsoft"))).with(new MessageTruncator(15)).getDecoratedChannel(googlePlus);

		channel.deliverMessage("Microsoft Windows is great!!");

		GooglePlusChannel googlePlusChannel = (GooglePlusChannel) builder.buildChannel(googlePlus);
		assertEquals("### Windows ...", googlePlusChannel.getLastMessage());
	}
}
