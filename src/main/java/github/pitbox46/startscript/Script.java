package github.pitbox46.startscript;

import com.google.gson.*;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.loading.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

public class Script {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static File jsonFile;

    public static void init(Path folder) {
        File file = new File(FileUtils.getOrCreateDirectory(folder, "startscript").toFile(), "startscript.json");
        try {
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write(GSON.toJson(new JsonArray()));
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
        jsonFile = file;
    }

    public static boolean readFile(MinecraftServer server) {
        try (Reader reader = new FileReader(jsonFile)) {
            JsonArray jsonArray = JSONUtils.fromJson(GSON, reader, JsonArray.class);
            if(jsonArray != null) {
                for(JsonElement element: jsonArray) {
                     server.getCommandManager().handleCommand(server.getCommandSource(), element.getAsString());
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
