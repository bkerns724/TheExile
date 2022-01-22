package theArcanist.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theArcanist.ArcanistMod;
import theArcanist.util.TexLoader;

public class WaterEffectPatch {
    @SpirePatch(
            clz = FlashAtkImgEffect.class,
            method = "loadImage"
    )
    public static class WaterPatch {
        @SpirePrefixPatch
        public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(FlashAtkImgEffect __instance) {
            AbstractGameAction.AttackEffect effect = ReflectionHacks.getPrivate(__instance, FlashAtkImgEffect.class, "effect");
            if (effect == ArcanistMod.Enums.WATER) {
                Texture texture = TexLoader.getTexture(ArcanistMod.WATER_EFFECT_FILE);
                TextureAtlas.AtlasRegion atReg = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
                return SpireReturn.Return(atReg);
            }
            if (effect == ArcanistMod.Enums.BLOOD) {
                Texture texture = TexLoader.getTexture(ArcanistMod.BLOOD_EFFECT_FILE);
                TextureAtlas.AtlasRegion atReg = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
                return SpireReturn.Return(atReg);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = FlashAtkImgEffect.class,
            method = "playSound"
    )
    public static class WaterSoundPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(FlashAtkImgEffect __instance, AbstractGameAction.AttackEffect effect) {
            if (effect == ArcanistMod.Enums.WATER) {
                CardCrawlGame.sound.play("ATTACK_POISON");
                return SpireReturn.Return();
            }
            if (effect == ArcanistMod.Enums.BLOOD) {
                CardCrawlGame.sound.play("ATTACK_POISON");
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}