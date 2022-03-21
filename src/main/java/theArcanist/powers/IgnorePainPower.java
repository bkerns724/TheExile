package theArcanist.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import theArcanist.ArcanistMod;
import theArcanist.actions.IgnorePainAction;

import static theArcanist.util.Wiz.adp;
import static theArcanist.util.Wiz.atb;

public class IgnorePainPower extends AbstractArcanistPower {
    public static String POWER_ID = ArcanistMod.makeID(IgnorePainPower.class.getSimpleName());

    public IgnorePainPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        int count = amount;
        for (AbstractCard card : adp().drawPile.group) {
            if (card.type == AbstractCard.CardType.STATUS) {
                atb(new IgnorePainAction(card));
                --count;
            }
            if (count == 0)
                return;
        }
        for (AbstractCard card : adp().discardPile.group) {
            if (card.type == AbstractCard.CardType.STATUS) {
                atb(new IgnorePainAction(card));
                --count;
            }
            if (count == 0)
                return;
        }
    }
}