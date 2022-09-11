package theExile.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExile.actions.VoodooDollAction;
import theExile.util.Wiz;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.adp;
import static theExile.util.Wiz.atb;

public class VoodooDoll extends AbstractExileCard {
    public final static String ID = makeID(VoodooDoll.class.getSimpleName());
    private final static int DAMAGE = 6;
    private final static int UPGRADE_DAMAGE = 2;
    private final static int MAGIC = 2;
    private final static int COST = 1;

    public VoodooDoll() {
        super(ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
    }

    public void applyAttributes() {
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        addModifier(elenum.ELDRITCH);
    }

    @Override
    public void singleTargetUse(AbstractMonster m) {
        DamageModContainer container = new DamageModContainer(this, DamageModifierManager.modifiers(this));
        DamageInfo info = BindingHelper.makeInfo(container, adp(), damage, DamageInfo.DamageType.NORMAL);
        atb(new VoodooDollAction(magicNumber, info, m, Wiz.getAttackEffect(damage, damageModList, false)));
    }

    public void upp() {
        upgradeDamage(UPGRADE_DAMAGE);
    }
}