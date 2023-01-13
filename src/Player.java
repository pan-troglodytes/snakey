import java.net.InetSocketAddress;
import java.util.Date;

public class Player {
    private Item i;
    private InetSocketAddress ia;
    private Date d;
    Player(InetSocketAddress ia, Item i ) {
        this.ia = ia;
        this.i = i;
        this.d = d;
    }
    public Item getItem() {
        return i;
    }
    public InetSocketAddress getInetSockerAddress() {
        return ia;
    }
    public Date getDate() {
        return d;
    }
    public void setDate(Date d) {
        this.d = d;
    }
}
