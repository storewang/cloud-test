package com.alibaba.csp.sentinel.dashboard.dao.entity;

import lombok.Data;

/**
 * Sentinel Version
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class SentinelVersion {
    private int majorVersion;
    private int minorVersion;
    private int fixVersion;
    private String postfix;

    public SentinelVersion() {
        this(0, 0, 0);
    }

    public SentinelVersion(int major, int minor, int fix) {
        this(major, minor, fix, null);
    }

    public SentinelVersion(int major, int minor, int fix, String postfix) {
        this.majorVersion = major;
        this.minorVersion = minor;
        this.fixVersion = fix;
        this.postfix = postfix;
    }

    /**
     * 000, 000, 000
     */
    public int getFullVersion() {
        return majorVersion * 1000000 + minorVersion * 1000 + fixVersion;
    }

    public SentinelVersion setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }
    public SentinelVersion setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }
    public SentinelVersion setFixVersion(int fixVersion) {
        this.fixVersion = fixVersion;
        return this;
    }
    public SentinelVersion setPostfix(String postfix) {
        this.postfix = postfix;
        return this;
    }
    public boolean greaterThan(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() > version.getFullVersion();
    }

    public boolean greaterOrEqual(SentinelVersion version) {
        if (version == null) {
            return true;
        }
        return getFullVersion() >= version.getFullVersion();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        SentinelVersion that = (SentinelVersion)o;

        if (getFullVersion() != that.getFullVersion()) { return false; }
        return postfix != null ? postfix.equals(that.postfix) : that.postfix == null;
    }

    @Override
    public int hashCode() {
        int result = majorVersion;
        result = 31 * result + minorVersion;
        result = 31 * result + fixVersion;
        result = 31 * result + (postfix != null ? postfix.hashCode() : 0);
        return result;
    }
}
