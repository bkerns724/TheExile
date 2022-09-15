package theExile.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static theExile.util.Wiz.*;

public class CallLoseHpAction extends AbstractGameAction {
    private static final float DURATION = 0.25F;

    public CallLoseHpAction(AbstractCreature target, int amount) {
        this.setValues(target, adp(), amount);
        actionType = ActionType.DAMAGE;
        duration = startDuration = DURATION;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT)
            isDone = true;
        else {
            if (duration == DURATION && target.currentHealth > 0)
                CardCrawlGame.sound.play("NECRONOMICON");

            tickDuration();
            if (isDone) {
                if (target.currentHealth > 0)
                    target.damage(new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS));

                if (adRoom().monsters.areMonstersBasicallyDead())
                    AbstractDungeon.actionManager.clearPostCombatActions();

                att(new WaitAction(0.1F));
            }
        }
    }
}
