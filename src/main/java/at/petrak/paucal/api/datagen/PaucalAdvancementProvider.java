package at.petrak.paucal.api.datagen;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class PaucalAdvancementProvider extends AdvancementProvider {
    protected final String modid;

    public PaucalAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn, String modid) {
        super(generatorIn, fileHelperIn);
        this.modid = modid;
    }

    protected DisplayInfo simpleDisplay(ItemLike icon, String name, FrameType frameType) {
        return simpleDisplayWithBackground(icon, name, frameType, null);
    }

    protected DisplayInfo simpleDisplayWithBackground(ItemLike icon, String name, FrameType frameType,
        ResourceLocation background) {
        return display(new ItemStack(icon), name, frameType, background, true, true, false);
    }

    protected DisplayInfo display(ItemStack icon, String name, FrameType frameType, ResourceLocation background,
        boolean showToast, boolean announceChat, boolean hidden) {
        String expandedName = "advancement." + this.modid + ":" + name;
        return new DisplayInfo(icon,
            new TranslatableComponent(expandedName),
            new TranslatableComponent(expandedName + ".desc"),
            background, frameType, showToast, announceChat, hidden);
    }
}
