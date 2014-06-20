package de.heikomaass.refreshfever.app.ui.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import java.text.DateFormat;

import de.heikomaass.refreshfever.app.R;

/**
 * Created by hmaass on 15.06.14.
 */
public class TimePreference extends DialogPreference {
    private LocalTime time;
    private TimePicker picker = null;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        time = new LocalTime();
        setPositiveButtonText(context.getString(R.string.set));
        setNegativeButtonText(context.getString(R.string.cancel));

    }
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        picker.setIs24HourView(Boolean.TRUE);
        return picker;
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setCurrentHour(time.getHourOfDay());
        picker.setCurrentMinute(time.getMinuteOfHour());
    }

    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            time = new LocalTime(picker.getCurrentHour(), picker.getCurrentMinute());
            setSummary(getSummary());
            String timeString = time.toString();
            if (callChangeListener(timeString)) {
                persistString(timeString);
                notifyChanged();
            }
        }
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            if (defaultValue == null) {
                time = LocalTime.parse(getPersistedString(new LocalTime().toString()));
            } else {
                time = LocalTime.parse(getPersistedString((String) defaultValue));
            }
        } else {
            if (defaultValue == null) {
                time = new LocalTime();
            } else {
                time = LocalTime.parse((String) defaultValue);
            }
        }
        setSummary(getSummary());
    }

    public CharSequence getSummary() {
        if (time == null) {
            return null;
        }
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        return timeFormat.format(time.toDateTimeToday().toDate());
    }
}
