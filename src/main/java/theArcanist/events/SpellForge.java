package theArcanist.events;

import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theArcanist.ArcanistMod;
import theArcanist.TheArcanist;
import theArcanist.cards.AbstractArcanistCard;
import theArcanist.cards.VoidCurse;
import theArcanist.damagemods.DarkDamage;
import theArcanist.damagemods.ForceDamage;
import theArcanist.damagemods.IceDamage;
import theArcanist.damagemods.SoulFireDamage;
import theArcanist.patches.TipsInDialogPatch;

import java.util.ArrayList;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.adp;

public class SpellForge extends AbstractArcanistEvent {
    public static final String ID = makeID(SpellForge.class.getSimpleName());
    private static final EventStrings eventStrings;
    private static final String IMAGE_PATH;
    private static final EventUtils.EventType TYPE = EventUtils.EventType.SHRINE;
    private static final int GOLD_COST_A0 = 50;
    private static final int GOLD_COST_A15 = 65;
    private static final int GOLD_GAIN_A0 = 250;
    private static final int GOLD_GAIN_A15 = 200;
    private static final int GOLD_INCREASE = 25;
    private boolean isRemoving = false;
    private AbstractArcanistCard.elenum element;

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        IMAGE_PATH = ArcanistMod.makeImagePath("events/" + SpellForge.class.getSimpleName() + ".jpg");
    }

    public static AddEventParams getParams() {
        AddEventParams params = new AddEventParams();
        params.eventClass = SpellForge.class;
        params.eventID = ID;
        params.eventType = TYPE;
        params.dungeonIDs = new ArrayList<>();
        params.dungeonIDs.add(TheCity.ID);
        params.playerClass = TheArcanist.Enums.THE_ARCANIST;
        params.bonusCondition = () -> ((adp().gold >= getGoldCost() && checkForNonElementalCard())
                || checkForElementalCard());
        return params;
    }

    public SpellForge() {
        super(eventStrings, IMAGE_PATH, getGoldCost(), getGoldGain());

        setChoices();
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            if (isRemoving) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.player.masterDeck.removeCard(c);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new VoidCurse(), c.current_x, c.current_y));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                adp().gainGold(getGoldGain());
                imageEventText.updateBodyText(descriptions[5]);
            }
            else if (element != null) {
                AbstractArcanistCard c = (AbstractArcanistCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                c.addModifier(element);
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),
                        Settings.WIDTH/2.0f, Settings.HEIGHT/2.0f));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH/2.0f, (float)Settings.HEIGHT/2.0F));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                adp().loseGold(amount);
                amount += GOLD_INCREASE;
                imageEventText.updateBodyText(descriptions[4]);
            }
            screen = CUR_SCREEN.INTRO;
            setChoices();
        }
    }

    protected void buttonEffect(int buttonPressed) {
        if (screen == CUR_SCREEN.INTRO) {
            switch (buttonPressed) {
                case 0:
                    isRemoving = false;
                    screen = CUR_SCREEN.ELE_CHOICE;
                    imageEventText.updateBodyText(descriptions[1]);
                    imageEventText.clearAllDialogs();

                    imageEventText.setDialogOption(options[5]);
                    LargeDialogOptionButton but = imageEventText.optionList.get(0);
                    TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, IceDamage.getPowerTips());
                    imageEventText.setDialogOption(options[6]);
                    but = imageEventText.optionList.get(1);
                    TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, ForceDamage.getPowerTips());
                    imageEventText.setDialogOption(options[7]);
                    but = imageEventText.optionList.get(2);
                    TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, DarkDamage.getPowerTips());
                    imageEventText.setDialogOption(options[8]);
                    but = imageEventText.optionList.get(3);
                    TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, SoulFireDamage.getPowerTips());
                    break;
                case 1:
                    isRemoving = true;
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c :  CardGroup.getGroupWithoutBottledCards(adp().masterDeck).group) {
                        if (c instanceof AbstractArcanistCard && !((AbstractArcanistCard) c).damageModList.isEmpty())
                            group.addToTop(c);
                    }
                    AbstractDungeon.gridSelectScreen.open(group, 1, descriptions[3], false,
                            false, false, false);
                    break;
                case 2:
                    openMap();
                    break;
            }
        } else if (screen == CUR_SCREEN.ELE_CHOICE) {
            CardGroup eleGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (buttonPressed == 0)
                element = AbstractArcanistCard.elenum.ICE;
            else if (buttonPressed == 1)
                element = AbstractArcanistCard.elenum.FORCE;
            else if (buttonPressed == 2)
                element = AbstractArcanistCard.elenum.DARK;
            else
                element = AbstractArcanistCard.elenum.FIRE;
            for (LargeDialogOptionButton x : imageEventText.optionList)
                TipsInDialogPatch.ButtonPreviewField.previewTips.set(x, null);
            for (AbstractCard c :  CardGroup.getGroupWithoutBottledCards(adp().masterDeck).group)
                if (c instanceof AbstractArcanistCard && ((AbstractArcanistCard) c).damageModList.isEmpty()
                        && c.type == AbstractCard.CardType.ATTACK)
                    eleGroup.addToTop(c);
            AbstractDungeon.gridSelectScreen.open(eleGroup, 1, descriptions[2], false);
        }
    }

    private enum CUR_SCREEN {
        INTRO,
        ELE_CHOICE
    }

    private static int getGoldCost() {
        if (AbstractDungeon.ascensionLevel < 15)
            return GOLD_COST_A0;
        return GOLD_COST_A15;
    }

    private static int getGoldGain() {
        if (AbstractDungeon.ascensionLevel < 15)
            return GOLD_GAIN_A0;
        return GOLD_GAIN_A15;
    }

    private static boolean checkForElementalCard() {
        for (AbstractCard card : CardGroup.getGroupWithoutBottledCards(adp().masterDeck).group)
            if (card instanceof AbstractArcanistCard && !((AbstractArcanistCard) card).damageModList.isEmpty())
                return true;
        return false;
    }

    private static boolean checkForNonElementalCard() {
        for (AbstractCard card : CardGroup.getGroupWithoutBottledCards(adp().masterDeck).group)
            if (card instanceof AbstractArcanistCard && ((AbstractArcanistCard) card).damageModList.isEmpty()
                    && card.type == AbstractCard.CardType.ATTACK)
                return true;
        return false;
    }

    private void setChoices() {
        imageEventText.clearAllDialogs();
        VoidCurse curse = new VoidCurse();
        if (adp().gold >= amount && checkForNonElementalCard())
            imageEventText.setDialogOption(options[0]);
        else
            imageEventText.setDialogOption(options[1], true);
        if (checkForElementalCard())
            imageEventText.setDialogOption(options[2].replace("!CardString!",
                    FontHelper.colorString(curse.name, "r")), curse);
        else
            imageEventText.setDialogOption(options[3], true);
        imageEventText.setDialogOption(options[4]);
    }
}