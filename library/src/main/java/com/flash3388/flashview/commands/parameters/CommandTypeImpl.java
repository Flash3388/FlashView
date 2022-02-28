package com.flash3388.flashview.commands.parameters;

import com.flash3388.flashview.commands.CommandType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandTypeImpl implements CommandType {

    private final String mId;
    private final String mName;
    private final List<CommandParameter<?>> mParameters;
    private final Map<String, String> mAdditionalProperties;

    public CommandTypeImpl(String id, String name,
                           List<CommandParameter<?>> parameters,
                           Map<String, String> additionalProperties) {
        mId = id;
        mName = name;
        mParameters = Collections.unmodifiableList(parameters);
        mAdditionalProperties = Collections.unmodifiableMap(additionalProperties);
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public List<CommandParameter<?>> getParameters() {
        return mParameters;
    }

    @Override
    public Map<String, String> getAdditionalProperties() {
        return mAdditionalProperties;
    }
}
