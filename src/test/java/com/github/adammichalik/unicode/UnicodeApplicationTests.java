package com.github.adammichalik.unicode;


import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

class UnicodeTest {

    @Test
    void length() {
        System.out.println("💩".length());

        /*
        printCodePoints("💩");
         */
    }

    @Test
    void codePoints() {
        System.out.println("💩".codePoints().count());
    }

    @Test
    void substring() {
        System.out.println("💩".substring(0, 1));
    }

    @Test
    void reverseStringGoogle() {
        var dump = "🚽💩";
        var reverse = new StringBuilder();
        for (int i = dump.length() - 1; i >= 0; i--) {
            reverse.append(dump.charAt(i));
        }
        System.out.println(reverse);

        printCodePoints(dump);
        printChars(dump);
        printCodePoints(reverse.toString());
        printChars(reverse.toString());
    }

    @Test
    void reverseStringCorrect() {
        var dump = "🚽💩";
        System.out.println(new StringBuilder(dump).reverse());
    }

    @Test
    void bytes() {
        Charset[] charsets = {UTF_8, UTF_16, UTF_16LE, UTF_16BE, Charset.forName("UTF-32"), Charset.forName("UTF-32LE"), Charset.forName("UTF-32BE")};
        for (Charset charset : charsets) {
            System.out.println(charset + ": " + Hex.encodeHexString("💩".getBytes(charset), false));
        }
        System.out.println("Platform (" + Charset.defaultCharset() + "): " + Hex.encodeHexString("💩".getBytes(), false));
    }

    @Test
    void equals() {
        var Ü1 = "Ü";
        var Ü2 = "Ü";
        System.out.println(Ü1 + "=" + Ü2 + " : " + Ü1.equals(Ü2));
    }

    @Test
    void üü() {
        var Ü1 = "Ü";
        var Ü2 = "Ü";
        printChars(Ü1);
        printChars(Ü2);
        for (Normalizer.Form form : Normalizer.Form.values()) {
            System.out.println("=== " + form + " ===");
            printChars(Normalizer.normalize(Ü1, form));
            printChars(Normalizer.normalize(Ü2, form));
        }
    }

    @Test
    void diacritics() {
        var come = "C̷̙̲̝͖ͭ̏ͥͮ͟Oͮ͏̮̪̝͍M̲̖͊̒ͪͩͬ̚̚͜Ȇ̴̟̟͙̞ͩ͌͝";
        System.out.println(come.length());
        System.out.println(come.codePoints().count());

        /*
        System.out.println(StringUtils.stripAccents(come));
        System.out.println(StringUtils.stripAccents(come).length());
        System.out.println(StringUtils.stripAccents(come).codePoints().count());
         */
    }

    @Test
    void ligatures() {
        System.out.println("Æ".codePoints().count());
        System.out.println("ﬁ".codePoints().count());
        /*
        System.out.println("﷽".codePoints().count());
        // In the name of God, Most Compassionate, Most Merciful
         */
    }

    @Test
    void flags() {
        printChars("🇳🇱");
        printChars("🇳🇴");

        /*
        printChars("🇹🇼");
         */
    }

    @Test
    void growingFamily() {
        printChars("👨");
        /*
        printChars("👨‍👩");
         */
        /*
        printChars("👨‍👩‍👧");
         */
        /*
        printChars("👨‍👩‍👧‍👦");
         */

        /*
        System.out.println("👨‍👩‍👧‍👦".length());
        System.out.println("👨‍👩‍👧‍👦".codePoints().count());
         */
    }

    @Test
    void astronaut() {
        printChars("👩🏼‍🚀");

        /*
        printChars("👩");
        printChars("🏼");
        printChars("🚀");
         */
    }

    private static void printChars(String str) {
        printHex(str.chars());
    }

    private static void printCodePoints(String str) {
        printHex(str.codePoints());
    }

    private static void printHex(IntStream intStream) {
        System.out.println(intStream.mapToObj(Integer::toHexString).map(String::toUpperCase).collect(Collectors.joining(" ")));
    }
}
