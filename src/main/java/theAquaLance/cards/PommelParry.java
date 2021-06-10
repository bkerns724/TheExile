package theAquaLance.cards;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static theAquaLance.AquaLanceMod.makeID;
import static theAquaLance.util.Wiz.*;

public class PommelParry extends AbstractEasyCard {
    public final static String ID = makeID("PommelParry");
    private final static int BLOCK = 7;
    private final static int UPGRADE_BLOCK = 3;
    private final static int MAGIC = 1;

    public PommelParry() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new DrawCardAction(magicNumber));
        atb(new DiscardAction(p, p, magicNumber, false));
    }

    public void upp() {
        upgradeBlock(UPGRADE_BLOCK);
    }
}