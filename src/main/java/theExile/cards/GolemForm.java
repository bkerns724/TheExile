package theExile.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExile.powers.GolemPower;
import theExile.powers.SteelhidePower;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.adp;
import static theExile.util.Wiz.applyToSelf;

public class GolemForm extends AbstractExileCard {
    public final static String ID = makeID(GolemForm.class.getSimpleName());
    private final static int MAGIC = 2;
    private final static int UPGRADE_MAGIC = 1;
    public final static int SECOND_MAGIC = 35;
    private final static int COST = 3;

    public GolemForm() {
        super(ID, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    public void applyAttributes() {
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagic = secondMagic = SECOND_MAGIC;
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new SteelhidePower(adp(), magicNumber));
        applyToSelf(new GolemPower(1));
    }

    public void upp() {
        upgradeMagicNumber(UPGRADE_MAGIC);
    }
}