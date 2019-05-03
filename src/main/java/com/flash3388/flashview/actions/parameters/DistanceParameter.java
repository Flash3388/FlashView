package com.flash3388.flashview.actions.parameters;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ValueType;

public class DistanceParameter implements ActionParameter {

    @Override
    public String getName() {
        return "Distance";
    }

    @Override
    public ValueType getValueType() {
        return ValueType.DOUBLE;
    }
}
