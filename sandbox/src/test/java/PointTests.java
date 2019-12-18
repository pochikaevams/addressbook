import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {

    @Test
    public void testDistance() {
        Point p1= new Point(1,0);
        Point p2= new Point(2,4);
        Assert.assertEquals(p1.dist(p2),4.123105625617661);
        }

    @Test
    public void testDistanceNotPass() {
        Point p3= new Point(10,75);
        Point p4= new Point(2,3);
        Assert.assertEquals(p3.dist(p4),74);
    }


}
