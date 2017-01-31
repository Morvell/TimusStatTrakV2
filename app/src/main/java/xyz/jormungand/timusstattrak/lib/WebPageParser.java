package xyz.jormungand.timusstattrak.lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebPageParser {

    public static  OriginalUserTimusData PageParse(String string,String userName){
        Pattern p = Pattern.compile("("+ userName+"</A></TD><TD>)[0-9]*(</TD><TD>)[0-9]*(</TD><TD>)[0-9]*" +
                    "(\\s)(\\w){3}\\s(\\d){4}\\s(\\d){2}.?(\\d){2}");

        Matcher m = p.matcher(string);
        String stringAfterMatcher = null;
        if (m.find()){
            stringAfterMatcher = m.group();
        }
        String splitStringAfterMatcher = null;
        if (stringAfterMatcher != null) {
            splitStringAfterMatcher = stringAfterMatcher.replaceAll("(</TD><TD>)|(</A></TD><TD>)", "@");
        }

        return new OriginalUserTimusData(splitStringAfterMatcher);
    }
}
