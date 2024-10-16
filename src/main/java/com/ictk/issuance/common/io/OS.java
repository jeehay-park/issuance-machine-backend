package com.ictk.issuance.common.io;


public class OS {
    
    
    private OS() {
        // nada
    }
    
    public static String getOsName() {
        return System.getProperty("os.name", "unknown");
    }
    
    public static OSType getOSType() {
        return OSType.toOSType(platform());
    }
    
    public static String platform() {
        String osname = System.getProperty("os.name", "generic").toLowerCase();
        
        if (osname.contains("window")) {
            return "windows";
        }
        else if ( osname.contains("linux") || osname.contains("freebsd") ) {
            return "linux";
        }
        else if (osname.contains("mac") || osname.contains("darwin") ) {
            return "mac";
        }
        else if (osname.contains("sun os") || osname.contains("sunos") || osname.contains("solaris") ) {
            return "solaris";
        }
        else if (osname.contains("hpux") || osname.contains("hp-ux") ) {
            return "hpux";
        }
        else if (osname.contains("aix") ) {
            return "aix";
        }
        else
            return "generic";
    }
    
    public static boolean isWindows() {
        return (getOsName().toLowerCase().indexOf("windows") >= 0);
    }
    
    public static boolean isLinux() {
        return getOsName().toLowerCase().indexOf("linux") >= 0;
    }
    
    public static boolean isUnix() {
        final String os = getOsName().toLowerCase();
        
        // XXX: this obviously needs some more work to be "true" in general (see
        // bottom of file)
        if ((os.indexOf("sunos") >= 0) || (os.indexOf("linux") >= 0)) {
            return true;
        }
        
        if (isMac() && (System.getProperty("os.version", "").startsWith("10."))) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isMac() {
        final String os = getOsName().toLowerCase();
        return os.contains("mac") || os.contains("darwin");
    }

    public static boolean isSolaris() {
        final String os = getOsName().toLowerCase();
        // return os.indexOf("sunos") >= 0;
        return os.contains("sun os") || os.contains("sunos") || os.contains("solaris");
    }
    
    public static boolean isHpux() {
        final String os = getOsName().toLowerCase();
        return os.contains("hpux") || os.contains("hp-ux") ;
    }
    
    public static boolean isAix() {
        final String os = getOsName().toLowerCase();
        return os.contains("aix") ;
    }
    
//    public static String findWindowsSystemRoot() {
//        if (!isWindows()) {
//            return null;
//        }
//        final char begin = 'c';
//        final char end = 'z';
//
//        for (char drive = begin; drive < end; drive++) {
//            File root = new File(drive + ":\\WINDOWS");
//            if (root.exists() && root.isDirectory()) {
//                return root.getAbsolutePath().toString();
//            }
//            root = new File(drive + ":\\WINNT");
//            if (root.exists() && root.isDirectory()) {
//                return root.getAbsolutePath().toString();
//            }
//        }
//        return null;
//    }
    
}
