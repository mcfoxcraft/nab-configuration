package com.froobworld.nabconfiguration.utils;

import com.froobworld.nabconfiguration.patcher.structure.YamlElement;
import com.froobworld.nabconfiguration.patcher.structure.YamlGibberish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public final class YamlUtils {

    private YamlUtils() {}

    public static boolean isComment(String line) {
        return line.trim().startsWith("#");
    }

    public static List<String> elementsToBody(List<YamlElement> elements, int indentLevel) {
        List<String> lines = new ArrayList<>();
        for (Iterator<YamlElement> iterator = elements.iterator(); iterator.hasNext();) {
            YamlElement element = iterator.next();
            lines.addAll(element.toLines());
            if (iterator.hasNext()) {
                lines.add("");
            }
        }

        return lines.stream()
                .map(string -> (string.isEmpty() ? "" : String.join("", Collections.nCopies(indentLevel, " "))) + string)
                .collect(Collectors.toList());
    }

    public static YamlGibberish commentOut(YamlElement element) {
        List<String> lines = element.toLines();
        return new YamlGibberish(
                lines.stream()
                .map(line -> "#" + line)
                .collect(Collectors.toList())
        );
    }

}
