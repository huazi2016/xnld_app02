package com.zxqy.xunilaidian.service

import com.zxqy.xunilaidian.utils.SpUtils

object SettingHelper {

    private const val KEY_PHONE = "PHONE"
    private const val KEY_TIME = "TIME"
    private const val KEY_CONTACT = "CONTACT"
    private const val KEY_LOCAL = "LOCAL"
    private const val KEY_RINGTONE_URI = "KEY_RINGTONE_URI"
    private const val KEY_RINGTONE_TITLE = "KEY_RINGTONG_TITLE"
    private const val KEY_VIBRATOR = "KEY_VIBRATOR"
    private const val KEY_WALL_PAGER_TITLE = "KEY_WALL_PAGER_TITLE"
    private const val KEY_REPEAT_SWITCH = "KEY_REPEAT_SWITCH"
    private const val KEY_REPEAT_INTERVAL = "KEY_REPEAT_INTERVAL"
    private const val KEY_REPEAT_COUNT = "KEY_REPEAT_COUNT"
    private const val KEY_RADOM_CONTACT_SWITCH = "KEY_RADOM_CONTACT_SWITCH"
    private const val KEY_AVATAR = "KEY_AVATAR"

    fun setPhone(phone: String) {
        SpUtils.getInstance().putString(KEY_PHONE, phone)
    }

    fun getPhone(): String? {
        return SpUtils.getInstance().getString(KEY_PHONE, "13800138000")
    }

    fun setContact(contact: String) {
        SpUtils.getInstance().putString(KEY_CONTACT, contact)
    }

    fun getContact(): String? {
        return SpUtils.getInstance().getString(KEY_CONTACT, "张三")
    }

    fun setLocal(local: String) {
        SpUtils.getInstance().putString(KEY_LOCAL, local)
    }

    fun getLocal(): String? {
        return SpUtils.getInstance().getString(KEY_LOCAL, "北京")
    }

    fun setTime(time: Int) {
        SpUtils.getInstance().putInt(KEY_TIME, time)
    }

    fun getTime(): Int? {
        return SpUtils.getInstance().getInt(KEY_TIME, 5)
    }



    fun setRingtoneUri(uri: String) {
        SpUtils.getInstance().putString(KEY_RINGTONE_URI, uri)
    }

    fun getRingtoneUri(): String? {
        return SpUtils.getInstance().getString(KEY_RINGTONE_URI, "")
    }


    fun setRingtoneTitle(ringtoneTitle: String) {
        SpUtils.getInstance().putString(KEY_RINGTONE_TITLE, ringtoneTitle)
    }

    fun getRingtoneTitle(): String? {
        return SpUtils.getInstance().getString(KEY_RINGTONE_TITLE, "默认")
    }

    fun setWallPagerTitle(wallPagerTitle: String) {
        SpUtils.getInstance().putString(KEY_WALL_PAGER_TITLE, wallPagerTitle)
    }

    fun getWallPagerTitle(): String {
        return SpUtils.getInstance().getString(KEY_WALL_PAGER_TITLE, "默认")
    }

    fun setVibrator(vibrator: Boolean) {
        SpUtils.getInstance().putBoolean(KEY_VIBRATOR, vibrator)
    }

    fun getVibrator(): Boolean {
        return SpUtils.getInstance().getBoolean(KEY_VIBRATOR, true)
    }

    /*
        private const val KEY_REPEAT_SWITCH = "KEY_REPEAT_SWITCH"
    private const val KEY_REPEAT_INTERVAL = "KEY_REPEAT_INTERVAL"
    private const val KEY_REPEAT_COUNT = "KEY_REPEAT_COUNT"

     */

    fun setRepeat(repeat: Boolean) {
        SpUtils.getInstance().putBoolean(KEY_REPEAT_SWITCH, repeat)
    }
    fun getRepeat(): Boolean {
        return SpUtils.getInstance().getBoolean(KEY_REPEAT_SWITCH, false)
    }

    fun setRepeatInterval(interval: Int) {
        SpUtils.getInstance().putInt(KEY_REPEAT_INTERVAL, interval)
    }
    fun getRepeatInterval(): Int {
        return SpUtils.getInstance().getInt(KEY_REPEAT_INTERVAL, 2)
    }

    fun setRepeatCount(interval: Int) {
        SpUtils.getInstance().putInt(KEY_REPEAT_COUNT, interval)
    }
    fun getRepeatCount(): Int {
        return SpUtils.getInstance().getInt(KEY_REPEAT_COUNT, 2)
    }

    fun setRandomContact(random: Boolean) {
        SpUtils.getInstance().putBoolean(KEY_RADOM_CONTACT_SWITCH, random)
    }
    fun getRandomContact(): Boolean {
        return SpUtils.getInstance().getBoolean(KEY_RADOM_CONTACT_SWITCH, false)
    }
    fun setAvatarPath(avatar: String) {
        SpUtils.getInstance().putString(KEY_AVATAR, avatar)
    }

    fun getAvatarPath(): String {
        return SpUtils.getInstance().getString(KEY_AVATAR, "")
    }

}