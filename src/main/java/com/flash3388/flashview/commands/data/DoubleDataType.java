package com.flash3388.flashview.commands.data;

import java.util.Optional;

public class DoubleDataType implements DataType<Double> {

    @Override
    public boolean isValid(Object o) {
        return o instanceof Double && Double.isFinite((Double)o);
    }

    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public Optional<Double> tryConvert(String value) {
        try {
            return Optional.of(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
