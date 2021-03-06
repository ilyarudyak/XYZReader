package com.example.xyzreader.ui;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.ui.images.ImageLoaderHelper;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment /*extends Fragment*/
        extends DialogFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_ITEM_ID = "item_id";

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private int mColumnNumber;

    // we use vibrant color from an image instead of muted one
    // this is our primary color - we use it if no color provided by Palette
    private int mVibrantColor = 0xffffc107;

    private ImageView mToolbarImageView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set column number
        mColumnNumber = getResources().getInteger(R.integer.column_number);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        mToolbarImageView = (ImageView) mRootView.findViewById(R.id.toolbar_image_view);

        mRootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        bindViews();

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mColumnNumber > 1) {
            // set dialog window size
            int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }
    }

    // helper methods
    private void bindViews() {

        if (mRootView == null) {
            return;
        }

        TextView titleTextView = (TextView) mRootView.findViewById(R.id.detail_article_title);
        TextView subtitleTextView = (TextView) mRootView.findViewById(R.id.detail_article_subtitle);
        TextView bodyTextView = (TextView) mRootView.findViewById(R.id.detail_article_body);

        if (mCursor != null) {

            mRootView.setVisibility(View.VISIBLE);

            titleTextView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            subtitleTextView.setText(
                    DateUtils.getRelativeTimeSpanString(
                            mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString() + " "
                            + mCursor.getString(ArticleLoader.Query.AUTHOR));

            bodyTextView.setText(Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)));

            setToolbarImageView();
            setFontForLogo();

        } else {
            mRootView.setVisibility(View.GONE);
            titleTextView.setText("N/A");
            subtitleTextView.setText("N/A");
            bodyTextView.setText("N/A");
        }
    }
    private void setToolbarImageView() {

        ImageLoader.ImageListener imageListener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap != null) {

                    Palette p = Palette.from(bitmap).generate();
                    Palette.Swatch vs = p.getVibrantSwatch();

                    if (vs != null) {
                        mVibrantColor = vs.getRgb();
                    }

                    mRootView.findViewById(R.id.title_linear_layout).setBackgroundColor(mVibrantColor);
                    mToolbarImageView.setImageBitmap(imageContainer.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };

        ImageLoaderHelper.getInstance(getActivity()).getImageLoader()
                .get(mCursor.getString(ArticleLoader.Query.PHOTO_URL), imageListener);
    }
    private void setFontForLogo() {
        TextView logoTextView = (TextView) mRootView.findViewById(R.id.toolbar_logo_text_view);
        logoTextView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "UnifrakturMaguntia-Book.ttf"));
    }



    // ------------------- loader callbacks -------------------

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }

        bindViews();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        bindViews();
    }

}
