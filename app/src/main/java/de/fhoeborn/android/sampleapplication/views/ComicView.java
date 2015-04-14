package de.fhoeborn.android.sampleapplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import uk.co.senab.photoview.PhotoView;


/**
 * This represents a comic strip with a alternative text
 */
public class ComicView extends PhotoView {

    private String alternativeText;

    public ComicView(Context context) {
        super(context);
        init(null, 0);
    }

    public ComicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ComicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alternativeText != null && !alternativeText.isEmpty()) {
                    Toast.makeText(getContext(), alternativeText, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setAlternativeText(String alternativeText) {
        this.alternativeText = alternativeText;
    }


}
