package de.heikomaass.refreshfever.app.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.LocalTime;

import de.heikomaass.refreshfever.app.R;

/**
 * Created by hmaass on 21.06.14.
 */
public class UrlPreference extends DialogPreference {
    private EditText urlView;
    private String url;

    public UrlPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected View onCreateDialogView() {
        urlView = new EditText(getContext());
        return urlView;
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            if (defaultValue == null) {
                url = getPersistedString("https://");
            } else {
                url = getPersistedString((String) defaultValue);
            }
        } else {
            if (defaultValue == null) {
                url = "https://";
            } else {
                url = (String) defaultValue;
            }
        }
        setSummary(getSummary());
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        urlView.setText(url, TextView.BufferType.EDITABLE);

        urlView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // noop

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updatePositiveButton();
            }
        });
    }

    protected void showDialog(Bundle state) {
        super.showDialog(state);
        updatePositiveButton();
    }

    private void updatePositiveButton() {
        AlertDialog alertDialog = (AlertDialog) this.getDialog();
        Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (button != null) {
            button.setEnabled(validUrl());
        }
    }

    private boolean validUrl() {
        Editable text = urlView.getText();
        if (text != null) {
            Uri uri = Uri.parse(text.toString());
            return !TextUtils.isEmpty(uri.getScheme()) && !TextUtils.isEmpty(uri.getHost());
        }
        return false;
    }

    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            url = urlView.getText().toString();
            setSummary(getSummary());
            if (callChangeListener(url)) {
                persistString(url);
                notifyChanged();
            }
        }
    }

    public String getSummary() {
        return url;
    }

}
