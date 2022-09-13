package theExile.cards;

import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.adp;
import static theExile.util.Wiz.applyToSelf;

public class GolemForm extends AbstractExileCard {
    public final static String ID = makeID(GolemForm.class.getSimpleName());
    private final static int MAGIC = 6;
    private final static int UPGRADE_MAGIC = 2;
    private final static int SECOND_MAGIC = 4;
    private final static int UPGRADE_SECOND = 1;
    private final static int COST = 3;

    public GolemForm() {
        super(ID, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    public void applyAttributes() {
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagic = secondMagic = SECOND_MAGIC;
    }

    public void nonTargetUse() {
        applyToSelf(new MetallicizePower(adp(), magicNumber));
        applyToSelf(new StrengthPower(adp(), magicNumber));
    }

    public void upp() {
        upMagic(UPGRADE_MAGIC);
        upMagic2(UPGRADE_SECOND);
    }
}