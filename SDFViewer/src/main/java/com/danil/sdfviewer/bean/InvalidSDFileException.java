package com.danil.sdfviewer.bean;

/**
 *
 * @author danil
 */
public class InvalidSDFileException extends Exception{
    public InvalidSDFileException(String message) {
            super(message);
        }

    InvalidSDFileException() {
        super();
    }
}
