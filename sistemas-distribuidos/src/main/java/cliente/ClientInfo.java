package cliente;

import java.io.Serializable;
import java.util.Objects;

public class ClientInfo implements Serializable {
   
	private static final long serialVersionUID = 1L;
	private String ipAddress;
    private int port;

    public ClientInfo(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ClientInfo other = (ClientInfo) obj;
        return Objects.equals(ipAddress, other.ipAddress) && port == other.port;
    }
}
