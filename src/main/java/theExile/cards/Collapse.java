package theExile.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExile.powers.CollapsePower;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.applyToEnemy;

public class Collapse extends AbstractExileCard {
    public final static String ID = makeID(Collapse.class.getSimpleName());
    private final static int DAMAGE = 18;
    private final static int UPGRADE_DAMAGE = 6;
    private final static int COST = 2;

    public Collapse() {
        super(ID, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
    }

    @Override
    protected void applyAttributes() {
        baseDamage = DAMAGE;
        addModifier(elenum.FORCE);
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new CollapsePower(m, damage));
    }

    public void upp() {
        upgradeDamage(UPGRADE_DAMAGE);
    }
}