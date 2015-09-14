# XYZReader
Project 5 from Udacity nanodegree

## Colors
We use Amber colors for our mini palette. This is our fix to this user feedback:
> Kagure says: “The color scheme is really sad and I shouldn't feel sad.”

We use `Indigo A200` as an accent color. We use Palette to get vibrant color from image on detail screen so we don't have single color on this screen. But all these colors are close to red so we can use `Indigo` as an accent.

## List screen
1. We change from cards to simple list as per recommendations in class and guide to material [design](https://www.google.com/design/spec/components/cards.html#cards-usage): "A quickly scannable list, instead of cards, is an appropriate way to represent homogeneous content that doesn't have many actions.". We also add divider to this list - there is no default divider in `RecyclerView`.
2. We don't use provided `DynamicHeightNetworkImageView` - it seems to preserve an image aspect ratio. We prefer to have all images displayed in the same size so we use `NetworkImageView` and `scaleType="centerCrop"`.
3. We use standard `Roboto` font for text and `UnifrakturMaguntia-Book` for the Title. It's one of a recommended pattern from the class. Our reader with this font looks like a newspaper. The removed logo is used bold or black font weight. It can be associated with fat people and not healthy food.
4. We make our toolbar as an action bar with `setSupportActionBar(mToolbar)` and add overflow menu. We use menu to add `Snackbar` upon click on `About`. We also wrap toolbar in `CollapsingToolbarLayout` and `AppBarLayout` to make it collapsible and work with `CoordinatorLayout`.

## Detail screen

