package com.cipherdyne.jfwknop;

import java.util.LinkedList;

public class RecentFiles extends LinkedList<String> {

    private static final long serialVersionUID = -5400390659162560804L;
    private final int         maxFiles;

    /**
     * Constructor
     * 
     * @param maxFiles Number of files to save in the list
     */
    public RecentFiles(final int maxFiles) {
        this.maxFiles = maxFiles;
    }

    @Override
    public boolean add(final String filename) {

        final boolean actionPerformed = false;

        final int pos = lookup(filename);
        if (pos < size()) {
            remove(pos);
        }

        addFirst(filename);
        if (size() > this.maxFiles)
            removeLast();

        return actionPerformed;
    }

    private int lookup(final String match) {
        int pos;
        for (pos = 0; pos < size(); pos++) {
            if (match.equals(get(pos)))
                break;
        }
        return pos;
    }
}
