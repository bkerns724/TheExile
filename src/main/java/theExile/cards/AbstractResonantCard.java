package theExile.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theExile.ExileMod;
import theExile.cards.cardUtil.Resonance;
import theExile.powers.ResonantCostZeroPower;

import static theExile.cards.AbstractExileCard.elenum.*;
import static theExile.util.Wiz.adRoom;
import static theExile.util.Wiz.adp;

public abstract class AbstractResonantCard extends AbstractExileCard {
    public Resonance resonance;

    public AbstractResonantCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, cost, type, rarity, target);
        resonance = new Resonance();
        setResonance();
        initializeDescription();
    }

    protected abstract void setResonance();

    public void singleTargetUse(AbstractMonster m) {
        resonance.resonanceEffects(this, m);
    }

    public void setMultiDamage(boolean mult) {
        isMultiDamage = mult;
    }

    @Override
    public boolean freeToPlay() {
        if (super.freeToPlay())
            return true;

        if (adp() != null && AbstractDungeon.currMapNode != null &&
                adRoom() != null && adRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                adp().hasPower(ResonantCostZeroPower.POWER_ID))
            return true;

        return false;
    }

    public void addModifier(elenum element, boolean tips) {
        if (damageModList.contains(element))
            return;

        if (element == ICE && !damageModList.contains(FAKE_ICE)) {
            addModifier(FAKE_ICE);
            resonance.addModifier(ICE);
        }
        if (element == FORCE && !damageModList.contains(FAKE_FORCE)) {
            addModifier(FAKE_FORCE);
            resonance.addModifier(FORCE);
        }
        if (element == ELDRITCH && !damageModList.contains(FAKE_ELDRITCH)) {
            addModifier(FAKE_ELDRITCH);
            resonance.addModifier(ELDRITCH);
        }
        if (element == LIGHTNING && !damageModList.contains(FAKE_LIGHTNING)) {
            addModifier(FAKE_LIGHTNING);
            resonance.addModifier(LIGHTNING);
        }

        initializeDescription();
    }

    @Override
    public void applyPowers() {
        baseDamage = resonance.getDamage();
        type = resonance.getCardType();
        target = resonance.getCardTarget();
        if (target == CardTarget.ALL_ENEMY || target == ExileMod.Enums.AUTOAIM_ENEMY)
            isMultiDamage = true;
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = resonance.getDamage();
        baseBlock = resonance.getBlock();
        type = resonance.getCardType();
        target = resonance.getCardTarget();
        if (target == CardTarget.ALL_ENEMY || target == ExileMod.Enums.AUTOAIM_ENEMY)
            isMultiDamage = true;
        super.calculateCardDamage(mo);
    }

    @Override
    public void initializeDescription() {
        boolean inReward = false;
        if(adRoom() != null) {
            for (RewardItem item : adRoom().rewards) {
                if (item.type == RewardItem.RewardType.CARD && item.cards.contains(this)) {
                    inReward = true;
                    break;
                }
            }
        }

        if (resonance != null && adRoom() != null && adp() != null && !inReward
                && ((!AbstractDungeon.gridSelectScreen.forUpgrade) || !AbstractDungeon.gridSelectScreen.confirmScreenUp)) {
            baseDamage = resonance.getDamage();
            type = resonance.getCardType();
            target = resonance.getCardTarget();
            rawDescription = resonance.getDescription();
            overrideRawDesc = true;
        } else
            overrideRawDesc = false;
        super.initializeDescription();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractResonantCard copy = (AbstractResonantCard) super.makeStatEquivalentCopy();
        copy.resonance = resonance.resClone();
        copy.initializeDescription();
        initializeDescription();
        return copy;
    }

    public void upp() {
    }
}