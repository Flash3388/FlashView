package com.flash3388.flashview.commands.data;

import java.util.Optional;

public class IntegerDataType implements DataType<Integer> {

    @Override
    public boolean isValid(Object o) {
        return o instanceof Integer;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Optional<Integer> tryConvert(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
