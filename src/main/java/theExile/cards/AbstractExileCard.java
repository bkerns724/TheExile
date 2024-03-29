package theExile.cards;

import basemod.AutoAdd;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import theExile.TheExile;
import theExile.actions.AttackAction;
import theExile.cards.cardvars.CardSaveObject;
import theExile.damagemods.*;
import theExile.icons.Eldritch;
import theExile.icons.Force;
import theExile.icons.Ice;
import theExile.icons.Lightning;
import theExile.powers.AbstractExilePower;
import theExile.powers.MetamagicPower;
import theExile.powers.RingingPower;
import theExile.relics.VialOfBlackBlood;
import theExile.relics.Fling;
import theExile.util.CardArtRoller;
import theExile.util.Wiz;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static theExile.ExileMod.*;
import static theExile.cards.AbstractExileCard.elenum.*;
import static theExile.util.Wiz.*;

@AutoAdd.Ignore
public abstract class AbstractExileCard extends CustomCard implements CustomSavable<CardSaveObject> {
    protected CardStrings cardStrings;
    protected static final CardStrings thisCardStrings =
            CardCrawlGame.languagePack.getCardStrings(makeID(AbstractExileCard.class.getSimpleName()));

    public int secondMagic;
    public int baseSecondMagic;
    public boolean upgradedSecondMagic;
    public boolean isSecondMagicModified;

    public int thirdMagic;
    public int baseThirdMagic;
    public boolean upgradedThirdMagic;
    public boolean isThirdMagicModified;

    private float rotationTimer = 0;
    protected int previewIndex;
    protected ArrayList<AbstractCard> cardToPreview = new ArrayList<>();

    protected boolean needsArtRefresh = false;
    protected boolean overrideRawDesc = false;

    public boolean beingDiscarded = false;

    public ArrayList<elenum> damageModList = new ArrayList<>();
    public boolean sigil = false;
    public int resonance = 0;

