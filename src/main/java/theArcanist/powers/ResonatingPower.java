package theArcanist.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theArcanist.ArcanistMod;
import theArcanist.cards.GenericResonantCard;

import static java.lang.Math.max;
import static theArcanist.util.Wiz.*;

public class ResonatingPower extends AbstractArcanistPower implements OnReceivePowerPower {
    public static String POWER_ID = ArcanistMod.makeID("Resonating");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int DEDUCTION = 4;
    public static final int TYPE_BONUS = 4;

    public boolean cold = false;
    public boolean force = false;
    public boolean dark = false;
    public boolean fire = false;
    public int jinx = 0;
    public int chaos = 0;

    public ResonatingPower(AbstractCreature owner, int amount, boolean cold, boolean dark, boolean force, boolean fire,
                           int jinx, int chaos) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.name = NAME;
        this.dark = dark;
        this.cold = cold;
        this.force = force;
        this.fire = fire;
        this.jinx = jinx;
        this.chaos = chaos;
    }

    @Override
    public boolean onReceivePower(AbstractPower pow, AbstractCreature target, AbstractCreature source) {
        if (target == owner && pow instanceof ResonatingPower)
            stackPower((ResonatingPower) pow);
        return true;
    }

    public void stackPower(ResonatingPower pow) {
        ArcanistMod.logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        amount += max(0, pow.amount - DEDUCTION);
        jinx += pow.jinx;
        chaos += pow.chaos;
        if (pow.force) {
            if (!force)
                force = true;
            else
                amount += TYPE_BONUS;
        }
        if (pow.cold) {
            if (!cold)
                cold = true;
            else
                amount += TYPE_BONUS;
        }
        if (pow.dark) {
            if (!dark)
                dark = true;
            else
                amount += TYPE_BONUS;
        }
        if (pow.fire) {
            if (!fire)
                fire = true;
            else
                amount += TYPE_BONUS;
        }
    }

    @Override
    public void stackPower(int stackAmount) {
    }

    public void atEndOfTurn(boolean isPlayer) {
        GenericResonantCard card = new GenericResonantCard(amount, cold, dark, force, fire, jinx, chaos);
        topDeck(card);
        atb(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void atEndOfRound() {
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}