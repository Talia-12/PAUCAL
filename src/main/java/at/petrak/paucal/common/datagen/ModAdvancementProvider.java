package at.petrak.paucal.common.datagen;

import at.petrak.paucal.PaucalMod;
import at.petrak.paucal.api.datagen.PaucalAdvancementProvider;
import at.petrak.paucal.common.advancement.BeContributorTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class ModAdvancementProvider extends PaucalAdvancementProvider {
    public ModAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn, PaucalMod.MOD_ID);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement.Builder.advancement()
            .display(invisible())
            .addCriterion("on_login", new BeContributorTrigger.Instance(EntityPredicate.Composite.ANY,
                MinMaxBounds.Ints.atLeast(1), null, null))
            .rewards(AdvancementRewards.Builder.function(modLoc("welcome")))
            .save(consumer, prefix("be_patron"));
    }
}
