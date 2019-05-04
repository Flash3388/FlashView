package com.flash3388.flashview.commands.parameters;

import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.parameters.range.ValueRange;

public interface CommandParameterType<T> {

    String getName();
    DataType<T> getValueType();
    ValueRange<T> getValueRange();
}
