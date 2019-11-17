package com.flash3388.flashview.commands.parameters;

public class CommandParameter<T> {

    private final CommandParameterType<T> mParameterType;
    private final T mValue;

    public CommandParameter(CommandParameterType<T> parameterType, T value) {
        mParameterType = parameterType;
        mValue = value;
    }

    public CommandParameterType<T> getParameterType() {
        return mParameterType;
    }

    public T getValue() {
        return mValue;
    }
}
