package com.flash3388.flashview.commands.parameters;

import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.data.DataTypes;
import com.flash3388.flashview.commands.parameters.range.ValueRange;

public class DoubleParameterType implements CommandParameterType<Double> {

    private final String mName;
    private final String mMeasurementUnit;
    private final ValueRange<Double> mValueRange;

    public DoubleParameterType(String name, String measurementUnit, ValueRange<Double> valueRange) {
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
    public DataType<Double> getValueType() {
        return DataTypes.DOUBLE;
    }

    @Override
    public ValueRange<Double> getValueRange() {
        return mValueRange;
    }
}
