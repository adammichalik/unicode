package com.github.adammichalik.unicode;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.List;
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
        System.out.println("Reverse: " + reverse);

        System.out.print("CP dump:       ");
        printCodePoints(dump);
        System.out.print("Chars dump:    ");
        printChars(dump);
        System.out.print("Chars reverse: ");
        printChars(reverse.toString());
        System.out.print("CP reverse:    ");
        printCodePoints(reverse.toString());
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
        printChars("U");
        normalizeCompare(Ü1, Ü2);
    }

    private static void normalizeCompare(String s1, String s2) {
        for (Normalizer.Form form : Normalizer.Form.values()) {
            System.out.println("=== " + form + " ===");
            printChars(Normalizer.normalize(s1, form));
            printChars(Normalizer.normalize(s2, form));
        }
    }

    @Test
    void ligature() {
        System.out.println("ﬁ");
        printChars("ﬁ");
        System.out.println("ﬁ".codePoints().count());
        normalizeCompare("ﬁ", "fi");
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

    private static void printCodePoints(String str) {
        printHex(str.codePoints());
    }

    private static void printHex(IntStream intStream) {
        System.out.println(intStream.mapToObj(Integer::toHexString).map(String::toUpperCase).collect(Collectors.joining(" ")));
    }
}
