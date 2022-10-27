package com.company.truonghoc.utils;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class StringUtility {
    private static final Logger logger = LoggerFactory.getLogger(StringUtility.class);

    // *Mảng các ký tự có dấu tiếng Việt
    private static char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự', };

    // *Mảng các ký tự thay thế không dấu
    private static char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u', };

    // *Mảng các ký tự số tự nhiên
    private static char[] DIGIT_CHARACTERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static String[] pattern = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    /**
     * Bỏ dấu 1 ký tự
     *
     * @param ch
     * @return
     */
    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    /**
     * Bỏ dấu 1 chuỗi
     *
     * @param s
     * @return
     */
    public static String removeAccent(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * Chuyển đổi chuỗi text phục vụ truy vấn tìm kiếm gần đúng trong SQL
     *
     * @param s
     * @return sqlStr
     */
    public static String convertToSearchTextSQL(String s) {
        s = s.toLowerCase();
        String VN_a_CHARACTERS = "aáàãảạăằắẵẳặâầấẫẩậ";
        String VN_e_CHARACTERS = "eéèẽẻẹêếềễểệ";
        String VN_o_CHARACTERS = "oóòõỏọơớờỡởợôốồỗổộ";
        String VN_i_CHARACTERS = "iíìĩỉị";
        String VN_u_CHARACTERS = "uùúũủụưứừữửự";
        String VN_y_CHARACTERS = "yýỳỷỹỵ";
        String VN_d_CHARACTERS = "dđ";

        String sqlStr = "";
        for (int i= 0; i < s.length(); i++) {
            String kytu = Character.toString(s.charAt(i));
            kytu = kytu.toLowerCase();
            if (VN_a_CHARACTERS.indexOf(kytu) >= 0) {
                sqlStr = sqlStr + "[" + VN_a_CHARACTERS + "]";
            } else if (VN_e_CHARACTERS.indexOf(kytu) >= 0) {
                sqlStr = sqlStr + "[" + VN_e_CHARACTERS + "]";
            } else if (VN_o_CHARACTERS.indexOf(kytu) >= 0) {
                sqlStr = sqlStr + "[" + VN_o_CHARACTERS + "]";
            } else if (VN_i_CHARACTERS.indexOf(kytu) >= 0) {
                sqlStr = sqlStr + "[" + VN_i_CHARACTERS + "]";
            } else if (VN_u_CHARACTERS.indexOf(kytu) >= 0) {
                sqlStr = sqlStr + "[" + VN_u_CHARACTERS + "]";
            } else if (VN_y_CHARACTERS.indexOf(kytu) >= 0) {
                sqlStr = sqlStr + "[" + VN_y_CHARACTERS + "]";
            } else if (VN_d_CHARACTERS.indexOf(kytu) >= 0) {
                sqlStr = sqlStr + "[" + VN_d_CHARACTERS + "]";
            } else {
                sqlStr = sqlStr + kytu;
            }
        }
        return sqlStr;
    }

    /**
     * Chuyển đổi ký tự ['] thành [''] phục vụ truy vấn SQL
     *
     * @param s
     * @return
     */
    public static String convertApostropheSQL(String s) {
        return s.replace("'","''");
    }

    /**
     * Viết hoa chữ cái đầu các cụm từ: Quận Cầu Giấy Tp.Hà Nội
     */
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    /**
     * Viết hoa chữ cái đầu câu (1 từ): Xin chào
     */
    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String getDigitChar(String s) {
        String sd = "";
        if (s.isEmpty())
            return sd;
        for (int i = 0; i < s.length(); i++) {
            int index = Arrays.binarySearch(DIGIT_CHARACTERS, s.charAt(i));
            if (index >= 0) {
                sd = sd + s.charAt(i);
            }
        }
        return sd;
    }

    public static String transfer(int num, int soKyTu) {
        String s = String.valueOf(num);
        String d = s;
        while (d.length() < soKyTu) {
            d = "0" + d;
        }
        return d;
    }

    public static String transfer(String num, int soKyTu) {
        String s = String.valueOf(num);
        String d = s;
        while (d.length() < soKyTu) {
            d = "0" + d;
        }
        return d;
    }

    public static String getGioiTinhFromDb(Integer gioiTinhValue){
        String result = "";
        if(gioiTinhValue != null) {
            switch (gioiTinhValue) {
                case 1:
                    result = "Nam";
                    break;
                case 2:
                    result = "Nữ";
                    break;
            }
        }
        return result;
    }

    public static String dateTimeToStrEx(Date date, boolean isSign) {
        SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = temp.format(date);
        if (isSign) {
            result = result.replace("-","").replace(" ","").replace(":","");
            result = result.substring(2);
        }
        return result;
    }
    /**
     * Cắt địa chỉ thành 4 thành phần
     */
    public static Map<String,String> lay_XaPhuong_QuanHuyen_TinhTP_Theo_DiaChi(String address) {
        String thonPho  = "";
        String xaPhuong = "";
        String quanHuyen = "";
        String tinhThanh ="";
        Map<String,String> result = new HashMap<>();

        if(address != null && !address.isEmpty()) {
            String inputLower = address.toLowerCase();
            inputLower = inputLower.replace("'", " ");
            inputLower = inputLower.replace("”", " ");
            inputLower = inputLower.replace("“", " ");
            inputLower = inputLower.replace("\"", " ");

            while (inputLower.contains("  ")) {
                inputLower = inputLower.replace("  ", " ");
            }
            inputLower = inputLower.trim();

            String diaChi = inputLower.replaceAll("\\s+", " ");
            if (!(diaChi.contains(" h.") || diaChi.contains("q.") || diaChi.contains("tx.") || diaChi.contains(" t.") || diaChi.contains("tp.")
                    || diaChi.contains("x.") || diaChi.contains(" p.")) && diaChi.indexOf(",") > 0)
                diaChi = diaChi.replace(". ", ", ");
            diaChi = diaChi.replace("—", "-");
            //++ Phân tách thành các cụm từ đại diện cho xã phường, quận huyện và tỉnh thành
            boolean blQHInTT = false;
            int soDauPhay = diaChi.split(",").length;
            int soDauBang = diaChi.split("-").length;
            if (diaChi.contains("-") && (soDauPhay == 0 || soDauBang > soDauPhay)) {
                String[] arrayDiaChi = diaChi.split("\\-");
                if (arrayDiaChi != null && arrayDiaChi.length > 2) {
                    tinhThanh = arrayDiaChi[arrayDiaChi.length - 1];
                    tinhThanh = tinhThanh.trim();
                    if (tinhThanh.startsWith("quận") || tinhThanh.startsWith("huyện") || tinhThanh.startsWith("h.") || tinhThanh.startsWith("q.") || tinhThanh.startsWith("tx.")) {
                        tinhThanh = "";
                        quanHuyen = arrayDiaChi[arrayDiaChi.length - 1];
                        //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                        quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
                        //quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");

                        //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                        //Thành XX
                        quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                        quanHuyen = quanHuyen.trim();
                        xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                        xaPhuong = xaPhuong.trim();
                    } else {
                        tinhThanh = tinhThanh.replace("t.", "").replace("tp", "");
                        tinhThanh = tinhThanh.trim();
                        //* 07062022: Trường hợp tỉnh thành và quận huyện bị chia cách bởi các ký tự khác như dấu '.'
                        if(tinhThanh.contains(".")) {
                            //tinhThanh = tinhThanh.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp.", "");
                            //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                            //Thành XX
                            tinhThanh = tinhThanh.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp.", "");

                            tinhThanh = tinhThanh.replace(".", ",");
                            String[] arrayTinhThanh = tinhThanh.split("\\,");
                            tinhThanh = "";
                            for (int index = 1; index < arrayTinhThanh.length; index++) {
                                tinhThanh += " " + arrayTinhThanh[index];
                            }
                            tinhThanh = tinhThanh.trim();
                            quanHuyen = arrayTinhThanh[0];
                            quanHuyen = quanHuyen.trim();
                            blQHInTT = true;
                        } else {
                            quanHuyen = arrayDiaChi[arrayDiaChi.length - 2];
                            quanHuyen = quanHuyen.trim();
                        }
                        //* Cần kiểm tra trường hợp chỉ cụm từ thứ 2 là xã phường, thứ 3 là thôn phố
                        if (quanHuyen.startsWith("phường") || quanHuyen.startsWith("p.") || quanHuyen.startsWith("xã") || quanHuyen.startsWith("x.")) {
                            quanHuyen = "";
                            xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                            xaPhuong = xaPhuong.trim();
                            thonPho = arrayDiaChi[arrayDiaChi.length - 3];
                        } else {
                            //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                            quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
                            //quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                            //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                            //Thành XX
                            quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                            quanHuyen = quanHuyen.trim();
                            if(blQHInTT) {
                                xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                                thonPho = arrayDiaChi[arrayDiaChi.length - 3];
                            } else {
                                xaPhuong = arrayDiaChi[arrayDiaChi.length - 3];
                            }
                            //xaPhuong = arrayDiaChi[arrayDiaChi.length - 3];
                            xaPhuong = xaPhuong.trim();
                            thonPho = thonPho.trim();
                            if (quanHuyen.indexOf("quận") > 0 || quanHuyen.indexOf("huyện") > 0) {
                                int p = quanHuyen.indexOf("quận") > 0 ? quanHuyen.indexOf("quận") : quanHuyen.indexOf("huyện");
                                if (quanHuyen.startsWith("phường") || quanHuyen.startsWith("xã"))
                                    xaPhuong = quanHuyen.substring(0, p);
                                quanHuyen = quanHuyen.substring(p, quanHuyen.length());
                            }
                        }
                    }
                } else if (arrayDiaChi != null && arrayDiaChi.length > 1) {
                    tinhThanh = arrayDiaChi[arrayDiaChi.length - 1];
                    tinhThanh = tinhThanh.trim();
                    if (tinhThanh.startsWith("quận") || tinhThanh.startsWith("h.") || tinhThanh.startsWith("q.") || tinhThanh.startsWith("tx.")) {
                        tinhThanh = "";
                        quanHuyen = arrayDiaChi[arrayDiaChi.length - 1];
                        //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                        quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
                        //quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                        //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                        //Thành XX
                        quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");
                        quanHuyen = quanHuyen.trim();
                        xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                        xaPhuong = xaPhuong.trim();
                    } else {
                        tinhThanh = tinhThanh.replace("t.", "").replace("tp", "");
                        tinhThanh = tinhThanh.trim();
                        quanHuyen = arrayDiaChi[arrayDiaChi.length - 2];
                        quanHuyen = quanHuyen.trim();
                        //* Cần kiểm tra trường hợp chỉ có xã phường và tỉnh thành
                        if (quanHuyen.startsWith("phường") || quanHuyen.startsWith("p.") || quanHuyen.startsWith("xã") || quanHuyen.startsWith("x.")) {
                            quanHuyen = "";
                            xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                            xaPhuong = xaPhuong.trim();
                        } else {
                            //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                            quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
//                            quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                            //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                            //Thành XX
                            quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                            quanHuyen = quanHuyen.trim();
                        }
                    }
                }
                if (arrayDiaChi != null && arrayDiaChi.length > 3) {
                    String tempTXP = "";
                    for (int c = 0; c < arrayDiaChi.length - 3; c++)
                        tempTXP = tempTXP + (tempTXP.length() > 0 ? "-" : "") + arrayDiaChi[c];
                    thonPho = tempTXP + (thonPho.length() > 0 ? "-" : "") + thonPho;
                    thonPho = thonPho.replaceAll("\\s+", " ");
                }
            } else if (diaChi.contains(",")) {
                String[] arrayDiaChi = diaChi.split("\\,");
                if (arrayDiaChi != null && arrayDiaChi.length > 2) {
                    tinhThanh = arrayDiaChi[arrayDiaChi.length - 1];
                    if (tinhThanh.startsWith("quận") || tinhThanh.startsWith("h.") || tinhThanh.startsWith("q.") || tinhThanh.startsWith("tx.")) {
                        tinhThanh = "";
                        quanHuyen = arrayDiaChi[arrayDiaChi.length - 1];
                        //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                        quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
//                        quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                        //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                        //Thành XX
                        quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                        quanHuyen = quanHuyen.trim();
                        xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                        xaPhuong = xaPhuong.trim();
                    } else {
                        tinhThanh = tinhThanh.replace("t.", "").replace("tp", "");
                        tinhThanh = tinhThanh.trim();
                        //* 07062022: Trường hợp tỉnh thành và quận huyện bị chia cách bởi các ký tự khác như dấu '.'
                        if(tinhThanh.contains(".")) {
//                            tinhThanh = tinhThanh.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp.", "");
                            //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                            //Thành XX
                            tinhThanh = tinhThanh.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp.", "");

                            tinhThanh = tinhThanh.replace(".", ",");
                            String[] arrayTinhThanh = tinhThanh.split("\\,");
                            tinhThanh = "";
                            for (int index = 1; index < arrayTinhThanh.length; index++) {
                                tinhThanh += " " + arrayTinhThanh[index];
                            }
                            tinhThanh = tinhThanh.trim();
                            quanHuyen = arrayTinhThanh[0];
                            quanHuyen = quanHuyen.trim();
                            blQHInTT = true;
                        } else {
                            quanHuyen = arrayDiaChi[arrayDiaChi.length - 2];
                            quanHuyen = quanHuyen.trim();
                        }

                        //* Cần kiểm tra trường hợp chỉ cụm từ thứ 2 là xã phường, thứ 3 là thôn phố
                        if (quanHuyen.startsWith("phường") || quanHuyen.startsWith("p.") || quanHuyen.startsWith("xã") || quanHuyen.startsWith("x.")) {
                            quanHuyen = "";
                            xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                            xaPhuong = xaPhuong.trim();
                            thonPho = arrayDiaChi[arrayDiaChi.length - 3];
                        } else {
                            //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                            quanHuyen = quanHuyen.replace("ql. ", "").replace("tp.", "");
//                            quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                            //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                            //Thành XX
                            quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                            quanHuyen = quanHuyen.trim();
                            if(blQHInTT) {
                                xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                                thonPho = arrayDiaChi[arrayDiaChi.length - 3];
                            } else {
                                xaPhuong = arrayDiaChi[arrayDiaChi.length - 3];
                            }
                            //xaPhuong = arrayDiaChi[arrayDiaChi.length - 3];
                            xaPhuong = xaPhuong.trim();
                            thonPho = thonPho.trim();
                        }
                    }
                } else if (arrayDiaChi != null && arrayDiaChi.length > 1) {
                    tinhThanh = arrayDiaChi[arrayDiaChi.length - 1];
                    if (tinhThanh.startsWith("quận") || tinhThanh.startsWith("h.") || tinhThanh.startsWith("q.") || tinhThanh.startsWith("tx.")) {
                        tinhThanh = "";
                        quanHuyen = arrayDiaChi[arrayDiaChi.length - 1];
                        //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                        quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
//                        quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                        //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                        //Thành XX
                        quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                        quanHuyen = quanHuyen.trim();
                        xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                        xaPhuong = xaPhuong.trim();
                    } else {
                        tinhThanh = tinhThanh.replace("t.", "").replace("tp", "");
                        tinhThanh = tinhThanh.trim();
                        quanHuyen = arrayDiaChi[arrayDiaChi.length - 2];
                        quanHuyen = quanHuyen.trim();
                        //* Cần kiểm tra trường hợp chỉ có xã phường và tỉnh thành
                        if (quanHuyen.startsWith("phường") || quanHuyen.startsWith("p.") || quanHuyen.startsWith("xã") || quanHuyen.startsWith("x.")) {
                            quanHuyen = "";
                            xaPhuong = arrayDiaChi[arrayDiaChi.length - 2];
                            xaPhuong = xaPhuong.trim();
                        } else {
                            //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                            quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
//                            quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                            //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                            //Thành XX
                            quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                            quanHuyen = quanHuyen.trim();
                        }
                    }
                }
                if (arrayDiaChi != null && arrayDiaChi.length > 3) {
                    String tempTXP = "";
                    for (int c = 0; c < arrayDiaChi.length - 3; c++)
                        tempTXP = tempTXP + (tempTXP.length() > 0 ? "," : "") + arrayDiaChi[c];
                    thonPho = tempTXP + (thonPho.length() > 0 ? "," : "") + thonPho;
                    thonPho = thonPho.replaceAll("\\s+", " ");
                }
            } else {
                int p3 = diaChi.indexOf("tỉnh") > 0 ? diaChi.indexOf("tỉnh") : diaChi.indexOf("thành phố");
                if (p3 < 0)
                    p3 = diaChi.indexOf("thành phô") > 0 ? diaChi.indexOf("thành phô") : diaChi.indexOf("thành pho");
                if (p3 < 0)
                    p3 = diaChi.indexOf("t.") > 0 ? diaChi.indexOf("t.") : diaChi.indexOf("tp");
                tinhThanh = p3 >= 0 ? diaChi.substring(p3) : "";
                tinhThanh = tinhThanh.replace("t.", "").replace("tp", "");
                tinhThanh = tinhThanh.trim();
                int p2 = diaChi.indexOf("quận") > 0 ? diaChi.indexOf("quận") : diaChi.indexOf("huyện");
                if (p2 < 0)
                    p2 = diaChi.indexOf("q.") > 0 ? diaChi.indexOf("q.") : diaChi.indexOf("h.");
                if (p2 < 0)
                    p2 = diaChi.indexOf("tx.");
                //nếu thành phố thuộc tỉnh => huyện  (tp hạ long .tỉnh Quảng Ninh)
                if ((p2 < 0) && (diaChi.indexOf("tỉnh") > 0 || diaChi.indexOf("t.") > 0)) {
                    p2 = diaChi.indexOf("thành phố") > 0 ? diaChi.indexOf("thành phố") : diaChi.indexOf("tp");
                    if (p2 < 0)
                        p2 = diaChi.indexOf("thành phô");
                    if (p2 < 0)
                        p2 = diaChi.indexOf("thành pho");
                    if (p2 < 0)
                        p2 = diaChi.indexOf("thanh pho");
                }
                if (p2 >= 0)
                    quanHuyen = p3 > 0 ? diaChi.substring(p2, p3 - 1) : diaChi.substring(p2);
                else
                    quanHuyen = "";

                //* 03062022: Bổ sung do có trường hợp viết sai chỉnh tả*//
                quanHuyen = quanHuyen.replace("ql.", "").replace("tp.", "");
//                quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "").replace("tp", "");
                //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
                //Thành XX
                quanHuyen = quanHuyen.replace("h.", "").replace("q.", "").replace("tx.", "tx ").replace("tp", "");

                int p1 = diaChi.indexOf("xã") > 0 ? diaChi.indexOf("xã") : diaChi.indexOf("phường");
                if (p1 < 0) {
                    p1 = diaChi.indexOf("x.") > 0 ? diaChi.indexOf("x.") : diaChi.indexOf("p.");
                    if (diaChi.indexOf("tx.") > 0 && (p1-diaChi.indexOf("tx."))==1)
                        p1 = -1;
                }
                if (p1 < 0)
                    p1 = diaChi.indexOf("thị trấn") > 0 ? diaChi.indexOf("thị trấn") : p1;
                if (p1 >= 0) {
                    p2 = p2 > 0 ? p2 : p3;
                    xaPhuong = p2 > p1 ? diaChi.substring(p1, p2 - 1) : diaChi.substring(p1);
                } else
                    xaPhuong = p2 > 1 ? diaChi.substring(0, p2 - 1) : "";
                if (p1 > 0)
                    thonPho = diaChi.substring(0, p1 - 1);
            }

            //++ Chuẩn hóa các giá trị
            if (xaPhuong.indexOf("xã") > 4 || xaPhuong.indexOf("thị trấn") > 4 || xaPhuong.indexOf("x.") > 4 || xaPhuong.indexOf("p.") > 4 || xaPhuong.indexOf("phường") > 4) {
                int s = xaPhuong.indexOf("xã");
                if (s < 0)
                    s =xaPhuong.indexOf("thị trấn");
                if (s < 0)
                    xaPhuong.indexOf("x.");
                if (s < 0)
                    s = xaPhuong.indexOf("p.");
                if (s < 0)
                    s = xaPhuong.indexOf("phường");
                thonPho = thonPho + " "+ xaPhuong.substring(0,s);
                xaPhuong = xaPhuong.substring(s,xaPhuong.length());
            }
            if (xaPhuong.startsWith("xã") || xaPhuong.startsWith("x.") || xaPhuong.startsWith("p."))
                xaPhuong = xaPhuong.substring(2);
            else if (xaPhuong.startsWith("phường") || xaPhuong.startsWith("phuong"))
                xaPhuong = xaPhuong.substring(6);
            else if (xaPhuong.startsWith("thị trấn") || xaPhuong.startsWith("thi tran"))
                xaPhuong = xaPhuong.substring(8);
            else if (xaPhuong.startsWith("tfrị trấn"))
                xaPhuong = xaPhuong.substring(9);
            xaPhuong = xaPhuong.trim();

//            if (quanHuyen.startsWith("huyện") || quanHuyen.startsWith("huyen"))
//                quanHuyen = quanHuyen.substring(5);
//            else if (quanHuyen.startsWith("quận") || quanHuyen.startsWith("quan"))
//                quanHuyen = quanHuyen.substring(4);
//            else if (quanHuyen.startsWith("thị xã") || quanHuyen.startsWith("thi xa"))
//                quanHuyen = quanHuyen.substring(6);
//            else if (quanHuyen.startsWith("thành phố")||quanHuyen.startsWith("thành phô")||quanHuyen.startsWith("thành pho")||quanHuyen.startsWith("thanh pho"))
//                quanHuyen = quanHuyen.substring(9);
//            else if (quanHuyen.startsWith("."))
//                quanHuyen = quanHuyen.substring(1);
//            quanHuyen = quanHuyen.trim();

            //TODO: Bổ sung thêm perfix thị xã vào tên quận huyện 25.07.2022
            //Thành XX
            if (quanHuyen.startsWith("huyen"))
                quanHuyen = quanHuyen.replace("huyen", "huyện");
            else if (quanHuyen.startsWith("quan"))
                quanHuyen = quanHuyen.replace("quan", "quận");
            else if (quanHuyen.startsWith("thi xa"))
                quanHuyen = quanHuyen.replace("thi xa", "thị xã");
            else if (quanHuyen.startsWith("tx"))
                quanHuyen = quanHuyen.replace("tx", "thị xã");
            else if (quanHuyen.startsWith("thành phô"))
                quanHuyen = quanHuyen.replace("thành phô", "thành phố");
            else if(quanHuyen.startsWith("thành pho"))
                quanHuyen = quanHuyen.replace("thành pho", "thành phố");
            else if(quanHuyen.startsWith("thanh pho"))
                quanHuyen = quanHuyen.replace("thanh pho", "thành phố");
            else if (quanHuyen.startsWith("."))
                quanHuyen = quanHuyen.substring(1);
            quanHuyen = quanHuyen.trim();


            if (tinhThanh.startsWith("thành phố") || tinhThanh.startsWith("thanh pho") || tinhThanh.startsWith("thành phô") || tinhThanh.startsWith("thành pho"))
                tinhThanh = tinhThanh.substring(9);
            else if (tinhThanh.startsWith("tỉnh") || tinhThanh.startsWith("tinh"))
                tinhThanh = tinhThanh.substring(4);
            else if (tinhThanh.startsWith("."))
                tinhThanh = tinhThanh.substring(1);
            tinhThanh = tinhThanh.trim();
        }
        //12-2-2020: Xét trường hợp nhầm tp thuộc tỉnh với tỉnh (có xaPhuong, tinhThanh thiếu quanHuyen)
        if (!StringUtils.isEmpty(xaPhuong.trim()) && !StringUtils.isEmpty(tinhThanh.trim()) && StringUtils.isEmpty(quanHuyen.trim())) {
            quanHuyen = tinhThanh.trim();
        }
        result.put("ThonPho", StringUtility.capitalizeString(thonPho.trim()));
        result.put("XaPhuong", StringUtility.capitalizeString(xaPhuong.trim()));
        result.put("QuanHuyen", StringUtility.capitalizeString(quanHuyen.trim()));
        result.put("TinhThanh", StringUtility.capitalizeString(tinhThanh.trim()));

        if(address != null && !address.isEmpty()) {
            if (result.get("ThonPho").length() == 0 && result.get("XaPhuong").length() == 0 && result.get("QuanHuyen").length() == 0
                    && result.get("TinhThanh").length() == 0 && address.length() > 0)
                result.put("TinhThanh", address.trim());
        }
        return result;
    }

    /**
     * Chuyển đổi số sang chuỗi cách đọc số của Việt Nam
     * @param intNum
     * @return vnTextNum
     */
    public static String StrNumberToVNText(String intNum) {
        String vnTextNum="";
        if (StrToPositiveInt(intNum) >= 20) {
            String char1 = intNum.substring(0,1);
            String char2 = intNum.substring(1);
            String no1 = NumberToVNText(StrToPositiveInt(char1));
            String no2 = NumberToVNText(StrToPositiveInt(char2));
            if (char2.equals("5"))
                vnTextNum = no1 +" lăm";
            else if (char2.equals("4"))
                vnTextNum = no1 +" tư";
            else
                vnTextNum = no1 + (char2.equals("0") ? " mươi" : (char2.equals("1") ? " mốt":" "+no2));
        } else if (StrToPositiveInt(intNum) >= 10) {
            if (StrToPositiveInt(intNum) == 10)
                vnTextNum = "mười";
            else {
                String char2 = intNum.substring(1);
                if (char2.equals("5"))
                    vnTextNum = "mười lăm";
                else
                    vnTextNum = "mười " + NumberToVNText(StrToPositiveInt(char2));
            }
        } else {
            String char2 = intNum.substring(1);
            vnTextNum = NumberToVNText(StrToPositiveInt(char2));
        }
        return vnTextNum;
    }

    public static String NumberToVNText(Integer intNum) {
        String[] vnNaturalNumberText = {"không","một","hai","ba","bốn","năm","sáu","bẩy","tám","chín"};
        return vnNaturalNumberText[intNum];
    }

    public static int StrToPositiveInt(String value){
        int result = 0;
        try {
            result = Integer.valueOf(value);
        } catch (Exception e) {
            result = -1;
        }
        return result;
    }

    public static int ToInt(double value) {
        int re = 0;
        try {
            re = (int) Math.round(value);
        } catch (Exception ex) {
        }
        return re;
    }

    public static int ToInt(String value) {
        int re = 0;
        try {
            re = Integer.valueOf(value);
        } catch (Exception ex) {
        }
        return re;
    }

    public static Date StrToDate(String stringDate) {
        stringDate = stringDate.trim();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
        } catch (Exception e) {
            try {
                date = new SimpleDateFormat("dd/M/yyyy").parse(stringDate);
            } catch (Exception ex) {
                try {
                    date = new SimpleDateFormat("d/MM/yyyy").parse(stringDate);
                } catch (Exception exception) {
                    try {
                        date = new SimpleDateFormat("d/MM/yy").parse(stringDate);
                    } catch (Exception exception1) {
                        try {
                            date = new SimpleDateFormat("d/M/yy").parse(stringDate);
                        } catch (Exception exception2) {
                            try {
                                date = new SimpleDateFormat("dd/M/yy").parse(stringDate);
                            } catch (Exception exception3) {
                            }
                        }
                    }
                }
            }
        }
        return date;
    }

    public static Date StrToDateByExcel(String stringDate, boolean isVnFormat) {
        stringDate = stringDate.trim();
        Date date = null;
        try {
            int nDay = 1;
            int nMonth = 1;
            int nYear;
            if(isVnFormat) {
                String[] arrSplits = stringDate.split("/");
                if(arrSplits.length >= 3) {
                    nDay = Integer.valueOf(arrSplits[0]);
                    nMonth = Integer.valueOf(arrSplits[1]);
                    nYear = Integer.valueOf(arrSplits[2]);
                } else if(arrSplits.length >= 2) {
                    nMonth = Integer.valueOf(arrSplits[0]);
                    nYear = Integer.valueOf(arrSplits[1]);
                } else {
                    nYear = Integer.valueOf(arrSplits[0]);
                }
            } else {
                String[] arrSplits = stringDate.split("/");
                if(arrSplits.length >= 3) {
                    nDay = Integer.valueOf(arrSplits[1]);
                    nMonth = Integer.valueOf(arrSplits[0]);
                    nYear = Integer.valueOf(arrSplits[2]);
                } else if(arrSplits.length >= 2) {
                    nMonth = Integer.valueOf(arrSplits[0]);
                    nYear = Integer.valueOf(arrSplits[1]);
                } else {
                    nYear = Integer.valueOf(arrSplits[0]);
                }
            }

            nMonth = nMonth > 0 ? (nMonth - 1) : nMonth;
            if(nYear < 1000) {
                nYear = 2000 + nYear;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(nYear, nMonth, nDay, 0, 0, 0);
            date = calendar.getTime();
        } catch (Exception e) {
            if(isVnFormat) {
                date = StrToDate(stringDate);
            } else {
                int strLength = stringDate.length();
                try {
                    if (strLength == 9) {
                        date = new SimpleDateFormat("d/MM/yyyy").parse(stringDate);
                    } else if (strLength == 8) {
                        date = new SimpleDateFormat("d/M/yyyy").parse(stringDate);
                    } else if (strLength == 7) {
                        date = new SimpleDateFormat("dd/M/yy").parse(stringDate);
                    } else if (strLength == 6) {
                        date = new SimpleDateFormat("d/M/yy").parse(stringDate);
                    }
                } catch (Exception ex) {
                    try {
                        if (strLength == 9) {
                            date = new SimpleDateFormat("dd/M/yyyy").parse(stringDate);
                        } else if (strLength == 8) {
                            date = new SimpleDateFormat("dd/MM/yy").parse(stringDate);
                        } else if (strLength == 7) {
                            date = new SimpleDateFormat("d/MM/yy").parse(stringDate);
                        } else if (strLength == 6) {
                            date = new SimpleDateFormat("M/d/yy").parse(stringDate);
                        }
                    } catch (Exception ex1) {

                    }
                }
            }
        }
        return date;
    }

    public static int soKyTuKhacBiet(String sourceVal, String destValue) {
        if (sourceVal == null || destValue == null)
            return 100;
        StringBuilder sb = new StringBuilder(sourceVal);
        StringBuilder db = new StringBuilder(destValue);
        int j = 0;
        int count = sb.length();
        if (db.length() < sb.length()) {
            j = sb.length() - db.length();
            count = db.length();
        } else {
            j = db.length() - sb.length();
        }
        for (int i = 0; i < count; i++) {
            if (!Character.toString(sb.charAt(i)).equals(Character.toString(db.charAt(i))))
                j++;
        }
        return j;
    }

    public static String removeNotChuCaiChar(String input) {
        if(input==null||input.isEmpty())
            return "";
        String ouput="";
        char [] inputChar=input.toCharArray();
        String vnenString ="(aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵzAÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ)";

        for (char c : inputChar) {
            if (vnenString.contains(""+c) )
            {
                ouput=ouput+c;
            }
            else
                ouput = ouput+" ";
        }
        while (ouput!=null && ouput.contains("  "))
        {
            ouput =ouput.replace("  "," ");
        }
        ouput =ouput.trim();
        while (ouput!=null && ouput.startsWith(" ")||ouput.startsWith(":"))
        {
            ouput =ouput.substring(1);
        }
        ouput =ouput.trim();
        return ouput;
    }

    public static Integer getSoTienByStringInput(String soTien) {
        //char[] DIGIT_CHARACTERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        soTien = soTien.replace(".", "");
        soTien = soTien.replace(" ", "");
        Integer result = StringUtility.StrToPositiveInt(soTien);
        if (result == -1) {
            int start_index = 0;
            for (int i = 0; i < soTien.length(); i++) {
                if (Arrays.binarySearch(DIGIT_CHARACTERS, soTien.charAt(i)) >= 0) {
                    start_index = i;
                    break;
                }
            }
            String tienStr = soTien.substring(start_index, soTien.length());
            int end_index = tienStr.length();
            for (int i = 0; i < tienStr.length(); i++) {
                if (Arrays.binarySearch(DIGIT_CHARACTERS, soTien.charAt(i)) < 0) {
                    end_index = i;
                    break;
                }
            }
            tienStr = tienStr.substring(0, end_index);
            result = StringUtility.StrToPositiveInt(tienStr);
        }
        return result;
    }

    public static String removeNotLetterChar(String input, String lettersExcept) {
        if(input==null||input.isEmpty())
            return "";
        String ouput="";
        char [] inputChar=input.toCharArray();
        String vnenString ="aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵzAÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ";
        vnenString = vnenString + lettersExcept;

        for (char c : inputChar) {
            if (vnenString.contains(""+c) )
            {
                ouput=ouput+c;
            }
            else
                ouput = ouput+" ";
        }
        while (ouput!=null && ouput.contains("  "))
        {
            ouput =ouput.replace("  "," ");
        }
        ouput =ouput.trim();
        while (ouput!=null && ouput.startsWith(" ")||ouput.startsWith(":"))
        {
            ouput =ouput.substring(1);
        }
        ouput =ouput.trim();
        return ouput;
    }

    public static String dateToStrNgayThangNam(Date date) {
        if (date == null) {
            return "";
        }

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int ngay = calendar.get(Calendar.DAY_OF_MONTH);
            int thang = calendar.get(Calendar.MONTH)+1;
            String ngayStr = ngay < 10 ? ("0"+ngay) : String.valueOf(ngay);
            String thangStr = thang < 10 ? ("0"+thang) : String.valueOf(thang);
            return "ngày "+ngayStr+" tháng "+thangStr+" năm "+calendar.get(Calendar.YEAR);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return "";
    }


    /**
     * standard pattern "dd-MM-yyyy"
     * @param stringDate
     * @return
     */
    public static String formatDate(String stringDate) {
        stringDate = stringDate.trim();
        String strDate = "";
        //++nếu đầu vào là dạng ISO 8601 (UTC)
        if (stringDate.indexOf("-") == 4) {
            if (stringDate.length() > 10)
                stringDate = stringDate.substring(0, 10);
            String[] arrayStr = stringDate.split("-");
            int mmNum = 0;
            try {
                mmNum = Integer.valueOf(arrayStr[1]);
            } catch (Exception ex) {
            }
            if (mmNum > 12) {
                if (arrayStr.length > 2)
                    strDate = transfer(arrayStr[1], 2) + "-" + transfer(arrayStr[2], 2) + "-" + arrayStr[0];
            } else {
                if (arrayStr.length > 2)
                    strDate = transfer(arrayStr[2], 2) + "-" + transfer(arrayStr[1], 2) + "-" + arrayStr[0];
            }
        }
        else if (stringDate.indexOf("-") > 0) {
            String mmm = stringDate.substring(3, 5);
            //++nếu format tháng không phải là số ví dụ: "31-Dec-1998"
            if (indexOfMonth(stringDate) > 0) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    Date d = formatter.parse(stringDate);
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    strDate = dateFormat.format(d);
                } catch (Exception ex){
                    strDate = stringDate;
                }
            } else {// cố gắng phân biệt được (mm-dd-yyyy với dd-mm-yyyy)
                String[] arrayStr = stringDate.split("-");
                int mmNum = 0;
                try {
                    mmNum = Integer.valueOf(arrayStr[1]);
                } catch (Exception ex) {
                }
                if (mmNum > 12) {
                    if (arrayStr.length > 2)
                        strDate = transfer(arrayStr[1], 2) + "-" + transfer(arrayStr[0], 2) + "-" + arrayStr[2];
                    else //nếu chỉ có tháng năm
                        strDate = stringDate;
                } else {
                    if (arrayStr.length > 2)
                        strDate = transfer(arrayStr[0], 2) + "-" + transfer(arrayStr[1], 2) + "-" + arrayStr[2];
                    else //nếu chỉ có tháng năm
                        strDate = stringDate;
                }
            }
        }
        //++ nếu đầu vào từ MS.Net date
        else if (stringDate.indexOf("/Date(") >= 0) {
            String strValue = stringDate.substring(stringDate.indexOf("(") + 1, stringDate.indexOf(")"));
            Date d = new Date(Long.parseLong(strValue));
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            strDate = dateFormat.format(d);
        }
        else if (stringDate.indexOf("/") > 0) {
            //+nếu là format "yyyy/mm/dd"
            if (stringDate.indexOf("/") == 4) {
                if (stringDate.length() > 10)
                    stringDate = stringDate.substring(0, 10);
                String[] arrayStr = stringDate.split("/");
                int mmNum = 0;
                try {
                    mmNum = Integer.valueOf(arrayStr[1]);
                } catch (Exception ex) {
                }
                if (mmNum > 12) {
                    if (arrayStr.length > 2)
                        strDate = transfer(arrayStr[1], 2) + "-" + transfer(arrayStr[2], 2) + "-" + arrayStr[0];
                    else
                        strDate = stringDate.replace("/","-");
                } else {
                    if (arrayStr.length > 2)
                        strDate = transfer(arrayStr[2], 2) + "-" + transfer(arrayStr[1], 2) + "-" + arrayStr[0];
                    else
                        strDate = stringDate.replace("/","-");
                }
            }
            else {// cố gắng phân biệt được (mm/dd/yyyy với dd/mm/yyyy)
                String[] arrayStr = stringDate.split("/");
                int mmNum = 0;
                try {
                    mmNum = Integer.valueOf(arrayStr[1]);
                } catch (Exception ex) {
                }
                if (mmNum > 12) {
                    if (arrayStr.length > 2)
                        strDate = transfer(arrayStr[1], 2) + "-" + transfer(arrayStr[0], 2) + "-" + arrayStr[2];
                    else
                        strDate = stringDate.replace("/","-");
                } else {
                    if (arrayStr.length > 2)
                        strDate = transfer(arrayStr[0], 2) + "-" + transfer(arrayStr[1], 2) + "-" + arrayStr[2];
                    else
                        strDate = stringDate.replace("/","-");
                }
            }
        }
        //++ nếu đầu vào dạng: "12 31, 1998" hoặc "Thu, Dec 31 1998"
        else if (stringDate.indexOf(",") > 0) {
            if (indexOfMonth(stringDate) > 0) {
                SimpleDateFormat formatter =new SimpleDateFormat("E, MMM dd yyyy");
                try {
                    Date d = formatter.parse(stringDate);
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    strDate = dateFormat.format(d);
                } catch (Exception ex){
                    strDate = stringDate;
                }
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("MM dd, yyyy");
                try {
                    Date d = formatter.parse(stringDate);
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    strDate = dateFormat.format(d);
                } catch (Exception ex){
                    strDate = stringDate;
                }
            }
        }
        else if (stringDate.indexOf(".") > 0) {
            //++ nếu đầu vào theo ANSI (yyyy.mm.dd)
            if (stringDate.indexOf(".") == 4 && stringDate.length() >=10)
                strDate = stringDate.substring(8, 10)+"-"+stringDate.substring(5, 7)+"-"+stringDate.substring(0, 4);
            else if (stringDate.length() >=10)// German(dd.mm.yyyy)
                strDate = stringDate.substring(0, 2)+"-"+stringDate.substring(3, 5)+"-"+stringDate.substring(6, 10);
            else
                strDate = stringDate.replace(".","-");
        } else {
            if (stringDate.length() == 8)
                try {
                    strDate = formatDateExtends(stringDate);
                } catch (Exception ex) {}
            else
                strDate = stringDate;
        }
        return strDate;
    }

    /**
     * Convert special StringDate("20180818") to standard pattern "dd-MM-yyyy"
     * @param sourceVal
     * @return
     */
    public static String formatDateExtends(String sourceVal) {
        String ngayThangNam = "";
        String prefix0 = sourceVal.substring(0, 2);
        String prefix1 = sourceVal.substring(2, 4);
        String prefix2 = sourceVal.substring(4);
        //++nếu 4 ký tự đầu tiên là năm
        //if (Integer.parseInt(prefix1) > 1912) {
        if (Integer.parseInt(prefix0) >= 19 && Integer.parseInt(prefix1) > 12) {
            String nam = sourceVal.substring(0, 4);
            String thang = sourceVal.substring(4, 6);
            String ngay = sourceVal.substring(6, 8);
            ngayThangNam = ngay + "-" + thang + "-" + nam;
        } else if (Integer.parseInt(prefix2) < 1912) {
            //+nếu 4 ký tự cuối không phải là năm
            String nam = sourceVal.substring(0, 4);
            String thang = sourceVal.substring(4, 6);
            String ngay = sourceVal.substring(6, 8);
            ngayThangNam = ngay + "-" + thang + "-" + nam;
        } else {
            String nam = sourceVal.substring(4);
            String thang = sourceVal.substring(2, 4);
            String ngay = sourceVal.substring(0, 2);
            ngayThangNam = ngay + "-" + thang + "-" + nam;
        }
        return ngayThangNam;
    }

    /**
     * Lấy ngày hiện tại theo muối giờ việt nam
     * @return
     */
    public static Date getCurrentDateTime(String timeZone) {
        if(StringUtils.isEmpty(timeZone)) {
            timeZone = "Asia/Ho_Chi_Minh";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        return calendar.getTime();
    }

    private static int indexOfMonth(String strDate) {
        int result = -1;
        for (String thangEn : pattern) {
            if (strDate.indexOf(thangEn) >= 0) {
                result = strDate.indexOf(thangEn);
                break;
            }
        }
        return result;
    }

    public static Date setTimesToJavaUtilDate(Date date, int hours, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * Cắt chuỗi nếu dài quá trước khi lưu vào DB
     *
     * @param metaClass
     * @param fieldValue
     * @return fieldValue
     */
    public static String truncateFieldDataTypeString(MetaClass metaClass, String fieldName, String fieldValue)
    {
        if (metaClass == null ||fieldName == null || fieldValue == null)
            return fieldValue;
        MetaProperty property = metaClass.getPropertyNN(fieldName);
        Map<String, Object> mapAnnotations = property.getAnnotations();
        if(mapAnnotations.containsKey("length")) {
            Integer maxLength = Integer.valueOf(mapAnnotations.get("length").toString());
            if (fieldValue.length() <= maxLength)
                return fieldValue;
            else
                return fieldValue.substring(0, maxLength);
        }
        return fieldValue;
    }
}
