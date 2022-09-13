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
import com.megacrit.cardcrawl.powers.AbstractPower;
import theExile.ExileMod;
import theExile.icons.Ice;
import theExile.powers.ElementalProwessIce;
import theExile.powers.FrostbitePower;
import theExile.relics.BlueMarbles;
import theExile.util.Wiz;

import java.util.ArrayList;

import static theExile.util.Wiz.*;

@AutoAdd.Ignore
public class IceDamage extends AbstractDamageModifier {
    public static final String ID = ExileMod.makeID(IceDamage.class.getSimpleName());
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public TooltipInfo iceTooltip;
    public TooltipInfo iceTooltip2;
    private boolean visibleTips = true;
    private final static int THRESHOLD = 3;

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
        frostbite = frostbite / THRESHOLD;
        float mult = 1f;
        AbstractPower pow = adp().getPower(ElementalProwessIce.POWER_ID);
        if (pow != null)
            mult += pow.amount;
        frostbite = frostbite * mult;
        int frostbiteInt = (int) frostbite;
        if (frostbiteInt > 0) {
            if (adp() != null && adp().hasRelic(BlueMarbles.ID))
                Wiz.forAllMonstersLiving(mon -> applyToEnemy(mon, new FrostbitePower(mon, frostbiteInt)));
            else
                applyToEnemyTop(target, new FrostbitePower(target, frostbiteInt));
        }
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
