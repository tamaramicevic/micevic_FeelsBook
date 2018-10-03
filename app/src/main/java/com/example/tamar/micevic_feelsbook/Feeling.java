package com.example.tamar.micevic_feelsbook;

import java.io.Serializable;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Feeling implements Serializable {
    private Date date;
    private String feeling;
    private String comment;
    private static final Integer MAX_CHARS = 100;

    // constructor without a comment added
    Feeling(String feeling, Date date) {
        this.feeling = feeling;
        this.date = date;
        this.comment = "";
    }

    // constructor that is completely empty
    Feeling() {
        this.feeling = "No current feeling";
        this.date = Calendar.getInstance().getTime();
        this.comment = "";
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public void setComment(String comment) throws CommentTooLongException {
        if (comment.length() <= this.MAX_CHARS ) {
            this.comment = comment;
        } else {
            throw new CommentTooLongException();
        }
        this.comment = comment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFeeling() {
        return this.feeling;
    }

    public String getComment() {
        return this.comment;
    }

    public Date getDate() {
        return this.date;
    }

    public String getDateAsString() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String s = formatter.format(date);
        return s;
    }

    public void setDateAsDateObj(String strDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = formatter.parse(strDate);
            this.date = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
