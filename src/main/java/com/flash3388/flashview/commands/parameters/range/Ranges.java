package com.flash3388.flashview.commands.parameters.range;

import com.jmath.ExtendedMath;

public class Ranges {

    private Ranges() {}

    public static ValueRange<Double> doubleRange(double min, double max) {
        return value -> ExtendedMath.constrained(value, min, max);
    }
}
