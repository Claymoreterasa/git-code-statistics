package edu.xidian.ridelab.counters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Author: cwz
 * Time: 2018/4/29
 * Description:
 */
public interface Counter {
    Logger LOGGER = LoggerFactory.getLogger(Counter.class);
    boolean filter(String fileName);
    Statistics count(String fileName, InputStream fileStream);
}
