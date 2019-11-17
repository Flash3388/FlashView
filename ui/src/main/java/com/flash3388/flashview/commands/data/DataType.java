package com.flash3388.flashview.commands.data;

import java.util.Optional;

public interface DataType<T> {

    boolean isValid(Object o);
    Class<T> getType();
    Optional<T> tryConvert(String value);
}
