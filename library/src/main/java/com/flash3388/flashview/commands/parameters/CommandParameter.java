package com.flash3388.flashview.commands.parameters;

import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.parameters.range.ValueRange;

public interface CommandParameter<T> {

    String getName();
    String getMeasurementUnit();
    DataType<T> getValueType();
    ValueRange<T> getValueRange();
}
