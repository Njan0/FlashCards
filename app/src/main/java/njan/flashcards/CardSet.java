package njan.flashcards;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardSet implements Serializable {
    public static class Card implements Serializable {
        private String front;
        private String back;

        public Card(String front, String back) {
            this.front = front;
            this.back = back;
        }
    }

    private static final String ns = null;
    private static final String CARD_SET_NAME = "CardSet";
    private static final String CARD_NAME = "Card";
    private static final String FRONT_NAME = "Front";
    private static final String BACK_NAME = "Back";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String TEXT_SIZE_ATTRIBUTE = "textSize";

    private static final String DEFAULT_NAME = "unnamed";
    private static final float DEFAULT_TEXT_SIZE = 32;

    private String name;
    private float textSize;
    private List<Card> cards;

    public CardSet(XmlPullParser parser) {
        name = DEFAULT_NAME;
        textSize = DEFAULT_TEXT_SIZE;
        cards = new ArrayList<Card>();

        try {
            while (next(parser) != XmlPullParser.START_TAG) {
                if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
                    // unexpected end
                    name = null;
                    return;
                }
            }
            readSet(parser);
        } catch (XmlPullParserException | IOException e) {
            name = null;
            cards.clear();
        }
    }

    public String getName() {
        return name;
    }

    public float getSize() {
        return textSize;
    }

    public void setName(String newName) {
        name = newName;
    }

    public boolean isEmpty() { return cards.isEmpty(); }

    /**
     * Generate a list of front/back pairs and
     * shuffle the result.
     * @return Shuffled list of front/back pairs
     */
    public List<Pair<String, String>> generateCards() {
        List<Pair<String, String>> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(new Pair<>(card.front, card.back));
        }

        Collections.shuffle(result);
        return result;
    }

    /**
     * Custom next which skips whitespace text
     * @param parser
     * @return current eventType
     */
    private static int next(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() == XmlPullParser.TEXT) {
            if (!parser.isWhitespace())
                break;
        }

        return parser.getEventType();
    }

    /**
     * Read card set
     * @param parser
     */
    private void readSet(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, CARD_SET_NAME);

        String nameAttr = parser.getAttributeValue(null, NAME_ATTRIBUTE);
        if (nameAttr != null) {
            name = nameAttr;
        }

        try {
            String textSizeAttr = parser.getAttributeValue(null, TEXT_SIZE_ATTRIBUTE);
            if (textSizeAttr != null) {
                textSize = Float.parseFloat(textSizeAttr);
            }
        } catch (NumberFormatException e) { }

        while (next(parser) != XmlPullParser.END_TAG) {
            String name = parser.getName();
            if (CARD_NAME.equals(name)) {
                // read card and add to set
                cards.add(readCard(parser));
            } else if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
                throw new IllegalStateException();
            } else {
                // skip elements which are not cards
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, CARD_SET_NAME);
    }

    /**
     * Read a card
     * @param parser
     * @return
     */
    private static Card readCard(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, CARD_NAME);
        next(parser);

        // read <front>front_text</front>
        String front = readTextTag(parser, FRONT_NAME);
        next(parser);

        // read <back>back_text</back>
        String back = readTextTag(parser, BACK_NAME);
        next(parser);

        parser.require(XmlPullParser.END_TAG, ns, CARD_NAME);

        return new Card(front, back);
    }

    /**
     * Read element which only contains text:
     * <name>text</name>
     * @param parser
     * @param name
     * @return text
     */
    private static String readTextTag(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, name);
        String result = parser.nextText();
        parser.require(XmlPullParser.END_TAG, ns, name);

        return result;
    }

    /**
     * Skip current element
     * @param parser
     */
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth > 0) {
            switch (next(parser)) {
                case XmlPullParser.END_TAG:
                    --depth;
                    break;
                case XmlPullParser.START_TAG:
                    ++depth;
                    break;
            }
        }
    }
}
