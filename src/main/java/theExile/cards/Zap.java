package theExile.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theExile.damagemods.ScourgeType;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.applyToEnemy;

public class Zap extends AbstractExileCard {
    public final static String ID = makeID(Zap.class.getSimpleName());
    private final static int DAMAGE = 5;
    private final static int UPGRADE_DAMAGE = 3;
    private final static int COST = 1;

    public Zap() {
        super(ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
    }

    @Override
    protected void applyAttributes() {
        baseDamage = DAMAGE;
        DamageModifierManager.addModifier(this, new ScourgeType());
        hasScourge = true;
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        if (getJinxAmountCard(m) > 0)
            applyToEnemy(m, new WeakPower(m, getJinxAmountCard(m), false));
    }

    public void upp() {
        upgradeDamage(UPGRADE_DAMAGE);
    }
}