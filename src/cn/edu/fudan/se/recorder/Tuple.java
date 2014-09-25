package cn.edu.fudan.se.recorder;

import java.util.Arrays;

/**
 * Created by Dawnwords on 2014/9/20.
 */
public class Tuple {
    public final String name;
    public final float[] values;

    public Tuple(String name, float[] values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "name='" + name + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
