package theAquaLance.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAquaLance.AquaLanceMod;
import theAquaLance.powers.ElementalFormPower;

import java.util.Iterator;

import static theAquaLance.AquaLanceMod.makeID;
import static theAquaLance.util.Wiz.*;

public class ElementalForm extends AbstractEasyCard {
    public final static String ID = makeID("ElementalForm");
    private final static int MAGIC = 1;
    private final static int COST = 3;

    public ElementalForm() {
        super(ID, COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = MAGIC;
        isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean secondPlay = adp().hasPower(ElementalForm.ID);
        applyToSelf(new ElementalFormPower(adp(), magicNumber));

        if (!AquaLanceMod.isThemeSong())
            return;

        Iterator<AbstractMonster> var1 = AbstractDungeon.getMonsters().monsters.iterator();
        AbstractMonster mo;
        do {
            if (!var1.hasNext()) {
                return;
            }
            mo = var1.next();
        } while(mo.type != AbstractMonster.EnemyType.BOSS);

        if (!secondPlay) {
            CardCrawlGame.music.fadeAll();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly("THEME_SONG");
        }
    }

    public void upp() {
        uDesc();
        isEthereal = false;
    }
}