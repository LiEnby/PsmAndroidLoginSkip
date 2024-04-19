package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void WriteTxtFile(String txtFile, String txt){
        try {
            System.out.println("Writing: "+txtFile);
            FileWriter txtStream = new FileWriter(txtFile);
            txtStream.write(txt);
            txtStream.close();
        } catch (IOException e) {
            System.out.println("Failed to write file" + txtFile);
        }

    }

    public static void main(String[] args) {

        if(args.length < 4) {
            System.out.println("Usage: <android_id> <psm.apk uid> <email address> <password> <account_id>");
            return;
        }


        StringEncryptor stringEncryptor = new StringEncryptor(args[0], Integer.parseInt(args[1]));
        String emailAddress = args[2];
        String password = args[3];
        long accountId = Long.parseLong(args[4], 16);


        String encryptedEmail = stringEncryptor.encryptString(emailAddress);
        String encryptedPassword = stringEncryptor.encryptString(password);
        String encryptedAccountId = stringEncryptor.encryptString(String.valueOf(accountId));

        System.out.println("Username: "+encryptedEmail);
        System.out.println("Password: "+encryptedPassword);
        System.out.println("AccountId: "+encryptedAccountId);

        new File("shared_prefs").mkdirs();

        WriteTxtFile("shared_prefs/SigninInfo.xml", "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map>\n<string name=\"SignedInUsername\">"+encryptedEmail+"\n</string>\n<boolean name=\"PassSave\" value=\"true\" />\n<string name=\"Password\">"+encryptedPassword+"\n</string>\n<boolean name=\"AutoSignIn\" value=\"true\" />\n</map>\n");
        WriteTxtFile("shared_prefs/com.playstation.psstore_preferences.xml", "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map>\n<boolean name=\"key_upgradeDownloadTableForNeedWifi\" value=\"true\" />\n<string name=\"last_signin_account_id\">"+encryptedAccountId+"\n</string>\n<long name=\"last_signin_account_region\" value=\"2\" />\n<int name=\"key_psstore\" value=\"1\" />\n<int name=\"key_downloader\" value=\"1\" />\n<int name=\"psm_license_agree_version_code\" value=\"1170\" />\n<boolean name=\"key_notDisplayAgainEndOfSupportPreNavi\" value=\"true\" />\n<int name=\"key_xmlcache\" value=\"1\" />\n<string name=\"last_signin_account_country\">US</string>\n<boolean name=\"key_notDisplayAgainContentStartNavi\" value=\"true\" />\n<int name=\"key_startcontent\" value=\"1\" />\n<int name=\"key_nsxevent\" value=\"1\" />\n<boolean name=\"key_upgradeLibraryTableForLocationUseConfirmationDate\" value=\"true\" />\n<int name=\"key_install\" value=\"1\" />\n<string name=\"update_md5\">387ce7e424258aef426aaa5be8a1638a</string>\n<boolean name=\"psm_license_agree\" value=\"true\" />\n<int name=\"key_guestinfo\" value=\"1\" />\n<string name=\"last_signin_account_language\">en</string>\n<int name=\"key_cache\" value=\"2\" />\n<int name=\"key_signinfo\" value=\"2\" />\n</map>\n");
        WriteTxtFile("shared_prefs/RunningContentInfo.xml", "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map>\n<null name=\"title_id\" />\n<null name=\"next_title_id\" />\n</map>\n");
        WriteTxtFile("shared_prefs/LocalLibrary.xml", "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map>\n<boolean name=\"notDisplayAgain\" value=\"true\" />\n<int name=\"sortType\" value=\"0\" />\n<boolean name=\"isList\" value=\"false\" />\n</map>\n");

    }


}

