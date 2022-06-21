package theArcanist.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theArcanist.powers.JinxPower;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.applyToEnemy;

public class FieryCurse extends AbstractArcanistCard {
    public final static String ID = makeID(FieryCurse.class.getSimpleName());
    private final static int MAGIC = 2;
    private final static int UPGRADE_MAGIC = 1;
    private final static int COST = 1;
    private final static int DAMAGE = 8;

    public FieryCurse() {
        super(ID, COST, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
    }

    @Override
    protected void applyAttributes() {
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        magicOneIsDebuff = true;
        addModifier(elenum.FIRE);
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        dmg(m);
        applyToEnemy(m, new JinxPower(m, magicNumber));
    }

    public void upp() {
        upgradeMagicNumber(UPGRADE_MAGIC);
    }
}