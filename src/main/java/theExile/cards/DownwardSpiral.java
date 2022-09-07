package theExile.cards;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExile.powers.JinxPower;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.applyToEnemy;
import static theExile.util.Wiz.getDebuffCount;

public class DownwardSpiral extends AbstractExileCard {
    public final static String ID = makeID(DownwardSpiral.class.getSimpleName());
    private final static int COST = 1;
    private final static int UPGRADED_COST = 0;

    public DownwardSpiral() {
        super(ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
    }

    public void applyAttributes() {
        exhaust = true;
    }

    @Override
    public void singleTargetUse(AbstractMonster m) {
        int count = getDebuffCount(m);
        if (count > 0)
            applyToEnemy(m, new JinxPower(m, count));
    }

    public void upp() {
        upgradeBaseCost(UPGRADED_COST);
    }
}