package com.michael.qwitter.Utils;

import java.util.ArrayList;

public class StatusParser
{

    private static ArrayList<Integer> parse(String str, char tag, char delimeter)
    {
        //Has bug where text ending in # includes that in previous tag
        ArrayList<Integer> hashtagIndices = new ArrayList<>();
        int len = str.length() - 1;
        int start = -1;
        int end;

        if(len == 0 && str.charAt(0) == tag)
        {
            hashtagIndices.add(0);
            hashtagIndices.add(0);
        }

        for(int i = 0; i < len; i++)
        {
            if(str.charAt(i) == tag)    //if we have a hashtag we start a new start, also can serve as an end for an existing hashtag
            {
                if(start < 0)
                {
                    start = i;
                }
                else if (start >= 0)
                {
                    end = i - 1;
                    hashtagIndices.add(start);
                    hashtagIndices.add(end);
                    start = i;
                }
                if (i == len - 1)
                {
                    start = i;
                    end = i;
                    hashtagIndices.add(start);
                    hashtagIndices.add(end);
                }
            }
            else if(str.charAt(i) == delimeter)   //if we have a hashtag and find a mention
            {
                if (start >= 0)
                {
                    end = i - 1;
                    hashtagIndices.add(start);
                    hashtagIndices.add(end);
                    start = -1;
                }
            }
            else if(Character.isWhitespace(str.charAt(i))) //if we have a hashtage and find the end of the word
            {
                if (start >= 0)
                {
                    end = i - 1;
                    hashtagIndices.add(start);
                    hashtagIndices.add(end);
                    start = -1;
                }
            }
            else if(i == len - 1)   //if we have a hashtag and are at the end of the text
            {
                if (start >= 0)
                {
                    end = i + 1;
                    hashtagIndices.add(start);
                    hashtagIndices.add(end);
                    start = -1;
                }
            }
        }

        return hashtagIndices;
    }

    public static ArrayList<Integer> parseForHashTags(String str)
    {
        return parse(str, '#','@');
    }

    public static ArrayList<Integer> parseForMentions(String str)
    {
        return parse(str, '@','#');
    }

}
