import org.example.SourceOffset;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class OffsetTests {
    @Test
    public void ValidateOffset()
    {
        SourceOffset offset = new SourceOffset();
        offset.SourceStart = Long.valueOf(5);
        offset.DestinationStart = Long.valueOf(10);
        offset.Length = Long.valueOf(3);

        assertEquals(offset.getDestinationValue(4L), null);
        assertEquals(offset.getDestinationValue(5L), Long.valueOf(10L));
        assertEquals(offset.getDestinationValue(6L), Long.valueOf(11L));
        assertEquals(offset.getDestinationValue(7L), Long.valueOf(12L));
        assertEquals(offset.getDestinationValue(8L), null);


    }
}
