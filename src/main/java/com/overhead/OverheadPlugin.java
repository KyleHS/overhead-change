package com.overhead;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

@Slf4j
@PluginDescriptor(
	name = "Barrows Feedback Sounds",
	description = "Plays one of two sounds depending if a barrows unique is received",
	tags = {"barrows", "unique", "sound", "spongebob", "magic conch", "sound adder"}
)
public class OverheadPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private BarrowsChestConfig config;

	@Inject
	private ItemManager itemManager;

	@Provides
	BarrowsChestConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BarrowsChestConfig.class);
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == WidgetInfo.BARROWS_REWARD_GROUP_ID)
		{
			checkBarrowsChestContents();
		}
	}

	private void checkBarrowsChestContents()
	{
		Widget rewardsWidget = client.getWidget(WidgetInfo.BARROWS_REWARD_INVENTORY);
		if (rewardsWidget != null)
		{
			Item[] items = rewardsWidget.getWidgetItems();

			boolean hasBarrowsItem = false;

			for (Item item : items)
			{
				if (isBarrowsItem(item.getId()))
				{
					hasBarrowsItem = true;
					break;
				}
			}

			playSound(hasBarrowsItem);
		}
	}

	private boolean isBarrowsItem(int itemId)
	{
		// List of Barrows item IDs (replace with actual IDs)
		int[] barrowsItemIds = {
				// Example IDs, replace with actual Barrows item IDs
				4708, // Ahrim's hood
				4710, // Ahrim's staff
				// Add all Barrows item IDs here
		};

		for (int id : barrowsItemIds)
		{
			if (id == itemId)
			{
				return true;
			}
		}
		return false;
	}

	private void playSound(boolean hasBarrowsItem)
	{
		String soundFilePath = hasBarrowsItem ? "path/to/item_found_sound.wav" : "path/to/nothing_found_sound.wav";
		playSoundFile(soundFilePath);
	}

	private void playSoundFile(String soundFilePath)
	{
		try
		{
			File soundFile = new File(soundFilePath);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
