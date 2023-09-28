package dev.mrshawn.deathmessages.config;

import dev.mrshawn.deathmessages.DeathMessages;
import dev.mrshawn.deathmessages.utils.CommentedConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class PlayerDeathMessages {

	public final String fileName = "PlayerDeathMessages";

	CommentedConfiguration config;

	File file;

	public PlayerDeathMessages() {
	}

	private static final PlayerDeathMessages instance = new PlayerDeathMessages();

	public static PlayerDeathMessages getInstance() {
		return instance;
	}

	public CommentedConfiguration getConfig() {
		return config;
	}

	public void save() {
		try {
			config.save(file);
		} catch (Exception e) {
			File f = new File(DeathMessages.getInstance().getDataFolder(), fileName + ".broken." + new Date().getTime());
			DeathMessages.getInstance().getLogger().severe("Could not save: " + fileName + ".yml");
			DeathMessages.getInstance().getLogger().severe("Regenerating file and renaming the current file to: " + f.getName());
			DeathMessages.getInstance().getLogger().severe("You can try fixing the file with a yaml parser online!");
			file.renameTo(f);
			initialize();
		}
	}

	public void reload() {
		try {
			config = CommentedConfiguration.loadConfiguration(file);
		} catch (Exception e) {
			File f = new File(DeathMessages.getInstance().getDataFolder(), fileName + ".broken." + new Date().getTime());
			DeathMessages.getInstance().getLogger().severe("Could not save: " + fileName + ".yml");
			DeathMessages.getInstance().getLogger().severe("Regenerating file and renaming the current file to: " + f.getName());
			DeathMessages.getInstance().getLogger().severe("You can try fixing the file with a yaml parser online!");
			file.renameTo(f);
			initialize();
			e.printStackTrace();
		}
	}

	public void initialize() {
		file = new File(DeathMessages.getInstance().getDataFolder(), fileName + ".yml");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			copy(DeathMessages.getInstance().getResource(fileName + ".yml"), file);
		}
		config = CommentedConfiguration.loadConfiguration(file);
		try {
			config.syncWithConfig(file, DeathMessages.getInstance().getResource(fileName + ".yml"), "Mobs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
