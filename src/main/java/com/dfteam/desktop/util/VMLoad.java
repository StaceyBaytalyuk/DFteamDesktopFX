package com.dfteam.desktop.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import java.text.DecimalFormat;

public class VMLoad { //TODO docs

    private Session session;

    public VMLoad(String ip, String login) {
        try {
            JSch jsch = new JSch();

            session = jsch.getSession(login, ip);
            jsch.addIdentity("key", privKey(), pubKey(), null);
            session.setPassword("");

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect(3000);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String Command(String command){
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.connect();

            InputStream in = channel.getInputStream();

            byte[] tmp = new byte[1024];
            StringBuilder str = new StringBuilder("");
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    str.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
//                    System.out.println("Exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    // swallow                }
                }
            }
            return str.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double ProcLoadStat(){
        try{
            double tmp = Double.parseDouble(Command("top -d 0.5 -b -n2 | grep \"Cpu(s)\"|tail -n 1 | awk '{print $8}'"));
            return 100 - tmp;
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public double MemFreeStat(){
        return Double.parseDouble(Command("free -m | awk '{print ($3+$5)}'").split("\n")[1]);
    }

    public double MemTotalStat(){
        return Double.parseDouble(Command("free -m | awk '{print ($2)}'").split("\n")[1]);
    }

    private DecimalFormat df = new DecimalFormat("#.##");

    public String DiskTotalSize(){
        String rez = Command("df -B 1 / | awk '{print ($4)}'").split("\n")[1];
        return df.format(Double.parseDouble(rez)/1024/1024/1024);
    }

    public String DiskUsedSize(){
        String rez = Command("df -B 1 / | awk '{print ($3)}'").split("\n")[1];
        return df.format(Double.parseDouble(rez)/1024/1024/1024);
    }

    public String DiskUsedPersent(){
        return Command("df -B 1 / | awk '{print ($5)}'").split("\n")[1];
    }

    public boolean isConnected(){
        return session.isConnected();
    }

    public VMLoad(String ip, String login, String password) {
        try {
            JSch jsch = new JSch();

            session = jsch.getSession(login, ip);
            session.setPassword(password);

            session.connect();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        session.disconnect();
    }

    private byte[] privKey() {
        return ("-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpQIBAAKCAQEAudfQpF36fKMAiVmbMxL7nn8oZJmuCNXrXVG4Njz7nPH5nsJd\n" +
                "wnyawVrJqdex3pC/O/EnOQoo+yHl06n/ryPP2vhIZp3X6swxRBa/rvruWH09UHxi\n" +
                "0iTWNc6+UzH9ETbchuu6IRQ4EAZB4yEHsiDPnsCIegVI12sHj9QfBDWIVN87xvF/\n" +
                "AdhjOzF289dmb7jLtiDq0ly7aA/S4UnGtPP4hNFwwl+MyVpiU2A9EtMMazad4vLP\n" +
                "yi3IMjxTRnPKWrD5FLOn5wCvQZPMuikWYk24olwfRl5zeKfZyew62FNXb7Ppy+2Z\n" +
                "z2OT0tlSow539sl5JCpgSSCIIhjkOP0I/9bplwIDAQABAoIBAQCXZdfVb7I2g1xW\n" +
                "iKdE7nwJ3TF88y2yiQNbXM2jcf7RFtfynAlh/bzdNCcBtHR1CpqgLI91im9kV3My\n" +
                "AlYHyjb0/juNTZ2u7HZeL+IsnMQ7Vpvyy8J9zACuiSTIBftSYM07JSLW++/ILjDS\n" +
                "5f+s/8nkXJlSPqfexpS8eWRjO3E7IdXDoaH12v+v9LFIyK6NTJArs35rlYdOWgSR\n" +
                "EMfd9oxaZieoXndo+sNm0TQApZ8u1qhGvvw40C7FuzC+AmzGPX3I90BoYLNHTT91\n" +
                "LXWHkRIXAez3kO1bhmCDCu9RSGikqb5QOc11HK6PzrPs0ua2Re1Pp+4q477BCbNX\n" +
                "bmeKCuDRAoGBAN+XVHPvBNuPTC8ufkr6kM0iT/AdnRH9Tl4OrfV1aUc+EJmE493/\n" +
                "jdxHspPENHy96BIuViIqSJ5G36HyxXa5auQfIWYPf7aUEZCOKuv6ZsZzXG7QfDx+\n" +
                "lNktiQCzlNv4lmRApMiLD5gZWZJWeUPJmdyhpt9wzezp9JClBHvRVF8NAoGBANTH\n" +
                "yZcCsQ6Jua3bYEOyUwRSCCYfoJsPx8f9a7NpJmQho2ZYreLwuR8hOUj6ab3Ky9dB\n" +
                "xJsFdXuYoYas3FDOsJiFCh0Yblv2CLCtKcbW66WNEK68m+hMxLh4MsShAplcdqAg\n" +
                "jxLsiF1J+46Rk9qXqw2r7RaE0xoWuB9xP1a8+WIzAoGBAMhm5rcSoJP6GBqh3Whu\n" +
                "tKJoAqYApOue0kLTfW/n0HieVFqFFYfAqfGqAZEX5sN3oy3IgkMVCwlyFLce1YXY\n" +
                "IspcbJ8BfUKz7BpYknf3c26jr1FTZEcEXX1aptUOIHoYkRNCLW/h1BRLhFD5WK89\n" +
                "Xr611M+oV6nk9+M9RIuFz81xAoGBALOPWY4nBlV3YgrWKxlVvfrSYvARFo5kNbqO\n" +
                "IDoJzQ0I2gQ/Z5YwgHhFqDrunxc7sOMZLBmJ9Md1WTH2ZAGJOruq4YsGF/Ng35qA\n" +
                "ttWGftt4/JAIuCTDy+rD/Eu0eCYFQlIEx9g3ZSgci72XGkeIYCAsaunq8t58N6rI\n" +
                "/aNr26PDAoGAL9RbmMFti63Ab+Z8mJGt/iKv/q9cXoTWPd+tpFkOBc5zaY5yk2+i\n" +
                "JMMMr0T8pfbYlaSZp2WllKwvPUV8KY/Jj9a4NHYw2nXzt/1MaGaiTmRhE7CnJydK\n" +
                "+rF1wuupE2NHxZjIVVJYjfmx+cLalyMbgu6utH6PC7QwHzbW0Q7yLWU=\n" +
                "-----END RSA PRIVATE KEY-----").getBytes();
    }

    private byte[] pubKey() {
        return "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC519CkXfp8owCJWZszEvuefyhkma4I1etdUbg2PPuc8fmewl3CfJrBWsmp17HekL878Sc5Cij7IeXTqf+vI8/a+EhmndfqzDFEFr+u+u5YfT1QfGLSJNY1zr5TMf0RNtyG67ohFDgQBkHjIQeyIM+ewIh6BUjXaweP1B8ENYhU3zvG8X8B2GM7MXbz12ZvuMu2IOrSXLtoD9LhSca08/iE0XDCX4zJWmJTYD0S0wxrNp3i8s/KLcgyPFNGc8pasPkUs6fnAK9Bk8y6KRZiTbiiXB9GXnN4p9nJ7DrYU1dvs+nL7ZnPY5PS2VKjDnf2yXkkKmBJIIgiGOQ4/Qj/1umX chitkiu@chitkiu-Aspire-ES1-731G".getBytes();

    }

}