package com.example.tamar.micevic_feelsbook;

// Exception to catch if an inputted comment is greater than 100 characters
public class CommentTooLongException extends Exception {
    CommentTooLongException() {
        super("The comment inputted is too long. Maximum characters is 100.");
    }
}
