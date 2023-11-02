package com.github.adammichalik.unicode;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

class UnicodeTest {

    @Test
    void length() {
        print("💩length:", "💩".length());

        print("💩 CP:", codePoints("💩"));
    }

    @Test
    void codePoints() {

        print("💩 CP count:", "💩".codePoints().count());
    }

    @Test
    void substring() {
        print("💩 substring:", "💩".substring(0, 1));

        print("💩 chars:", chars("💩"));
        print("💩 chars[0]", chars("💩".substring(0, 1)));
    }

    @Test
    void reverseStringGoogle() {
        var dump = "🚽💩";
        var reverse = new StringBuilder();
        for (int i = dump.length() - 1; i >= 0; i--) {
            reverse.append(dump.charAt(i));
        }
        print("Reverse:", reverse);

        print("CP dump:      ", codePoints(dump));
        print("Chars dump:   ", chars(dump));
        print("Chars reverse:", chars(reverse.toString()));
        print("CP reverse:   ", codePoints(reverse.toString()));
    }

    @Test
    void reverseStringCorrect() {
        var dump = "🚽💩";
        print(new StringBuilder(dump).reverse());
    }

    @Test
    void bytes() {
        Charset[] charsets = {UTF_8, UTF_16, UTF_16LE, UTF_16BE, Charset.forName("UTF-32"), Charset.forName("UTF-32LE"), Charset.forName("UTF-32BE")};
        for (Charset charset : charsets) {
            System.out.printf("%-8s: %17s%n", charset, toHexString("💩".getBytes(charset)));
        }
        System.out.printf("D(%s): %17s%n", Charset.defaultCharset(), toHexString("💩".getBytes()));
    }

    @Test
    void equals() {
        var Ü1 = "Ü";
        var Ü2 = "Ü";
        print(Ü1 + "=" + Ü2 + " :", Ü1.equals(Ü2));
    }

    @Test
    void üü() {
        var Ü1 = "Ü";
        var Ü2 = "Ü";
        print(Ü1 + 1, chars(Ü1));
        print(Ü2 + 2, chars(Ü2));
        print("U", chars("U"));
        normalizeCompare(Ü1, Ü2);
    }

    @Test
    void ligature() {
        compareLigature("ﬁ", "fi");
        compareLigature("ĳ", "ij");
    }

    private static void compareLigature(String ligature, String split) {
        System.out.println(ligature);
        printChars(ligature);
        System.out.println(ligature.codePoints().count());
        normalizeCompare(ligature, split);
    }

    private static void normalizeCompare(String s1, String s2) {
        for (Normalizer.Form form : Normalizer.Form.values()) {
            print("=== ", form, " ===");
            print(s1 + 1, chars(Normalizer.normalize(s1, form)));
            print(s2 + 2, chars(Normalizer.normalize(s2, form)));
        }
    }

    @Test
    void theLargestUnicodeCharacter() {
        System.out.println("﷽".codePoints().count());
        /*
        In the name of God, Most Compassionate, Most Merciful
        */
    }

    @Test
    void diacritics() {
        var zalgoText = "Ẓ̵̈́̉͜â̴̖̻ĺ̷̮͈g̸̲̹̈o̵͖͇̾̀ ̵̺̐̈́t̴̹̳̏ḙ̵̏̄x̶̥̲́̃t̶̜͎͌͝";
        System.out.println(zalgoText.codePoints().count());

        /*
        System.out.println(StringUtils.stripAccents(zalgoText));
        System.out.println(StringUtils.stripAccents(zalgoText).codePoints().count());
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

    @Test
    void encodeDecode() {
        for (String string : List.of("hello", "żółć", "안녕", "🚽💩")) {
            encodeDecode(string);
        }
    }

    private static void encodeDecode(String string) {
        for (Charset encodingCharset : new Charset[]{UTF_8, UTF_16, Charset.forName("UTF-32")}) {
            var stringBytes = string.getBytes(encodingCharset);
            var out = new ByteArrayOutputStream();
            out.writeBytes(stringBytes);
            for (Charset decodingCharset : new Charset[]{UTF_8, UTF_16, Charset.forName("UTF-32")}) {
                System.out.printf("%-6s -> %-6s: %s%n", encodingCharset, decodingCharset, out.toString(decodingCharset));
            }
        }
        System.out.println();
    }

    private static void printChars(String str) {
        printHex(str.chars());
    }

    private static String chars(String str) {
        return toHexString(str.chars(), 4);
    }

    private static void printCodePoints(String str) {
        printHex(str.codePoints());
    }

    private static String codePoints(String str) {
        return toHexString(str.codePoints(), 4);
    }

    private static void printHex(IntStream intStream) {
        System.out.println(toHexString(intStream, 4));
    }

    private static String toHexString(byte[] bytes) {
        return toHexString(IntStream.range(0, bytes.length).map(i -> 0xFF & bytes[i]), 2);
    }

    private static String toHexString(IntStream intStream, int digits) {
        return intStream.mapToObj(i -> hexFormat.toHexDigits(i, digits)).collect(joining(" "));
    }

    private static void print(Object... objects) {
        System.out.println(Arrays.stream(objects).map(String::valueOf).collect(joining(" ")));
    }

    private static final HexFormat hexFormat = HexFormat.of().withUpperCase();
}
