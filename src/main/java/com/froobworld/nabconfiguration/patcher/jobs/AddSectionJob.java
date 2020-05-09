package com.froobworld.nabconfiguration.patcher.jobs;

import com.froobworld.nabconfiguration.patcher.structure.YamlElement;
import com.froobworld.nabconfiguration.patcher.structure.YamlField;
import com.froobworld.nabconfiguration.patcher.structure.YamlFile;
import com.froobworld.nabconfiguration.patcher.structure.YamlSection;

import java.util.*;
import java.util.regex.Pattern;

public class AddSectionJob implements PatchJob {
    private String key;
    private String value;
    private List<String> comment;
    private String before;

    public AddSectionJob(Properties properties) {
        this.key = properties.getProperty("key");
        this.value = properties.getProperty("value");
        String commentString = properties.getProperty("comment");
        this.comment = commentString == null ? Collections.emptyList() : Arrays.asList(commentString.split("\n"));
        this.before = properties.getProperty("before");
    }


    @Override
    public void modify(YamlFile yamlFile) {
        String[] splitKey = key.split(Pattern.quote("."));
        List<YamlElement> elementList = yamlFile.getElements();
        for (int i = 0; i < splitKey.length; i++) {
            String currentKey = splitKey[i];
            Optional<YamlElement> nextElement = elementList.stream()
                    .filter(element -> {
                        if (element instanceof YamlField) {
                            return ((YamlField) element).getKey().equals(currentKey);
                        } else if (element instanceof YamlSection) {
                            return ((YamlSection) element).getKey().equals(currentKey);
                        }
                        return false;
                    })
                    .findFirst();
            if (i == splitKey.length - 1) {
                if (nextElement.isPresent()) {
                    throw new IllegalArgumentException("Tried to add section with key " + key + ", but element already exists.");
                } else {
                    YamlSection section = new YamlSection(key, comment, value, new ArrayList<>());
                    if (before == null) {
                        elementList.add(section);
                    } else {
                        int index = 0;
                        for (YamlElement element : elementList) {
                            if (element instanceof YamlField) {
                                if (((YamlField) element).getKey().equals(before)) {
                                    break;
                                }
                                index++;
                            } else if (element instanceof YamlSection) {
                                if (((YamlSection) element).getKey().equals(before)) {
                                    break;
                                }
                                index++;
                            }
                        }
                        elementList.add(index, section);
                    }
                }
            } else {
                if (nextElement.isPresent() && nextElement.get() instanceof YamlSection) {
                    elementList = ((YamlSection) nextElement.get()).elements();
                } else {
                    throw new IllegalArgumentException("Tried to add section with key " + key + ", but a section named " + currentKey + " does not exist.");
                }
            }
        }
    }

}