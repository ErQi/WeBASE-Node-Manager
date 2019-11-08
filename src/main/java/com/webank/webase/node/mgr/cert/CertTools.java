/**
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.webase.node.mgr.cert;


import com.webank.webase.node.mgr.base.code.ConstantCode;
import com.webank.webase.node.mgr.base.exception.NodeMgrException;
import com.webank.webase.node.mgr.base.tools.Web3Tools;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.utils.Numeric;
import org.springframework.core.io.ClassPathResource;
import sun.security.ec.ECPublicKeyImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CertTools {
    public static final String crtContentHead = "-----BEGIN CERTIFICATE-----\n" ;
    public static final String crtContentTail = "-----END CERTIFICATE-----\n" ;
    public static final String crtTailForConcat = "\n-----END CERTIFICATE-----\n" ;

    public static final String TYPE_CHAIN = "chain";
    public static final String TYPE_AGENCY = "agency";
    public static final String TYPE_NODE = "node";
    public static final String TYPE_SDK = "sdk";
    // 首次启动时需要拉取
    public static boolean isPullFrontCertsDone = false;
    /**
     * 获取证书类型 和 名字
     * @return
     * @throws IOException
     */
    public static String  getCertType(Principal subjectDN) {
        return subjectDN.toString().split(",")[0].split("=")[1];
    }
    public static String  getCertName(Principal subjectDN) {
        return subjectDN.toString().split(",")[2].split("=")[1];
    }

    /**
     * 给cert的内容加上头和尾
     * begin ...
     * end ...
     */
    public static String addHeadAndTail(String certContent) {
        String headToConcat = crtContentHead;
        String fullCert = headToConcat.concat(certContent).concat(crtTailForConcat);
        return fullCert;
    }

    /**
     * getPublicKey
     * @param key
     * @return String
     */
    public static String getPublicKeyString(PublicKey key) {
        ECPublicKeyImpl pub = (ECPublicKeyImpl) key;
        byte[] pubBytes = pub.getEncodedPublicValue();
        String publicKey = Numeric.toHexStringNoPrefix(pubBytes);
        publicKey = publicKey.substring(2); //证书byte[]为130位，只取128位，去除开头的04标记位
        return publicKey;
    }

    public static String getAddress(PublicKey key) {
        ECPublicKeyImpl pub = (ECPublicKeyImpl) key;
        byte[] pubBytes = pub.getEncodedPublicValue();
        String publicKey = Numeric.toHexStringNoPrefix(pubBytes);
        publicKey = publicKey.substring(2); //128位
        String address = Keys.getAddress(publicKey);
        return address;
    }

    // crt文件中默认首个是节点证书 0 isnode ca, 1 is agency ca, 2 is chain
    public static List<String> getCrtContentList(String certContent) {
        List<String> list = new ArrayList<>();
        if(!certContent.startsWith(crtContentHead)){
            throw new NodeMgrException(ConstantCode.CERT_FORMAT_ERROR);
        }
        String[] nodeCrtStrArray = certContent.split(crtContentHead);
        for(int i = 0; i < nodeCrtStrArray.length; i++) {
            String[] nodeCrtStrArray2 = nodeCrtStrArray[i].split(crtContentTail);
            for(int j = 0; j < nodeCrtStrArray2.length; j++) {
                String ca = nodeCrtStrArray2[j];
                if(ca.length() != 0) {
                    list.add(formatStr(ca));
                }
            }
        }
        return list;
    }

    public static String formatStr(String string) {
        return string.substring(0, string.length() - 1);
    }

    /**
     * byte数组转hex
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes){
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }
}
