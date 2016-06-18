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
package com.cipherdyne.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Utility class that provides SSH helpers
 *
 * @author franck
 */
public class SshUtils {

    // Logger
    static final Logger LOGGER = LogManager.getLogger(SshUtils.class.getName());

    /**
     * Copy file to remote server through SCP. Note: StrictHostKeyChecking is disabled
     *
     * @param hostname Hostname or IP address of the remote SSH server to copy file to
     * @param port Port to use to connect to remote SSH server
     * @param username Username to use to authenticate to the remote SSH server
     * @param password Password to use to authenticate to remote SSH server
     * @param filename Full path to the file to copy
     *
     * @throws java.io.IOException
     * @throws com.jcraft.jsch.JSchException
     */
    static public void scpFile(String hostname, int port, String username, String password, String filename) throws IOException, JSchException {

        String sourceFile = filename;
        String destFile = sourceFile
            .substring(sourceFile.lastIndexOf('/') + 1);
        FileInputStream fis;

        // Create a SSH session and connect to the remote SSH server
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        final Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(password);
        session.connect();

        // Build the command and open a terminal to execute it - exec 'scp -t rfile' remotely
        String command = "scp -t " + destFile;
        final Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // create I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        final InputStream in = channel.getInputStream();

        // Connect to the remote terminal
        channel.connect();

        // Ensure file integrity
        if (checkAck(in) != 0) {
            LOGGER.error("File integrity error");
            throw new IOException("File integrity error");
        }

        final File _lfile = new File(sourceFile);

        // send "C0644 filesize filename", where filename should not include
        // '/'
        final long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (sourceFile.lastIndexOf('/') > 0) {
            command += sourceFile
                .substring(sourceFile.lastIndexOf('/') + 1);
        } else {
            command += sourceFile;
        }
        command += "\n";
        out.write(command.getBytes());
        out.flush();
        if (checkAck(in) != 0) {
            LOGGER.error("File integrity error");
            throw new IOException("File integrity error");
        }

        // send content of sourceFile
        fis = new FileInputStream(sourceFile);
        final byte[] buf = new byte[1024];
        while (true) {
            final int len = fis.read(buf, 0, buf.length);
            if (len <= 0) {
                break;
            }
            out.write(buf, 0, len); // out.flush();
        }
        fis.close();
        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();
        if (checkAck(in) != 0) {
            LOGGER.error("File integrity error");
            throw new IOException("File integrity error");
        }

        // Disconnect the remote terminal
        channel.disconnect();

        // Close the remote session
        session.disconnect();
    }

    /**
     *
     * @param in
     * @return
     * @throws IOException
     */
    static private int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) {
            return b;
        }
        if (b == -1) {
            return b;
        }

        if (b == 1 || b == 2) {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }
}
