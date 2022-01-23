package net.avicus.atlas.core.runtimeconfig.fields;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.avicus.compendium.StringUtil;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DurationField extends ConfigurableField<Duration> {

    private static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
        .appendDays().appendSuffix("d")
        .appendHours().appendSuffix("h")
        .appendMinutes().appendSuffix("m")
        .appendSecondsWithOptionalMillis().appendSuffix("s")
        .appendSeconds()
        .toFormatter();

    public DurationField(String name, Supplier<Duration> valueGetter,
        Consumer<Duration> valueModifier) {
        super(name, valueGetter, valueModifier);
    }

    public DurationField(String name) {
        super(name);
    }

    @Override
    public Duration parse(String... data) throws Exception {
        return periodFormatter.parsePeriod(data[0]).toStandardDuration();
    }

    @Override
    public String getValue(Duration value) throws Exception {
        return StringUtil.secondsToClock((int) value.getStandardSeconds());
    }
}
