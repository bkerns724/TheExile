package theAquaLance.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAquaLance.patches.AbstractCardPatch;
import theAquaLance.powers.IceArmorPower;

import static theAquaLance.AquaLanceMod.makeID;
import static theAquaLance.util.Wiz.*;

public class IceArmor extends AbstractEasyCard {
    public final static String ID = makeID("IceArmor");
    private final static int MAGIC = 1;
    private final static int COST = 3;

    public IceArmor() {
        super(ID, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new IceArmorPower(adp(), magicNumber));
    }

    public void upp() {
        uDesc();
        AbstractCardPatch.AbstractCardField.replenish.set(this, true);
    }
}