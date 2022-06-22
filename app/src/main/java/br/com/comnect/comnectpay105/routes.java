package br.com.comnect.comnectpay105;

public class routes {

    private static String url = "http://192.168.20.152/";

    public static String getPendentes = url + "API/get-pendentes.php";
    public static String updateStatus = url + "API/update-status.php";
    public static String postTransaction = url + "SCOPE/post-transaction.php";
    public static String getPdvConfig = url + "";
    public static String getSettingsPortal = "http://update2.comnect.com.br:5022";
}
