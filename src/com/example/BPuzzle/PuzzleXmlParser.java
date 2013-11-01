package com.example.BPuzzle;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Konráð
 * Date: 1.11.2013
 * Time: 00:09
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleXmlParser {

    private  static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException,IOException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(in,null);
            parser.nextTag();

            return readFeed(parser);
        }
        catch (XmlPullParserException xEx){
               throw xEx;
        }
        catch (IOException e){
               throw e;
        }
        finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException,IOException{
        ArrayList<Puzzle> entries = new ArrayList<Puzzle>();

        parser.require(XmlPullParser.START_TAG,ns,"challenge");

        while(parser.next() != XmlPullParser.END_TAG){

            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();

            if(name.equals("puzzle")){
                entries.add(readPuzzle(parser));
            }
            else{
                skip(parser);
            }
        }
        return entries;
    }

    private Puzzle readPuzzle(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG,ns,"puzzle");

        String id = parser.getAttributeValue(null,"id");
        String lvl = null;
        String[] setup = null;

        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();
            if(name.equals("level")){
                lvl = readLevel(parser);
            } else if(name.equals("setup")){
                setup = readSetup(parser);
            } else {
                skip(parser);
            }
        }
        return new Puzzle(id,lvl,setup);
    }

    private String readLevel(XmlPullParser parser)  throws XmlPullParserException, IOException{

        parser.require(XmlPullParser.START_TAG,ns,"level");
        String level = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"level");
        return level;
    }

    private String[] readSetup(XmlPullParser parser) throws XmlPullParserException, IOException{

        parser.require(XmlPullParser.START_TAG,ns,"setup");
        String setup = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"setup");
        return  setup.split(", ");
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException{

        String text = "";
        if(parser.next() == XmlPullParser.TEXT){
            text = parser.getText();
            parser.nextTag();
        }
        return text;
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
