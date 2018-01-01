package com.instadownloader.instadpdownloader.Utils;

/**
 * Created by ayush on 23/1/17.
 */

public class Constants {
    public static String BILLABONG_TTF="Billabong.ttf";
    public static final short ACTION_DOWNLOAD_DP=1;
    public static final short ACTION_VIEW_PHOTOS=2;
    public static final short ACTION_SERVICE_VIEW_PHOTOS=3;
    public static final String BUNDLE_IMAGE_URL="imageUrl";
    public static final String BUNDLE_IMAGES_URL_LIST="imageUrls";
    public static final String BUNDLE_IMAGES_URL_THUMBNAIL_LIST ="imageUrlsThumb";
    public static final String FOLDER_NAME="/Insta Downloads";

    public static final String REGEX_FOR_FILTER_USERNAME="((https?:\\/\\/)?(www\\.)?instagram\\.com\\/)?([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?)";
    public static final String REGEX_FOR_FILTER_USERNAME_JAVA="^((https?://)?(www.)?instagram.com/)?([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:.(?!.))){0,28}(?:[A-Za-z0-9_]))?)$";
}
