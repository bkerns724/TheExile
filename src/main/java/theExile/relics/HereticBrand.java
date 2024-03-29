package theExile.relics;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Pride;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theExile.TheExile;
import theExile.cards.ExilePride;

import static theExile.ExileMod.makeID;

public class HereticBrand extends AbstractExileRelic {
    public static final String ID = makeID(HereticBrand.class.getSimpleName());

    public HereticBrand() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, TheExile.Enums.EXILE_BROWN_COLOR);
        cardToPreview = new ExilePride();
        setUpdatedDescription();
    }

    public void onEquip() {
        AbstractCard card = new ExilePride();
        SoulboundField.soulbound.set(card, true);

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card,
                (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        UnlockTracker.markCardAsSeen(Pride.ID);

        ++AbstractDungeon.player.energy.energyMaster;
    }

    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }
}
