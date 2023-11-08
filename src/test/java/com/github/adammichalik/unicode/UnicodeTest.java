package com.github.adammichalik.unicode;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;

@SuppressWarnings("ALL")
class UnicodeTest {

    @Test
    void length() {
        print("💩length:", "💩".length());

        /*
        print("💩 CP:", codePoints("💩"));
        print("💩 CP count:", "💩".codePoints().count());
         */
    }

    @Test
    void substring() {
        print("💩 substring:", "💩".substring(0, 1));

        /*
        print("💩 chars:", chars("💩"));
        print("💩 chars[0]", chars("💩".substring(0, 1)));
         */
    }

    @Test
    void reverseStringHackerRank() {
        var string = "🚽💩";
        print("String: ", string);
        var reverse = new StringBuilder();
        for (int i = string.length() - 1; i >= 0; i--) {
            reverse.append(string.charAt(i));
        }
        print("Reverse:", reverse);

        print("CP string:    ", codePoints(string));
        print("Chars string: ", chars(string));
        print("Chars reverse:", chars(reverse.toString()));
        print("CP reverse:   ", codePoints(reverse.toString()));
    }

    @Test
    void reverseStringCorrect() {
        var string = "🚽💩";
        print(new StringBuilder(string).reverse());
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
    void encodeDecode() {
        for (String string : List.of("hello", "żółć", "안녕", "🚽💩")) {
            encodeDecode(string);
        }
    }

    @Test
    void equals() {
        var Ü1 = "Ü";
        var Ü2 = "Ü";
        print(Ü1 + "=" + Ü2 + ":", Ü1.equals(Ü2));
    }

    @Test
    void üü() {
        var Ü1 = "Ü";
        var Ü2 = "Ü";
        print(Ü1 + 1, codePoints(Ü1));
        print(Ü2 + 2, codePoints(Ü2));
        print("U", codePoints("U"));
        normalizeCompare(Ü1, Ü2);
    }

    @Test
    void ligature() {
        compareLigature("ﬃ", "ffi");
        print();
        compareLigature("ĳ", "ij");
    }

    @Test
    void diacritics() {
        var zalgoText = "Ẓ̵̈́̉͜â̴̖̻ĺ̷̮͈g̸̲̹̈o̵͖͇̾̀ ̵̺̐̈́t̴̹̳̏ḙ̵̏̄x̶̥̲́̃t̶̜͎͌͝";
        print(zalgoText, zalgoText.codePoints().count());

        /*
        var stripped = StringUtils.stripAccents(zalgoText);
        print(stripped, stripped.codePoints().count());
         */
    }

    @Test
    void growingFamily() {
        print("👨", codePoints("👨"));
        print("👨‍👩", codePoints("👨‍👩"));
        print("👨‍👩‍👧", codePoints("👨‍👩‍👧"));
        print("👨‍👩‍👧‍👦", codePoints("👨‍👩‍👧‍👦"));

        print("👨‍👩‍👧‍👦", "length:", "👨‍👩‍👧‍👦".length(), "CP:", "👨‍👩‍👧‍👦".codePoints().count());
    }

    @Test
    void astronaut() {
        print("👩🏼‍🚀", codePoints("👩🏼‍🚀"));

        /*
        print("👩", codePoints("👩"));
        print("🏼", codePoints("🏼"));
        print("🚀", codePoints("🚀"));
         */
    }

//  ----- Utilities -----

    private static void compareLigature(String ligature, String split) {
        print(ligature, codePoints(ligature), "(CP: " + ligature.codePoints().count() + ")");
        normalizeCompare(ligature, split);
    }

    private static void normalizeCompare(String s1, String s2) {
        for (Normalizer.Form form : Normalizer.Form.values()) {
            print("=== ", form, " ===");
            print(s1, codePoints(Normalizer.normalize(s1, form)));
            print(s2, codePoints(Normalizer.normalize(s2, form)));
        }
    }

    private static String chars(String str) {
        return toHexString(str.chars(), 4);
    }

    private static String codePoints(String str) {
        return toHexString(str.codePoints(), 8, nibble -> StringUtils.stripStart(nibble, "0"));
    }

    private static String toHexString(byte[] bytes) {
        return toHexString(IntStream.range(0, bytes.length).map(i -> 0xFF & bytes[i]), 2);
    }

    private static String toHexString(IntStream intStream, int digits) {
        return toHexString(intStream, digits, identity());
    }

    private static String toHexString(IntStream intStream, int digits, Function<String, String> nibblePostFormatter) {
        return intStream.mapToObj(i -> nibblePostFormatter.apply(hexFormat.toHexDigits(i, digits))).collect(joining(" "));
    }

    private static void print(Object... objects) {
        System.out.println(Arrays.stream(objects).map(String::valueOf).collect(joining(" ")));
    }

    private static void encodeDecode(String string) {
        print("===", string, "===");
        for (Charset encodingCharset : new Charset[]{UTF_8, UTF_16, Charset.forName("UTF-32")}) {
            var stringBytes = string.getBytes(encodingCharset);
            var out = new ByteArrayOutputStream();
            out.writeBytes(stringBytes);
            print(encodingCharset, "bytes:", toHexString(stringBytes));
            for (Charset decodingCharset : new Charset[]{UTF_8, UTF_16, Charset.forName("UTF-32")}) {
                System.out.printf("%-6s -> %-6s: %s%n", encodingCharset, decodingCharset, out.toString(decodingCharset));
            }
        }
        print();
    }

    private static final HexFormat hexFormat = HexFormat.of().withUpperCase();
}
