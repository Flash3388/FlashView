package com.flash3388.flashview.commands.parameters;

import com.flash3388.flashview.commands.parameters.range.ValueRange;

public enum KnownParameterTypes {
    INTEGER {
        @Override
        public CommandParameter<?> newParameter(String name, String measurementUnit, ValueRange<?> range) {
            return new IntegerParameter(name, measurementUnit, (ValueRange<Integer>) range);
        }
    },
    DOUBLE {
        @Override
        public CommandParameter<?> newParameter(String name, String measurementUnit, ValueRange<?> range) {
            return new DoubleParameter(name, measurementUnit, (ValueRange<Double>) range);
        }
    }
    ;

    public abstract CommandParameter<?> newParameter(String name, String measurementUnit, ValueRange<?> range);
}
