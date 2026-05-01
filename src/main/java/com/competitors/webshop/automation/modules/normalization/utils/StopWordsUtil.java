package com.competitors.webshop.automation.modules.normalization.utils;

import java.util.Set;

public final class StopWordsUtil {

    private static final Set<String> STOP_WORDS = Set.of(

            "med", "og", "til", "for", "inkl", "ink", "fra",
            "flere", "valgfritt", "eller",

            "microphone", "mic", "mikrofon",
            "kondensatormikrofon", "kondensatormik",
            "stormembranmikrofon", "studio",

            "bundle", "set", "sett", "stereopar",
            "matchet", "komplett", "kit",
            "stereo", "par", "series", "edition",

            "black", "white", "silver", "svart",
            "hvit", "sort", "nikkel", "cobalt",
            "purple", "pink", "red", "champagne"
    );
    public static boolean isStopWord(String word) {
        return STOP_WORDS.contains(word);
    }
}
