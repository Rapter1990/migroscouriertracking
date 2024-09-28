package com.casestudy.migroscouriertracking.common.model.mapper;

import java.util.Collection;
import java.util.List;

/**
 * A generic interface for mapping objects from one type to another.
 *
 * @param <S> the source type to map from
 * @param <T> the target type to map to
 */
public interface BaseMapper<S, T> {

    /**
     * Maps a single source object to a target object.
     *
     * @param source the source object to be mapped
     * @return the mapped target object
     */
    T map(S source);

    /**
     * Maps a collection of source objects to a list of target objects.
     *
     * @param sources the collection of source objects to be mapped
     * @return a list of mapped target objects
     */
    List<T> map(Collection<S> sources);

}