package com.ictk.issuance.common.io;

public enum OSType {
    
    windows, linux, solaris, mac, hpux, aix, generic;
    
    public static OSType toOSType(String str) {
        try {
            return valueOf(str);
        } catch(Exception e) {
            return generic;
        }
    }
}

