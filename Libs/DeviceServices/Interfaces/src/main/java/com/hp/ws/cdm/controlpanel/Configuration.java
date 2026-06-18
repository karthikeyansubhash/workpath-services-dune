
package com.hp.ws.cdm.controlpanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class Configuration {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * The brightness level expressed as a range
     * 
     */
    @SerializedName("brightness")
    @Expose
    private Integer brightness;
    /**
     * The speaker volume level for control panel sounds in percentage form where 0 is off and 100 is full volume
     * 
     */
    @SerializedName("speakerVolume")
    @Expose
    private Integer speakerVolume;
    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    @SerializedName("currentLanguage")
    @Expose
    private Configuration.Language currentLanguage;
    /**
     * The set of possible keyboard layouts
     * 
     */
    @SerializedName("defaultKeyboardLayout")
    @Expose
    private Configuration.KeyboardLayout defaultKeyboardLayout;
    /**
     * The set of possible keyboard layouts
     * 
     */
    @SerializedName("currentKeyboardLayout")
    @Expose
    private Configuration.KeyboardLayout currentKeyboardLayout;
    /**
     * The mode for reset after job start
     * 
     */
    @SerializedName("restartAfterJobStart")
    @Expose
    private Configuration.ResetAfterJobStartMode restartAfterJobStart;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("showCustomSignInMessage")
    @Expose
    private Property.FeatureEnabled showCustomSignInMessage;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("customSignInMessage")
    @Expose
    private Property.FeatureEnabled customSignInMessage;
    /**
     * The set of possible themes for various experience
     * 
     */
    @SerializedName("theme")
    @Expose
    private Configuration.Theme theme;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("autoContinue")
    @Expose
    private Property.FeatureEnabled autoContinue;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("displayAlternateKeyboards")
    @Expose
    private Property.FeatureEnabled displayAlternateKeyboards;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("languageSelectionEnabled")
    @Expose
    private Property.FeatureEnabled languageSelectionEnabled;
    /**
     * A list of all language to keyboard layout maps.  There will be an entry in this list for every language supported by the device.
     * 
     */
    @SerializedName("languageDefaultKeyboardMappings")
    @Expose
    private List<LanguageDefaultKeyboardMapping> languageDefaultKeyboardMappings = new ArrayList<LanguageDefaultKeyboardMapping>();
    /**
     * A list of all keyboard layouts supported by the device and whether or not they should be displayed in the alternate keyboard layout list.
     * 
     */
    @SerializedName("alternateKeyboards")
    @Expose
    private List<AlternateKeyboard> alternateKeyboards = new ArrayList<AlternateKeyboard>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("homescreenCustomization")
    @Expose
    private Property.FeatureEnabled homescreenCustomization;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("iterativeSound")
    @Expose
    private Property.FeatureEnabled iterativeSound;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("resetMenuEnabled")
    @Expose
    private Property.FeatureEnabled resetMenuEnabled;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * The brightness level expressed as a range
     * 
     */
    public Integer getBrightness() {
        return brightness;
    }

    /**
     * The brightness level expressed as a range
     * 
     */
    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    /**
     * The speaker volume level for control panel sounds in percentage form where 0 is off and 100 is full volume
     * 
     */
    public Integer getSpeakerVolume() {
        return speakerVolume;
    }

    /**
     * The speaker volume level for control panel sounds in percentage form where 0 is off and 100 is full volume
     * 
     */
    public void setSpeakerVolume(Integer speakerVolume) {
        this.speakerVolume = speakerVolume;
    }

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public Configuration.Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public void setCurrentLanguage(Configuration.Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    /**
     * The set of possible keyboard layouts
     * 
     */
    public Configuration.KeyboardLayout getDefaultKeyboardLayout() {
        return defaultKeyboardLayout;
    }

    /**
     * The set of possible keyboard layouts
     * 
     */
    public void setDefaultKeyboardLayout(Configuration.KeyboardLayout defaultKeyboardLayout) {
        this.defaultKeyboardLayout = defaultKeyboardLayout;
    }

    /**
     * The set of possible keyboard layouts
     * 
     */
    public Configuration.KeyboardLayout getCurrentKeyboardLayout() {
        return currentKeyboardLayout;
    }

    /**
     * The set of possible keyboard layouts
     * 
     */
    public void setCurrentKeyboardLayout(Configuration.KeyboardLayout currentKeyboardLayout) {
        this.currentKeyboardLayout = currentKeyboardLayout;
    }

    /**
     * The mode for reset after job start
     * 
     */
    public Configuration.ResetAfterJobStartMode getRestartAfterJobStart() {
        return restartAfterJobStart;
    }

    /**
     * The mode for reset after job start
     * 
     */
    public void setRestartAfterJobStart(Configuration.ResetAfterJobStartMode restartAfterJobStart) {
        this.restartAfterJobStart = restartAfterJobStart;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getShowCustomSignInMessage() {
        return showCustomSignInMessage;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setShowCustomSignInMessage(Property.FeatureEnabled showCustomSignInMessage) {
        this.showCustomSignInMessage = showCustomSignInMessage;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCustomSignInMessage() {
        return customSignInMessage;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCustomSignInMessage(Property.FeatureEnabled customSignInMessage) {
        this.customSignInMessage = customSignInMessage;
    }

    /**
     * The set of possible themes for various experience
     * 
     */
    public Configuration.Theme getTheme() {
        return theme;
    }

    /**
     * The set of possible themes for various experience
     * 
     */
    public void setTheme(Configuration.Theme theme) {
        this.theme = theme;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAutoContinue() {
        return autoContinue;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAutoContinue(Property.FeatureEnabled autoContinue) {
        this.autoContinue = autoContinue;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDisplayAlternateKeyboards() {
        return displayAlternateKeyboards;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDisplayAlternateKeyboards(Property.FeatureEnabled displayAlternateKeyboards) {
        this.displayAlternateKeyboards = displayAlternateKeyboards;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getLanguageSelectionEnabled() {
        return languageSelectionEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setLanguageSelectionEnabled(Property.FeatureEnabled languageSelectionEnabled) {
        this.languageSelectionEnabled = languageSelectionEnabled;
    }

    /**
     * A list of all language to keyboard layout maps.  There will be an entry in this list for every language supported by the device.
     * 
     */
    public List<LanguageDefaultKeyboardMapping> getLanguageDefaultKeyboardMappings() {
        return languageDefaultKeyboardMappings;
    }

    /**
     * A list of all language to keyboard layout maps.  There will be an entry in this list for every language supported by the device.
     * 
     */
    public void setLanguageDefaultKeyboardMappings(List<LanguageDefaultKeyboardMapping> languageDefaultKeyboardMappings) {
        this.languageDefaultKeyboardMappings = languageDefaultKeyboardMappings;
    }

    /**
     * A list of all keyboard layouts supported by the device and whether or not they should be displayed in the alternate keyboard layout list.
     * 
     */
    public List<AlternateKeyboard> getAlternateKeyboards() {
        return alternateKeyboards;
    }

    /**
     * A list of all keyboard layouts supported by the device and whether or not they should be displayed in the alternate keyboard layout list.
     * 
     */
    public void setAlternateKeyboards(List<AlternateKeyboard> alternateKeyboards) {
        this.alternateKeyboards = alternateKeyboards;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getHomescreenCustomization() {
        return homescreenCustomization;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setHomescreenCustomization(Property.FeatureEnabled homescreenCustomization) {
        this.homescreenCustomization = homescreenCustomization;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIterativeSound() {
        return iterativeSound;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIterativeSound(Property.FeatureEnabled iterativeSound) {
        this.iterativeSound = iterativeSound;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getResetMenuEnabled() {
        return resetMenuEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setResetMenuEnabled(Property.FeatureEnabled resetMenuEnabled) {
        this.resetMenuEnabled = resetMenuEnabled;
    }


    /**
     * The set of possible keyboard layouts
     * 
     */
    public enum KeyboardLayout {

        @SerializedName("qwerty")
        QWERTY("qwerty"),
        @SerializedName("azerty")
        AZERTY("azerty"),
        @SerializedName("qwertz")
        QWERTZ("qwertz"),
        @SerializedName("arabic")
        ARABIC("arabic"),
        @SerializedName("belgianComma")
        BELGIAN_COMMA("belgianComma"),
        @SerializedName("belgianPeriod")
        BELGIAN_PERIOD("belgianPeriod"),
        @SerializedName("bulgarian")
        BULGARIAN("bulgarian"),
        @SerializedName("catalan")
        CATALAN("catalan"),
        @SerializedName("chineseSimplified")
        CHINESE_SIMPLIFIED("chineseSimplified"),
        @SerializedName("chineseTraditional")
        CHINESE_TRADITIONAL("chineseTraditional"),
        @SerializedName("croatian")
        CROATIAN("croatian"),
        @SerializedName("czech")
        CZECH("czech"),
        @SerializedName("czechQwerty")
        CZECH_QWERTY("czechQwerty"),
        @SerializedName("danish")
        DANISH("danish"),
        @SerializedName("dutch")
        DUTCH("dutch"),
        @SerializedName("englishUK")
        ENGLISH_UK("englishUK"),
        @SerializedName("englishUS")
        ENGLISH_US("englishUS"),
        @SerializedName("estonian")
        ESTONIAN("estonian"),
        @SerializedName("finnish")
        FINNISH("finnish"),
        @SerializedName("frenchBelgian")
        FRENCH_BELGIAN("frenchBelgian"),
        @SerializedName("frenchCanadian")
        FRENCH_CANADIAN("frenchCanadian"),
        @SerializedName("frenchFrance")
        FRENCH_FRANCE("frenchFrance"),
        @SerializedName("frenchSwitzerland")
        FRENCH_SWITZERLAND("frenchSwitzerland"),
        @SerializedName("germanGermany")
        GERMAN_GERMANY("germanGermany"),
        @SerializedName("germanSwitzerland")
        GERMAN_SWITZERLAND("germanSwitzerland"),
        @SerializedName("greek")
        GREEK("greek"),
        @SerializedName("hebrew")
        HEBREW("hebrew"),
        @SerializedName("hungarian")
        HUNGARIAN("hungarian"),
        @SerializedName("hungarian101Key")
        HUNGARIAN_101_KEY("hungarian101Key"),
        @SerializedName("icelandic")
        ICELANDIC("icelandic"),
        @SerializedName("irish")
        IRISH("irish"),
        @SerializedName("italian")
        ITALIAN("italian"),
        @SerializedName("japaneseKana")
        JAPANESE_KANA("japaneseKana"),
        @SerializedName("japaneseRomaji")
        JAPANESE_ROMAJI("japaneseRomaji"),
        @SerializedName("koreanHangul")
        KOREAN_HANGUL("koreanHangul"),
        @SerializedName("latvianQwerty")
        LATVIAN_QWERTY("latvianQwerty"),
        @SerializedName("lithuanian")
        LITHUANIAN("lithuanian"),
        @SerializedName("norwegian")
        NORWEGIAN("norwegian"),
        @SerializedName("polish214")
        POLISH_214("polish214"),
        @SerializedName("portuguese")
        PORTUGUESE("portuguese"),
        @SerializedName("portugueseBrazilianAbnt")
        PORTUGUESE_BRAZILIAN_ABNT("portugueseBrazilianAbnt"),
        @SerializedName("romanian")
        ROMANIAN("romanian"),
        @SerializedName("russian")
        RUSSIAN("russian"),
        @SerializedName("serbianCyrillic")
        SERBIAN_CYRILLIC("serbianCyrillic"),
        @SerializedName("serbianLatin")
        SERBIAN_LATIN("serbianLatin"),
        @SerializedName("slovak")
        SLOVAK("slovak"),
        @SerializedName("slovakQwerty")
        SLOVAK_QWERTY("slovakQwerty"),
        @SerializedName("slovenian")
        SLOVENIAN("slovenian"),
        @SerializedName("spanishLatinAmerican")
        SPANISH_LATIN_AMERICAN("spanishLatinAmerican"),
        @SerializedName("spanishSpain")
        SPANISH_SPAIN("spanishSpain"),
        @SerializedName("swedish")
        SWEDISH("swedish"),
        @SerializedName("thai")
        THAI("thai"),
        @SerializedName("turkishF")
        TURKISH_F("turkishF"),
        @SerializedName("turkishQ")
        TURKISH_Q("turkishQ"),
        @SerializedName("ukrainian")
        UKRAINIAN("ukrainian");
        private final String value;
        private final static Map<String, Configuration.KeyboardLayout> CONSTANTS = new HashMap<String, Configuration.KeyboardLayout>();

        static {
            for (Configuration.KeyboardLayout c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        KeyboardLayout(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.KeyboardLayout fromValue(String value) {
            Configuration.KeyboardLayout constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public enum Language {

        @SerializedName("ar")
        AR("ar"),
        @SerializedName("bg")
        BG("bg"),
        @SerializedName("ca")
        CA("ca"),
        @SerializedName("cs")
        CS("cs"),
        @SerializedName("da")
        DA("da"),
        @SerializedName("de")
        DE("de"),
        @SerializedName("el")
        EL("el"),
        @SerializedName("en")
        EN("en"),
        @SerializedName("en-EU")
        EN_EU("en-EU"),
        @SerializedName("es")
        ES("es"),
        @SerializedName("et")
        ET("et"),
        @SerializedName("eu")
        EU("eu"),
        @SerializedName("fi")
        FI("fi"),
        @SerializedName("fr")
        FR("fr"),
        @SerializedName("fr-CA")
        FR_CA("fr-CA"),
        @SerializedName("he")
        HE("he"),
        @SerializedName("hr")
        HR("hr"),
        @SerializedName("hu")
        HU("hu"),
        @SerializedName("id")
        ID("id"),
        @SerializedName("it")
        IT("it"),
        @SerializedName("ja")
        JA("ja"),
        @SerializedName("ko")
        KO("ko"),
        @SerializedName("lv")
        LV("lv"),
        @SerializedName("lt")
        LT("lt"),
        @SerializedName("ms")
        MS("ms"),
        @SerializedName("nb")
        NB("nb"),
        @SerializedName("nl")
        NL("nl"),
        @SerializedName("nn")
        NN("nn"),
        @SerializedName("no")
        NO("no"),
        @SerializedName("pl")
        PL("pl"),
        @SerializedName("pt")
        PT("pt"),
        @SerializedName("ro")
        RO("ro"),
        @SerializedName("ru")
        RU("ru"),
        @SerializedName("sk")
        SK("sk"),
        @SerializedName("sl")
        SL("sl"),
        @SerializedName("sv")
        SV("sv"),
        @SerializedName("th")
        TH("th"),
        @SerializedName("tr")
        TR("tr"),
        @SerializedName("uk")
        UK("uk"),
        @SerializedName("vi")
        VI("vi"),
        @SerializedName("zh")
        ZH("zh"),
        @SerializedName("zh-TW")
        ZH_TW("zh-TW"),
        @SerializedName("zh-CN")
        ZH_CN("zh-CN");
        private final String value;
        private final static Map<String, Configuration.Language> CONSTANTS = new HashMap<String, Configuration.Language>();

        static {
            for (Configuration.Language c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Language(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.Language fromValue(String value) {
            Configuration.Language constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The mode for reset after job start
     * 
     */
    public enum ResetAfterJobStartMode {

        @SerializedName("disabled")
        DISABLED("disabled"),
        @SerializedName("immediate")
        IMMEDIATE("immediate"),
        @SerializedName("delayed")
        DELAYED("delayed");
        private final String value;
        private final static Map<String, Configuration.ResetAfterJobStartMode> CONSTANTS = new HashMap<String, Configuration.ResetAfterJobStartMode>();

        static {
            for (Configuration.ResetAfterJobStartMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ResetAfterJobStartMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.ResetAfterJobStartMode fromValue(String value) {
            Configuration.ResetAfterJobStartMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The set of possible themes for various experience
     * 
     */
    public enum Theme {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("lo")
        LO("lo"),
        @SerializedName("mid")
        MID("mid"),
        @SerializedName("dark")
        DARK("dark"),
        @SerializedName("light")
        LIGHT("light"),
        @SerializedName("hybrid")
        HYBRID("hybrid");
        private final String value;
        private final static Map<String, Configuration.Theme> CONSTANTS = new HashMap<String, Configuration.Theme>();

        static {
            for (Configuration.Theme c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Theme(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.Theme fromValue(String value) {
            Configuration.Theme constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
