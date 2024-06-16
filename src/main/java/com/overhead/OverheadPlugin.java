package com.overhead;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.game.ItemManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.InputStream;

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

	private static final int BARROWS_CHEST_ID = 20724; // Opened Barrows Chest ID

	@Provides
	BarrowsChestConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BarrowsChestConfig.class);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOption().equalsIgnoreCase("open") && event.getIdentifier() == BARROWS_CHEST_ID)
		{
			client.createMenuEntry(-1)
					.setOption("Check")
					.setTarget(event.getTarget())
					.setType(MenuAction.RUNELITE)
					.onClick(e -> checkBarrowsChestContents());
		}
	}

	private void checkBarrowsChestContents()
	{
		Item[] items = client.getItemContainer(InventoryID.BARROWS_REWARD).getItems();

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

	private boolean isBarrowsItem(int itemId)
	{
		int[] barrowsItemIds = {
				//ahrim's
				4708, // Ahrim's hood
				4710, // Ahrim's staff
				4712, // Ahrim's robetop
				4714, // Ahrim's robeskirt

				//dharok's
				4716, // Dharok's helm
				4720, // Dharok's platebody
				4722, // Dharok's platelegs
				4718, // Dharok's greataxe

				//guthan's
				4724, // Guthan's helm
				4728, // Guthan's body
				4730, // Guthan's chainskirt
				4726, // Guthan's warspear

				//karil's
				4732, // Karil's coif
				4736, // Karil's leathertop
				4738, // Karil's leatherskirt
				4734, // Karil's crossbow

				//torag's
				4745, // Torag's helm
				4749, // Torag's platebody
				4751, // Torag's platelegs
				4747, // Torag's hammers

				//verac's
				4753, // Verac's helm
				4757, // Verac's brassard
				4759, // Verac's plateskirt
				4755, // Verac's flail
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
		String soundFilePath = hasBarrowsItem ? "path/to/item_found_sound.wav" : "/magic-conch-nothing.wav";
		playSoundFile(soundFilePath);
	}

	private void playSoundFile(String soundFilePath)
	{
		try (InputStream audioSrc = getClass().getResourceAsStream(soundFilePath))
		{
			if (audioSrc == null) {
				log.error("Sound file not found: " + soundFilePath);
				return;
			}

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioSrc);
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
