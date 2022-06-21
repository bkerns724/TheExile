package theArcanist.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import theArcanist.powers.SteelhidePower;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.adp;
import static theArcanist.util.Wiz.applyToSelf;

public class GolemForm extends AbstractArcanistCard {
    public final static String ID = makeID(GolemForm.class.getSimpleName());
    private final static int MAGIC = 3;
    private final static int UPGRADE_MAGIC = 1;
    private final static int SECOND_MAGIC = 10;
    private final static int UPGRADE_SECOND = 3;
    private final static int COST = 3;

    public GolemForm() {
        super(ID, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    public void applyAttributes() {
        baseMagicNumber = magicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new SteelhidePower(adp(), magicNumber));
        applyToSelf(new MetallicizePower(adp(), secondMagic));
    }

    public void upp() {
        upgradeMagicNumber(UPGRADE_MAGIC);
        upMagic2(UPGRADE_SECOND);
    }
}