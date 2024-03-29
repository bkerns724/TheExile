package theExile;

import basemod.*;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theExile.cards.AbstractExileCard;
import theExile.cards.cardvars.SecondMagicNumber;
import theExile.cards.cardvars.ThirdMagicNumber;
import theExile.events.ClericsRequest;
import theExile.events.ResearchCenter;
import theExile.events.VoidSpirits;
import theExile.icons.*;
import theExile.orbs.CrazyPanda;
import theExile.potions.*;
import theExile.relics.AbstractExileRelic;
import theExile.util.ClickableForPower;
import theExile.util.ClickyFtue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

import static theExile.TheExile.Enums.THE_EXILE;
import static theExile.util.Wiz.*;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ExileMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        AddAudioSubscriber,
        OnStartBattleSubscriber,
        OnPlayerTurnStartSubscriber,
        PostBattleSubscriber,
        PostUpdateSubscriber,
        PostInitializeSubscriber {
    public static final String SETTINGS_FILE = "ExileModSettings";

    public static final String modID = "exilemod";
    public static final String RESOURCES_PRE = "exilemodResources/";

    public static Logger logger = LogManager.getLogger(ExileMod.class.getName());

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static final String EXTENDED_CUT_SETTING = "ExtendedCutSetting";
    public static final String TUTORIAL_SETTING = "TutorialSetting";
    public static final String SETTINGS_STRINGS = "Settings";

    private static SpireConfig modConfig = null;

    public static final String SHOULDER1 = RESOURCES_PRE + "images/char/mainChar/shoulder.png";
    public static final String SHOULDER2 = RESOURCES_PRE + "images/char/mainChar/shoulder2.png";
    public static final String CORPSE = RESOURCES_PRE + "images/char/mainChar/corpse.png";
    private static final String ATTACK_S_ART = RESOURCES_PRE + "images/512/attack.png";
    private static final String SKILL_S_ART = RESOURCES_PRE + "images/512/skill.png";
    private static final String POWER_S_ART = RESOURCES_PRE + "images/512/power.png";
    private static final String CARD_ENERGY_S = RESOURCES_PRE + "images/512/energy.png";
    private static final String TEXT_ENERGY = RESOURCES_PRE + "images/512/text_energy.png";
    private static final String ATTACK_L_ART = RESOURCES_PRE + "images/1024/attack.png";
    private static final String SKILL_L_ART = RESOURCES_PRE + "images/1024/skill.png";
    private static final String POWER_L_ART = RESOURCES_PRE + "images/1024/power.png";
    private static final String CARD_ENERGY_L = RESOURCES_PRE + "images/1024/energy.png";
    private static final String CHARSELECT_BUTTON = RESOURCES_PRE + "images/charSelect/charButton.png";
    private static final String CHARSELECT_PORTRAIT = RESOURCES_PRE + "images/charSelect/charBG.png";

    public static final String LIGHTNING_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Lightning.png";
    public static final String WATER_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Water.png";
    public static final String BLOOD_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Blood.png";
    public static final String ACID_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Acid.png";
    public static final String ACID_M_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Acid_M.png";
    public static final String ICE_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Ice.png";
    public static final String ICE_M_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Ice_M.png";
    public static final String DEMON_FIRE_EFFECT_FILE = RESOURCES_PRE + "images/vfx/DemonFire.png";
    public static final String FORCE_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Force.png";
    public static final String FORCE_M_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Force_M.png";
    public static final String FORCE_L_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Force_L.png";
    public static final String DARK_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Dark.png";
    public static final String DARK_M_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Dark_M.png";
    public static final String DARK_L_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Dark_L.png";
    public static final String DARK_L2_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Dark_L2.png";
    public static final String RESONANT_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Resonant.png";
    public static final String RESONANT_M_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Resonant_M.png";
    public static final String RESONANT_L_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Resonant_L.png";
    public static final String BEE_EFFECT_FILE = RESOURCES_PRE + "images/vfx/BeeAttack.png";
    public static final String BELL_EFFECT_FILE = RESOURCES_PRE + "images/vfx/BellAttack.png";

    public static final String ELEPHANT_EFFECT_FILE = RESOURCES_PRE + "images/vfx/Elephant/Elephant.png";
    public static final String ELEPHANT_HEAD_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantHead.png";
    public static final String ELEPHANT_LEG_ONE_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantLegOne.png";
    public static final String ELEPHANT_LEG_TWO_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantLegTwo.png";
    public static final String ELEPHANT_LEG_THREE_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantLegThree.png";
    public static final String ELEPHANT_LEG_FOUR_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantLegFour.png";
    public static final String ELEPHANT_TAIL_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantTail.png";
    public static final String ELEPHANT_RUMP_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantRump.png";
    public static final String ELEPHANT_TRUNK_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantTrunk.png";
    public static final String ELEPHANT_EAR_TOP_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantEarTop.png";
    public static final String ELEPHANT_EAR_BOTTOM_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantEarBottom.png";
    public static final String ELEPHANT_TOP_TORSO_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantTopTorso.png";
    public static final String ELEPHANT_LEFT_TORSO_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantBottomLeftTorso.png";
    public static final String ELEPHANT_RIGHT_TORSO_FILE = RESOURCES_PRE + "images/vfx/Elephant/ElephantBottomRightTorso.png";

    public static final String FTUE_IMG = RESOURCES_PRE + "images/ui/Ftue.png";

    public static final String COLD_KEY = makeID("Cold");
    private static final String COLD_OGG = RESOURCES_PRE + "audio/Cold.ogg";
    public static final String COLD_M_KEY = makeID("Cold_M");
    private static final String COLD_M_OGG = RESOURCES_PRE + "audio/Cold_M.ogg";
    public static final String PEW_KEY = makeID("Pew");
    private static final String PEW_OGG = RESOURCES_PRE + "audio/Pew.ogg";
    public static final String JINX_KEY = makeID("Jinx");
    private static final String JINX_OGG = RESOURCES_PRE + "audio/Jinx.ogg";
    public static final String BEES_KEY = makeID("SwarmOfBees");
    private static final String BEES_OGG = RESOURCES_PRE + "audio/SwarmOfBees.ogg";
    public static final String ELEPHANT_KEY = makeID("Elephant");
    private static final String ELEPHANT_OGG = RESOURCES_PRE + "audio/Elephant.ogg";

    private static final String BADGE_IMG = RESOURCES_PRE + "images/Badge.png";
    private static final String[] REGISTRATION_STRINGS = {
            "The Exile", "Bryan", "This mod adds a new character, the Exile."
    };

    private static ModLabeledToggleButton alwaysExtendedCutButton = null;
    private static ModLabeledToggleButton onceExtendedCutButton = null;
    private static ModLabeledToggleButton neverExtendedCutButton = null;

    private static ModLabeledToggleButton alwaysTutorialButton = null;
    private static ModLabeledToggleButton onceTutorialButton = null;
    private static ModLabeledToggleButton neverTutorialButton = null;

    public static float time = 0.0f;
    public static final Color purpleColor = new Color(0.45f, 0f, 0.6f, 1.0f);
    public static final Color darkColor = new Color(0.25f, 0.25f, 0.25f, 1.0f);
    public static final Color EXILE_EYE_COLOR = purpleColor.cpy();

    public static final ArrayList<CrazyPanda> pandaList = new ArrayList<>();

    public static int attacksThisTurn = 0;
    private static final int ATTACK_THRESHOLD = 3;
    public static int CHARGE_AMOUNT = 3;


    public ExileMod() {
        BaseMod.addColor(TheExile.Enums.EXILE_BROWN_COLOR, EXILE_EYE_COLOR, EXILE_EYE_COLOR, EXILE_EYE_COLOR,
                EXILE_EYE_COLOR, EXILE_EYE_COLOR, EXILE_EYE_COLOR, EXILE_EYE_COLOR,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
    }

    public static String makePath(String resourcePath) {
        return RESOURCES_PRE + "" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return RESOURCES_PRE + "images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return RESOURCES_PRE + "images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return RESOURCES_PRE + "images/powers/" + resourcePath;
    }

    public static class Enums {
        @SpireEnum
        public static AbstractGameAction.AttackEffect WATER;
        @SpireEnum
        public static AbstractGameAction.AttackEffect BLOOD;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ACID;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ACID_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ACID_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect FORCE;
        @SpireEnum
        public static AbstractGameAction.AttackEffect FORCE_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect FORCE_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ICE;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ICE_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ICE_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ELDRITCH;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ELDRITCH_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect ELDRITCH_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect FIRE_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect FIRE_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect DARK_WAVE;
        @SpireEnum
        public static AbstractGameAction.AttackEffect DARK_WAVE_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect DARK_WAVE_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect RESONANT;
        @SpireEnum
        public static AbstractGameAction.AttackEffect RESONANT_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect RESONANT_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect BLUNT_MASSIVE;
        @SpireEnum
        public static AbstractGameAction.AttackEffect SLASH_MASSIVE;
        @SpireEnum
        public static AbstractGameAction.AttackEffect LIGHTNING_S;
        @SpireEnum
        public static AbstractGameAction.AttackEffect LIGHTNING_M;
        @SpireEnum
        public static AbstractGameAction.AttackEffect LIGHTNING_L;
        @SpireEnum
        public static AbstractGameAction.AttackEffect BEE;
        @SpireEnum
        public static AbstractGameAction.AttackEffect BELL;
        @SpireEnum
        public static AbstractCard.CardRarity UNIQUE;
        @SpireEnum
        public static NeowReward.NeowRewardType UNIQUE_CARD_REWARD;
        @SpireEnum
        public static AbstractPotion.PotionRarity EVENT;
        @SpireEnum
        public static FtueTip.TipType CLICKY_IMAGE;
        @SpireEnum
        public static AbstractCard.CardTarget AUTOAIM_ENEMY;
        @SpireEnum
        public static AbstractCard.CardRarity CARD_CHOICE;
    }

    public static String makeCardPath(String resourcePath) {
        return RESOURCES_PRE + "images/cards/" + resourcePath;
    }

    public static void initialize() {
        BaseMod.subscribe(new ExileMod());

        try {
            Properties defaults = new Properties();
            defaults.put(EXTENDED_CUT_SETTING, Integer.toString(1));
            defaults.put(TUTORIAL_SETTING, Integer.toString(1));
            modConfig = new SpireConfig(modID, "Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum extendedSetting {
        ALWAYS,
        ONCE,
        NEVER
    }

    private enum tutorialSetting {
        ALWAYS,
        ONCE,
        NEVER
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new TheExile(TheExile.characterStrings.NAMES[1], THE_EXILE),
                CHARSELECT_BUTTON, CHARSELECT_PORTRAIT, THE_EXILE);
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(AbstractExileRelic.class)
                .any(AbstractExileRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());

        CustomIconHelper.addCustomIcon(Force.get());
        CustomIconHelper.addCustomIcon(Ice.get());
        CustomIconHelper.addCustomIcon(Eldritch.get());
        CustomIconHelper.addCustomIcon(Lightning.get());
        CustomIconHelper.addCustomIcon(Question.get());

        new AutoAdd(modID)
                .packageFilter(AbstractExileCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, RESOURCES_PRE + "localization/eng/Cardstrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, RESOURCES_PRE + "localization/eng/Relicstrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, RESOURCES_PRE + "localization/eng/Charstrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, RESOURCES_PRE + "localization/eng/Powerstrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, RESOURCES_PRE + "localization/eng/Potionstrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, RESOURCES_PRE + "localization/eng/UIstrings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, RESOURCES_PRE + "localization/eng/Orbstrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, RESOURCES_PRE + "localization/eng/Eventstrings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(RESOURCES_PRE + "localization/eng/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        setupSettings();
        addPotions();
        addEvents();
    }

    private void setupSettings () {
        ModPanel settingsPanel = new ModPanel();
        String[] settingStrings = CardCrawlGame.languagePack.getUIString(makeID(SETTINGS_STRINGS)).TEXT;

        float currentYposition = 740f;

        ModLabel extendedLabel = new ModLabel(settingStrings[0], 375.0f,
                currentYposition, settingsPanel, label -> {});
        settingsPanel.addUIElement(extendedLabel);
        currentYposition -= 60.0f;

        alwaysExtendedCutButton = new ModLabeledToggleButton(settingStrings[1], 410.0f,
                currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                (modConfig.getInt(EXTENDED_CUT_SETTING) == 0), settingsPanel, label -> {}, button -> {
            if (modConfig != null && button.enabled) {
                modConfig.setInt(EXTENDED_CUT_SETTING, 0);
                saveConfig();
                if (onceExtendedCutButton != null)
                    onceExtendedCutButton.toggle.enabled = false;
                if (neverExtendedCutButton != null)
                    neverExtendedCutButton.toggle.enabled = false;
            }
        });
        settingsPanel.addUIElement(alwaysExtendedCutButton);
        currentYposition -= 60.0f;

        onceExtendedCutButton = new ModLabeledToggleButton(settingStrings[2], 410.0f,
                currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                (modConfig.getInt(EXTENDED_CUT_SETTING) == 1), settingsPanel, label -> {}, button -> {
            if (modConfig != null && button.enabled) {
                modConfig.setInt(EXTENDED_CUT_SETTING, 1);
                saveConfig();
                if (alwaysExtendedCutButton != null)
                    alwaysExtendedCutButton.toggle.enabled = false;
                if (neverExtendedCutButton != null)
                    neverExtendedCutButton.toggle.enabled = false;
            }
        });
        settingsPanel.addUIElement(onceExtendedCutButton);
        currentYposition -= 60.0f;

        neverExtendedCutButton = new ModLabeledToggleButton(settingStrings[3], 410.0f,
                currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                (modConfig.getInt(EXTENDED_CUT_SETTING) == 2), settingsPanel, label -> {}, button -> {
            if (modConfig != null && button.enabled) {
                modConfig.setInt(EXTENDED_CUT_SETTING, 2);
                saveConfig();
                if (onceExtendedCutButton != null)
                    onceExtendedCutButton.toggle.enabled = false;
                if (alwaysExtendedCutButton != null)
                    alwaysExtendedCutButton.toggle.enabled = false;
            }
        });
        settingsPanel.addUIElement(neverExtendedCutButton);
        currentYposition -= 60.0f;

        ModLabel tutorialLabel = new ModLabel(settingStrings[4], 375.0f,
                currentYposition, settingsPanel, label -> {});
        settingsPanel.addUIElement(tutorialLabel);
        currentYposition -= 60.0f;

        alwaysTutorialButton = new ModLabeledToggleButton(settingStrings[1], 410.0f,
                currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                (modConfig.getInt(TUTORIAL_SETTING) == 0), settingsPanel, label -> {}, button -> {
            if (modConfig != null && button.enabled) {
                modConfig.setInt(TUTORIAL_SETTING, 0);
                saveConfig();
                if (onceTutorialButton != null)
                    onceTutorialButton.toggle.enabled = false;
                if (neverTutorialButton != null)
                    neverTutorialButton.toggle.enabled = false;
            }
        });
        settingsPanel.addUIElement(alwaysTutorialButton);
        currentYposition -= 60.0f;

        onceTutorialButton = new ModLabeledToggleButton(settingStrings[2], 410.0f,
                currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                (modConfig.getInt(TUTORIAL_SETTING) == 1), settingsPanel, label -> {}, button -> {
            if (modConfig != null && button.enabled) {
                modConfig.setInt(TUTORIAL_SETTING, 1);
                saveConfig();
                if (alwaysTutorialButton != null)
                    alwaysTutorialButton.toggle.enabled = false;
                if (neverTutorialButton != null)
                    neverTutorialButton.toggle.enabled = false;
            }
        });
        settingsPanel.addUIElement(onceTutorialButton);
        currentYposition -= 60.0f;

        neverTutorialButton = new ModLabeledToggleButton(settingStrings[3], 410.0f,
                currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                (modConfig.getInt(TUTORIAL_SETTING) == 2), settingsPanel, label -> {}, button -> {
            if (modConfig != null && button.enabled) {
                modConfig.setInt(TUTORIAL_SETTING, 2);
                saveConfig();
                if (onceTutorialButton != null)
                    onceTutorialButton.toggle.enabled = false;
                if (alwaysTutorialButton != null)
                    alwaysTutorialButton.toggle.enabled = false;
            }
        });
        settingsPanel.addUIElement(neverTutorialButton);

        Texture badgeTexture = new Texture(BADGE_IMG);
        BaseMod.registerModBadge(badgeTexture, REGISTRATION_STRINGS[0], REGISTRATION_STRINGS[1], REGISTRATION_STRINGS[2],
                settingsPanel);
    }

    private static void saveConfig() {
        try {
            modConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isExtendedCut() {
        if (modConfig == null)
            return false;
        int x = modConfig.getInt(EXTENDED_CUT_SETTING);
        if (x == 2)
            return false;
        if (x == 0)
            return true;
        if (onceExtendedCutButton != null && neverExtendedCutButton != null) {
            onceExtendedCutButton.toggle.enabled = false;
            neverExtendedCutButton.toggle.enabled = true;
            modConfig.setInt(EXTENDED_CUT_SETTING, 2);
            saveConfig();
        }
        return true;
    }

    public static boolean needTutorial() {
        if (modConfig == null)
            return false;
        int x = modConfig.getInt(TUTORIAL_SETTING);
        if (x == 2)
            return false;
        if (x == 0)
            return true;
        if (onceTutorialButton != null && neverTutorialButton != null) {
            onceTutorialButton.toggle.enabled = false;
            neverTutorialButton.toggle.enabled = true;
            modConfig.setInt(TUTORIAL_SETTING, 2);
            saveConfig();
        }
        return true;
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(COLD_KEY, COLD_OGG);
        BaseMod.addAudio(COLD_M_KEY, COLD_M_OGG);
        BaseMod.addAudio(PEW_KEY, PEW_OGG);
        BaseMod.addAudio(JINX_KEY, JINX_OGG);
        BaseMod.addAudio(BEES_KEY, BEES_OGG);
        BaseMod.addAudio(ELEPHANT_KEY, ELEPHANT_OGG);
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if (room.event instanceof VoidSpirits) {
            if (AbstractDungeon.ascensionLevel < 15)
                forAllMonstersLiving(m -> atb(new IncreaseMaxHpAction(m, VoidSpirits.HEALTH_BUFF_A0, true)));
            else
                forAllMonstersLiving(m -> atb(new IncreaseMaxHpAction(m, VoidSpirits.HEALTH_BUFF_A15, true)));
            return;
        }
        if (adp().chosenClass == THE_EXILE && AbstractDungeon.floorNum == 1) {
            if (needTutorial())
                AbstractDungeon.ftue = new ClickyFtue("whee", "boop", Settings.WIDTH / 2f, Settings.HEIGHT / 2f);
        }
        // shouldn't be necessary
        pandaList.clear();
    }

    @Override
    public void receivePostUpdate() {
        time += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        ClickableForPower.getClickableList().clear();
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        attacksThisTurn = 0;
    }

    private static void addEvents() {
        BaseMod.addEvent(ClericsRequest.getParams());
        BaseMod.addEvent(VoidSpirits.getParams());
        BaseMod.addEvent(ResearchCenter.getParams());
    }

    private static void addPotions() {
        BaseMod.addPotion(CursedBrew.class, Color.PURPLE.cpy(), Color.BLACK.cpy(), null, CursedBrew.POTION_ID, THE_EXILE);
        BaseMod.addPotion(StoneskinPotion.class, Color.BROWN.cpy(), null, null, StoneskinPotion.POTION_ID, THE_EXILE);
        BaseMod.addPotion(VialOfAcid.class, Color.GREEN.cpy(), null, null, VialOfAcid.POTION_ID, THE_EXILE);
        BaseMod.addPotion(ElixirOfFalseHealth.class, Color.YELLOW.cpy(), Color.GOLD.cpy(), null, ElixirOfFalseHealth.POTION_ID, THE_EXILE);
        BaseMod.addPotion(UnicornBlood.class, Color.WHITE.cpy(), null, null, UnicornBlood.POTION_ID, THE_EXILE);
        BaseMod.addPotion(SiphoningPoison.class, Color.BLACK.cpy(), null, null, SiphoningPoison.POTION_ID, THE_EXILE);
        BaseMod.addPotion(SteelhidePotion.class, Color.GRAY.cpy(), null, null, SteelhidePotion.POTION_ID, THE_EXILE);
    }
}