    private static final Color FLAVOR_BOX_COLOR = new Color(0.45f, 0, 0.65f, 1.0f);
    private static final Color FLAVOR_TEXT_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);

    public static final String COLD_STRING = Ice.CODE;
    public static final String FORCE_STRING = Force.CODE;
    public static final String ELDRITCH_STRING = Eldritch.CODE;
    public static final String LIGHTNING_STRING = Lightning.CODE;

    private final ArrayList<CardGlowBorder> myGlowList = new ArrayList<>();
    private float myGlowTimer = 0f;
    private boolean sigilGlowing = false;

    protected boolean miscTwo = false;

    private boolean wasSingleTarget = false;

    public enum elenum {
        ICE,
        FORCE,
        ELDRITCH,
        LIGHTNING
    }

    public AbstractExileCard(final String cardID, final int cost, final CardType type, final CardRarity rarity, final CardTarget target) {
        this(cardID, cost, type, rarity, target, TheExile.Enums.EXILE_BROWN_COLOR);
    }

    public AbstractExileCard(final String cardID, final int cost, final CardType type, final CardRarity rarity, final CardTarget target, final CardColor color) {
        super(cardID, "", getCardTextureString(cardID.replace(modID + ":", ""), type),
                cost, "", type, color, rarity, target);

        if (cardStrings == null)
            cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
        name = cardStrings.NAME;

        FlavorText.AbstractCardFlavorFields.boxColor.set(this, FLAVOR_BOX_COLOR);
        FlavorText.AbstractCardFlavorFields.textColor.set(this, FLAVOR_TEXT_COLOR);

        if (textureImg.contains("ui/missing.png")) {
            if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty()) {
                CardArtRoller.computeCard(this);
            } else
                needsArtRefresh = true;
        }

        applyAttributes();

        if (sigil && adp() != null && adp().hasRelic(Fling.ID)) {
            this.cost = 1;
            costForTurn = 1;
        }

        initializeDescription();
    }

    protected abstract void applyAttributes();

    public void initTitle() {
        initializeTitle();
    }

    public void onPickup() {}

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        ArrayList<TooltipInfo> list = new ArrayList<>();
        if (rarity == Enums.UNIQUE)
            list.add(new TooltipInfo(thisCardStrings.EXTENDED_DESCRIPTION[12], thisCardStrings.EXTENDED_DESCRIPTION[13]));
        return list;
    }

    @Override
    public void triggerOnManualDiscard() {
        if (!sigil)
            return;
        beingDiscarded = true;
        autoPlayWhenDiscarded();
        BindingPatches.DisableReactionaryActionBinding.disableBefore(null);
        forAllMonstersLiving(m -> {
            for (AbstractPower pow : m.powers)
                if (pow instanceof AbstractExilePower)
                    ((AbstractExilePower) pow).onDiscardSigil();
        });
        for (AbstractPower pow : adp().powers) {
            if (pow instanceof AbstractExilePower)
                ((AbstractExilePower) pow).onDiscardSigil();
        }
        BindingPatches.DisableReactionaryActionBinding.enableAfter(null);
    }

    protected void autoPlayWhenDiscarded() {
        AbstractDungeon.player.discardPile.removeCard(this);
        AbstractDungeon.player.drawPile.removeCard(this);
        AbstractDungeon.player.hand.removeCard(this);
        AbstractDungeon.getCurrRoom().souls.remove(this);
        AbstractDungeon.player.limbo.group.add(this);
        target_y = Settings.HEIGHT / 2.0f;
        target_x = Settings.WIDTH / 2.0f;
        targetAngle = 0;
        targetDrawScale = 0.8f;
        lighten(true);

        if (this.target == CardTarget.ENEMY) {
            AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(null,true,
                    AbstractDungeon.cardRandomRng);
            att(new NewQueueCardAction(this, monster, false, true));
        }else
            att(new NewQueueCardAction(this, true, false, true));
        att(new UnlimboAction(this));
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean superBool = super.canUse(p, m);

        if (!superBool)
            return false;
        if (adp() != null && adp().hasRelic(Fling.ID))
            return true;
        if (!beingDiscarded && sigil) {
            cantUseMessage = thisCardStrings.EXTENDED_DESCRIPTION[9];
            return false;
        }

        return true;
    }

    @Override
    public boolean cardPlayable(AbstractMonster m) {
        if (adp() != null && adp().hasPower(MetamagicPower.POWER_ID))
            return true;
        return super.cardPlayable(m);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (adp() != null && !adp().hasPower(MetamagicPower.POWER_ID)) {
            if (m != null)
                singleTargetUse(m);
            autoTargetUse(getTarget());
        }
        else if (adp() != null && adp().hasPower(MetamagicPower.POWER_ID)) {
            forAllMonstersLiving(mo -> {
                if (canUse(adp(), mo)) {
                    singleTargetUse(mo);
                    autoTargetUse(mo);
                }
            });
            AbstractPower pow = adp().getPower(MetamagicPower.POWER_ID);
            atb(new ReducePowerAction(adp(), adp(), pow, 1));
            if (wasSingleTarget) {
                target = CardTarget.ENEMY;
                wasSingleTarget = false;
            }
        }
        nonTargetUse();
        if (sigil)
            beingDiscarded = false;
    }

    public void singleTargetUse(AbstractMonster m) {}

    public void nonTargetUse() {}

    public void autoTargetUse(AbstractMonster m) {}

    public AbstractMonster getTarget() {return null;}

    @Override
    public void applyPowers() {
        if (adp() != null && adp().hasPower(MetamagicPower.POWER_ID) && target == CardTarget.ENEMY) {
            wasSingleTarget = true;
            target = CardTarget.ALL_ENEMY;
        } else if (wasSingleTarget && adp() != null && !adp().hasPower(MetamagicPower.POWER_ID)) {
            wasSingleTarget = false;
            target = CardTarget.ENEMY;
        }
        int temp = baseDamage;
        AbstractPower pow = adp().getPower(RingingPower.POWER_ID);
        if (pow != null)
            baseDamage += resonance * adp().getPower(RingingPower.POWER_ID).amount;
        super.applyPowers();
        baseDamage = temp;
        isDamageModified = baseDamage != damage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (resonance <= 0 || !adp().hasPower(RingingPower.POWER_ID)) {
            super.calculateCardDamage(mo);
            return;
        }
        int temp = baseDamage;
        AbstractPower pow = adp().getPower(RingingPower.POWER_ID);
        if (pow != null)
            baseDamage += resonance * adp().getPower(RingingPower.POWER_ID).amount;
        super.calculateCardDamage(mo);
        baseDamage = temp;
        isDamageModified = baseDamage != damage;
    }

    @Override
    protected Texture getPortraitImage() {
        if (textureImg.contains("ui/missing.png")) {
            return CardArtRoller.getPortraitTexture(this);
        }
        else
            return super.getPortraitImage();
    }

    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType) {
        String textureString;

        switch (cardType) {
            case ATTACK:
            case POWER:
            case SKILL:
                textureString = makeImagePath("cards/" + cardName + ".png");
                break;
            default:
                textureString = makeImagePath("ui/missing.png");
                break;
        }

        FileHandle h = Gdx.files.internal(textureString);
        if (!h.exists()) {
            textureString = makeImagePath("ui/missing.png");
        }
        return textureString;
    }

    public void resetAttributes() {
        super.resetAttributes();
        secondMagic = baseSecondMagic;
        isSecondMagicModified = false;
        thirdMagic = baseThirdMagic;
        isThirdMagicModified = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedSecondMagic) {
            secondMagic = baseSecondMagic;
            isSecondMagicModified = true;
        }
        if (upgradedThirdMagic) {
            thirdMagic = baseThirdMagic;
            isThirdMagicModified = true;
        }
    }

    protected void upgradeSecondMagic(int upgradeAmount) {
        baseSecondMagic += upgradeAmount;
        secondMagic = baseSecondMagic;
        upgradedSecondMagic = true;
    }

    protected void upgradeThirdMagic(int upgradeAmount) {
        baseThirdMagic += upgradeAmount;
        thirdMagic = baseThirdMagic;
        upgradedThirdMagic = true;
    }

    protected void upgradeCardToPreview() {
        for (AbstractCard q : cardToPreview)
            q.upgrade();
    }

    public abstract void upp();

    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upp();
            if (cardStrings.UPGRADE_DESCRIPTION != null)
                rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    protected String getCustomString() {
        return "";
    }

    @Override
    public void initializeDescription() {
        if (!overrideRawDesc) {
            if (cardStrings == null)
                cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
            if (upgraded && cardStrings.UPGRADE_DESCRIPTION != null)
                rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            else
                rawDescription = cardStrings.DESCRIPTION;
        }

        if (damageModList != null) {
            if (damageModList.contains(ICE))
                rawDescription = rawDescription.replace("!D! ", "!D! " + COLD_STRING + " ");
            if (damageModList.contains(ELDRITCH))
                rawDescription = rawDescription.replace("!D! ", "!D! " + ELDRITCH_STRING + " ");
            if (damageModList.contains(FORCE))
                rawDescription = rawDescription.replace("!D! ", "!D! " + FORCE_STRING + " ");
            if (damageModList.contains(LIGHTNING))
                rawDescription = rawDescription.replace("!D! ", "!D! " + LIGHTNING_STRING + " ");
        }

        rawDescription = rawDescription.replace("!C!", getCustomString());

        if (selfRetain)
            rawDescription = thisCardStrings.EXTENDED_DESCRIPTION[0] + rawDescription;
        if (isInnate)
            rawDescription = thisCardStrings.EXTENDED_DESCRIPTION[11] + rawDescription;
        if (isEthereal)
            rawDescription = thisCardStrings.EXTENDED_DESCRIPTION[10] + rawDescription;
        if (sigil)
            rawDescription = thisCardStrings.EXTENDED_DESCRIPTION[1] + rawDescription;
        if (resonance == 1)
            rawDescription = thisCardStrings.EXTENDED_DESCRIPTION[6].replace("!X!", "") + rawDescription;
        if (resonance > 1)
            rawDescription = thisCardStrings.EXTENDED_DESCRIPTION[6].replace("!X!", " " + resonance) + rawDescription;

        if (exhaust || (damageModList != null && damageModList.contains(ELDRITCH) && adp() != null
                && adp().hasRelic(VialOfBlackBlood.ID)))
            rawDescription = rawDescription + thisCardStrings.EXTENDED_DESCRIPTION[7];

        super.initializeDescription();
    }

    public void update() {
        if (sigil && AbstractDungeon.actionManager.currentAction instanceof DiscardAction
                && hasEnoughEnergy())
            sigilGlowing = true;
        else if (sigilGlowing)
            stopSigilGlowing();
        sigilUpdateGlow();
        super.update();
        updateCardArtRoller();
    }

    private void updateCardArtRoller() {
        if (needsArtRefresh) {
            CardArtRoller.computeCard(this);
        }
        if (!cardToPreview.isEmpty()) {
            if (hb.hovered) {
                if (rotationTimer <= 0F) {
                    rotationTimer = getRotationTimeNeeded();
                    if (previewIndex >= cardToPreview.size())
                        previewIndex = 0;
                    cardsToPreview = cardToPreview.get(previewIndex);
                    if (previewIndex == cardToPreview.size() - 1) {
                        previewIndex = 0;
                    } else {
                        previewIndex++;
                    }
                } else {
                    rotationTimer -= Gdx.graphics.getDeltaTime();
                }
            }
        }
    }

    public String cardArtCopy() {
        return null;
    }

    protected float getRotationTimeNeeded() {
        return 1f;
    }

    public void addModifier(elenum element, boolean tips) {
        if (damageModList.contains(element))
            return;

        damageModList.add(element);
        if (element == ICE)
            DamageModifierManager.addModifier(this, new IceDamage(tips));
        if (element == elenum.FORCE)
            DamageModifierManager.addModifier(this, new ForceDamage(tips));
        if (element == elenum.ELDRITCH)
            DamageModifierManager.addModifier(this, new EldritchDamage(tips));
        if (element == LIGHTNING)
            DamageModifierManager.addModifier(this, new DeathLightningDamage(tips));
        initializeDescription();
    }

    public void addModifier(ArrayList<elenum> elements, boolean tips) {
        for (elenum e : elements)
            addModifier(e, tips);
    }

    public void addModifier(ArrayList<elenum> elements) {
        for (elenum e : elements)
            addModifier(e, true);
    }

    public void addModifier(elenum element) {
        addModifier(element, true);
    }

    public int getDamageForVFX() {
        int amount = damage;
        if (isMultiDamage && multiDamage != null && multiDamage.length > 0) {
            for (int x : multiDamage)
                if (x > amount)
                    amount = x;
        }
        return amount;
    }

    private AbstractGameAction.AttackEffect getAttackEffect() {
        return Wiz.getAttackEffect(getDamageForVFX(), damageModList);
    }

    public void dmg(AbstractMonster m) {
        dmg(m, getAttackEffect(), false);
    }

    public void dmg(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        dmg(m, fx, false);
    }

    public void dmgTop(AbstractMonster m) {
        dmg(m, getAttackEffect(),  true);
    }

    public void dmgTop(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        dmg(m, fx, true);
    }

    public void allDmg() {
        dmg(null, getAttackEffect(), false);
    }

    public void allDmg(AbstractGameAction.AttackEffect fx) {
        dmg(null, fx, false);
    }

    public void allDmgTop() { dmg(null, getAttackEffect(), true); }

    public void allDmgTop(AbstractGameAction.AttackEffect fx) {
        dmg(null, fx, true);
    }

    public void dmg(AbstractMonster m, AbstractGameAction.AttackEffect fx, boolean top) {
        AbstractGameAction action;
        if (m == null)
            action = new AttackAction(multiDamage, fx);
        else {
            DamageInfo info = new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.NORMAL);
            action = new AttackAction(m, info, fx);
        }
        AbstractGameAction curAction = AbstractDungeon.actionManager.currentAction;
        if (AbstractDungeon.actionManager.currentAction != null) {
            ArrayList<AbstractDamageModifier> list = BindingPatches.BoundGameActionFields.actionDelayedDamageMods.get(curAction);
            BindingHelper.bindAction(list, action);
        }
        if (top)
            att(action);
        else
            atb(action);
    }

    public void blck() {
        atb(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    protected void blckTop() {
        att(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    protected void upMagic(int x) {upgradeMagicNumber(x);}

    protected void upMagic2(int x) {upgradeSecondMagic(x);}

    protected void upMagic3(int x) {upgradeThirdMagic(x);}

    public void triggerOnDeath() {}

    @Override
    protected void renderTitle(SpriteBatch sb) {
        initializeTitle();
        Color renderColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");
        boolean useSmallTitleFont = ReflectionHacks.getPrivate(this, AbstractCard.class, "useSmallTitleFont");
        if (isLocked) {
            FontHelper.cardTitleFont.getData().setScale(drawScale);
            FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, LOCKED_STRING, current_x, current_y,
                    0.0F, 175.0F * drawScale * Settings.scale, angle, false, renderColor);
        } else if (!isSeen) {
            FontHelper.cardTitleFont.getData().setScale(drawScale);
            FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, UNKNOWN_STRING, current_x, current_y,
                    0.0F, 175.0F * drawScale * Settings.scale, angle, false, renderColor);
        } else {
            if (!useSmallTitleFont)
                FontHelper.cardTitleFont.getData().setScale(drawScale);
            else
                FontHelper.cardTitleFont.getData().setScale(drawScale * 0.85F);

            if (upgraded) {
                Color color = Settings.GREEN_TEXT_COLOR.cpy();
                color.a = renderColor.a;
                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, name, current_x, current_y,
                        0.0F, 175.0F * drawScale * Settings.scale, angle, false, color);
            } else {
                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, name, current_x, current_y,
                        0.0F, 175.0F * drawScale * Settings.scale, angle, false, renderColor);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (sigil)
            sigilRenderGlow(sb);
        super.render(sb);
    }

    private void sigilUpdateGlow() {
        if (!sigil)
            return;
        if (sigilGlowing) {
            myGlowTimer -= Gdx.graphics.getDeltaTime();
            if (myGlowTimer < 0.0F) {
                myGlowList.add(new CardGlowBorder(this, glowColor));
                myGlowTimer = 0.3F;
            }
        }

        Iterator<CardGlowBorder> i = this.myGlowList.iterator();
        while(i.hasNext()) {
            CardGlowBorder e = i.next();
            e.update();
            if (e.isDone)
                i.remove();
        }
    }

    public void stopSigilGlowing() {
        if (!sigil)
            return;
        sigilGlowing = false;
        glowColor = BLUE_BORDER_GLOW_COLOR;

        CardGlowBorder e;
        for(Iterator<CardGlowBorder> var1 = myGlowList.iterator(); var1.hasNext(); e.duration /= 5.0F)
            e = var1.next();
    }

    private void sigilRenderGlow(SpriteBatch sb) {
        if (sigilGlowing) {
            sb.setBlendFunction(770, 1);
            TextureAtlas.AtlasRegion img;
            switch (this.type) {
                case ATTACK:
                    img = ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
                    break;
                case POWER:
                    img = ImageMaster.CARD_POWER_BG_SILHOUETTE;
                    break;
                default:
                    img = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
            }

            sb.setColor(glowColor);
            sb.draw(img, current_x + img.offsetX - (float) img.originalWidth / 2.0F, current_y + img.offsetY - (float) img.originalWidth / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalWidth / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, drawScale * Settings.scale * 1.04F, drawScale * Settings.scale * 1.03F, angle);
        }

        for (CardGlowBorder cardGlowBorder : myGlowList) {
            ((AbstractGameEffect) cardGlowBorder).render(sb);
        }

        sb.setBlendFunction(770, 771);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractExileCard card = (AbstractExileCard) super.makeStatEquivalentCopy();
        card.sigil = sigil;
        card.selfRetain = selfRetain;
        card.miscTwo = miscTwo;

        card.addModifier(damageModList, true);

        card.initializeDescription();
        return card;
    }

    @Override
    public CardSaveObject onSave() {
        CardSaveObject obj = new CardSaveObject();
        if (damageModList != null) {
            if (this instanceof EnchantedDagger)
                obj.elements.addAll(((EnchantedDagger) this).stableList);
            else
                obj.elements.addAll(damageModList);
        }
        obj.sigil = sigil;
        obj.misc = miscTwo;
        obj.cost = cost;
        return obj;
    }

    @Override
    public void onLoad(CardSaveObject obj) {
        if (damageModList == null)
            damageModList = new ArrayList<>();

        addModifier(obj.elements);
        if (this instanceof EnchantedDagger)
            ((EnchantedDagger) this).stableList.addAll(obj.elements);

        sigil = obj.sigil;
        miscTwo = obj.misc;
        cost = obj.cost;
        costForTurn = obj.cost;

        initializeDescription();
    }

    @Override
    public Type savedType() {
        return new TypeToken<CardSaveObject>(){}.getType();
    }
}