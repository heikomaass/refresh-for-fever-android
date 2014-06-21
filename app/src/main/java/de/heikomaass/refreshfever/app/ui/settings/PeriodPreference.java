package de.heikomaass.refreshfever.app.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.joda.time.Duration;

import de.heikomaass.refreshfever.app.R;

/**
 * Created by hmaass on 20.06.14.
 */
public class PeriodPreference extends DialogPreference {
    private NumberPicker numberPicker;
    private TextView hoursView;
    private final Context context;
    private long periodInMillies;

    private long DEFAULT_PERIOD_IN_MILLIES = 60 * 60 * 1000L;

    public PeriodPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setDialogLayoutResource(R.layout.period_preference);
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        hoursView = (TextView) view.findViewById(R.id.hours_title);
        numberPicker = (NumberPicker) view.findViewById(R.id.hours_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(48);

        int value = hoursFromMillies(this.periodInMillies);

        numberPicker.setValue(value);
        updateHours();
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                updateHours();
            }
        });
    }

    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        Long value;
        if (restorePersistedValue) {
            value = getPersistedLong(DEFAULT_PERIOD_IN_MILLIES);
            if (value != null) {
                periodInMillies = value;
            }
        } else {
            value = (Long) defaultValue;
            if (value != null) {
                periodInMillies = value;
            }
            persistLong(value);
        }
        setSummary();

    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return new Long(a.getInteger(index, (int) DEFAULT_PERIOD_IN_MILLIES));
    }

    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            numberPicker.clearFocus();
            int hours = numberPicker.getValue();
            periodInMillies = milliesFromHours(hours);
            persistLong(periodInMillies);
            setSummary();
        }
    }

    private void setSummary() {
        int hours = hoursFromMillies(this.periodInMillies);
        String hoursText = getSummary(hours);
        setSummary(hoursText);
    }

    private void updateHours() {
        int hours = this.numberPicker.getValue();
        String hoursText = getPickerLabel(hours);
        hoursView.setText(hoursText);

    }

    private int hoursFromMillies(long millies) {
        return (int) Duration.millis(millies).getStandardHours();
    }

    private long milliesFromHours(long hours) {
        return Duration.standardHours(hours).getMillis();
    }

    private String getSummary(int hours) {
        return String.format(context.getResources().getQuantityText(R.plurals.duration_hours, hours).toString(), hours);
    }

    private String getPickerLabel(int hours) {
        return String.format(context.getResources().getQuantityText(R.plurals.pref_period_hours_label, hours).toString());
    }
}
