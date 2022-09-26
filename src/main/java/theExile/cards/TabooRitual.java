package theExile.cards;

import theExile.powers.TabooRitualPower;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.applyToSelf;

public class TabooRitual extends AbstractExileCard {
    public final static String ID = makeID(TabooRitual.class.getSimpleName());
    private final static int MAGIC = 2;
    private final static int UPGRADE_MAGIC = 1;
    private final static int COST = 1;

    public TabooRitual() {
        super(ID, COST, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    public void applyAttributes() {
        baseMagicNumber = magicNumber = MAGIC;
    }

    public void nonTargetUse() {
        applyToSelf(new TabooRitualPower(magicNumber));
    }

    public void upp() {
        upMagic(UPGRADE_MAGIC);
    }
}