package theArcanist.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theArcanist.ArcanistMod;

import static theArcanist.util.Wiz.*;

public class BrrZerkPower extends AbstractArcanistPower {
    public static String POWER_ID = ArcanistMod.makeID("BrrZerk");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BrrZerkPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.name = NAME;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != owner && info.type == DamageInfo.DamageType.NORMAL)
        applyToEnemyTop(target, new FrostbitePower(target, amount));
    }

    @Override
    public void upDescription() {
        description = DESCRIPTIONS[0];
    }
}