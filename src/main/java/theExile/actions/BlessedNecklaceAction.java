package theExile.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;

import static theExile.util.Wiz.adp;
import static theExile.util.Wiz.att;

public class BlessedNecklaceAction extends AbstractGameAction {
    private int blockAmount;

    public BlessedNecklaceAction(int blockAmount) {
        this.blockAmount = blockAmount;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            int count = adp().hand.size();
            if (count != 0) {
                att(new DiscardAction(adp(), adp(), count, true));
                att(new GainBlockAction(adp(), count*blockAmount));
            }

            isDone = true;
        }
    }
}
