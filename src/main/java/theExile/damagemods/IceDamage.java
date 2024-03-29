package theExile.damagemods;

import basemod.AutoAdd;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.CardStrings;
import theExile.ExileMod;
import theExile.icons.Ice;
import theExile.powers.FrostbitePower;
import theExile.relics.BlueMarbles;

import java.util.ArrayList;

import static theExile.util.Wiz.adp;
import static theExile.util.Wiz.applyToEnemyTop;

@AutoAdd.Ignore
public class IceDamage extends AbstractDamageModifier {
    public static final String ID = ExileMod.makeID(IceDamage.class.getSimpleName());
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public TooltipInfo iceTooltip;
    public TooltipInfo iceTooltip2;
    private boolean visibleTips = true;

    private int blockedAmount = 0;

    public IceDamage() {
        iceTooltip = null;
        iceTooltip2 = null;
    }

    public IceDamage(boolean visibleTips) {
        this();
        this.visibleTips = visibleTips;
    }

    @Override
    public boolean shouldPushIconToCard(AbstractCard card) {
        return true;
    }

    @Override
    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature target) {
        this.blockedAmount = blockedAmount;
    }

    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
        if (adp() == null || adp() == target)
            return;
        float frostbite = blockedAmount + lastDamageTaken;
        if (adp().hasRelic(BlueMarbles.ID))
            frostbite = frostbite * BlueMarbles.MULT;
        int frostbiteInt = (int) frostbite;
        if (frostbiteInt > 0)
            applyToEnemyTop(target, new FrostbitePower(target, frostbiteInt));
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        if (!visibleTips)
            return new ArrayList<>();
        if (iceTooltip == null)
            iceTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        if (iceTooltip2 == null)
            iceTooltip2 = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[1], cardStrings.EXTENDED_DESCRIPTION[2]);
        return new ArrayList<TooltipInfo>() { { add(iceTooltip); add(iceTooltip2); } };
    }

    public static ArrayList<PowerTip> getPowerTips() {
        ArrayList<PowerTip> list = new ArrayList<>();
        list.add(new PowerTip(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]));
        list.add(new PowerTip(cardStrings.EXTENDED_DESCRIPTION[1], cardStrings.EXTENDED_DESCRIPTION[2]));
        return list;
    }

    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        IceDamage output = new IceDamage(visibleTips);
        output.iceTooltip = this.iceTooltip;
        output.iceTooltip2 = this.iceTooltip2;
        return output;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return new Ice();
    }
}
