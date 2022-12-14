package packageM;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Node<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    private K __key = null;
    private V __value = null;
    private String __code = "";
    private double __possibility = 0d;

    public Node(K key, V value) {
        this.__key = key;
        this.__value = value;
    }

    public K getKey() {
        return this.__key;
    }

    public V getValue() {
        return this.__value;
    }

    public void setValue(V value) {
        this.__value = value;
    }

    public String getCode() {
        return this.__code;
    }

    public void setCode(String code) {
        this.__code = code;
    }

    public double getPossibility() {
        return this.__possibility;
    }

    public void setPossibility(double possibility) {
        this.__possibility = round(possibility, 3);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    @Override
    public String toString() {
        return this.__key + ": " + this.__possibility;
    }
}