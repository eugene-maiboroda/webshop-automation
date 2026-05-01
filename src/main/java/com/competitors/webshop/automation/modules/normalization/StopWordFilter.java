package com.competitors.webshop.automation.modules.normalization;

import com.competitors.webshop.automation.modules.normalization.utils.StopWordsUtil;

import java.util.regex.Pattern;

public class StopWordFilter {

    private static final Pattern PARENTHESES = Pattern.compile("\\(.*?\\)");
    private static final Pattern SEPARATOR_DASH = Pattern.compile("\\s+-\\s+");
    private static final Pattern TRAILING_PUNCT = Pattern.compile("[*.,!]+$");
    private static final Pattern MULTI_SPACE = Pattern.compile("\\s{2,}");
    private static final String EMPTY = "";
    private static final String SPACE = " ";

    public String apply(String name) {
        if (name == null || name.isBlank()) {
            return EMPTY;
        }

        String result = name.toLowerCase();

        result = PARENTHESES.matcher(result).replaceAll(SPACE);
        result = SEPARATOR_DASH.matcher(result).replaceAll(SPACE);
        result = TRAILING_PUNCT.matcher(result).replaceAll(EMPTY);

        String[] words = result.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            String clean = word.replaceAll("[^a-zæøå0-9\\-]", EMPTY);
            if (!clean.isBlank() && !StopWordsUtil.isStopWord(clean)) {
                sb.append(clean).append(SPACE);
            }
        }

        result = MULTI_SPACE.matcher(sb.toString().trim()).replaceAll(SPACE);

        return result;
    }

    public static void main(String[] args) {
        StopWordFilter filter = new StopWordFilter();
        String input = "Neumann TLM 103 Studiosett mikrofon(nikkel finish) inkl EA1";
        String output = filter.apply(input);
        System.out.println("Input:  " + input);
        System.out.println("Output: " + output);
    }
}