package theExile.powers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.*;

public class AuraOfMisfortunePower extends AbstractExilePower {
    public static String POWER_ID = makeID(AuraOfMisfortunePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AuraOfMisfortunePower(int amount) {
        super(POWER_ID, PowerType.BUFF, false, adp(), amount);
        this.name = NAME;
    }

    @Override
    public void atStartOfTurn() {
        forAllMonstersLiving(m -> applyToEnemy(m, new JinxPower(m, amount)));
    }
}