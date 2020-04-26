package com.glyart.mcitaliavotes.utils;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class WebUtil {
    
    private static final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    public static JsonElement getJsonData(String stringURL) throws IOException {
        URL url = new URL(stringURL);
        StringBuilder data = new StringBuilder();
        IOUtils.readLines(url.openStream(), Charset.defaultCharset()).forEach(data::append);

        return plugin.getGson().fromJson(data.toString(), JsonElement.class);
    }
    
}
