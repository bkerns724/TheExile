package theExile.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExile.ExileMod;

import static theExile.util.Wiz.adp;

public class SoulFeedPower extends AbstractExilePower implements OnFatalPower {
    public static String POWER_ID = ExileMod.makeID(SoulFeedPower.class.getSimpleName());

    public SoulFeedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void onFatal(AbstractMonster monster) {
        adp().increaseMaxHp(1, true);
    }
}