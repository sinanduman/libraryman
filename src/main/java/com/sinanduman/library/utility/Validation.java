package com.sinanduman.library.utility;

import com.sinanduman.library.model.Member;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class Validation {

    public static boolean isValidMember(Member member) {
        return isValidEmail(member.getEmail())
                && isValidDate(member.getJoinDate())
                && hasTextAndLessThan(member.getName(), 100)
                && hasTextAndLessThan(member.getSurname(), 100)
                && isValidPhoneNumber(member.getPhoneNumber(), 50);
    }

    public static boolean isEmpty(String value) {
        return !StringUtils.hasText(value);
    }

    public static boolean isValidEmail(String value) {
        if (isEmpty(value)) return false;

        Pattern pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(value).matches();
    }

    public static boolean isValidPhoneNumber(String value, int limit) {
        if (isEmpty(value) || value.length() > limit) return false;

        Pattern pattern = Pattern.compile("^0?[1-9]{3}[ ]?[0-9]{3}[ ]?[0-9]{2}[ ]?[0-9]{2}$");
        return pattern.matcher(value).matches();
    }

    public static boolean isValidPhoneNumber(String value) {
        return isValidPhoneNumber(value, 50);
    }

    public static boolean isValidDate(String value) {
        if (isEmpty(value)) return false;
        Pattern pattern = Pattern.compile("^[12][0-9]{3}-[0-9]{2}-[0-9]{2}[T ][0-9]{2}:[0-9]{2}:[0-9]{2}.*");
        return pattern.matcher(value).matches();
    }

    public static boolean hasTextAndLessThan(String value, int limit) {
        if (!StringUtils.hasText(value))
            return false;
        return value.length() <= limit;
    }
}
