package com.ictk.issuance.common.io;

import com.ictk.issuance.common.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;


public class ProcessOutputStream extends OutputStream {
    
    private static Logger logger = LogManager.getLogger(ProcessOutputStream.class);
    
    private final List<String> lines = new LinkedList<String>();
    
    protected void processLine(String line, int level) {
        // logger.debug("process line : "+line);
        lines.add(line);
    }   
    public String getOutput() {
        return StringUtils.join(lines, "\n");
    }
    
    /** Initial buffer size. */
    private static final int INTIAL_SIZE = 132;

    /** Carriage return */
    private static final int CR = 0x0d;

    /** Linefeed */
    private static final int LF = 0x0a;

    /** the internal buffer */
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream( INTIAL_SIZE);

    private boolean skip = false;

    private OSType ostype;
    private final int level;

    /**
     * Creates a new instance of this class.
     * Uses the default level of 999.
     */
    public ProcessOutputStream() {
        this(OSType.generic, 999);
    }
    
    public ProcessOutputStream(OSType ostype) {
        this(ostype, 999);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param level loglevel used to log data written to this stream.
     */
    public ProcessOutputStream(OSType ostype, final int level) {
        this.ostype = ostype;
        this.level = level;
    }

    /**
     * Write the data to the buffer and flush the buffer, if a line separator is
     * detected.
     *
     * @param cc data to log (byte).
     * @see OutputStream#write(int)
     */
    @Override
    public void write(final int cc) throws IOException {
        final byte c = (byte) cc;
        if (c == '\n' || c == '\r') {
            if (!skip) {
                processBuffer();
            }
        } else {
            buffer.write(cc);
        }
        skip = c == '\r';
    }

    /**
     * Flush this log stream.
     *
     * @see OutputStream#flush()
     */
    @Override
    public void flush() {
        if (buffer.size() > 0) {
            processBuffer();
        }
    }

    /**
     * Writes all remaining data from the buffer.
     *
     * @see OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        if (buffer.size() > 0) {
            processBuffer();
        }
        super.close();
    }

    /**
     * @return the trace level of the log system
     */
    public int getMessageLevel() {
        return level;
    }

    /**
     * Write a block of characters to the output stream
     *
     * @param b the array containing the data
     * @param off the offset into the array where data starts
     * @param len the length of block
     * @throws IOException if the data cannot be written into the stream.
     * @see OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(final byte[] b, final int off, final int len)
            throws IOException {
        // find the line breaks and pass other chars through in blocks
        int offset = off;
        int blockStartOffset = offset;
        int remaining = len;
        while (remaining > 0) {
            while (remaining > 0 && b[offset] != LF && b[offset] != CR) {
                offset++;
                remaining--;
            }
            // either end of buffer or a line separator char
            final int blockLength = offset - blockStartOffset;
            if (blockLength > 0) {
                buffer.write(b, blockStartOffset, blockLength);
            }
            while (remaining > 0 && (b[offset] == LF || b[offset] == CR)) {
                write(b[offset]);
                offset++;
                remaining--;
            }
            blockStartOffset = offset;
        }
    }

    /**
     * Converts the buffer to a string and sends it to {@code processLine}.
     */
    protected void processBuffer() {
        try {
            if(ostype==OSType.windows) {
                String ms949str = buffer.toString("MS949");
                processLine(new String(ms949str.getBytes(), "UTF8"));
            } else 
                processLine(buffer.toString());
            buffer.reset();
        } catch(Exception e) {
            logger.error("error ***** "+e.getMessage(), e);
        }
    }

    /**
     * Logs a line to the log system of the user.
     *
     * @param line
     *            the line to log.
     */
    protected void processLine(final String line) {
        processLine(line, level);
    }

    /**
     * Logs a line to the log system of the user.
     *
     * @param line the line to log.
     * @param logLevel the log level to use
     */
    // protected abstract void processLine(final String line, final int logLevel);
    
}