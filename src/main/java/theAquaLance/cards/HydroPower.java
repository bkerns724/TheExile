package theAquaLance.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BerserkPower;
import theAquaLance.patches.AbstractCardPatch;

import static theAquaLance.AquaLanceMod.makeID;
import static theAquaLance.util.Wiz.*;

public class HydroPower extends AbstractEasyCard {
    public final static String ID = makeID("HydroPower");
    private final static int MAGIC = 1;
    private final static int UPGRADED_COST = 1;

    public HydroPower() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BerserkPower(p, magicNumber));
    }

    public void upp() {
        AbstractCardPatch.AbstractCardField.replenish.set(this, true);
        upgradeBaseCost(UPGRADED_COST);
    }
}