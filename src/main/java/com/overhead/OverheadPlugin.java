package com.overhead;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class OverheadPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BarrowsChestConfig config;

	@Provides
	BarrowsChestConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BarrowsChestConfig.class);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		// Check for the specific chat message indicating a Barrows chest opening
		if (event.getMessage().contains("You've found a hidden tunnel!") || event.getMessage().contains("You find a piece of the Barrows equipment")) {
			// Play sound
			playSound();
		}
	}

	private void playSound() {
		try {
			File soundFile = new File("path/to/your/soundfile.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(soundFile));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}



	@Provides
    BarrowsChestConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BarrowsChestConfig.class);
	}
}
