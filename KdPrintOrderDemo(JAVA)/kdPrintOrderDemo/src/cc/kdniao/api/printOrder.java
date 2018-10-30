package cc.kdniao.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.messaging.saaj.util.Base64;

/**
 * Servlet implementation class printOrder
 */
@WebServlet("/printOrder")
public class printOrder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    final String EBussinessID = "1291811";//kdniao.com EBusinessID
    final String AppKey = "9a534f4f-a28a-4221-a010-d1d10ec5eb89"; //kdniao.com AppKey
    final Integer IsPreview = 1; //是否预览 0-不预览 1-预览x`

    /**
     * @see HttpServlet#HttpServlet()
     */
    public printOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        //response.getWriter().append("Served at: ").append(request.getContextPath());
        PrintWriter print = response.getWriter();
        String jsonResult = "";
        try {
            String ip = getIpAddress(request);
            jsonResult = getPrintParam(ip);
        } catch (Exception e) {
            //write log
        }
        print.println(jsonResult);
        print.flush();
        print.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnsupportedEncodingException {
        // TODO Auto-generated method stub
        response.setContentType("");
        PrintWriter print = response.getWriter();
        String jsonResult = "";
        try {
            String ip = getIpAddress(request);
            jsonResult = getPrintParam(ip);
        } catch (Exception e) {
            //wirte log
        }
        print.println(jsonResult);
        print.flush();
        print.close();
    }

    /**
     * get print order param to json string
     * @return
     * 
     * @throws Exception 
     */
    private String getPrintParam(String ip) throws Exception {
        String data = "[{\"OrderCode\":\"234351215333113311353\",\"PortName\":\"SF\"},{\"OrderCode\":\"234351215333113311354\",\"PortName\":\"打印机名称二\"}]";
        String result = "{\"RequestData\": \"" + URLEncoder.encode(data, "UTF-8") + "\", \"EBusinessID\":\"" + EBussinessID + "\", \"DataSign\":\"" + encrpy(ip + data, AppKey) + "\", \"IsPreview\":\""
                        + IsPreview + "\"}";
        return result;
    }

    private String md5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    private String encrpy(String content, String key) throws UnsupportedEncodingException, Exception {
        String charset = "UTF-8";
        return new String(Base64.encode(md5(content + key, charset).getBytes(charset)));
    }

    /** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     *  
     * @param request 
     * @return 
     * @throws IOException 
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

}
