package io.github.jojoti.util.shareidv1;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

public class SharedIdTest {

    private static SharedIdSignHashFactory sharedIdSignHashFactory;
    private static SharedIdHashFacotry sharedIdHashFacotry;
    private static SharedIdExpireSignFactory sharedIdExpireSignFactory;

    @BeforeClass
    public static void init() {
        sharedIdSignHashFactory = GuiceApp.sharedIdSignHash();
        sharedIdHashFacotry = GuiceApp.sharedIdHash();
        sharedIdExpireSignFactory = GuiceApp.sharedIdExpireHash();
    }

    @Test
    public void testSharedIdSignHash() {
        SharedId<Long, SharedIdSignDecodeValue> sharedId
                = SharedIdTest.sharedIdSignHashFactory.create("dd", "gggggggggg");

        String encodeStr = sharedId.encode(1L);

        SharedIdSignDecodeValue decodeId = sharedId.decode(encodeStr);

        TestCase.assertEquals(1L, decodeId.getId().longValue());
    }

    @Test
    public void testShareIdsSignHash() {
        SharedId<Long, SharedIdSignDecodeValue> sharedId = SharedIdTest.sharedIdSignHashFactory.create("qqqqq", "xxxx");

        String encodeStr = sharedId.encode(1L);

        SharedIdSignDecodeValue decodeId = sharedId.decode(encodeStr);

        TestCase.assertEquals(1, decodeId.getId().longValue());
    }

    @Test
    public void testSharedIdHash() {
        SharedId<Long, Long> sharedId = SharedIdTest.sharedIdHashFacotry.create("dd");

        String encodeStr = sharedId.encode(1L);

        long decodeId = sharedId.decode(encodeStr);

        TestCase.assertEquals(1, decodeId);
    }

    @Test
    public void testSharedIdExpireHash() {
        SharedId<SharedIdExpireEncodeValue, SharedIdExpireDecodeValue> sharedId
                = SharedIdTest.sharedIdExpireSignFactory.create("dd", "iii");

        String encodeStr = sharedId.encode(new SharedIdExpireEncodeValue(1L, 1000));

        SharedIdExpireDecodeValue decodeId = sharedId.decode(encodeStr);

        TestCase.assertEquals(1L, decodeId.getId().longValue());
    }

    @Test
    public void testSharedIdExpireHashExpire() throws Exception {

        SharedId<SharedIdExpireEncodeValue, SharedIdExpireDecodeValue> sharedId
                = SharedIdTest.sharedIdExpireSignFactory.create("dd", "dd");

        String encodeStr = sharedId.encode(new SharedIdExpireEncodeValue(1L, 1000));

        Thread.sleep(1001);

        SharedIdExpireDecodeValue decodeId = sharedId.decode(encodeStr);

        TestCase.assertEquals(1L, decodeId.getExpireId().longValue());
        TestCase.assertTrue(decodeId.isExpired());
    }

}