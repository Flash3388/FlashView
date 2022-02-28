package com.flash3388.flashview.commands.parameters;

import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.data.DataTypes;
import com.flash3388.flashview.commands.parameters.range.ValueRange;

public class IntegerParameter implements CommandParameter<Integer> {

    private final String mName;
    private final String mMeasurementUnit;
    private final ValueRange<Integer> mValueRange;

    public IntegerParameter(String name, String measurementUnit, ValueRange<Integer> valueRange) {
        mName = name;
        mMeasurementUnit = measurementUnit;
        mValueRange = valueRange;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getMeasurementUnit() {
        return mMeasurementUnit;
    }

    @Override
    public DataType<Integer> getValueType() {
        return DataTypes.INTEGER;
    }

    @Override
    public ValueRange<Integer> getValueRange() {
        return mValueRange;
    }
}
