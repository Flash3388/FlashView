package com.flash3388.flashview.commands.data;

import com.flash3388.flashview.commands.parameters.CommandParameterType;
import com.flash3388.flashview.commands.parameters.DoubleParameterType;
import com.flash3388.flashview.commands.parameters.IntegerParameterType;

public enum DataTypes {
    DOUBLE("double", new DoubleDataType()) {
        @Override
        public CommandParameterType createParameterType(String name, String measurementUnit) {
            return new DoubleParameterType(name, measurementUnit, null);
        }
    },
    INTEGER("int", new IntegerDataType()) {
        @Override
        public CommandParameterType createParameterType(String name, String measurementUnit) {
            return new IntegerParameterType(name, measurementUnit, null);
        }
    };

    private final String mName;
    private final DataType<?> mDataType;

    DataTypes(String name, DataType<?> dataType) {
        mName = name;
        mDataType = dataType;
    }

    public String getName() {
        return mName;
    }

    public <T> DataType<T> getDataType() {
        return (DataType<T>) mDataType;
    }

    public abstract CommandParameterType createParameterType(String name, String measurementUnit);

    public static DataType<?> forName(String name) {
        for (DataTypes types : DataTypes.values()) {
            if (types.getName().equalsIgnoreCase(name)) {
                return types.getDataType();
            }
        }

        throw new EnumConstantNotPresentException(DataTypes.class, name);
    }

    public static CommandParameterType parameterType(DataType<?> type, String name, String measurementUnit) {
        for (DataTypes types : DataTypes.values()) {
            if (types.getDataType().equals(type)) {
                return types.createParameterType(name, measurementUnit);
            }
        }

        throw new EnumConstantNotPresentException(DataTypes.class, type.getType().getName());
    }
}
