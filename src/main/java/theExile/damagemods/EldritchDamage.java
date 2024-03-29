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
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import theExile.ExileMod;
import theExile.actions.MyAddTempHPAction;
import theExile.icons.Eldritch;
import theExile.powers.EldritchGraspPower;

import java.util.ArrayList;

import static theExile.util.Wiz.*;

@AutoAdd.Ignore
public class EldritchDamage extends AbstractDamageModifier {
    public static final String ID = ExileMod.makeID(EldritchDamage.class.getSimpleName());
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public TooltipInfo darkTooltip;
    public TooltipInfo darkTooltip2;
    private boolean visibleTips = true;
    private static final int THRESHOLD = 3;

    private int blockedAmount = 0;

    public EldritchDamage() {
        darkTooltip = null;
        darkTooltip2 = null;
    }

    public EldritchDamage(boolean visTips) {
        this();
        visibleTips = visTips;
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
        float tempHP = blockedAmount + lastDamageTaken;
        tempHP = tempHP / THRESHOLD;
        if ((int)tempHP > 0)
            att(new MyAddTempHPAction(adp(), adp(), (int)tempHP));
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractPower pow = adp().getPower(EldritchGraspPower.POWER_ID);
        if (pow == null)
            return;
        applyToEnemyTop(target, new ConstrictedPower(target, adp(), pow.amount));
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        if (!visibleTips)
            return new ArrayList<>();
        if (darkTooltip == null)
            darkTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        if (darkTooltip2 == null)
            darkTooltip2 = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[1], cardStrings.EXTENDED_DESCRIPTION[2]);
        return new ArrayList<TooltipInfo>() { { add(darkTooltip); add(darkTooltip2);} };
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
        EldritchDamage output = new EldritchDamage(visibleTips);
        output.darkTooltip = this.darkTooltip;
        output.darkTooltip2 = this.darkTooltip2;
        return output;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return new Eldritch();
    }
}
