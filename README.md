# XYZReader
Project 5 from Udacity nanodegree

## Colors
We use Amber colors for our mini palette. This is our fix to this user feedback:
> Kagure says: “The color scheme is really sad and I shouldn't feel sad.”

We use `Indigo A200` as an accent color. We use Palette to get vibrant color from image on detail screen so we don't
have single color on this screen. But all these colors are close to red so we can use `Indigo` as an accent.

## Phone - List screen
1. We change from cards to simple list as per recommendations in class and guide to material
[design](https://www.google.com/design/spec/components/cards.html#cards-usage): "A quickly scannable list,
instead of cards, is an appropriate way to represent homogeneous content that doesn't have many actions.".
We also add divider to this list - there is no default divider in `RecyclerView`.
2. We don't use provided `DynamicHeightNetworkImageView` - it seems to preserve an image aspect ratio. We prefer to have
 all images displayed in the same size so we use `NetworkImageView` and `scaleType="centerCrop"`. Majority of our
11 images has ratio about 1.5 so we use it for our `ImageView`.
3. We use standard `Roboto` font for text and `UnifrakturMaguntia-Book` for the Title. It's one of a recommended
pattern from the class. Our reader with this font looks like a newspaper. The removed logo is used bold or black font
weight. It can be associated with fat people and not healthy food. We use `Display1` font for app title - `Headline` that
is recommended in guide is probably too small with our custom font.
4. We make our toolbar as an action bar with `setSupportActionBar(mToolbar)` and add overflow menu.
We use menu to add `Snackbar` upon click on `About`. We also wrap toolbar in `CollapsingToolbarLayout` and `AppBarLayout`
to make it collapsible and work with `CoordinatorLayout`.
5. We use screen edge left and right margins: 16dp, margin between image and text is also 16dp. We also set vertical
dimensions for our list item to 72dp (56dp - image, 8dp - margin top and 8dp - margin bottom). So we can not actually get
72dp from left edge to text. Probably it's not applicaple in our case. We also set `list_app_bar_layout_height=152` that is
sum of first 3 positions from *List with subheadings* (Status bar: 24dp Toolbar: 56dp Title and list items: 72dp) - see
[here](https://www.google.com/design/spec/layout/metrics-keylines.html#metrics-keylines-keylines-spacing). We follow provided
recommendations but probably we'd prefer slightly increase images.

## Phone - Detail screen
1. We use the same approach as in the list screen: design library classes, collapsible toolbar with parallax effect etc.
2. We use background image with ratio 2:3 so we use `list_app_bar_layout_height=240` (based on Nexus 5 size). We use `72dp`
left margin for logo as mentioned in guide. We use transparent `StatusBar` (we set it using `Theme.Bacon.Detail`) so we
increase `detail_toolbar_height=80dp` (status bar 24dp + toolbar 56dp).

## Phone - landscape
We use expand pattern from *Adaptive UI* section of the guide. An example of this pattern - `Play` app. When we select a
movie and rotate a screen we will see the same pattern. In other words we wrap our `Recycler View` or article into
 card and put it on top of a toolbar. We don't use elevation like in class (with Sunshine) but rather simply delete
 `app:layout_behavior` flags. We center logo so it will be revealed after scrolling. We use width of the card as a balance
between readability (45-75 letters) and unused space.

## Tablet
1. We increase `list_app_bar_layout_height=168`. Again this is a sum of Status bar: 24dp Toolbar: 64dp
Title and list items: 80dp.
2. We increase fonts on a tablet using custom styles. We define `<style name="TextAppearance.Bacon.Display1"
parent="@style/TextAppearance.AppCompat.Display1">` and override `<item name="android:textSize">42dp</item>`
only for tablet layout. This approach let us use the same layout file both for phone and tablet layouts.
3. We use `CardView` for tablet layout instead of a simple list. We use `<integer name="column_number">3</integer>`
to manage number of columns in portrait and landscape. We also use this parameter to choose proper layout manager in
`ArticleListActivity`. We use 3 columns for portrait and 4 for landscape.
4. We define image sizes specifically for Nexus 5 and Nexus 7. Probably we have to be more generic especially for a tablet.
5. We use a dialog (adaptive ui pattern suggested in class) to show an article instead of traditional detail screen
or 2-pane layout. This is kind of new pattern so there are a lot of questions: what sizes should be this fragment,
how to use AppCombat.Dialog theme etc.



