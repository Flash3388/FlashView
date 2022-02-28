package com.flash3388.flashview.commands.parameters;

public class CommandParameterValue<T> {

    private final CommandParameter<T> mParameterType;
    private final T mValue;

    public CommandParameterValue(CommandParameter<T> parameterType, T value) {
        mParameterType = parameterType;
        mValue = value;
    }

    public CommandParameter<T> getParameterType() {
        return mParameterType;
    }

    public T getValue() {
        return mValue;
    }
}
