package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.ui.decoration.HorizontalDividerItemDecoration;
import com.example.xyzreader.ui.images.ImageLoaderHelper;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = ArticleListActivity.class.getSimpleName();

    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // set as ActionBar, add navigation and menu
        setToolbar();

        // set font for logo
        setFontForLogo();

        // initialize loader
        getLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            Log.d(TAG, "loading data...");
            refresh();
        }
    }

    // helper methods
    private void setFontForLogo() {
        TextView logoTextView = (TextView) mCoordinatorLayout.findViewById(R.id.toolbar_logo_text_view);
        logoTextView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "UnifrakturMaguntia-Book.ttf"));
    }
    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // use Toolbar as an Action Bar replacement
        setSupportActionBar(mToolbar);

        // remove title - we use custom text field instead
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    private void setArticleAdapter(Cursor cursor) {
        // set layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);

        // set divider
        Drawable divider = getResources().getDrawable(R.drawable.padded_divider);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration(divider));

        ArticleAdapter articleAdapter = new ArticleAdapter(cursor);
        articleAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(articleAdapter);
    }
    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    // ------------------------- menu ---------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.about) {
            Snackbar.make(mCoordinatorLayout, "Project 5 from Udacity nanodegree",
                    Snackbar.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ------------------ loader methods ------------------

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        setArticleAdapter(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    // ------------------- RecyclerView classes -----------------

    private class ArticleAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Cursor mCursor;

        public ArticleAdapter(Cursor cursor) {
            mCursor = cursor;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(ArticleLoader.Query._ID);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item_article, parent, false);
            final ViewHolder vh = new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(getDetailIntent(vh));
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            holder.subtitleView.setText(getSubtitleText());

            // we use setImageUrl() from volley lab to get an image into NetworkImageView
            // we use 2 parameters: (a) url of an image; (b) loader to make this call;
            ImageLoader imageLoader = ImageLoaderHelper.getInstance(ArticleListActivity.this).getImageLoader();
            holder.thumbnailView.setImageUrl(mCursor.getString(ArticleLoader.Query.THUMB_URL), imageLoader);

        }

        @Override
        public int getItemCount() {
            if(mCursor == null) {
                return 0;
            }
            return mCursor.getCount();
        }

        // helper methods
        private String getSubtitleText() {
            return DateUtils.getRelativeTimeSpanString(
                    mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString()
                    + " by "
                    + mCursor.getString(ArticleLoader.Query.AUTHOR);
        }
        private Intent getDetailIntent(ViewHolder vh) {
            int position = vh.getAdapterPosition();
            long id = getItemId(position);
            return new Intent(Intent.ACTION_VIEW, ItemsContract.Items.buildItemUri(id));
        }

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (NetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.item_article_title);
            subtitleView = (TextView) view.findViewById(R.id.item_article_date_author);
        }
    }
}
