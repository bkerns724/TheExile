package theArcanist.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theArcanist.powers.JinxPower;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.applyToEnemy;
import static theArcanist.util.Wiz.forAllMonstersLiving;

public class CursedSigil extends AbstractArcanistCard {
    public final static String ID = makeID(CursedSigil.class.getSimpleName());
    private final static int MAGIC = 2;
    private final static int UPGRADE_MAGIC = 1;

    public CursedSigil() {
        super(ID, -2, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = MAGIC;
        magicOneIsDebuff = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(monster -> applyToEnemy(monster, new JinxPower(monster, magicNumber)));
    }

    public void upp() {
        upgradeMagicNumber(UPGRADE_MAGIC);
    }
}