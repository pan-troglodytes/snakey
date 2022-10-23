import java.net.InetSocketAddress;
import java.util.Date;

public class Player {
    Item i;
    InetSocketAddress ia;
    Date d;
    Player(InetSocketAddress ia, Item i ) {
        this.ia = ia;
        this.i = i;
        this.d = d;
    }
    public void setDate(Date d) {
        this.d = d;
    }
}
