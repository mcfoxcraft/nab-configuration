package com.froobworld.nabconfiguration.patcher.jobs;

import com.froobworld.nabconfiguration.patcher.structure.YamlElement;
import com.froobworld.nabconfiguration.patcher.structure.YamlField;
import com.froobworld.nabconfiguration.patcher.structure.YamlFile;
import com.froobworld.nabconfiguration.patcher.structure.YamlSection;

import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.regex.Pattern;

public class MoveJob implements PatchJob {
    private String fromKey;
    private String toKey;
    private String before;

    public MoveJob(Properties properties) {
        fromKey = properties.getProperty("from");
        toKey = properties.getProperty("to");
        before = properties.getProperty("before");
    }


    @Override
    public void modify(YamlFile yamlFile) {
        String[] fromKeySplit = fromKey.split(Pattern.quote("."));
        String[] toKeySplit = toKey.split(Pattern.quote("."));
        String finalFromKey = fromKeySplit[fromKeySplit.length - 1];
        String finalToKey = toKeySplit[toKeySplit.length - 1];
        List<YamlElement> fromDestination = yamlFile.getElements();
        List<YamlElement> toDestination = yamlFile.getElements();
        for (int i = 0; i < fromKeySplit.length - 1; i++) {
            String nextKey = fromKeySplit[i];
            boolean found = false;
            for (YamlElement element : fromDestination) {
                if (element instanceof YamlSection) {
                    if (((YamlSection) element).getKey().equals(nextKey)) {
                        fromDestination = ((YamlSection) element).elements();
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                throw new IllegalStateException();
            }
        }
        for (int i = 0; i < toKeySplit.length - 1; i++) {
            String nextKey = toKeySplit[i];
            boolean found = false;
            for (YamlElement element : toDestination) {
                if (element instanceof YamlSection) {
                    if (((YamlSection) element).getKey().equals(nextKey)) {
                        toDestination = ((YamlSection) element).elements();
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                throw new IllegalStateException();
            }
        }
        YamlElement toMove = null;
        for (ListIterator<YamlElement> iterator = fromDestination.listIterator(); iterator.hasNext();) {
            YamlElement next = iterator.next();
            if (next instanceof YamlField) {
                if (((YamlField) next).getKey().equals(finalFromKey)) {
                    toMove = next;
                    iterator.remove();
                }
            } else if (next instanceof YamlSection) {
                if (((YamlSection) next).getKey().equals(finalFromKey)) {
                    toMove = next;
                    iterator.remove();
                }
            }
        }

        if (before == null) {
            toDestination.add(toMove);
        } else {
            int index = 0;
            for (YamlElement element : toDestination) {
                if (element instanceof YamlField) {
                    if (((YamlField) element).getKey().equals(finalFromKey)) {
                        break;
                    }
                } else if (element instanceof YamlSection) {
                    if (((YamlSection) element).getKey().equals(finalFromKey)) {
                        break;
                    }
                }
                index++;
            }
            if (toMove instanceof YamlField) {
                ((YamlField) toMove).setKey(finalToKey);
            } else if (toMove instanceof YamlSection) {
                ((YamlSection) toMove).setKey(finalToKey);
            }
            toDestination.add(index, toMove);
        }
    }
}
