
package scrolls.maths;

/**
 *
 * @author knaxel
 */
public class ScrollsUtil {

    public static String IntegerToRoman(int n) {
        String roman = "";
        int repeat;
        repeat = n / 1000;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "M";
        }
        n = n % 1000;
        repeat = n / 900;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "CM";
        }
        n = n % 900;
        repeat = n / 500;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "D";
        }
        n = n % 500;
        repeat = n / 400;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "CD";
        }
        n = n % 400;
        repeat = n / 100;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "C";
        }
        n = n % 100;
        repeat = n / 90;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "XC";
        }
        n = n % 90;
        repeat = n / 50;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "L";
        }
        n = n % 50;
        repeat = n / 40;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "XL";
        }
        n = n % 40;
        repeat = n / 10;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "X";
        }
        n = n % 10;
        repeat = n / 9;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "IX";
        }
        n = n % 9;
        repeat = n / 5;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "V";
        }
        n = n % 5;
        repeat = n / 4;
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "IV";
        }
        n = n % 4;
        repeat = n / 1; // or simply repeat=n or i<=n in the condition part of the loop
        for (int i = 1; i <= repeat; i++) {
            roman = roman + "I";
        }
        return roman;
    }

}
