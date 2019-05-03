package com.flash3388.flashview.actions.parameters;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ValueType;

public class DegreesParameter implements ActionParameter {

    @Override
    public String getName() {
        return "Degrees";
    }

    @Override
    public ValueType getValueType() {
        return ValueType.INTEGER;
    }
}
