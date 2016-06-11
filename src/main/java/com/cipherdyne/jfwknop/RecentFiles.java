/* 
 * Copyright (C) 2016 Franck Joncourt <franck.joncourt@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
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
