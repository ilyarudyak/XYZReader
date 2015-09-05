# XYZReader
Project 5 from Udacity nanodegree

## Fonts
We use Roboto for everything except Logo. We use `Display1` style for Logo, `Headline` for an article title on detail page, `Subhead` for a title on cards etc. In case of dark font we defined colors (`Black 87%` and `Black 54%`) but we use defaults. We use `style` attribute instead of `android` and `TextAppearance.AppCompat` instead of `TextAppearance.Material`.

| id                        | style           | color           |
| ------------------------- |---------------- | --------------- |
| logo_text_view            | Display1        | auto            |
| item_article_title        | Subhead         | auto            |
| item_article_date_author  | Body2           | secondary_text  |
| detail_article_title      | Title.Inverse   | auto            |
| detail_article_byline     | Subhead.Inverse | auto            |
| detail_article_body       | Body1           | auto            |

For Logo we use `UnifrakturMaguntia`. So our `Reader app` looks like a newspaper. We set font in a usual way - using `Typeface` in `java` code.
