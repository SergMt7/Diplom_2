package model;

import org.apache.commons.lang3.RandomStringUtils;
public class UserGenerator {
    public static User random() {
        return new User(RandomStringUtils.randomAlphabetic(5) + "@yandex.ru", "123123", "Name");
    }
}
