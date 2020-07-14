package net.avicus.atlas.core.util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;

public class CollectionUtils {

  @Nullable
  public static <C> C highestNumberObject(List<C> list, Class<? extends C> clazz,
      Function<C, Number> refMethod) {
    C highest = list
        .stream().filter(v -> v.getClass().equals(clazz))
        .max((a, b) -> Integer
            .max(refMethod.apply(a).intValue(), refMethod.apply(b).intValue()))
        .orElse(null);

    return highest;
  }

  @Nullable
  public static <N, C> Multiset.Entry<N> mostCommonAttributeEntry(List<C> list,
      Class<? extends C> clazz, Function<C, N> refMethod) {
    Multiset<N> commons = HashMultiset.create();
    list.stream().filter(action -> action.getClass().equals(clazz))
        .forEach(action -> commons.add(refMethod.apply((C) action)));

    return commons.entrySet()
        .stream()
        .max(Comparator.comparing(Multiset.Entry::getCount)).orElse(null);
  }

  @Nullable
  public static <C> Number highestNumber(List<C> list, Class<? extends C> clazz,
      Function<C, Number> refMethod) {
    C highest = highestNumberObject(list, clazz, refMethod);

    if (highest != null) {
      return refMethod.apply(highest);
    }

    return null;
  }

  @Nullable
  public static <N, C> N mostCommonAttribute(List<C> list, Class<? extends C> clazz,
      Function<C, N> refMethod) {
    return Optional.ofNullable(mostCommonAttributeEntry(list, clazz, refMethod))
        .map(Multiset.Entry::getElement).orElse(null);
  }
}
