package theArcanist.relics;

import com.megacrit.cardcrawl.powers.SadisticPower;
import theArcanist.TheArcanist;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class TormentorsMask extends AbstractEasyRelic {
    public static final String ID = makeID("TormentorsMask");
    private static final int SADISTIC_AMOUNT = 2;

    public TormentorsMask() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT, TheArcanist.Enums.ARCANIST_BLARPLE_COLOR);
    }

    @Override
    public void atBattleStart() {
        applyToSelf(new SadisticPower(adp(), SADISTIC_AMOUNT));
    }
}