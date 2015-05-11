package test.dorothy.graduation.test;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/2.
 */
public class BookXmlParser {

    public Book parser(String bookXml) {
        Book book = new Book();
        XmlPullParser parser = Xml.newPullParser();
        Reader reader = new StringReader(bookXml);
        try {
            parser.setInput(reader);
            parser.nextTag();
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.END_TAG) {
                    break;
                } else if (parser.getEventType() == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (tag != null && tag.equals("title")) {
                        book.setTitle(parser.nextText());
                    } else if (tag != null && tag.equals("author")) {
                        book.setAuthor(parser.nextText());
                    } else if (tag != null && tag.equals("content")) {
                        List<String> paras = readParas(parser);
                        book.setParas(paras);
                    } else {
                        skip(parser);
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return book;
    }

    private List<String> readParas(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> paras = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag != null && tag.equals("p")) {
                paras.add(parser.nextText());
            }

        }
        Log.d("paras ","paras " + paras.toString());
        return paras;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
