package eu.jpereira.trainings.designpatterns.structural.decorator.channel;

/**
 * Extends the default builder with the Google+ channel; existing builders stay untouched.
 */
public class GooglePlusSocialChannelBuilder extends DefaultSocialChannelBuilder {

	@Override
	protected void addDefaultChannels() {
		super.addDefaultChannels();
		plugChannel(new SocialChannelProperties().putProperty(SocialChannelPropertyKey.NAME, GooglePlusChannel.NAME), GooglePlusChannel.class);
	}
}
