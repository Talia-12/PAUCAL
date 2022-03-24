package at.petrak.paucal.contrib;

import at.petrak.paucal.PaucalMod;
import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableCommentedConfig;
import com.electronwill.nightconfig.toml.TomlParser;
import net.minecraft.DefaultUncaughtExceptionHandler;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ContributorsLoader {
    private static final ConcurrentHashMap<UUID, Contributor> CONTRIBUTORS = new ConcurrentHashMap<>();
    private static boolean startedLoading = false;

    @Nullable
    public static Contributor getContributor(UUID uuid) {
        return CONTRIBUTORS.get(uuid);
    }

    public static void loadContributors() {
        if (!startedLoading) {
            startedLoading = true;

            var thread = new Thread(ContributorsLoader::fetch);
            thread.setName("PAUCAL Contributors Loading Thread");
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(PaucalMod.LOGGER));
            thread.start();
        }
    }

    private static void fetch() {
        UnmodifiableCommentedConfig config;
        try {
            var url = new URL("https://raw.githubusercontent.com/gamma-delta/PAUCAL/main/contributors.toml");
            config = new TomlParser().parse(url).unmodifiable();
        } catch (IOException exn) {
            PaucalMod.LOGGER.warn("Couldn't load contributors from Github... oh well :(");
            return;
        }

        var keys = config.valueMap().keySet();
        for (var key : keys) {
            try {
                AbstractConfig rawEntry = config.get(key);
                UUID uuid = UUID.fromString(key);
                var contributor = new Contributor(uuid, rawEntry);
                CONTRIBUTORS.put(uuid, contributor);
            } catch (Exception exn) {
                PaucalMod.LOGGER.warn("Exception when loading contributor {}: {}", key, exn.getMessage());
            }
        }
    }
}
