package theArcanist.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theArcanist.powers.JinxPower;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.applyToEnemy;
import static theArcanist.util.Wiz.discard;

public class SimpleCurse extends AbstractArcanistCard {
    public final static String ID = makeID(SimpleCurse.class.getSimpleName());
    private final static int MAGIC = 2;
    private final static int UPGRADE_MAGIC = 1;
    private final static int COST = 0;
    private final static int DISCARD_AMT = 1;

    public SimpleCurse() {
        super(ID, COST, CardType.SKILL, CardRarity.BASIC, CardTarget.ENEMY);
    }

    @Override
    protected void applyAttributes() {
        baseMagicNumber = magicNumber = MAGIC;
        magicOneIsDebuff = true;
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new JinxPower(m, magicNumber));
        discard(DISCARD_AMT);
    }

    public void upp() {
        upgradeMagicNumber(UPGRADE_MAGIC);
    }
}