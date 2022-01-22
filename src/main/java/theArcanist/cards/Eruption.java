package theArcanist.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class Eruption extends AbstractEasyCard {
    public final static String ID = makeID("Eruption");
    private final static int DAMAGE = 32;
    private final static int UPGRADE_DAMAGE = 8;
    private final static int COST = 3;

    // attack, rare, all_enemy
    public Eruption() {
        super(ID, COST, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = DAMAGE;
        isMultiDamage = true;
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.FIRE);
    }

    public void upp() {
        upgradeDamage(UPGRADE_DAMAGE);
    }
}