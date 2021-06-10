package theAquaLance.powers;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theAquaLance.AquaLanceMod;

import javax.print.attribute.standard.MediaSize;

import static theAquaLance.util.Wiz.*;

public class SpeedPower extends AbstractEasyPower {
    public static final String POWER_ID = AquaLanceMod.makeID("Speed");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SpeedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.name = NAME;
        priority = 25;  // Same as Tools of the Trade
    }

    public void atStartOfTurnPostDraw() {
        flash();
        int drawAmount = 1;
        int discardAmount = 1;
        AbstractPower drawPow = adp().getPower(DrawNextTurnPower.POWER_ID);
        if (drawPow != null)
            drawAmount += drawPow.amount;
        AbstractPower discardPow = adp().getPower(DiscardNextTurnPower.POWER_ID);
        if (discardPow != null)
            discardAmount += discardPow.amount;
        addToBot(new DrawCardAction(owner, drawAmount));
        addToBot(new DiscardAction(owner, owner, discardAmount, false));
    }


    @Override
    public void updateDescription() {
        if (amount == 1)
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        else
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4];
    }
}