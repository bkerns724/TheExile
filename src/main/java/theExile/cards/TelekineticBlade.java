package theExile.cards;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import theExile.util.Wiz;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.adp;
import static theExile.util.Wiz.applyToSelf;

public class TelekineticBlade extends AbstractExileCard {
    public final static String ID = makeID(TelekineticBlade.class.getSimpleName());
    private final static int DAMAGE = 15;
    private final static int UPGRADE_DAMAGE = 5;
    private final static int COST = 2;

    public TelekineticBlade() {
        super(ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
    }

    public void applyAttributes() {
        baseDamage = DAMAGE;
    }

    @Override
    public void singleTargetUse(AbstractMonster m) {
        if (damageModList.isEmpty())
            dmg(m, Wiz.getSlashEffect(damage));
        else
            dmg(m);
    }

    @Override
    public void nonTargetUse() {
        applyToSelf(new EquilibriumPower(adp(), 1));
    }

    public void upp() {
        upgradeDamage(UPGRADE_DAMAGE);
    }
}