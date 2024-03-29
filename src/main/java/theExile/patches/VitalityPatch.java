package theExile.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theExile.powers.ResolvePower;

import static theExile.util.Wiz.*;

public class VitalityPatch {
    @SpirePatch2(
            clz = AbstractCard.class,
            method = "triggerOnManualDiscard"
    )
    public static class Vitality {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            if (adp() != null && __instance.type == AbstractCard.CardType.STATUS) {
                ResolvePower pow = (ResolvePower) adp().getPower(ResolvePower.POWER_ID);
                if (pow == null)
                    return;
                pow.flash();
                att(new ExhaustSpecificCardAction(__instance, adp().discardPile));
                att(new ExhaustSpecificCardAction(__instance, adp().drawPile));
                att(new ExhaustSpecificCardAction(__instance, adp().hand));
                att(new DrawCardAction(pow.amount));
            }
        }
    }
}
