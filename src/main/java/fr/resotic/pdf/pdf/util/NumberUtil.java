package fr.resotic.pdf.pdf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    public static final NumberFormat EN_EURO;
    public static final NumberFormat EN_POURCENT;
    public static final NumberFormat DECI;

    static {

        EN_EURO = DecimalFormat.getCurrencyInstance(Locale.FRANCE);

        EN_POURCENT = NumberFormat.getPercentInstance(Locale.FRANCE);
        EN_POURCENT.setMinimumFractionDigits(2);

        DECI = NumberFormat.getNumberInstance(Locale.FRANCE);
        DECI.setMinimumFractionDigits(1);
        DECI.setMaximumFractionDigits(3);

    }

    public static String getDecimale(Double val, Integer decimale) {
        NumberFormat number = NumberFormat.getNumberInstance(Locale.FRANCE);
        number.setMinimumFractionDigits(decimale != null ? decimale : 1);
        number.setMaximumFractionDigits(decimale != null ? decimale : 3);
        return number.format(val);
    }

    public static String getDecimale(float total, int decimale) {
        return getDecimale(new Double(total), decimale);
    }

    /**
     * Arrondi d'un double avec n éléments après la virgule.
     *
     * @param a La valeur à convertir.
     * @param n Le nombre de décimales à conserver.
     * @return La valeur arrondi à n décimales.
     */
    public static Integer arrondir(Double a, int n) {
        if (a == null)
            return null;
        else {
            double p = Math.pow(10.0, n);
            return (int) (Math.floor((a * p) + 0.5) / p);
        }
    }

    /**
     * Arrondi d'un double avec n éléments après la virgule.
     *
     * @param value  La valeur à convertir.
     * @param places Le nombre de décimales à conserver.
     * @return La valeur arrondi à n décimales.
     */
    public static BigDecimal round(double value, int places) {
        return round(new BigDecimal(value), places);
    }

    public static BigDecimal round(BigDecimal value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        return value.setScale(places, RoundingMode.HALF_UP);
        //return value.doubleValue();
    }

    public static int returnValOr0(Integer val) {
        if (val == null) {
            return 0;
        } else {
            return val;
        }
    }

    public static BigDecimal returnValOr0(BigDecimal val) {
        if (val == null) {
            return new BigDecimal(0);
        } else {
            return val;
        }
    }

    public static String getCurrency(String n) {
        try {
            BigDecimal number = new BigDecimal(Float.parseFloat(n));
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            return format.format(number);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getStringValueFromBigDecimal(BigDecimal valeur) {
        NumberFormat format = DecimalFormat.getInstance(Locale.FRANCE);
        Float v = valeur.floatValue();
        return format.format(v);
    }

    public static String getStringValueFromInteger(Integer valeur) {
        NumberFormat format = DecimalFormat.getInstance(Locale.FRANCE);
        Float v = valeur.floatValue();
        return format.format(v);
    }

    public static String formatNumberIfNumeric(BigDecimal v, Integer decimale) {
        return formatNumberIfNumeric(v, decimale, true);
    }

    public static String formatNumberIfNumeric(BigDecimal v, Integer decimale, boolean withSymbol) {
        if (v != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
            symbols.setDecimalSeparator(",".toCharArray()[0]);
            DecimalFormat decimalFormat;


            if (decimale != null && decimale == 2) {
                decimalFormat = new DecimalFormat("#,##0.00 " + (withSymbol ? "¤" : ""), symbols);
                v = v.setScale(decimale, RoundingMode.HALF_UP);
            } else if (decimale != null && decimale == 3) {
                decimalFormat = new DecimalFormat("#,##0.00# " + (withSymbol ? "¤" : ""), symbols);
                v = v.setScale(decimale, RoundingMode.HALF_UP);
            } else if (decimale != null && decimale == 4) {
                decimalFormat = new DecimalFormat("#,##0.00## " + (withSymbol ? "¤" : ""), symbols);
                v = v.setScale(decimale, RoundingMode.HALF_UP);
            } else {
                decimalFormat = new DecimalFormat("#,##0.00000 " + (withSymbol ? "¤" : ""), symbols);
                v = v.setScale(5, RoundingMode.HALF_UP);
            }

            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            return decimalFormat.format(v);
        } else {
            return "";
        }
    }
}
